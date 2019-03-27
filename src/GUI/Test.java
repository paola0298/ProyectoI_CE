package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        HBox vBox = new HBox();
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);

        Label label = new Label("Bienvenido");
        Button button = new Button("Pantalla 1");
        Button button2 = new Button("Pantalla 2");

        vBox.getChildren().addAll(label, button, button2);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(Test.class);
    }
}
