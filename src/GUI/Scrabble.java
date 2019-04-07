package GUI;

import EnumTypes.ACTIONS;
import Logic.Controller;
import Structures.LinkedList;
import Structures.Node;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Handler;


public class Scrabble extends Application {
    private String cwd = System.getProperty("user.dir");
    private Controller controller;
    private VBox rightPlayerInfoContainer;
    private VBox leftPlayerInfoContainer;
    private VBox upPlayerInfoContainer;
    private HBox actualPlayerInfoContainer;
    private HBox tokenBox;
    private ImageView letterSelected;
    private GridPane matrixContainer;

    @Override
    public void start(Stage stage) {
        controller = new Controller(this);

        // En este panel va a meter sus paneles con su parte de la interfaz.
        StackPane mainLayout = new StackPane();
        /////////////////////////////Pantalla de Inicio//////////////////////
        /* Pantalla inicial donde el jugador puede elegir entre ingresar a un partida, o crear una propia
        Cuando se crea una parida nueva se genera un código con el que se puede invitar a otros jugadores
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

        tokenBox = new HBox();
        tokenBox.setAlignment(Pos.CENTER);
        tokenBox.setSpacing(5);
        tokenLoader();


        actualPlayerInfoContainer = new HBox();
        actualPlayerInfoContainer.setStyle("-fx-background-color: white");
        actualPlayerInfoContainer.setAlignment(Pos.CENTER);
        actualPlayerInfoContainer.setSpacing(15);
        actualPlayerInfoContainer.setPrefHeight(200);
        actualPlayerInfoContainer.getChildren().addAll(tokenBox, scrabbleImageButton);

        upPlayerInfoContainer = new VBox();
        upPlayerInfoContainer.setStyle("-fx-background-color: white");
        upPlayerInfoContainer.setAlignment(Pos.CENTER);
        upPlayerInfoContainer.setPrefHeight(150);

        // contenedor de la cuadricula
        matrixContainer = new GridPane();
        matrixContainer.setStyle("-fx-background-color: white;\n" +
                "    -fx-border-width: 2px; -fx-border-color: black");
        matrixContainer.setOnMouseClicked(event->{
            putImageOnContainer((int)event.getSceneX(), (int)event.getSceneY());

        });
        matrixContainer.setGridLinesVisible(true);
        addColumnsAndRows();

        rightPlayerInfoContainer = new VBox();
        rightPlayerInfoContainer.setStyle("-fx-background-color: white");
        rightPlayerInfoContainer.setAlignment(Pos.CENTER);
        rightPlayerInfoContainer.setPrefWidth(150);

        leftPlayerInfoContainer = new VBox();
        leftPlayerInfoContainer.setStyle("-fx-background-color: white");
        leftPlayerInfoContainer.setAlignment(Pos.CENTER);
        leftPlayerInfoContainer.setPrefWidth(150);

        gameScreenContainer.setTop(upPlayerInfoContainer);
        gameScreenContainer.setBottom(actualPlayerInfoContainer);
        gameScreenContainer.setCenter(matrixContainer);
        gameScreenContainer.setRight(rightPlayerInfoContainer);
        gameScreenContainer.setLeft(leftPlayerInfoContainer);

        playerLoader();


        //Aquí añaden su panel al contenedor principal.
        mainLayout.getChildren().addAll(joinMatchContainer, gameScreenContainer, initialWindow);
        gameScreenContainer.toFront();
        Scene scene = new Scene(mainLayout, 1280, 900);
//        scene.getStylesheets().add("file:///" + cwd + "/res/styles.css");
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.setTitle("Scrabble TEC");
        stage.show();
    }

    public void show() {
        launch(Scrabble.class);
    }


    /**
     * @param path Ruta de la imagen
     * @return El objeto de la imagen creada
     */
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

    /**
     * Carga en la interfaz los jugadores presentes en la partida
     */
    private void playerLoader(){
        //TODO por cada jugador que este en la lista de jugadores del juego actual, cargar los datos en la interfaz

        // instanciar widgets;
        Image userImage;
        ImageView addUserImage;

        Text userScoreText;
        Text userScore;
        Text userName;

        // playersList
        // temporal, mientras se genera la lista de jugadores
        LinkedList<String> players =  new LinkedList<>();
        players.addLast("Hazel");
        players.addLast("Brayan");
        players.addLast("Marlon");
        players.addLast("Paola");
        Node<String> temp = players.getHead();

        int cont = 0;

        while(temp!=null){

            if (cont==0){
                userImage = imageLoader(cwd + "/res/userIcon.png");
                addUserImage = new ImageView(userImage);
                addUserImage.setFitHeight(120);
                addUserImage.setFitWidth(120);
            } else {
                userImage = imageLoader(cwd + "/res/userIcon2.png");
                addUserImage = new ImageView(userImage);
                addUserImage.setFitHeight(100);
                addUserImage.setFitWidth(100);
            }

            VBox playersBox = new VBox();
            playersBox.setAlignment(Pos.CENTER);
            userName = new Text(temp.getValue());

            //puntuacion del usuario
            HBox userScoreBox = new HBox();
            userScoreBox.setAlignment(Pos.CENTER);
            userScoreBox.setSpacing(10);
            userScoreBox.setAlignment(Pos.CENTER);
            userScoreText = new Text("Puntos:");
            userScore = new Text("50");
            userScoreBox.getChildren().addAll(userScoreText, userScore);
            playersBox.getChildren().addAll(userName, addUserImage, userScoreBox);

            if (cont==0){
                this.actualPlayerInfoContainer.getChildren().addAll(playersBox);
            } else if (cont==1){
                this.rightPlayerInfoContainer.getChildren().addAll(playersBox);
            } else if (cont == 2){
                this.leftPlayerInfoContainer.getChildren().addAll(playersBox);
            } else {
                this.upPlayerInfoContainer.getChildren().addAll(playersBox);
            }

            temp = temp.getNext();
            cont++;
        }



    }

    /**
     * Coloca en la interfaz una imagen a cada jugador con su cantidad de fichas
     */
    private void putOppositeNumberToken(){
        //TODO colocar en imagenes la cantidad de fichas que tienen los demás jugadores
    }
    /**
     * Método para cargar las imágenes de las fichas del jugador
     */
    private void tokenLoader(){
        ImageView aLetter = loadImageView("/res/tokenImages/A.png");
        aLetter.setOnMouseClicked(mouseEvent -> {
            letterSelected = aLetter;
        });
        ImageView bLetter = loadImageView("/res/tokenImages/B.png");
        ImageView cLetter = loadImageView("/res/tokenImages/C.png");
        ImageView dLetter = loadImageView("/res/tokenImages/D.png");
        ImageView eLetter = loadImageView("/res/tokenImages/R.png");
        ImageView fLetter = loadImageView("/res/tokenImages/F.png");
        ImageView gLetter = loadImageView("/res/tokenImages/G.png");

        tokenBox.getChildren().addAll(aLetter, bLetter, cLetter, dLetter,
                eLetter, fLetter, gLetter); //agregar las fichas


    }

    /**
     * @param path Ruta del archivo
     * @return Un objeto ImageView de la imagen agregada
     */
    private ImageView loadImageView(String path){
        Image tokenImage = imageLoader(cwd + path);
        ImageView addTokenImage = new ImageView(tokenImage);
        addTokenImage.setFitHeight(80);
        addTokenImage.setFitWidth(80);

        return addTokenImage;
    }


    /**
     * Método para agregar filas y columnas a la cuadrícula (matrixContainer)
     */
    private void addColumnsAndRows() {
        for (int i = 0; i < 15; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 15);
            matrixContainer.getColumnConstraints().add(colConst);

            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / 15);
            matrixContainer.getRowConstraints().add(rowConst);
        }
    }

    /**
     * @param xPos Posición X donde se presionó
     * @param yPos Posición Y donde se presionó
     */
    private void putImageOnContainer(int xPos, int yPos){
        System.out.println("PosX " + xPos);
        System.out.println("PosY " + yPos);
        LinkedList<Integer> rowColumnList = new LinkedList<>();
        getRowAndColumn(rowColumnList, xPos, yPos);
        ImageView image = new ImageView(letterSelected.getImage());
        image.setFitHeight(30);
        image.setFitWidth(30);
        matrixContainer.getChildren().add(image);
        GridPane.setHalignment(image, HPos.CENTER);
        tokenBox.getChildren().remove(letterSelected);

    }

    private void getRowAndColumn(LinkedList<Integer> rowColumnList, int xPos, int yPos) {
    }


}
