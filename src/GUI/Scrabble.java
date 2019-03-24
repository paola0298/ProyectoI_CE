package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Scrabble extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // En este panel va a meter sus paneles con su parte de la interfaz.
        StackPane mainLayout = new StackPane();

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.setTitle("Scrabble TEC");
        stage.show();
    }

    public void show() {
        launch(Scrabble.class);
    }

//    public static void main(String[] args) {
//        launch(Scrabble.class);
//    }
}
