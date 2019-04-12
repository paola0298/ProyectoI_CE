package Test;

import GUI.Scrabble;
import Logic.Game;
import Logic.Player;
import Logic.Token;
import Sockets.Client;
import Structures.LinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Controller2 {
    private JSONObject message;
    private JSONObject response;
    private String cwd = System.getProperty("user.dir");
    private Client client;
    private Scrabble2 gui;

    private String playerName = "-";
    private String player_id = "-";
    private String current_match_id = "-";
    private Game actualGame;
    private Player playerInstance;
    private Token[][] grid;


    /**
     * Este método es el constructor de la clase
     *
     * @param gui La interfaz que va a controlar la clase
     */
    public Controller2(Scrabble2 gui) {
        this.gui = gui;
        initialize();
    }

    /**
     * @return Devuelve la instancia de la partida actual
     */
    public Game getActualGame() {
        return actualGame;
    }

    /**
     * @return Devuelve el identificador de la partida actual
     */
    public String getCurrent_match_id() {
        return current_match_id;
    }

    /**
     * @return La instancia del jugador actual
     */
    public Player getPlayerInstance() {
        return playerInstance;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void addToken(Token tokenToAdd, int row, int column) {
        grid[column][row] = tokenToAdd;
    }

    public void removeToken(int row, int column) {
        grid[column][row] = null;
    }

    public Token getToken(int row, int column) {
        return grid[column][row];
    }

    /**
     * @param flag  Bandera que determina si se va a eliminar (false) o agregar (true) un token
     * @param token Token que se va a agregar o eliminar
     * @return Nueva lista de tokens actualizada
     */
    public void updateTokenList(boolean flag, Token token) {
        //TODO llamar este metodo cuando se coloca una ficha en el tablero o se elimina
        LinkedList<Token> actualList = getPlayerInstance().getTokenlist();

        if (flag) {
            actualList.addLast(token);
        } else {
            actualList.remove(token);
        }
        playerInstance.setTokenlist(actualList);
    }

    /**
     * Este método le pide al servidor crear una nueva partida con los jugadores máximos especificados
     *
     * @param max_players Cantidad máxima de jugadores en la partida.
     */
    public boolean create_match(String max_players) {
        message = prepare();
        message.put("action", "CREATE_MATCH");
        message.put("max_players", max_players);
        message.put("player_name", playerName);

        response = client.connect(message);

        if (response.get("status").equals("SUCCESS")) {
            System.out.println("Match created successfully");
            player_id = response.getString("player_id");
            current_match_id = response.getString("game_id");
            deserialize();
            updatePlayers();
            updateTokens();
            grid = actualGame.getGrid();
            gui.lockGui();
            waitTurn();
            return true;
        } else {
            System.out.println("Could not create match");
            return false;
        }
    }

    private void updatePlayers() {
        LinkedList<Player> actualPlayers = actualGame.getPlayers();
        gui.playerLoader2(actualPlayers);
    }

    public void updateTokens() {
        LinkedList<Token> actualtoken = playerInstance.getTokenlist();
        gui.tokenLoader(actualtoken);
    }

    public void updateInterface() {
        updateTokens();
        gui.matrixLoader(grid);
    }

    /**
     * Este método se conecta con el servidor cada 2 segundos para verificar si ya es el turno del jugador
     */
    private void waitTurn() {
        Thread caller = new Thread(() -> {
            boolean isTurn = false;
            while (!isTurn) {
                try {
                    message = prepare();
                    message.put("action", "CHECK_TURN");
                    message.put("match_id", current_match_id);
                    response = client.connect(message);

                    if (response.get("status").equals("SUCCESS")) {
                        System.out.println("Ya es tu turno");
                        isTurn = true;
                        deserialize();
                        updatePlayers();
                        updateTokens();
                        grid = actualGame.getGrid();

                    } else {
                        System.out.println("Aún no es tu turno");
                    }
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //TODO método a llamar para desbloquear la interfaz i.e unlockGUI();
            gui.unlockGui();
        });

        caller.setDaemon(true);
        caller.start();
    }

    /**
     * Este método le pide al servidor unir al jugador a una partida existente.
     *
     * @param match_id El identificador de la partida a la que se desea unir
     */
    public boolean join_match(String match_id) {
        message = prepare();
        message.put("action", "JOIN_MATCH");
        message.put("player_name", this.playerName);
        message.put("match_id", match_id);

        response = client.connect(message);

//        System.out.println(response.toString());

        if (response.get("status").equals("SUCCESS")) {
            System.out.println("Joined match succesfully");
            player_id = response.getString("player_id");
            this.current_match_id = match_id;
            deserialize();
            updatePlayers();
            updateTokens();
            grid = actualGame.getGrid();
            gui.lockGui();
            waitTurn();
            return true;
        } else {
            System.out.println("Could not join match");
            return false;
        }
    }

    /**
     * Deserializa los objetos recibidos desde el servidor
     */
    private void deserialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        String stringGame = response.getString("game");
        String stringPlayer = response.getString("player");

        try {
            actualGame = objectMapper.readValue(stringGame, Game.class);
            playerInstance = objectMapper.readValue(stringPlayer, Player.class);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error getting game object");
        }
    }

    /**
     * Este método le pide al servidor que desconecte al servidor de la partida actual.
     */
    public void leave_match() {
        message = prepare();
        message.put("action", "DISCONNECT");
        message.put("match_id", current_match_id);

        response = client.connect(message);

        if (response.get("status").equals("SUCCESS")) {
            System.out.println("Disconnected successfully");
            this.current_match_id = "-";
        } else {
            System.out.println("An error ocurred");
        }
    }

    /**
     * Este método le pide al servidor verificar si la palabra es válida.
     *
     * @param word Palabra a verificar en el servidor.
     */
    public void check_word(String word) {
//        message = prepare();
//        message.put("action", "CHECK_WORD");
//        message.put("match_id", current_match_id);
//        message.put("word", word);
//
//        response = client.connect(message);
//
//        if (response.get("response").equals("VALID")) {
//            System.out.println("The word is valid");
//        } else {
//            System.out.println("Try again");
//        }
        gui.searchWord(grid);
//        gui.test(grid);
    }


    /**
     * Este método verifica si ya es el turno del jugador.
     */
    public void check_turn() {
        message = prepare();
        message.put("action", "CHECK_TURN");
        message.put("match_id", current_match_id);
        response = client.connect(message);

    }

    public void callExpert() {
        message = prepare();
        message.put("action", "CALL_EXPERT");
        response = client.connect(message);
    }

    /**
     * Este método prepara un nuevo objeto JSON para usar en los pedidos al servidor.
     *
     * @return Objeto JSON base para generar cada pedido al servidor.
     */
    private JSONObject prepare() {
        JSONObject object = new JSONObject();
        object.put("player_id", this.player_id);
        return object;
    }

    /**
     * Este método carga las configuraciones del archivo de configuración, e instancia la clase Client.
     */
    private void initialize() {
        Properties props = new Properties();
        try {
            FileInputStream stream = new FileInputStream(cwd + "/res/settings.properties");
            props.load(stream);
            String host = props.get("host_ip").toString();
            int port = Integer.parseInt(props.get("host_port").toString());
            this.client = new Client(host, port);
            System.out.println("[Info] Settings initialized successfully");
        } catch (IOException e) {
            System.out.println("[Error] Could not read settings");
        }
    }

    private void initialize1() {
        client = new Client("localhost", 6307);
    }

}