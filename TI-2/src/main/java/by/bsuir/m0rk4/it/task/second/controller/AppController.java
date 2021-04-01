package by.bsuir.m0rk4.it.task.second.controller;

import by.bsuir.m0rk4.it.task.second.model.AppModel;
import by.bsuir.m0rk4.it.task.second.streamcipher.FileCryptoException;
import by.bsuir.m0rk4.it.task.second.uicomponents.ProgressForm;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppController {

    private final AppModel appModel;
    private final Stage stage;
    private File selectedReadFile;
    private File selectedWriteFile;

    @FXML
    private Button debugButton;
    @FXML
    private TextArea keyBitsTArea;
    @FXML
    private Label readFileLabel;
    @FXML
    private Label writeFileLabel;
    @FXML
    private Button processButton;
    @FXML
    private TextField lfsrInitialStateTField;
    @FXML
    private TextArea plaintextTArea;
    @FXML
    private TextArea resultingTextTArea;
    @FXML
    private Button selectReadFileButton;
    @FXML
    private Button selectWriteFileButton;

    public AppController(Stage stage, AppModel appModel) {
        this.appModel = appModel;
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        lfsrInitialStateTField.setTooltip(new Tooltip(
                "LFSR initial state\n"
                        + "must be a binary value\n"
                        + "36 digits long."
        ));
        lfsrInitialStateTField.textProperty().addListener(((observableValue, oldVal, newVal) -> {
            String filteredVal = newVal.replaceAll("[^01]", "");
            lfsrInitialStateTField.setText(filteredVal.length() > 36 ? oldVal : filteredVal);
            if (lfsrInitialStateTField.getText().length() == 36) {
                lfsrInitialStateTField.setStyle("-fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
            } else {
                lfsrInitialStateTField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            }
        }));

        selectReadFileButton.setOnAction(this::selectReadFile);
        selectWriteFileButton.setOnAction(this::selectWriteFile);

        processButton.setOnAction(this::processFile);

//        debugButton.setOnAction(this::convertToCsv);
    }

    private void convertToCsv(ActionEvent actionEvent) {
        String keyInitState = lfsrInitialStateTField.getText();
        if (keyInitState.length() != 36) {
            lfsrInitialStateTField.getTooltip().show(stage);
            return;
        }

        appModel.writeToCsv(keyInitState);
    }


    private void processFile(ActionEvent actionEvent) {
        String keyInitState = lfsrInitialStateTField.getText();
        if (keyInitState.length() != 36) {
            lfsrInitialStateTField.getTooltip().show(stage);
            return;
        }

        if (selectedReadFile == null) {
            showAlert("Specify a read file", "A file to read wasn't specified.");
            return;
        }

        if (selectedWriteFile == null) {
            showAlert("Specify a write file", "A file to write wasn't specified.");
            return;
        }

        try {
            String keyContents = appModel.getKeyContents(selectedReadFile, keyInitState);
            ProgressForm pForm = new ProgressForm();
            Task<Void> task = appModel.processFile(selectedReadFile, selectedWriteFile, keyInitState);
            pForm.activateProgressBar(task);

            task.setOnSucceeded(event -> {
                pForm.getDialogStage().close();
                processButton.setDisable(false);

                String writeContents = appModel.getFileContents(selectedWriteFile);
                keyBitsTArea.setText(keyContents);
                resultingTextTArea.setText(writeContents);
            });

            processButton.setDisable(true);
            pForm.getDialogStage().show();

            Thread thread = new Thread(task);
            thread.start();
        } catch (FileCryptoException e) {

        }
    }

    private void selectWriteFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setTitle("Select file");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            selectedWriteFile = file;
            String name = selectedWriteFile.getName();
            writeFileLabel.setText(name);
        }
    }

    private void selectReadFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setTitle("Select file");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedReadFile = file;
            String name = selectedReadFile.getName();
            readFileLabel.setText(name);
            String read = appModel.getFileContents(selectedReadFile);
            plaintextTArea.setText(read);
        }
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
