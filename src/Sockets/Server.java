package Sockets;

import Logic.Game;
import Logic.Player;
import Logic.Token;
import Structures.LinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
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

            Socket con = clientConnection();
            System.out.println("Conexion establecida");

            JSONObject msg = new JSONObject(receiveDataFromClient(con));

            System.out.println("JSON received: " + msg.toString());

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
//                    response.put("WORD_STATUS", "INVALID");
//                    sendResponse(response.toString(), con);
                    break;
                case "CHECK_TURN":
//                    response.put("YOUR_TURN", "NO");
//                    sendResponse(response.toString(), con);
                    break;
                case "GET_GAME":
                    // send the actual game
                    String match = msg.getString("match_id");
                    response = getActualGame(match);
                    sendResponse(response.toString(), con);
                    break;
                default:
                    sendResponse("Palabra clave no encontrada", con);
            }
            try {
                con.close();
            } catch (IOException e) {
                System.out.println("Error closing the connection " + e.getMessage());
            }

            System.out.println("\n");
        }

    }

    private JSONObject getActualGame(String matchID){
        JSONObject jsonObject = new JSONObject();
        Game actualGame = null;
        String result = "";
        for (int i=0; i<gamesList.getSize(); i++) {
            if (gamesList.get(i).getGameID().equals(matchID)) {
                actualGame = gamesList.get(i);
            }
        }

        ObjectMapper objectMapper =  new ObjectMapper();
        try {

            result = objectMapper.writeValueAsString(actualGame);
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("object_game", result);
            return jsonObject;

        } catch (IOException e) {
            e.getMessage();
        }
        return jsonObject;
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
        return TokenList;
    }
    /**
    *Se completa la lista que contiene todas las fichas disponibles para el juego, se agrupan las fichas que comparten la frecuencia en la que aparecen
     * @author Brayan
    *
    */
    public void fillTokenList(){
        Token A = new Token("res/images/token/A.png", 1, "A");
        Token E = new Token("res/images/token/E.png", 1, "E");
        Token O = new Token("res/images/token/O.png", 1, "O");
        Token I = new Token("res/images/token/I.png", 1, "I");
        Token S = new Token("res/images/token/S.png", 1, "S");
        Token N = new Token("res/images/token/N.png", 1, "N");
        Token R = new Token("res/images/token/R.png", 1, "R");
        Token U = new Token("res/images/token/U.png", 1, "U");
        Token D = new Token("res/images/token/D.png", 2, "D");
        Token L = new Token("res/images/token/L.png", 1, "L");
        Token T = new Token("res/images/token/T.png", 1, "T");
        Token C = new Token("res/images/token/C.png", 3, "C");
        Token G = new Token("res/images/token/G.png", 2, "G");
        Token B = new Token("res/images/token/B.png", 3, "B");
        Token M = new Token("res/images/token/M.png", 3, "M");
        Token P = new Token("res/images/token/P.png", 3, "P");
        Token H = new Token("res/images/token/H.png", 4, "H");
        Token F = new Token("res/images/token/F.png", 4, "F");
        Token V = new Token("res/images/token/V.png", 4, "V");
        Token Y = new Token("res/images/token/Y.png", 4, "Y");
        Token CH = new Token("res/images/token/CH.png", 5, "CH");
        Token Q = new Token("res/images/token/Q.png", 5, "Q");
        Token J = new Token("res/images/token/J.png", 8, "J");
        Token LL = new Token("res/images/token/LL.png", 8, "LL");
        Token Ñ = new Token("res/images/token/Ñ.png", 8, "Ñ");
        Token RR = new Token("res/images/token/RR.png", 8, "RR");
        Token X = new Token("res/images/token/X.png", 8, "X");
        Token Z = new Token("res/images/token/Z.png", 10, "Z");
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
            this.TokenList.addFirst(F);
            this.TokenList.addFirst(V);
            this.TokenList.addFirst(Y);
            this.TokenList.addFirst(CH);
            this.TokenList.addFirst(Q);
            this.TokenList.addFirst(J);
            this.TokenList.addFirst(LL);
            this.TokenList.addFirst(Ñ);
            this.TokenList.addFirst(RR);
            this.TokenList.addFirst(X);
            this.TokenList.addFirst(Z);

        }
        for (int bonus = 1;bonus <= 2;bonus++){
            this.TokenList.addFirst(Bonus);
        }

    }

    public static void main(String[] args) {




        Server server = new Server(6307);
        Player p = new Player("Brayan");
        System.out.println(server.getTokenList());
//        p.create_ID();
        p.assign_tokens(server.getTokenList());
        System.out.println("Servidor iniciado...");
        server.connectionListener();

    }
}
