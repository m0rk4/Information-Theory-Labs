package by.bsuir.mark;

import by.bsuir.mark.controller.AppController;
import by.bsuir.mark.model.AppModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final String VIEW_NAME = "primary.fxml";
    private static final String APP_NAME = "Information Theory - 1";
    private static final int MIN_WIDTH = 615;
    private static final int MIN_HEIGHT = 440;

    @Override
    public void start(Stage stage) throws IOException {
        URL resource = App.class.getResource(VIEW_NAME);
        FXMLLoader fxmlLoader = new FXMLLoader(resource);

        AppModel appModel = new AppModel();
        fxmlLoader.setControllerFactory(c -> new AppController(appModel));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setTitle(APP_NAME);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}