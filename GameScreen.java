package CE.Progra1.Interfaz;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameScreen extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        HBox hbox = new HBox(new Label("Hola"));
        hbox.setAlignment(Pos.CENTER);
        Scene scene =  new Scene(hbox, 1280,720);
        stage.setScene(scene);
        stage.setTitle("Hola");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


