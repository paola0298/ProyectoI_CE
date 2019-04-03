package Logic;

import EnumTypes.ACTIONS;
import GUI.Scrabble;
import Sockets.Client;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Controller {
    private String player_id = "-";
    private JSONObject message;
    private JSONObject response;
    private String cwd = System.getProperty("user.dir");
    private Client client;
    private Scrabble gui;

    public Controller(Scrabble gui) {
        this.gui = gui;
        initialize();
    }

    public void doAction(ACTIONS action) {
        message = new JSONObject();
        message.put("player_id", player_id);

        switch (action) {
            case CREATE_MATCH:
                System.out.println("Creating match");
                message.put("action", "CREATE_MATCH");
                message.put("max_players", "4");
                handleResponse(client.connect(message.toString()));
                break;
            case JOIN_MATCH:
                System.out.println("Joining match");
                break;
        }
    }

    private void handleResponse(String res) {
        response = new JSONObject(res);
        switch (response.get("CODE").toString()) {
            case "123456":
        }
    }

    /**
     * Carga las configuraciones del archivo de configuración, e instancia la clase Client.
     */
    public void initialize() {
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
