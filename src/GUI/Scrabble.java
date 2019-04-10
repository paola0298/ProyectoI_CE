package GUI;

import Logic.Controller;
import Logic.Player;
import Logic.Token;
import Structures.LinkedList;
import Structures.Node;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private BorderPane gameScreenContainer; //Ventana del juego.
    private GridPane matrixContainer; // Matríz del juego
    private VBox joinMatchContainer; //Ventana de unión a partida existente.
    private VBox initialWindow; // Ventana inicial
    private BorderPane newMatchWindow; //Ventana de partida nueva


    private String word = null;
    private LinkedList<LinkedList<String>> actualLetters;

    @Override
    public void start(Stage stage) {
        controller = new Controller(this);

        // En este panel va a meter sus paneles con su parte de la interfaz.
        StackPane mainLayout = new StackPane();

        init_initialWindow();
        init_newMatchWindow();
        init_joinWindow();
        init_gameWindow();

        //Aquí añaden su panel al contenedor principal.
        mainLayout.getChildren().addAll(joinMatchContainer, gameScreenContainer, initialWindow, newMatchWindow);
        Scene scene = new Scene(mainLayout, 1280, 900);
        scene.getStylesheets().add(("file:///" + cwd + "/res/styles.css").replace(" ", "%20"));
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

    private void init_initialWindow() {
        /////////////////////////////Pantalla de Inicio//////////////////////
        /* Pantalla inicial donde el jugador puede elegir entre ingresar a un partida, o crear una propia
        Cuando se crea una partida nueva se genera un código con el que se puede invitar a otros jugadores
        En esta ventana se especifíca cuantos jugadores tendrá la partida
         */
        initialWindow = new VBox();
//        initialWindow.setStyle("-fx-background-color: #1a8c24;");
        initialWindow.setStyle("-fx-background-color: white");
        initialWindow.setAlignment(Pos.CENTER);
        initialWindow.setSpacing(15);
        initialWindow.setPadding(new Insets(15));
        // El usuario ingresa su nombre
        Label playerName = new Label("Introduzca su nombre:");
        TextField playerNameInput = new TextField();
        playerNameInput.setMaxWidth(300);

        // El usuario escoge si quiere unirse a una partida o si va a crear una nueva
        Button joinButton = new Button("Unirme a una partida existente");
        joinButton.setOnAction(event -> {
            String name = playerNameInput.getText();
            if (!name.equals("")) {
                controller.setPlayerName(playerNameInput.getText());
                joinMatchContainer.toFront();
            } else {
                showAlert("Debe ingresar el nombre del jugador", "Campo requerido", Alert.AlertType.ERROR);
            }
        });

        Button newGameButton = new Button("Crear una nueva partida");
        newGameButton.setOnAction(actionEvent -> {
            String name = playerNameInput.getText();
            if (!name.equals("")) {
                controller.setPlayerName(playerNameInput.getText());
                newMatchWindow.toFront();
            }
            else{
                showAlert("Debe ingresar el nombre del jugador", "Campo requerido", Alert.AlertType.ERROR);
            }
        });

        initialWindow.getChildren().addAll(playerName,playerNameInput,joinButton,newGameButton);
    }

    private void init_joinWindow() {
        ///////////////////Pantalla de unión a partida existente//////////////////////
        joinMatchContainer = new VBox();
        joinMatchContainer.setStyle("-fx-background-color: white");
        joinMatchContainer.setAlignment(Pos.CENTER);
        joinMatchContainer.setSpacing(15);
        joinMatchContainer.setPadding(new Insets(15));

        HBox buttonContainer = backButtonContainer();

        VBox infoContainer = new VBox();
        infoContainer.setStyle("-fx-background-color: white");
        infoContainer.setAlignment(Pos.TOP_CENTER);
        infoContainer.setSpacing(15);
        infoContainer.setPadding(new Insets(15));
        infoContainer.setPrefHeight(550);

        Label joinTitle = new Label("Ingresa el código de la partida");
        TextField joinTextField = new TextField();
        joinTextField.setMaxWidth(200);
        Button joinButton = new Button("Unirse");
        Label joinResponse = new Label("");
        joinResponse.setId("join_message");
        joinButton.setOnAction(event -> {
            String match_id = joinTextField.getText();
            if (!match_id.equals("")) {
                if (controller.join_match(match_id)) {
                    gameScreenContainer.toFront();
                    playerLoader2(); //TODO actualizar metodo
                    tokenLoader();
                    String message = "El código de la partida es: " + controller.getCurrent_match_id();
                    showAlert(message, "Código de la partida", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("No es posible unirse a la sala", "Error de conexion", Alert.AlertType.ERROR);
                }
            }else {
                showAlert("Debe ingresar el código de la partida a la que desea unirse", "Campo requerido", Alert.AlertType.ERROR);
            }
        });
        infoContainer.getChildren().addAll(joinTitle, joinTextField, joinButton, joinResponse);
        joinMatchContainer.getChildren().addAll(buttonContainer, infoContainer);
    }

    private void init_newMatchWindow() {
        /* Window for create a new game displays an comboBox for choose the number of players in the game */
        newMatchWindow = new BorderPane();

        newMatchWindow.setPadding(new Insets(15, 20, 10, 10));
        newMatchWindow.setBackground(new Background(new BackgroundFill(Color.rgb(47,79,79), CornerRadii.EMPTY, Insets.EMPTY)));
        //espacio del border pane al boton

        HBox buttonContainer = backButtonContainer();

        Label numberPlayers = new Label("Seleccione la cantidad de jugadores");
        numberPlayers.setPadding(new Insets(2,2,2,2));
        numberPlayers.setBackground(new Background(new BackgroundFill(Color.rgb(143,188,143), CornerRadii.EMPTY, Insets.EMPTY)));
        numberPlayers.setTextFill(Color.rgb(34,139,34));
        numberPlayers.setFont(new Font("Serif",30));
        numberPlayers.setAlignment(Pos.CENTER);

        VBox matchOptionsContainer = new VBox();
        matchOptionsContainer.setSpacing(20);
        matchOptionsContainer.setAlignment(Pos.CENTER);

        //Create a ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("2");
        comboBox.getItems().add("3");
        comboBox.getItems().add("4");
        comboBox.setBackground(new Background(new BackgroundFill(Color.rgb(46,139,87), CornerRadii.EMPTY,Insets.EMPTY)));
        comboBox.setStyle("-fx-font: 30px \"Serif\";");

        matchOptionsContainer.getChildren().addAll(numberPlayers, comboBox);
        newMatchWindow.setCenter(matchOptionsContainer);
        Button startButton = new Button("Start Game");
        startButton.setOnMouseClicked(mouseEvent -> {
            int i = comboBox.getSelectionModel().getSelectedIndex();
            if (i > -1) {
                String selected = comboBox.getItems().get(i);
                if (controller.create_match(selected)) {
                    gameScreenContainer.toFront();
                    playerLoader2();
                    tokenLoader();
                    String message = "El código de la partida es: " + controller.getCurrent_match_id();
                    showAlert(message, "Código de la partida", Alert.AlertType.INFORMATION);
                }else{
                    showAlert("No se ha podido realizar la conexion con el servidor", "Error de conexion", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Debe seleccionar la cantidad de jugadores", "Campo requerido", Alert.AlertType.ERROR);
            }

        });

        startButton.setBackground(new Background(new BackgroundFill(Color.rgb(72,209,204), CornerRadii.EMPTY, Insets.EMPTY)));
        startButton.setTextFill(Color.rgb(0,100,0));
        startButton.setFont(new Font("Serif", 30));
        startButton.setAlignment(Pos.CENTER_RIGHT);
        newMatchWindow.setBottom(startButton);
        newMatchWindow.setLeft(buttonContainer);
        // Alignment.
        BorderPane.setAlignment(startButton, Pos.TOP_RIGHT);
        BorderPane.setAlignment(buttonContainer, Pos.TOP_LEFT);

        // Set margin for bottom area.
        BorderPane.setMargin(startButton, new Insets(10, 10, 10, 10));
    }

    private HBox backButtonContainer(){
        HBox buttonContainer = new HBox();
        buttonContainer.setStyle("-fx-background-color: white");
        buttonContainer.setAlignment(Pos.TOP_LEFT);
        buttonContainer.setPrefHeight(350);
        ImageView backButton = loadImageView("/res/images/backButton.png");
        backButton.setFitHeight(50);
        backButton.setFitWidth(50);
        backButton.setOnMouseClicked(mouseEvent -> {
            initialWindow.toFront();
        });
        buttonContainer.getChildren().addAll(backButton);

        return buttonContainer;
    }

    private void init_gameWindow() {
        /////////////////////////////Pantalla de Juego//////////////////////
        gameScreenContainer = new BorderPane();


        //Boton para enviar la palabra
        Image scrabbleImage = imageLoader(cwd + "/res/images/scrabble.jpg");
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

        ImageView showInfoButton = loadImageView("/res/images/infoIcon.png");
        showInfoButton.setOnMouseClicked(mouseEvent -> {
            String message = "El código de la partida es: " + controller.getCurrent_match_id();
            showAlert(message, "Código de la partida", Alert.AlertType.INFORMATION);
        });

        showInfoButton.setFitHeight(60);
        showInfoButton.setFitWidth(60);


        upPlayerInfoContainer.getChildren().addAll(showInfoButton);


        // contenedor de la cuadricula
        matrixContainer = new GridPane();
        matrixContainer.setStyle("-fx-background-color: white;\n" +
                "    -fx-border-width: 2px; -fx-border-color: black");
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

    }

    private void showAlert(String message, String title, Alert.AlertType type){
        Alert showID = new Alert(type);
        showID.setTitle(title);
        showID.setHeaderText(null);
        showID.setContentText(message);
        showID.showAndWait();
    }

    private void createWord(){


        
    }

    private void addToActualLetters(boolean child, int row, int column){
        LinkedList<String> tokenImage = new LinkedList<>();
        tokenImage.addLast(String.valueOf(child));
        tokenImage.addLast(String.valueOf(row));
        tokenImage.addLast(String.valueOf(column));

        if (!child){
            // la letra no esta agregada a la lista con su respectiva fila y columna

        } else {
            // el espacio en fila y columna esta ocupado, hay que reemplazar letra
        }
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
        System.out.println("Could not find " + path);
        return null;
    }

    /**
     * Carga en la interfaz los jugadores presentes en la partida
     */
    private void playerLoader2() {
        ImageView userImage;
        Text scoreLabel;
        Text userScore;
        Text userName;
        int c = 0;
        LinkedList<ImageView> imageForUser = new LinkedList<>();
        imageForUser.addLast(loadImageView("/res/images/user/player_pink.png"));
        imageForUser.addLast(loadImageView("/res/images/user/player_blue.png"));
        imageForUser.addLast(loadImageView("/res/images/user/player_red.png"));
        imageForUser.addLast(loadImageView("/res/images/user/player_green.png"));

        LinkedList<Player> playersToLoad = controller.getActualGame().getPlayers();

        for (int i=0; i<playersToLoad.getSize(); i++) {
            Player player = playersToLoad.get(i);
            System.out.println("Player: " + player.getName());
            userImage = imageForUser.get(i);
            userImage.setFitWidth(60);
            userImage.setFitHeight(100);

            VBox playerBox = new VBox();
            playerBox.setAlignment(Pos.CENTER);
            userName = new Text(player.getName());

            //Puntuación del usuario
            HBox userScoreBox = new HBox();
            userScoreBox.setAlignment(Pos.CENTER);
            userScoreBox.setSpacing(10);
            scoreLabel = new Text("Puntos: ");
            userScore = new Text(String.valueOf(player.getScore()));
            userScoreBox.getChildren().addAll(scoreLabel, userScore);
            playerBox.getChildren().addAll(userName, userImage, userScoreBox);

            System.out.println("Player id " + player.getplayerId());
            System.out.println("Actual player id " + player.getplayerId());

            if (player.getplayerId().equals(controller.getPlayerInstance().getplayerId())) {
                System.out.println("Adding actual player");
                this.actualPlayerInfoContainer.getChildren().add(playerBox);
            } else {
                System.out.println("Adding other players");
                switch (c) {
                    case 0:
                        this.rightPlayerInfoContainer.getChildren().add(playerBox);
                        break;
                    case 1:
                        this.upPlayerInfoContainer.getChildren().add(playerBox);
                        break;
                    case 2:
                        this.leftPlayerInfoContainer.getChildren().add(playerBox);
                        break;
                }
                c++;
            }

        }



    }
//    private void playerLoader() {
//        // instanciar widgets;
//        ImageView userImage;
//
//        Text scoreLabel;
//        Text userScore;
//        Text userName;
//
//        LinkedList<ImageView> imageForUser = new LinkedList<>();
//        imageForUser.addLast(loadImageView("/res/images/user/player_pink.png"));
//        imageForUser.addLast(loadImageView("/res/images/user/player_blue.png"));
//        imageForUser.addLast(loadImageView("/res/images/user/player_red.png"));
//        imageForUser.addLast(loadImageView("/res/images/user/player_green.png"));
//        Node<ImageView> imageTemp = imageForUser.getHead();
//
//        // playersList
//
//        LinkedList<Player> actualPlayers = controller.getActualGame().getPlayers();
//        Node<Player> playerTemp = actualPlayers.getHead();
//        Player actualPlayer = controller.getPlayerInstance();
//
//        if (actualPlayer != playerTemp.getValue()) {
//            actualPlayers.remove(actualPlayer);
//            actualPlayers.addFirst(actualPlayer);
//        }
//
//        int i = 0;
//        while (playerTemp != null) {
//            userImage = imageTemp.getValue();
//            userImage.setFitHeight(100);
//            userImage.setFitWidth(60);
//
//            VBox playersBox = new VBox();
//            playersBox.setAlignment(Pos.CENTER);
//            userName = new Text(playerTemp.getValue().getName());
//
//            //puntuacion del usuario
//            HBox userScoreBox = new HBox();
//            userScoreBox.setAlignment(Pos.CENTER);
//            userScoreBox.setSpacing(10);
//            userScoreBox.setAlignment(Pos.CENTER);
//            scoreLabel = new Text("Puntos:");
//            String score = String.valueOf(playerTemp.getValue().getScore());
//            userScore = new Text(score);
//            userScoreBox.getChildren().addAll(scoreLabel, userScore);
//            playersBox.getChildren().addAll(userName, userImage, userScoreBox);
//
//            if (i==0){
//                this.actualPlayerInfoContainer.getChildren().addAll(playersBox);
//            } else if (i==1){
//                this.rightPlayerInfoContainer.getChildren().addAll(playersBox);
//            } else if (i == 2){
//                this.leftPlayerInfoContainer.getChildren().addAll(playersBox);
//            } else {
//                this.upPlayerInfoContainer.getChildren().addAll(playersBox);
//            }
//
//            imageTemp = imageTemp.getNext();
//            playerTemp = playerTemp.getNext();
//            i++;
//
//        }
//
////
//
////        int i = 0;
////        while(temp!=null){
////            userImage = imageForUser.acces_index(i).getValue();
////            userImage.setFitHeight(100);
////            userImage.setFitWidth(60);
////
////            VBox playersBox = new VBox();
////            playersBox.setAlignment(Pos.CENTER);
////            userName = new Text(temp.getValue().getName());
////
////            //puntuacion del usuario
////            HBox userScoreBox = new HBox();
////            userScoreBox.setAlignment(Pos.CENTER);
////            userScoreBox.setSpacing(10);
////            userScoreBox.setAlignment(Pos.CENTER);
////            scoreLabel = new Text("Puntos:");
////            String score = String.valueOf(temp.getValue().getScore());
////            userScore = new Text(score);
////            userScoreBox.getChildren().addAll(scoreLabel, userScore);
////            playersBox.getChildren().addAll(userName, userImage, userScoreBox);
////
////        }
//
////
//////         temporal, mientras se genera la lista de jugadores
////        LinkedList<String> players =  new LinkedList<>();
////        players.addLast("Hazel");
////        players.addLast("Brayan");
////        players.addLast("Marlon");
////        players.addLast("Paola");
////        Node<String> temp = players.getHead();
////
////        int cont = 0;
////        int i = 0;
////
////        while(temp!=null){
////
////            userImage = imageForUser.acces_index(i).getValue();
////            userImage.setFitHeight(100);
////            userImage.setFitWidth(60);
////
////
////            VBox playersBox = new VBox();
////            playersBox.setAlignment(Pos.CENTER);
////            userName = new Text(temp.getValue());
////
////            //puntuacion del usuario
////            HBox userScoreBox = new HBox();
////            userScoreBox.setAlignment(Pos.CENTER);
////            userScoreBox.setSpacing(10);
////            userScoreBox.setAlignment(Pos.CENTER);
////            scoreLabel = new Text("Puntos:");
////            userScore = new Text("50");
////            userScoreBox.getChildren().addAll(scoreLabel, userScore);
////            playersBox.getChildren().addAll(userName, userImage, userScoreBox);
//
////            if (cont==0){
////                this.actualPlayerInfoContainer.getChildren().addAll(playersBox);
////            } else if (cont==1){
////                this.rightPlayerInfoContainer.getChildren().addAll(playersBox);
////            } else if (cont == 2){
////                this.leftPlayerInfoContainer.getChildren().addAll(playersBox);
////            } else {
////                this.upPlayerInfoContainer.getChildren().addAll(playersBox);
////            }
////
////            temp = temp.getNext();
////            cont++;
////            i++;
////        }
//
//
//
//
//    }

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
        LinkedList<Token> tokenLinkedList = controller.getPlayerInstance().getTokenlist();
        Node<Token> temp = tokenLinkedList.getHead();

        while (temp!=null){
            Token actualToken = temp.getValue();

            ImageView letter = loadImageView(actualToken.getImagePath());
            letter.setOnMouseClicked(mouseEvent -> {
                if (letterSelected == letter)
                    letterSelected = null;
                else
                    letterSelected = letter;
            });

            tokenBox.getChildren().addAll(letter);

            temp = temp.getNext();
        }
    }

    /**
     * @param path Ruta del archivo
     * @return Un objeto ImageView de la imagen agregada
     */
    private ImageView loadImageView(String path){
        Image image = imageLoader(cwd+path);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        return imageView;
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
                HBox tokenContainer =  new HBox();
                tokenContainer.setAlignment(Pos.CENTER);
                tokenContainer.setOnMouseClicked(mouseEvent -> {
                    int row = GridPane.getRowIndex(tokenContainer);
                    int column = GridPane.getColumnIndex(tokenContainer);

                    if (tokenContainer.getChildren().size()==0) {
                        if (letterSelected != null) {
                            putImageOnContainer(tokenContainer);

                            addToActualLetters(false, row, column);
                        }
                    }
                    else {
                        ImageView child = (ImageView) tokenContainer.getChildren().get(0);
                        child.setFitHeight(80);
                        child.setFitWidth(80);

                        tokenContainer.getChildren().remove(0);
                        tokenBox.getChildren().add(child);
                        addGestureToNewToken();

                        if (letterSelected != null) {
                            putImageOnContainer(tokenContainer);
                            addToActualLetters(true, row, column);
                        }
                    }
                });
                matrixContainer.add(tokenContainer, i, j);
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

    /**
     * Este método se encarga de cargar el estado actual del juego
     */
    public void loadMatch() {

    }


}
