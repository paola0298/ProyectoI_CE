package Sockets;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class ClientPool {
    public static void main(String[] args) {
        String host = "192.168.100.24";
        int port = 6307;
        int connections = 10;
        String message= "Esto es un mensaje de prueba!";
        String[] actions = new String[5];
        actions[0] = "CREATE_MATCH";
        actions[1] = "JOIN_MATCH";
        actions[2] = "CHECK_WORD";
        actions[3] = "CALL_EXPERT";
        actions[4] = "CHECK_TURN";

        Random random = new Random();

        ArrayList<Client> list = new ArrayList<>();

        for (int i=0; i<connections; i++) {
            list.add(new Client(host, port));
            System.out.println("Creating client " + i);
        }

        System.out.println("\n");

        for (int i=0; i<connections; i++){
            System.out.println("Client " + i + " connecting to server" );

            JSONObject obj = new JSONObject();
            obj.put("action", actions[random.nextInt(5)]);
            obj.put("player_id", "client_" + i);
            JSONObject response = new JSONObject(list.get(i).connect(obj.toString()));
            System.out.println(response.toString(2));
        }

        System.out.println("Done");
    }
}
