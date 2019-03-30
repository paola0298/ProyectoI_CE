package Logic;

import EnumTypes.ACTIONS;
import GUI.Scrabble;
import Sockets.Client;
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

    private String player_id = "-";
    private String current_match_id = "-";

    public Controller(Scrabble gui) {
        this.gui = gui;
        initialize();
    }

    /**
     * Este método le pide al servidor crear una nueva partida con los jugadores máximos especificados
     * @param max_players Cantidad máxima de jugadores en la partida.
     */
    public void create_match(int max_players) {
        message = prepare();
        message.put("action", "CREATE_MATCH");
        message.put("max_players", max_players);

        response = client.connect(message);

        if (response.get("status").equals("SUCCESS")) {
            System.out.println("Match created successfully");
            player_id = response.getString("player_id");
        } else {
            System.out.println("Could not create match");
        }
    }

    /**
     * Este método le pide al servidor unir al jugador a una partida existente.
     */
    public void join_match(String match_id) {
        message = prepare();
        message.put("action", "JOIN_MATCH");
        message.put("match_id", match_id);

        response = client.connect(message);

        if (response.get("status").equals("SUCCESS")) {
            System.out.println("Joined match succesfully");
            player_id = response.getString("player_id");
            this.current_match_id = match_id;
        } else {
            System.out.println("Could not join match");
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
        object.put("player_id", player_id);
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

}
