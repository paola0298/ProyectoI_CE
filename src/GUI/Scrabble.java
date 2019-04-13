package GUI;

import Logic.Controller;
import Logic.Player;
import Logic.Token;
import Structures.LinkedList;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.Optional;

/**
 * La clase Scrabble se encarga de crear la parte gráfica del proyecto
 */
public class Scrabble extends Application {
    private String cwd = System.getProperty("user.dir");
    private Controller controller;
    private VBox rightPlayerInfoContainer;
    private VBox leftPlayerInfoContainer;
    private VBox upPlayerInfoContainer;
    private HBox actualPlayerInfoContainer;
    private HBox actiosInMatch;
    private HBox tokenBox;
    private Token selectedToken = null;

    private boolean unlockedControls;

    private BorderPane gameScreenContainer; //Ventana del juego.
    private GridPane matrixContainer; // Matríz del juego
    private HBox joinMatchContainer; //Ventana de unión a partida existente.
    private VBox initialWindow; // Ventana inicial
    private BorderPane newMatchWindow; //Ventana de partida nueva

    private LinkedList<Token> lettersList = new LinkedList<>(); //Lista con las tokens puestas en el tablero

    /**
     * Este método inicializa la interfaz
     * @param stage Es la escena principal de la aplicación
     */
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

    /**
     * Método que será llamado para ejecutar la interfaz
     */
    public void show() {
        launch(Scrabble.class);
    }

    /**
     * Método que crea la ventana principal del juego
     */
    private void init_initialWindow() {
        /////////////////////////////Pantalla de Inicio//////////////////////
        /* Pantalla inicial donde el jugador puede elegir entre ingresar a un partida, o crear una propia
        Cuando se crea una partida nueva se genera un código con el que se puede invitar a otros jugadores
        En esta ventana se especifíca cuantos jugadores tendrá la partida
         */
        initialWindow = new VBox();
        initialWindow.setAlignment(Pos.TOP_CENTER);
        initialWindow.setSpacing(150);
//        initialWindow.setPadding(new Insets(15));

        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setSpacing(15);
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
            } else {
                showAlert("Debe ingresar el nombre del jugador", "Campo requerido", Alert.AlertType.ERROR);
            }
        });


        ImageView background = loadImageView("/res/images/letsPlay.jpg", 250, 1280);

        // colocando estilo
        initialWindow.setId("container-background");
        playerNameInput.setId("text-field");
        playerName.setId("label");
        joinButton.setId("button");
        newGameButton.setId("button");

        formContainer.getChildren().addAll(playerName, playerNameInput, joinButton, newGameButton);
        initialWindow.getChildren().addAll(background, formContainer);

    }

    /**
     * Método que crea la ventana para unirse a una nueva partida
     */
    private void init_joinWindow() {
        ///////////////////Pantalla de unión a partida existente//////////////////////
        joinMatchContainer = new HBox();
        joinMatchContainer.setStyle("-fx-background-color: white");
        joinMatchContainer.setAlignment(Pos.CENTER);
        joinMatchContainer.setSpacing(200);
        joinMatchContainer.setPadding(new Insets(5,0,0,0));

        HBox buttonContainer = backButtonContainer();

        VBox infoContainer = new VBox();
        infoContainer.setStyle("-fx-background-color: white");
        infoContainer.setAlignment(Pos.CENTER);
        infoContainer.setSpacing(15);
//        infoContainer.setPadding(new Insets(15));
//        infoContainer.setPrefHeight(550);

        Label joinTitle = new Label("Ingresa el código de la partida");
        TextField joinTextField = new TextField();
        joinTextField.setMaxWidth(200);
        Button joinButton = new Button("Unirse");
        Label joinResponse = new Label("");

        joinButton.setOnAction(event -> {
            String match_id = joinTextField.getText();
            if (!match_id.equals("")) {
                if (controller.join_match(match_id)) {
                    gameScreenContainer.toFront();
//
                    String message = "El código de la partida es: " + controller.getCurrent_match_id();
                    showAlert(message, "Código de la partida", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("No es posible unirse a la sala", "Error de conexion", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Debe ingresar el código de la partida a la que desea unirse", "Campo requerido", Alert.AlertType.ERROR);
            }
        });

        ImageView background = loadImageView("/res/images/gameRoom.png", 550,550);

        joinMatchContainer.setId("container-background");
        infoContainer.setId("container-background");
        joinTextField.setId("text-field");
        joinTitle.setId("label");
        joinButton.setId("button");

        infoContainer.getChildren().addAll(joinTitle, joinTextField, joinButton, joinResponse);
        joinMatchContainer.getChildren().addAll(buttonContainer, infoContainer, background);
    }

    /**
     * Método que crea la ventana para crear una nueva partida
     */
    private void init_newMatchWindow() {
        /* Window for create a new game displays an comboBox for choose the number of players in the game */
        newMatchWindow = new BorderPane();

        newMatchWindow.setPadding(new Insets(5, 10, 10, 5));

        //espacio del border pane al boton

        HBox buttonContainer = backButtonContainer();
        HBox buttonBackground = new HBox();
        buttonBackground.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBackground.setSpacing(350);

        Label numberPlayers = new Label("Seleccione la cantidad de jugadores");
        numberPlayers.setAlignment(Pos.CENTER);

        VBox matchOptionsContainer = new VBox();
        matchOptionsContainer.setSpacing(20);
        matchOptionsContainer.setAlignment(Pos.CENTER);

        //Create a ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("2");
        comboBox.getItems().add("3");
        comboBox.getItems().add("4");

        Button startButton = new Button("Start Game");
        startButton.setOnMouseClicked(mouseEvent -> {
            int i = comboBox.getSelectionModel().getSelectedIndex();
            if (i > -1) {
                String selected = comboBox.getItems().get(i);
                if (controller.create_match(selected)) {
                    gameScreenContainer.toFront();
//                    playerLoader2();
//                    tokenLoader();
                    String message = "El código de la partida es: " + controller.getCurrent_match_id();
                    showAlert(message, "Código de la partida", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("No se ha podido realizar la conexion con el servidor", "Error de conexion", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Debe seleccionar la cantidad de jugadores", "Campo requerido", Alert.AlertType.ERROR);
            }

        });

        ImageView background = loadImageView("/res/images/letters.jpg", 500, 744);

        // Estilo
        newMatchWindow.setId("container-background");
        matchOptionsContainer.setId("container-background");
        buttonBackground.setId("container-background");
        numberPlayers.setId("label");
        startButton.setId("button");
        comboBox.setId("combo-box-base");
        comboBox.setId("label");


        startButton.setAlignment(Pos.BOTTOM_RIGHT);


        matchOptionsContainer.getChildren().addAll(numberPlayers, comboBox);
        buttonBackground.getChildren().addAll(background, startButton);
        newMatchWindow.setBottom(buttonBackground);
        newMatchWindow.setCenter(matchOptionsContainer);
        newMatchWindow.setLeft(buttonContainer);


        // Alignment.
        BorderPane.setAlignment(startButton, Pos.TOP_RIGHT);
        BorderPane.setAlignment(buttonContainer, Pos.TOP_LEFT);

        // Set margin for bottom area.
        BorderPane.setMargin(startButton, new Insets(10, 10, 10, 10));
    }

    /**
     * @return Generá un contenedor y coloca el botón hacía atrás
     */
    private HBox backButtonContainer() {
        HBox buttonContainer = new HBox();
        buttonContainer.setId("container-background");
        buttonContainer.setAlignment(Pos.TOP_LEFT);
        buttonContainer.setPrefHeight(350);
        ImageView backButton = loadImageView("/res/images/backButton.png", 60, 60);
        backButton.setOnMouseClicked(mouseEvent -> {
            initialWindow.toFront();
        });
        buttonContainer.getChildren().addAll(backButton);

        return buttonContainer;
    }

    /**
     * Método que crea la ventana del juego
     */
    private void init_gameWindow() {
        /////////////////////////////Pantalla de Juego//////////////////////
        gameScreenContainer = new BorderPane();

        //Boton para enviar la palabra
        Image scrabbleImage = imageLoader("/res/images/scrabble.jpg");
        ImageView scrabbleImageButton = new ImageView(scrabbleImage);
        scrabbleImageButton.setFitWidth(150);
        scrabbleImageButton.setFitHeight(150);
        scrabbleImageButton.setOnMouseClicked(mouseEvent -> {
            //tomar palabra creada y enviarla al servidor
//            System.out.println("The word is: ");
            if (unlockedControls) {
                if (lettersList.getSize() > 0) {
                    int response = controller.check_word(lettersList);

                    if (response == 1) {
                        controller.deactivateTokens();
                        lettersList = new LinkedList<>(); //Se resetea cuando la palabra es valida
                        controller.updateInterface();

                    } else if (response == 0) {
                        int option = showOptions();

                        if (option == 0) {      //Llamar experto
                            if (controller.callExpert(lettersList)) {
                                System.out.println("Esperando la respuesta del experto");
                                showAlert("Esperando la respuesta del experto",
                                        "Información",
                                        Alert.AlertType.INFORMATION);
                            } else {
                                System.out.println("No se puede contactar al experto en este momento");
                                showAlert("No se puede contactar al experto en este momento",
                                        "Error de conexión",
                                        Alert.AlertType.ERROR);
                            }
                        } else if (option == 1) {
                            //Reintentar
                            System.out.println("Intentando nuevamente...");

                        } else {
                            System.out.println("Pasar turno...");
                            controller.returnTokens(lettersList);


                            if (!controller.passTurn(lettersList)) {
                                showAlert("Ocurrió un error al contactar con el servidor, inténtalo de nuevo",
                                        "Error de conexión",
                                        Alert.AlertType.ERROR);
                            }
                        }
                    } else {
                        //colocar alert informando error
                        showAlert("No se pudo conectar con el servidor", "Error de conexión", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Debes jugar fichas para continuar", "Error", Alert.AlertType.ERROR);
                }
            }
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

        actiosInMatch = new HBox();
        actiosInMatch.setAlignment(Pos.CENTER);
        actiosInMatch.setSpacing(20);

        ImageView leaveMatch = loadImageView("/res/images/exitIcon.png", 60, 60);
        leaveMatch.setOnMouseClicked(mouseEvent -> {
            controller.disconnect();
            initialWindow.toFront();
        });

        ImageView showInfoButton = loadImageView("/res/images/infoIcon.png", 60, 60);
        showInfoButton.setOnMouseClicked(mouseEvent -> {
            String message = "El código de la partida es: " + controller.getCurrent_match_id();
            showAlert(message, "Código de la partida", Alert.AlertType.INFORMATION);
        });
        actiosInMatch.getChildren().addAll(leaveMatch, showInfoButton);


        // contenedor de la cuadricula
        matrixContainer = new GridPane();
//        matrixContainer.setStyle("-fx-background-color: white;\n" +
//                "    -fx-border-width: 2px; -fx-border-color: black");
        matrixContainer.setGridLinesVisible(true);

        addColumnsAndRows();
        addBoxToGrid();

        rightPlayerInfoContainer = new VBox();
        rightPlayerInfoContainer.setAlignment(Pos.CENTER);
        rightPlayerInfoContainer.setPrefWidth(150);

        leftPlayerInfoContainer = new VBox();
        leftPlayerInfoContainer.setAlignment(Pos.CENTER);
        leftPlayerInfoContainer.setPrefWidth(150);

        gameScreenContainer.setId("container-background");
        upPlayerInfoContainer.setId("container-background");
        matrixContainer.setId("container-background");
        rightPlayerInfoContainer.setId("container-background");
        leftPlayerInfoContainer.setId("container-background");
        matrixContainer.setId("matrix");

        upPlayerInfoContainer.getChildren().addAll(actiosInMatch);
        gameScreenContainer.setTop(upPlayerInfoContainer);
        gameScreenContainer.setBottom(actualPlayerInfoContainer);
        gameScreenContainer.setCenter(matrixContainer);
        gameScreenContainer.setRight(rightPlayerInfoContainer);
        gameScreenContainer.setLeft(leftPlayerInfoContainer);

    }

    /**
     * Este método es para consultarle al jugador que desea hacer en caso de que
     * la palabra formada no sea válida
     * @return Devuelve cada caso
     */
    private int showOptions() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Palabra invalida");
        alert.setHeaderText(null);
        alert.setContentText("¿Qué deseas hacer?");

        ButtonType expert = new ButtonType("Contactar al experto");
        ButtonType retry = new ButtonType("Reintentar");
        ButtonType pass = new ButtonType("Pasar turno");

        alert.getButtonTypes().setAll(expert, retry, pass);
        int optionSelected = 1;
        boolean selected = false;

        while (!selected) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == expert) {
                    selected = true;
                    optionSelected = 0;
                } else if (result.get() == retry) {
                    selected = true;
                    optionSelected = 1;
                } else {
                    selected = true;
                    optionSelected = 2;
                }
            }
        }
        return optionSelected;
    }

    /**
     * Método para sacar al jugador de la partida en caso de que haya un error con el servidor
     */
    public void gameDisconnected() {

        showAlert("Se ha perdido la conexión con el servidor, regresando al menú inicial",
                "Conexión perdida", Alert.AlertType.ERROR);
        initialWindow.toFront();
        System.out.println("The client was disconnected from the server");
    }

    /**
     * Método que le muestra un mensaje al jugador
     * @param message Mensaje que se va a mostrar en la alerta
     * @param title El título del mensaje
     * @param type el tipo de la alerta
     */
    public void showAlert(String message, String title, Alert.AlertType type) {
        Alert showID = new Alert(type);
        showID.setTitle(title);
        showID.setHeaderText(null);
        showID.setContentText(message);
        showID.showAndWait();
    }

    /**
     * Método para construir la palabra que formó el jugador
     * @param actualMatrix Matriz interna actual
     * @return La palabra que formó el jugador
     */
    public String createWord(Token[][] actualMatrix) {
        StringBuilder word = new StringBuilder();
        System.out.println("Lista actual " + lettersList.toString());
        int temp = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Token token = actualMatrix[i][j];
                if (find(token)) {
                    word.append(token.getLetter());
//                    System.out.println("Actual token " + token.getLetter() +  "\n");
                }
            }
        }
        System.out.println("Final word " + word.toString().toLowerCase());
        return word.toString().toLowerCase();
    }

    /**
     * Método que busca un Token en una lista determinada
     * @param tokenToSearch  Token que se va a buscar
     * @return True si la ficha se encontró, false en caso contrario
     */
    private boolean find(Token tokenToSearch) {
        if (tokenToSearch!=null) {
            for (int i = 0; i < lettersList.getSize(); i++) {
                if (tokenToSearch == lettersList.get(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Método que genera una imagen
     * @param relativePath Ruta de la imagen
     * @return El objeto de la imagen creada
     */
    private Image imageLoader(String relativePath) {
        try {
            FileInputStream i = new FileInputStream(cwd + relativePath);
            return new Image(i);
        } catch (FileNotFoundException e) {
            System.out.println("Not found: " + relativePath);
            return null;
        }
    }

    /**
     * Carga en la interfaz los jugadores presentes en la partida
     * @param playersToLoad La lista de jugadores actuales
     */
    public void playerLoader2(LinkedList<Player> playersToLoad) {
        ImageView userImage;
        Text scoreLabel;
        Text userScore;
        Text userName;
        int c = 0;
        LinkedList<ImageView> imageForUser = new LinkedList<>();
        imageForUser.addLast(loadImageView("/res/images/user/player_pink.png", 100, 60));
        imageForUser.addLast(loadImageView("/res/images/user/player_blue.png", 100, 60));
        imageForUser.addLast(loadImageView("/res/images/user/player_red.png", 100, 60));
        imageForUser.addLast(loadImageView("/res/images/user/player_green.png", 100, 60));

//        LinkedList<Player> playersToLoad = controller.getActualGame().getPlayers();

        for (int i = 0; i < playersToLoad.getSize(); i++) {
            Player player = playersToLoad.get(i);
            userImage = imageForUser.get(i);

            VBox playerBox = new VBox();
            playerBox.setAlignment(Pos.CENTER);
            userName = new Text(player.getName());

            //Puntuación del usuario
            HBox userScoreBox = new HBox();
            userScoreBox.setAlignment(Pos.CENTER);
            userScoreBox.setSpacing(10);
            scoreLabel = new Text("Puntos: ");
            userScore = new Text(String.valueOf(player.getScore()));

            userName.setId("label");
            userScore.setId("label");
            scoreLabel.setId("label");


            userScoreBox.getChildren().addAll(scoreLabel, userScore);
            playerBox.getChildren().addAll(userName, userImage, userScoreBox);


            if (player.getplayerId().equals(controller.getPlayerInstance().getplayerId())) {

                Platform.runLater(() -> {
                    System.out.println(this.actualPlayerInfoContainer.getChildren());
                    ImageView scrabbleButton = (ImageView) this.actualPlayerInfoContainer.getChildren().get(1);
                    this.actualPlayerInfoContainer.getChildren().clear();
                    this.actualPlayerInfoContainer.getChildren().addAll(tokenBox, scrabbleButton, playerBox);
                });
//                this.actualPlayerInfoContainer.getChildren().add(playerBox);
            } else {
                switch (c) {
                    case 0:
                        Platform.runLater(() -> {
                            this.rightPlayerInfoContainer.getChildren().clear();
                            this.rightPlayerInfoContainer.getChildren().add(playerBox);
                        });
//
                        break;
                    case 1:
                        Platform.runLater(() -> {
                            this.upPlayerInfoContainer.getChildren().clear();
                            this.upPlayerInfoContainer.getChildren().addAll(actiosInMatch, playerBox);
                        });
                        break;
                    case 2:
                        Platform.runLater(() -> {
                            this.leftPlayerInfoContainer.getChildren().clear();
                            this.leftPlayerInfoContainer.getChildren().add(playerBox);
                        });
                        break;
                }
                c++;
            }

        }
    }

//    /**
//     * Coloca en la interfaz una imagen a cada jugador con su cantidad de fichas
//     */
//    private void putOppositeNumberToken() {
//
//    }

    /**
     * Método para cargar las imágenes de las fichas del jugador
     * @param tokenList Lista con los tokens actuales del jugador
     */
    public void tokenLoader(LinkedList<Token> tokenList) { //Carga las tokens disponibles del jugador (tokenbox)
        Platform.runLater(() -> tokenBox.getChildren().clear());
//        System.out.println("Lista de Tokens: " + tokenList);

        for (int i = 0; i < tokenList.getSize(); i++) {
            Token token = tokenList.get(i);
            token.setActive(true);
//            System.out.println("Adding token " + token.getLetter());
            ImageView letter = loadImageView(token.getImagePath(), 80, 80);

            letter.setOnMouseClicked(mouseEvent -> {
                if (unlockedControls) {
                    if (selectedToken == token) {
                        selectedToken = null;
                    } else {
                        selectedToken = token;
                    }
                }
            });
            Platform.runLater(() -> tokenBox.getChildren().add(letter));
        }
    }

    /**
     * @param path Ruta del archivo
     * @return Un objeto ImageView de la imagen agregada
     */
    private ImageView loadImageView(String path, int height, int width) {
        ImageView imageView = new ImageView(imageLoader(path));
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
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
    private void addBoxToGrid() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                HBox tokenContainer = new HBox();
                tokenContainer.setAlignment(Pos.CENTER);
                tokenContainer.setOnMouseClicked(mouseEvent -> {
                    if (unlockedControls) {
                        int row = GridPane.getRowIndex(tokenContainer);
                        int column = GridPane.getColumnIndex(tokenContainer);

                        if (tokenContainer.getChildren().size() == 0) {
                            if (selectedToken != null) {
//                            putImageOnContainer(tokenContainer);
                                controller.updateTokenList(false, selectedToken); // lo elimino de la lista del objeto jugador local
                                controller.addToken(selectedToken, row, column); // lo agrego a la matriz interna local
                                lettersList.addLast(selectedToken);
                                selectedToken = null;
                            }
                        }
                        else {
                            Token actualToken = controller.getToken(row, column); // Consigue la token de la posición actual
                            if (actualToken.isActive()) {
                                controller.updateTokenList(true, actualToken); // Elimina la token de la lista de tokens del objeto jugador
                                System.out.println("Removing");
                                controller.removeToken(row, column);
                                lettersList.remove(actualToken);

                                if (selectedToken != null) {
//                            putImageOnContainer(tokenContainer);
                                    controller.updateTokenList(false, selectedToken);
                                    controller.addToken(selectedToken, row, column);
                                    lettersList.addLast(selectedToken);
                                    selectedToken = null;
                                }
                            }
                            controller.updateInterface();
                        }
                        //actualizar la interfaz
                    }
                });
                matrixContainer.add(tokenContainer, i, j);
            }
        }
    }

    /**
     * Carga en la interfaz la matriz
     * @param actualMatrix Matriz interna actual
     */
    public void matrixLoader(Token[][] actualMatrix) { //Actualiza la matriz de la interfaz con los datos de la matriz interna
        //i columna j fila

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                HBox child = (HBox) (matrixContainer.getChildren().get((i * 15 + j) + 1));

                if (actualMatrix[i][j] != null) {
                    Token actualToken = actualMatrix[i][j];
                    ImageView image = loadImageView(actualToken.getImagePath(), 30, 30);
                    Platform.runLater(() -> {
                        child.getChildren().clear();
                        child.getChildren().add(image);
                    });

                } else {
                    if (child.getChildren().size() == 1) {
                        Platform.runLater(() -> child.getChildren().clear());

                    }
                }

            }
        }
    }

    /**
     * Bloquea la interfaz mientras los otros jugadores tienen su turno
     */
    public void lockGui() {
        unlockedControls = false;
        Platform.runLater(() -> actualPlayerInfoContainer.setStyle("-fx-background-color: gray;"));
    }

    /**
     * Desbloquea la interfaz para que el jugador actual pueda realizar su turno
     */
    public void unlockGui() {
        unlockedControls = true;
        Platform.runLater(() -> actualPlayerInfoContainer.setStyle("-fx-background-color: white;"));
    }

//    private void addGestureToNewToken() {
//        int size = tokenBox.getChildren().size();
//        ImageView newToken = (ImageView) tokenBox.getChildren().get(size-1);
//        newToken.setOnMouseClicked(mouseEvent -> {
//            if (letterSelected==newToken)
//                letterSelected=null;
//            else
//                letterSelected = newToken;
//        });
//
//    }

//    /**
//     * Coloca la letra de la ficha en la casilla correspondiente
//     */
//    private void putImageOnContainer(HBox imagecontainer){
//
//        ImageView image = loadImageView(selectedToken.getImagePath(), 30, 30);
//        imagecontainer.getChildren().add(image);
////        tokenBox.getChildren().remove(letterSelected);
////        letterSelected = null;
//    }



    /**
     * Este método se encarga de cargar el estado actual del juego
     */
    public void loadMatch() {

    }


}
