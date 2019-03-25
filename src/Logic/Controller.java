package Logic;

import EnumTypes.ACTIONS;
import GUI.Scrabble;
import Sockets.Client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Controller {
    private String cwd = System.getProperty("user.dir");
    private Client client;
    private Scrabble gui;

    public Controller() {
        gui = new Scrabble();
        gui.setController(this);
        initialize();
    }

    public void start() {
        Thread guiThread = new Thread(() -> gui.show());
        guiThread.setDaemon(true);
        guiThread.start();
    }

    public void doAction(ACTIONS action) {
        switch (action) {
            case CREATE_MATCH:
                break;
            case JOIN_MATCH:
                break;
        }
    }

    /**
     * Inicializa las configuraciones del archivo de configuración, e instancia la clase Client.
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
