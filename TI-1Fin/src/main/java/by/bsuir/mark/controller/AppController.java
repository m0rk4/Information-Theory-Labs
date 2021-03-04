package by.bsuir.mark.controller;

import by.bsuir.mark.logic.factory.CipherType;
import by.bsuir.mark.model.AppModel;
import by.bsuir.mark.validation.factory.LanguageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AppController {

    private final AppModel model;

    private static final String ENG_SAMPLE = "CRYPTOGRAPHY";
    private static final String RUS_SAMPLE = "КРИПТОГРАФИЯ";
    public static final String DATE_PATTERN = "d-MM-uuuu-HH-mm-ss";

    public Button saveEncodedButton;
    public Button saveDecodedButton;

    private CipherType selectedCipherType;
    private LanguageType selectedLanguageType;

    public Button clearButton;

    public Label langLabel;

    public ToggleGroup ciphersRadioButtonGroup;
    public RadioButton columnarRB;
    public RadioButton vigenereRB;
    public RadioButton playfairRB;

    public TextField keyTField;

    public TextArea plainTArea;
    public TextArea encodedTArea;
    public TextArea decodedTArea;

    public Button encodeButton;
    public Button decodeButton;

    public Button loadFileButton;

    public CheckBox isFastCBox;
    public TextField sampleTField;

    public AppController(AppModel model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("floppy-disk.png"));
        saveDecodedButton.setGraphic(new ImageView(image));
        saveEncodedButton.setGraphic(new ImageView(image));
        saveEncodedButton.setOnAction(this::saveFile);
        saveDecodedButton.setOnAction(this::saveFile);
        loadFileButton.setOnAction(this::loadFile);
        isFastCBox.setOnAction(this::updateSampleTextField);
        columnarRB.setOnAction(this::updateCipherType);
        vigenereRB.setOnAction(this::updateCipherType);
        playfairRB.setOnAction(this::updateCipherType);
        encodeButton.setOnAction(this::encode);
        decodeButton.setOnAction(this::decode);
        clearButton.setOnAction(this::clearTAreas);
        updateCipherType(null);
    }

    private void clearTAreas(ActionEvent e) {
        plainTArea.clear();
        encodedTArea.clear();
        decodedTArea.clear();
    }

    private void decode(ActionEvent e) {
        String key = keyTField.getText();
        String encodedText = isFastCBox.isSelected() ?
                sampleTField.getText() : plainTArea.getText();

        Optional<String> decodedText = model.decode(encodedText, key, selectedCipherType, selectedLanguageType);
        if (decodedText.isPresent()) {
            String decodedString = decodedText.get();
            decodedTArea.setText(decodedString);
            String formattedFilename = getFormattedFilename("DECODE-" + selectedCipherType.name());
            model.writeFileToLogs(decodedString, formattedFilename);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Key contains no alphabet (" + selectedLanguageType.name() + ") symbols.\nExample:\n\t" +
                            (selectedLanguageType.equals(LanguageType.RUS) ? RUS_SAMPLE : ENG_SAMPLE)
            );
        }
    }

    private String getFormattedFilename(String filePrefix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        String date = LocalDateTime.now().format(formatter);
        return filePrefix + "-" + date + ".txt";
    }

    private void encode(ActionEvent e) {
        String key = keyTField.getText();
        String plainText = isFastCBox.isSelected() ?
                sampleTField.getText() : plainTArea.getText();

        Optional<String> encodedText = model.encode(plainText, key, selectedCipherType, selectedLanguageType);
        if (encodedText.isPresent()) {
            String encodedString = encodedText.get();
            encodedTArea.setText(encodedString);
            String formattedFilename = getFormattedFilename("ENCODE-" + selectedCipherType.name());
            model.writeFileToLogs(encodedString, formattedFilename);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Key contains no alphabet (" + selectedLanguageType.name() + ") symbols.\nExample:\n\t" +
                            (selectedLanguageType.equals(LanguageType.RUS) ? RUS_SAMPLE : ENG_SAMPLE)
            );
        }
    }

    private void updateSampleTextField(ActionEvent e) {
        boolean isSelected = isFastCBox.isSelected();
        sampleTField.setEditable(isSelected);
    }

    private void updateCipherType(ActionEvent e) {
        if (columnarRB.isSelected()) {
            selectedCipherType = CipherType.COLUMNAR;
            selectedLanguageType = LanguageType.RUS;
            langLabel.setText(LanguageType.RUS.name());
        } else if (vigenereRB.isSelected()) {
            selectedCipherType = CipherType.VIGENERE;
            selectedLanguageType = LanguageType.RUS;
            langLabel.setText(LanguageType.RUS.name());
        } else {
            selectedCipherType = CipherType.PLAYFAIR;
            selectedLanguageType = LanguageType.ENG;
            langLabel.setText(LanguageType.ENG.name());
        }
    }

    private void saveFile(ActionEvent e) {
        Button source = (Button) e.getSource();
        String text;
        if (source == saveEncodedButton) {
            text = encodedTArea.getText();
        } else {
            text = decodedTArea.getText();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text file (.txt)", ".txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            String path = file.getPath();
            model.writeFile(text, path);
        }
    }

    private void loadFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setTitle("Choose File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Path path = file.toPath();
            Optional<List<String>> res = model.loadFile(path);
            if (res.isPresent()) {
                List<String> strings = res.get();
                String source = strings.stream()
                        .reduce("", (s1, s2) -> s1 + s2);
                plainTArea.setText(source);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "File Loading error.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String info) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(info);
        alert.show();
    }
}
