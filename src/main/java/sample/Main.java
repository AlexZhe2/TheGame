package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
            TabPane rootElement = (TabPane) loader.load();
            Scene scene = new Scene(rootElement);
            primaryStage.setTitle("The Game!");
            primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            primaryStage.show();

            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent e) {

                    if (e.getCode() == KeyCode.SPACE) {
                        Controller.stopSpace = true;
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        nu.pattern.OpenCV.loadLocally();
        launch(args);

    }
}
