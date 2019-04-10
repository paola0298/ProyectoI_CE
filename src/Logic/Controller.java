package Logic;

import GUI.Scrabble;
import Sockets.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Controller {
    private JSONObject message;
    private JSONObject response;
    private String cwd = System.getProperty("user.dir");
    private Client client;
    private Scrabble gui;

    private String playerName = "-";
    private String player_id = "-";
    private String current_match_id = "-";
    private Game actualGame;
    private Player playerInstance;


    /**
     * Este método es el constructor de la clase
     * @param gui La interfaz que va a controlar la clase
     */
    public Controller(Scrabble gui) {
        this.gui = gui;
        initialize1();
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
     * Este método coloca la instancia de la partica actual
     * @param actualGame Instancia de la partida actual
     */
    public void setActualGame(Game actualGame) {
        this.actualGame = actualGame;
    }

    /**
     * @return La instancia del jugador actual
     */
    public Player getPlayerInstance() {
        return playerInstance;
    }

    /**
     * Este método coloca la instancia del jugador actual
     * @param playerInstance Instancia del jugador actual
     */
    public void setPlayerInstance(Player playerInstance) {
        this.playerInstance = playerInstance;
    }

    /**
     * Este método le coloca el nombre al jugador
     * @param playerName Parámetro del nombre del jugador
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Este método le pide al servidor crear una nueva partida con los jugadores máximos especificados
     * @param max_players Cantidad máxima de jugadores en la partida.
     */
    public void create_match(String max_players) {
        message = prepare();
        message.put("action", "CREATE_MATCH");
        message.put("max_players", max_players);
        message.put("player_name", playerName);

        response = client.connect(message);

        if (response.get("status").equals("SUCCESS")) {
            System.out.println("Match created successfully");
            player_id = response.getString("player_id");
            current_match_id = response.getString("game_id");
            getInstances();

        } else {
            System.out.println("Could not create match");
        }
    }

    /**
     * Este método le pide al servidor unir al jugador a una partida existente.
     */
    public boolean join_match(String match_id) {
        message = prepare();
        message.put("action", "JOIN_MATCH");
        message.put("player_name", this.playerName);
        message.put("match_id", match_id);

        response = client.connect(message);

        if (response.getBoolean("status")) {
            System.out.println("Joined match succesfully");
            player_id = response.getString("player_id");
            this.current_match_id = match_id;

            getInstances();
            return true;
        } else {
            System.out.println("Could not join match");
            return false;
        }
    }

    private void getInstances() {
        ObjectMapper objectMapper =  new ObjectMapper();
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
     * @param word Palabra a verificar en el servidor.
     */
    public void check_word(String word) {
        message = prepare();
        message.put("match_id", current_match_id);
        message.put("word", word);

        response = client.connect(message);

        if (response.get("response").equals("VALID")) {
            System.out.println("The word is valid");
        } else {
            System.out.println("Try again");
        }
    }

    /**
     * Este método verifica si ya es el turno del jugador.
     */
    public void check_turn() {
        message = prepare();
        message.put("match_id", current_match_id);
        response = client.connect(message);

    }

    /**
     * Este método prepara un nuevo objeto JSON para usar en los pedidos al servidor.
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

    private void initialize1(){
        client = new Client("localhost", 7123);
    }



}
