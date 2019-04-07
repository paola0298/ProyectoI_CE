package GUI;

import Logic.Controller;
import Structures.LinkedList;
import Structures.Node;
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

public class Scrabble extends Application {
    private String cwd = System.getProperty("user.dir");
    private Controller controller;
    private VBox rightPlayerInfoContainer;
    private VBox leftPlayerInfoContainer;
    private VBox upPlayerInfoContainer;
    private HBox actualPlayerInfoContainer;
    private HBox tokenBox;
    private ImageView letterSelected = null;
    private GridPane matrixContainer;

    private VBox initialWindow; // Ventana inicial
    private VBox joinMatchContainer; //Ventana de unión a partida existente.
    private BorderPane gameScreenContainer; //Ventana del juego.


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
        initialWindow = new VBox();
        initialWindow.setStyle("-fx-background-color: #1a8c24;");
        initialWindow.setAlignment(Pos.CENTER);
        initialWindow.setSpacing(15);
        initialWindow.setPadding(new Insets(15));
        // El usuario ingresa su nombre
        Label Players_Name = new Label("Introduzca su nombre:");
        TextField Players_Name_Input = new TextField();
        Players_Name_Input.setMaxWidth(300);

        // El usuario escoge si quiere unirse a una partida o si va a crear una nueva
        Button Join = new Button("Unirme a una partida existente");
        Join.setOnAction(event -> {
            String name = Players_Name_Input.getText();
            if (!name.equals("")) {
                controller.setPlayerName(Players_Name_Input.getText());
                joinMatchContainer.toFront();
            }
        });

        Button New_Game = new Button("Crear una nueva partida");

        New_Game.setOnAction(actionEvent -> {
            String name = Players_Name_Input.getText();
            if (!name.equals("")) {
                controller.setPlayerName(Players_Name_Input.getText());
                joinMatchContainer.toFront();
                //Todo mostrar el panel de Hazel
            }
        });


        initialWindow.getChildren().addAll(Players_Name,Players_Name_Input,Join,New_Game);


        ///////////////////Pantalla de unión a partida existente//////////////////////
        joinMatchContainer = new VBox();
        joinMatchContainer.setStyle("-fx-background-color: white");
        joinMatchContainer.setAlignment(Pos.CENTER);
        joinMatchContainer.setSpacing(15);
        joinMatchContainer.setPadding(new Insets(15));
        Label joinTitle = new Label("Ingresa el código de la partida");
        TextField joinTextField = new TextField(); //Brayan: Agregué una d al final del nombre de la variable, en la siguiente línea también
        joinTextField.setMaxWidth(200);
        Button joinButton = new Button("Unirse");
        Label joinResponse = new Label("");
        joinResponse.setId("join_message");
        joinButton.setOnAction(event -> {
            //Decirle a controller que envie un request al servidor.
            String match_id = joinTextField.getText();
            controller.join_match(match_id);
            gameScreenContainer.toFront(); //TODO esté método lo llamaría Controller
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
//        matrixContainer.setOnMouseClicked(event->{
//            putImageOnContainer();
//
//        });
        matrixContainer.setGridLinesVisible(true);

        addColumnsAndRows();
        addBoxToGrid();

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

        /* Window for create a new game displays an comboBox for choose the number of players in the game */
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
        ComboBox<String> comboBox = new ComboBox<>();
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



//        scene.getStylesheets().add("file:///" + cwd + "/res/styles.css");
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.setTitle("Scrabble TEC");
        initialWindow.toFront();
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

        LinkedList<ImageView> imageForUser = new LinkedList<>();
        imageForUser.addLast(loadImageView("/res/player_pink.png"));
        imageForUser.addLast(loadImageView("/res/player_blue.png"));
        imageForUser.addLast(loadImageView("/res/player_red.png"));
        imageForUser.addLast(loadImageView("/res/player_green.png"));

        // playersList
        // temporal, mientras se genera la lista de jugadores
        LinkedList<String> players =  new LinkedList<>();
        players.addLast("Hazel");
        players.addLast("Brayan");
        players.addLast("Marlon");
        players.addLast("Paola");
        Node<String> temp = players.getHead();

        int cont = 0;
        int i = 0;

        while(temp!=null){

            addUserImage = imageForUser.acces_index(i).getValue();
            addUserImage.setFitHeight(100);
            addUserImage.setFitWidth(60);


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
            i++;
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
            if (letterSelected==aLetter)
                letterSelected=null;
            else
                letterSelected = aLetter;

        });
        ImageView bLetter = loadImageView("/res/tokenImages/B.png");
        bLetter.setOnMouseClicked(mouseEvent -> {
            if (letterSelected==bLetter)
                letterSelected=null;
            else
                letterSelected = bLetter;
        });
        ImageView cLetter = loadImageView("/res/tokenImages/C.png");
        cLetter.setOnMouseClicked(mouseEvent -> {
            if (letterSelected==cLetter)
                letterSelected=null;
            else
                letterSelected = cLetter;
        });
        ImageView dLetter = loadImageView("/res/tokenImages/D.png");
        dLetter.setOnMouseClicked(mouseEvent -> {
            if (letterSelected==dLetter)
                letterSelected=null;
            else
                letterSelected = dLetter;
        });
        ImageView eLetter = loadImageView("/res/tokenImages/R.png");
        eLetter.setOnMouseClicked(mouseEvent -> {
            if (letterSelected==eLetter)
                letterSelected=null;
            else
                letterSelected = eLetter;
        });
        ImageView fLetter = loadImageView("/res/tokenImages/F.png");
        fLetter.setOnMouseClicked(mouseEvent -> {
            if (letterSelected==fLetter)
                letterSelected=null;
            else
                letterSelected = fLetter;
        });
        ImageView gLetter = loadImageView("/res/tokenImages/G.png");
        gLetter.setOnMouseClicked(mouseEvent -> {
            if (letterSelected==gLetter)
                letterSelected=null;
            else
                letterSelected = gLetter;
        });

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
     * Método para agregar Boxes a cada cuadro de la cuadricula
     */
    private void addBoxToGrid(){
        for(int i=0; i<15; i++){
            for (int j=0; j<15; j++){
                HBox box =  new HBox();
                box.setAlignment(Pos.CENTER);
                box.setOnMouseClicked(mouseEvent -> {
                    if (box.getChildren().size()==0) {
                        if (letterSelected != null) {
                            putImageOnContainer(box);
                        }
                    }
                    else {
                        ImageView child = (ImageView) box.getChildren().get(0);
                        child.setFitHeight(80);
                        child.setFitWidth(80);

                        box.getChildren().remove(0);
                        tokenBox.getChildren().add(child);
                        addGestureToNewToken();

                        if (letterSelected != null) {
                            putImageOnContainer(box);
                        }
                    }
                });
                matrixContainer.add(box, i, j);
            }
        }
    }

    private void addGestureToNewToken() {
        int size = tokenBox.getChildren().size();
        ImageView newToken = (ImageView) tokenBox.getChildren().get(size-1);
        newToken.setOnMouseClicked(mouseEvent -> {
            if (letterSelected==newToken)
                letterSelected=null;
            else
                letterSelected = newToken;
        });

    }

    /**
     * Coloca la letra de la ficha en la casilla correspondiente
     */
    private void putImageOnContainer(HBox imagecontainer){
        ImageView image = new ImageView(letterSelected.getImage());
        image.setFitHeight(30);
        image.setFitWidth(30);
        imagecontainer.getChildren().add(image);
        tokenBox.getChildren().remove(letterSelected);
        letterSelected = null;
    }

    //TODO hacer métodos para actualizar la interfaz al recibir un mensaje del servidor.

}
