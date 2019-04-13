package Test;

import Logic.Game;
import Logic.Player;
import Sockets.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ExpertServiceTest {
    private String player_id;
    private String current_match_id;
    private String playerName;
    private JSONObject message;
    private JSONObject response;
    private Client client;
    private Game game;
    private Player player;

    public static void main(String[] args) {
        String host = "";
        int port = 0;
        ExpertServiceTest test = new ExpertServiceTest("Marlon", "localhost", 6307);



    }

    public ExpertServiceTest(String playerName, String host, int port) {
        this.playerName = playerName;
        this.client = new Client(host, port);
    }

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
            System.out.println(response.toString(4));
            waitTurn();
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
            game = objectMapper.readValue(stringGame, Game.class);
            player = objectMapper.readValue(stringPlayer, Player.class);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error getting game object");
        }
    }

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
                    } else {
                        System.out.println("Aún no es tu turno");
                    }
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                }
            }
        });

        caller.setDaemon(true);
        caller.start();
    }

    private JSONObject prepare() {
        JSONObject object = new JSONObject();
        object.put("player_id", this.player_id);
        return object;
    }

    public void callExpert(String phoneNum, String wordToCheck) {
        message = prepare();
        message.put("action", "CALL_EXPERT");
        message.put("match_id", current_match_id);
        message.put("expert_phone", phoneNum);
        message.put("word_to_check", wordToCheck);

        response = client.connect(message);

        if (response.get("status").equals("CONTACTING")) {
            System.out.println("Waiting for expert to answer");
            checkOnExpert();
        }
    }

    public void checkOnExpert() {
        Thread caller = new Thread(() -> {
            boolean check = true;
            while (check) {
                try {
                    message = prepare();
                    message.put("action", "DID_EXPERT_ANSWER");
                    message.put("match_id", current_match_id);

                    response = client.connect(message);

                    if (response.get("status").equals("ANSWERED")) {
                        System.out.println("El experto ya respondió");
                        check = false;
//                        showExpertAnswer(response.getString("expert_answer"));
                        System.out.println("Expert answer: " + response.getString("expert_answer"));

                    } else {
                        System.out.println("Aún no ha respondido el experto");
                    }
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            gui.unlockGui();
        });

        caller.setDaemon(true);
        caller.start();
    }

}
