package by.bsuir.m0rk4.it.task.second;

import by.bsuir.m0rk4.it.task.second.controller.AppController;
import by.bsuir.m0rk4.it.task.second.model.AppModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final String APP_VIEW = "primary.fxml";
    private static final String APP_NAME = "Information Theory - 2";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(APP_VIEW));
        AppModel appModel = new AppModel();
        fxmlLoader.setControllerFactory(c -> new AppController(stage, appModel));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}