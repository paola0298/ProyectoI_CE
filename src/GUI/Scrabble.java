package GUI;

import EnumTypes.ACTIONS;
import Logic.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class Scrabble extends Application {
    private String cwd = System.getProperty("user.dir");
    private Controller controller;

    @Override
    public void start(Stage stage) {
        controller = new Controller(this);

        // En este panel va a meter sus paneles con su parte de la interfaz.
        StackPane mainLayout = new StackPane();
        /////////////////////////////Pantalla de Inicio//////////////////////
        /* Pantalla inicial donde el jugador puede elegir entre ingresar a un partida, o crear una propia
        Cuando se crea una partida nueva se genera un código con el que se puede invitar a otros jugadores
        En esta ventana se especifíca cuantos jugadores tendrá la partida
         */
        VBox initialWindow = new VBox();
        initialWindow.setStyle("-fx-background-color: #1a8c24;");
        initialWindow.setAlignment(Pos.TOP_LEFT);
        initialWindow.setSpacing(15);
        initialWindow.setPadding(new Insets(15));
        // El usuario ingresa su nombre
        Label Players_Name = new Label("Introduzca su nombre:");
        TextField Players_Name_Input = new TextField();
        Players_Name_Input.setMaxWidth(300);

        // El usuario escoge si quiere unirse a una partida o si va a crear una nueva
        Button Join = new Button("Unirme a una partida existente");
        Button New_Game = new Button("Crear una nueva partida");
        Join.setTranslateY(30);
        New_Game.setTranslateY(50);


        initialWindow.getChildren().addAll(Players_Name,Players_Name_Input,Join,New_Game);


        //Pantalla de unión a partida existente.
        VBox joinMatchContainer = new VBox();
        joinMatchContainer.setStyle("-fx-background-color: gray;");
        joinMatchContainer.setAlignment(Pos.CENTER);
        joinMatchContainer.setSpacing(15);
        joinMatchContainer.setPadding(new Insets(15));
        Label joinTitle = new Label("Ingresa el código de la partida");
        joinTitle.setId("");
        TextField joinTextField = new TextField(); //Brayan: Agregué una d al final del nombre de la variable, en la siguiente línea también
        joinTextField.setMaxWidth(200);
        Button joinButton = new Button("Unirse");
        Label joinResponse = new Label("");
        joinResponse.setId("join_message");
        joinButton.setOnAction(event -> {
            //Decirle a controller que envie un request al servidor.
            controller.doAction(ACTIONS.CREATE_MATCH);
        });
        joinMatchContainer.getChildren().addAll(joinTitle, joinTextField, joinButton, joinResponse);

        /////////////////////////////Pantalla de Juego//////////////////////


        BorderPane gameScreenContainer = new BorderPane();

        //Imagen del usuario
        Image userImage = imageLoader(cwd + "/res/userIcon.png");
        ImageView addUserImage = new ImageView(userImage);
        addUserImage.setFitHeight(120);
        addUserImage.setFitWidth(120);

        //puntuacion del usuario
        HBox userScoreBox = new HBox();
        userScoreBox.setSpacing(10);
        userScoreBox.setAlignment(Pos.CENTER);


        Text userScoreText = new Text("Mi puntuación es: ");
        Text userScore = new Text("50");

        userScoreBox.getChildren().addAll(userScoreText, userScore);

        //Fichas

        Image token = imageLoader(cwd + "/res/token.png");
        ImageView tokenImage = new ImageView(token);
        tokenImage.setFitWidth(501);
        tokenImage.setFitHeight(82);


        //Boton para enviar la palabra
        Image scrabbleImage = imageLoader(cwd + "/res/scrabble.jpg");
        ImageView scrabbleImageButton = new ImageView(scrabbleImage);
        scrabbleImageButton.setFitWidth(150);
        scrabbleImageButton.setFitHeight(150);
        scrabbleImageButton.setOnMouseClicked(mouseEvent -> {
            //tomar palabra creada y enviarla al servidor
            System.out.println("The word is: ");
        });

        // contenedores

        VBox scoreAndToken = new VBox();
        scoreAndToken.setAlignment(Pos.CENTER);
        scoreAndToken.setSpacing(10);
        scoreAndToken.getChildren().addAll(userScoreBox, tokenImage); //agregar las fichas

        HBox actualPlayerInfoContainer = new HBox();
        actualPlayerInfoContainer.setStyle("-fx-background-color: white");
        actualPlayerInfoContainer.setAlignment(Pos.CENTER);
        actualPlayerInfoContainer.setSpacing(15);
        actualPlayerInfoContainer.setPrefHeight(210);
        actualPlayerInfoContainer.getChildren().addAll(addUserImage, scoreAndToken, scrabbleImageButton);

        VBox upPlayerInfoContainer = new VBox();
        upPlayerInfoContainer.setStyle("-fx-background-color: green");
        upPlayerInfoContainer.setPrefHeight(150);

        GridPane matrixContainer = new GridPane();
        matrixContainer.setStyle("-fx-background-color: purple");

        VBox rightPlayerInfoContainer = new VBox();
        rightPlayerInfoContainer.setStyle("-fx-background-color: red");
        rightPlayerInfoContainer.setPrefWidth(170);

        VBox leftPlayerInfoContainer = new VBox();
        leftPlayerInfoContainer.setStyle("-fx-background-color: orange");
        leftPlayerInfoContainer.setPrefWidth(170);

        gameScreenContainer.setTop(upPlayerInfoContainer);
        gameScreenContainer.setBottom(actualPlayerInfoContainer);
        gameScreenContainer.setCenter(matrixContainer);
        gameScreenContainer.setRight(rightPlayerInfoContainer);
        gameScreenContainer.setLeft(leftPlayerInfoContainer);
/**Window for create a new game
 *displays an comboBox for choose the number of players in the game
 *
 * @author Hazel ML
 * @version 1.0
 * @since 2019-27-03
 */
        BorderPane root = new BorderPane();

        root.setPadding(new Insets(15, 20, 10, 10));
        root.setBackground(new Background(new BackgroundFill(Color.rgb(47,79,79), CornerRadii.EMPTY, Insets.EMPTY)));
        //espacio del border pane al boton

        Label numberPlayers = new Label("Number of Players");
        numberPlayers.setPadding(new Insets(2,2,2,2));
        numberPlayers.setBackground(new Background(new BackgroundFill(Color.rgb(143,188,143), CornerRadii.EMPTY, Insets.EMPTY)));
        numberPlayers.setTextFill(Color.rgb(34,139,34));
        numberPlayers.setFont(new Font("Serif",30));
        numberPlayers.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        hBox.getChildren().add(numberPlayers);

        //Create a ComboBox
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("Two Players");
        comboBox.getItems().add("Three Players");
        comboBox.getItems().add("Four Players");
        comboBox.setBackground(new Background(new BackgroundFill(Color.rgb(46,139,87), CornerRadii.EMPTY,Insets.EMPTY)));
        comboBox.setStyle("-fx-font: 30px \"Serif\";");
        hBox.getChildren().add(comboBox);

        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        root.setCenter(hBox);
        Button startButton = new Button("Start Game");
        startButton.setPadding(new Insets(20, 10, 10, 20));
        startButton.setBackground(new Background(new BackgroundFill(Color.rgb(72,209,204), CornerRadii.EMPTY, Insets.EMPTY)));
        startButton.setTextFill(Color.rgb(0,100,0));
        startButton.setFont(new Font("Serif", 30));
        startButton.setAlignment(Pos.CENTER_RIGHT);
        root.setBottom(startButton);
        // Alignment.
        BorderPane.setAlignment(startButton, Pos.TOP_RIGHT);

        // Set margin for bottom area.
        BorderPane.setMargin(startButton, new Insets(10, 10, 10, 10));

        //Aquí añaden su panel al contenedor principal.
        mainLayout.getChildren().addAll(joinMatchContainer, gameScreenContainer, initialWindow, root);
        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add("file:///" + cwd + "/res/styles.css");
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.setTitle("Scrabble TEC");
        root.toFront();
        stage.show();
    }

    public void show() {
        launch(Scrabble.class);
    }


    private Image imageLoader(String path){
        try{
            FileInputStream i = new FileInputStream(path);
            return new Image(i);
        }catch (FileNotFoundException e){
            System.out.println("Couldn't load images!");
        }
        System.out.println("Returning null");
        return null;
    }

    private void playerLoader(String actualUserScore){
        //TODO por cada jugador que este en la lista de jugadores del juego actual, cargar los datos en la interfaz

        // instanciar widgets;
        Image userImage;
        ImageView addUserImage;

        Text userScoreText;
        Text userScore;


        //Right and left users
        //imagen del usuario
        VBox rightLeftPlayers = new VBox();
        userImage = imageLoader(cwd + "/res/userIcon2.png");
        addUserImage = new ImageView(userImage);
        addUserImage.setFitHeight(120);
        addUserImage.setFitWidth(120);


        //puntuacion del usuario
        HBox userScoreBox = new HBox();
        userScoreBox.setSpacing(10);
        userScoreBox.setAlignment(Pos.CENTER);
        userScoreText = new Text("Puntos obtenidos: ");
        userScore = new Text(actualUserScore);
        userScoreBox.getChildren().addAll(userScoreText, userScore);



    }

}
