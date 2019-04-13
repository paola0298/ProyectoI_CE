package Test;

import GUI.Scrabble;
import Logic.Game;
import Logic.Player;
import Logic.Token;
import Sockets.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class DummyClient {
    private JSONObject message;
    private JSONObject response;
    private String cwd = System.getProperty("user.dir");
    private Client client;

    private String playerName = "-";
    private String player_id = "-";
    private String current_match_id = "-";
    private Game actualGame;
    private Player playerInstance;
    private Token[][] grid;

    public DummyClient(String name, String host, int port) {
        this.playerName = name;
        initialize(host, port);
    }

    public boolean create_match(String max_players) {
        message = prepare();
        message.put("action", "CREATE_MATCH");
        message.put("max_players", max_players);
        message.put("player_name", playerName);

        response = client.connect(message);
        System.out.println(response.toString(4));

        if (response.get("status").equals("SUCCESS")) {
            System.out.println("Match created successfully");
            player_id = response.getString("player_id");
            current_match_id = response.getString("game_id");
            deserialize();
//            updatePlayers();
//            updateTokens();
            grid = actualGame.getGrid();
            return true;
        } else {
            System.out.println("Could not create match");
            return false;
        }
    }

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

    public boolean join_match(String match_id) {
        message = prepare();
        message.put("action", "JOIN_MATCH");
        message.put("player_name", this.playerName);
        message.put("match_id", match_id);

        response = client.connect(message);

        System.out.println(response.toString());

        if (response.getBoolean("status")) {
            System.out.println("Joined match succesfully");
            player_id = response.getString("player_id");
            this.current_match_id = match_id;
//            deserialize();
//            updatePlayers();
//            updateTokens();
            grid = actualGame.getGrid();
            return true;
        } else {
            System.out.println("Could not join match");
            return false;
        }
    }

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

    private void initialize(String host, int port) {
        this.client = new Client(host, port);
    }

    private JSONObject prepare() {
        JSONObject object = new JSONObject();
        object.put("player_id", this.player_id);
        return object;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public static void main(String[] args) {
        DummyClient client = new DummyClient("Marlon", "localhost", 6307);

        Scanner scanner = new Scanner(System.in);
        boolean isActive = true;
        while (isActive) {
            System.out.print("Ingresa una opción: ");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.print("Max players: ");
                    boolean flag = client.create_match(scanner.next());
                    System.out.println("Created? " + flag);
                    break;

                case 2:
                    System.out.print("Match id: ");
                    String match = scanner.next();
                    boolean flag2 = client.join_match(match);
                    System.out.println("Joined? " + flag2);
                    break;

                case 3:
                    System.out.println("Leaving match");
                    client.leave_match();
                    break;

                case 4:
                    System.out.println("Terminating");
                    isActive = false;
            }
        }
    }
}
