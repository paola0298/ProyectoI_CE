package Sockets;

import Logic.Game;
import Logic.Player;
import Logic.Token;
import Structures.LinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Paola
 * @version 1.0
 *
 * La clase Server realiza la conexión con el cliente, además envía y recibe mensajes
 */
public class Server {
    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private LinkedList<Game> gamesList = new LinkedList<>();
    private LinkedList<Token> TokenList = new LinkedList<>();

    /**
     * @param port Puerto en el cual el servidor esta escuchando
     */
    public Server(int port){
        fillTokenList();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating the socket " + e.getMessage());
        }
    }

    /**
     * @return La conexion con el cliente
     */
    public Socket clientConnection(){
        Socket con = null;
        try {
            con = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Error connecting to the client " + e.getMessage());
        }
        return con;
    }

    /**
     * @return El mensaje recibido del cliente
     */
    public String receiveDataFromClient(Socket con){
        String actualMessage = "";
        try {
            DataInputStream inputStream = new DataInputStream(con.getInputStream());
            actualMessage = inputStream.readUTF();
        } catch (EOFException ex) {
            System.out.println("Error reading stream " + ex.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading stream " + e.getMessage());
        }
        return actualMessage;
    }

    /**
     * @param response Respuesta para el cliente
     * @param con Conexion con el cliente
     */
    public void sendResponse(String response, Socket con){
        try {
            DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
            outputStream.writeUTF(response);
        } catch (IOException e) {
            System.out.println("Error writing stream " + e.getMessage());
        }
    }


    /**
     * Escucha las conexiones del cliente
     */
    public void connectionListener(){
        //Posible thread
//        Runnable task = () -> {
//            //codigo a ejecutar
//        };
//
//        Thread worker = new Thread(task);
//        worker.setDaemon(true);
//        worker.start();
//
//        Thread thread = new Thread(() -> {
//            //codigo a ejecutar
//        });
//        thread.setDaemon(true); // para que termine de ejecutarse, al terminar ejecucion principal que lo llama
//        thread.start();
//

        while (this.isRunning){
            System.out.println("Esperando conexión");
            Socket con = clientConnection();
            System.out.println("Conexion establecida");

            JSONObject msg = new JSONObject(receiveDataFromClient(con));

//            System.out.println("JSON received: " + msg.toString());

            JSONObject response;

            switch (msg.get("action").toString()) {
                case "CREATE_MATCH":
//                    response.put("CODE", "123456");
//                    sendResponse(response.toString(), con);

                    break;
                case "JOIN_MATCH":
//                    response.put("ID_PARTIDA", "1");
//                    response.put("MATRIZ", "matriz");
//                    sendResponse(response.toString(), con);
                    String matchID = msg.getString("match_id");
                    String playerName = msg.getString("player_name");
                    response = joinMatch(matchID, playerName);
                    break;
                case "CHECK_WORD":
//                    response.put("WORD_STATUS", "VALID");
//                    sendResponse(response.toString(), con);
                    break;
                case "CALL_EXPERT":
                    response = new JSONObject();

//                    if (msg.getString("status").equals("WAITING")) {
//                        if (!expertAnswer.equals("")) {
//                            response.put("status", "ANSWERED");
//                            response.put("WORD_STATUS", expertAnswer);
//                            contact_expert = false;
//                            expert_phone = "";
//                            expert_word = "";
//                            player_id = "";
//                        } else {
//                            response.put("status", "CONTACTING");
//                        }
//                    } else {
//                        contact_expert = true;
//                        expert_phone = msg.getString("phone");
//                        expert_word = msg.getString("word");
//                        player_id = msg.getString("player_id");
//
//                        response.put("status", "CONTACTING");
//                    }

                    sendResponse(response.toString(), con);
                    System.out.println("Esperando a contactar experto");
                    break;
                case "CHECK_TURN":
//                    response.put("YOUR_TURN", "NO");
//                    sendResponse(response.toString(), con);
                    break;
                case "EXPERT_SERVICE":
                    response = new JSONObject();

//                    if (!contact_expert && !waiting_ex_response) {
//                        response.put("status", "NO");
//
//                    } else if (contact_expert && !waiting_ex_response){
//                        contact_expert = false;
//                        waiting_ex_response = true;
//                        response.put("status", "SEND_SMS");
//                        response.put("phone", expert_phone);
//                        response.put("word", expert_word);
//                    } else {
//                        if (msg.getString("status").equals("WAITING")) {
//                            response.put("status", "WAITING");
//                        } else if (msg.getString("status").equals("ANSWERED")) {
//                            waiting_ex_response = false;
//                            expertAnswer = msg.getString("expert_answer");
//                        }
//                    }

                    sendResponse(response.toString(), con);
                    break;
                default:
                    sendResponse("Palabra clave no encontrada", con);
            }
            try {
                con.close();
                System.out.println("Conexión finalizada");
            } catch (IOException e) {
                System.out.println("Error closing the connection " + e.getMessage());
            }

            System.out.println("\n");
        }

    }

    private JSONObject joinMatch(String id, String name) {
        JSONObject obj = new JSONObject();
        Game game;
        for (int i=0; i<gamesList.getSize(); i++) {
            if (gamesList.get(i).getGameID().equals(id)) {
                game = gamesList.get(i);
                Player player = new Player(name);
                obj.put("status", game.addPlayer(player)); //Status define si el jugador pudo ingresar o no a la partida
                obj.put("player_id", player.getPlayer_ID());

            }
        }

        ObjectMapper mapper = new ObjectMapper();


        return obj;
    }

    public LinkedList<Token> getTokenList() {
        //TokenList.printList();
        return TokenList;
    }

    public void fillTokenList(){
        Token A = new Token("*****", 1, "A");
        Token E = new Token("*****", 1, "E");
        Token O = new Token("*****", 1, "O");
        Token I = new Token("*****", 1, "I");
        Token S = new Token("*****", 1, "S");
        Token N = new Token("*****", 1, "N");
        Token R = new Token("*****", 1, "R");
        Token U = new Token("*****", 1, "U");
        Token D = new Token("*****", 2, "D");
        Token L = new Token("*****", 1, "L");
        Token T = new Token("*****", 1, "T");
        Token C = new Token("*****", 3, "C");
        Token G = new Token("*****", 2, "G");
        Token B = new Token("*****", 3, "B");
        Token M = new Token("*****", 3, "M");
        Token P = new Token("*****", 3, "P");
        Token H = new Token("*****", 4, "H");
        Token F = new Token("*****", 4, "F");
        Token V = new Token("*****", 4, "V");
        Token Y = new Token("*****", 4, "Y");
        Token CH = new Token("*****", 5, "CH");
        Token Q = new Token("*****", 5, "Q");
        Token J = new Token("*****", 8, "J");
        Token LL = new Token("*****", 8, "LL");
        Token Ñ = new Token("*****", 8, "Ñ");
        Token RR = new Token("*****", 8, "RR");
        Token X = new Token("*****", 8, "X");
        Token Z = new Token("*****", 10, "Z");
        Token Bonus = new Token("*****", 0, "");


        //Se añaden las fichas A y E a la lista
        for (int ae = 1;ae <= 12; ae++){
            TokenList.addLast(A);
            TokenList.addFirst(E);
        }
        //Se añade la ficha O a la lista
        for (int o = 1;o <= 9;o++){
            this.TokenList.addFirst(O);
        }
        //Se añaden las fichas I y S a la lista
        for (int is = 1;is <= 6;is++){
            this.TokenList.addFirst(I);
            this.TokenList.addFirst(S);
        }
        //Se añaden las fichas N,R,U,D a la lista
        for (int nrud = 1;nrud <= 5;nrud++){
            this.TokenList.addFirst(N);
            this.TokenList.addFirst(R);
            this.TokenList.addFirst(U);
            this.TokenList.addFirst(D);
        }
        //Se añaden las fichas L,T,C a la lista
        for (int ltc = 1;ltc <= 4;ltc++){
            this.TokenList.addFirst(L);
            this.TokenList.addFirst(T);
            this.TokenList.addFirst(C);
        }
        //Se añaden las fichas G,B,M,P,H a la lista
        for (int gbmph = 1;gbmph <= 2;gbmph++){
            this.TokenList.addFirst(G);
            this.TokenList.addFirst(B);
            this.TokenList.addFirst(M);
            this.TokenList.addFirst(P);
            this.TokenList.addFirst(H);
        }
        //Se añaden las fichas F,V,Y,CH,Q,J,LL,Ñ,RR,X,Z a la lista
        for (int fvychqjllñrrxz = 1;fvychqjllñrrxz <= 1;fvychqjllñrrxz++){
            this.TokenList.addFirst(G);
            this.TokenList.addFirst(B);
            this.TokenList.addFirst(M);
            this.TokenList.addFirst(P);
            this.TokenList.addFirst(H);
        }
        for (int bonus = 1;bonus <= 2;bonus++){
            this.TokenList.addFirst(Bonus);
        }
        //TokenList.printList();
    }

    public static void main(String[] args) {
        int port = 7123;
        Server server = new Server(port);
        System.out.println("Servidor iniciado en puerto " + port);
        server.connectionListener();

    }
}
