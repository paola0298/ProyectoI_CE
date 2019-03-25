package GUI;

import Logic.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Scrabble extends Application {
    private String cwd = System.getProperty("user.dir");
    private Controller controller;
    public boolean isActive = true;

    @Override
    public void start(Stage stage) {
        // En este panel va a meter sus paneles con su parte de la interfaz.
        StackPane mainLayout = new StackPane();

        //Pantalla de unión a partida existente.
        VBox joinMatchContainer = new VBox();
        joinMatchContainer.setAlignment(Pos.CENTER);
        joinMatchContainer.setSpacing(15);
        joinMatchContainer.setPadding(new Insets(15));
        Label joinTitle = new Label("Ingresa el código de la partida");
        joinTitle.setId("");
        TextField joinTextFiel = new TextField();
        joinTextFiel.setMaxWidth(200);
        Button joinButton = new Button("Unirse");
        Label joinResponse = new Label("");
        joinResponse.setId("join_message");
        joinButton.setOnAction(event -> {

            //Decirle a controller que envie un request al servidor.

        });
        joinMatchContainer.getChildren().addAll(joinTitle, joinTextFiel, joinButton, joinResponse);

        //Aquí añaden su panel al contenedor principal.
        mainLayout.getChildren().addAll(joinMatchContainer);

        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add("file:///" + cwd + "/res/styles.css");
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.setTitle("Scrabble TEC");
        stage.setOnCloseRequest(event -> {
            this.isActive = false;
            System.out.println("Exiting GUI");
        });
        stage.show();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void show() {
        launch(Scrabble.class);
    }

}
