package Sockets;

import Logic.Game;
import Logic.Player;
import Logic.Token;
import Structures.LinkedList;
import Structures.Node;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


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
    private LinkedList<Token> totalTokenList = new LinkedList<>();

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
            String playerName;

            System.out.println("JSON received: " + msg.toString());

            JSONObject response;

            switch (msg.get("action").toString()) {
                case "CREATE_MATCH":
//                    response.put("CODE", "123456");
//                    sendResponse(response.toString(), con);
                    int maxPlayers = Integer.parseInt(msg.getString("max_players"));
                    playerName = msg.getString("player_name");
                    response = createMatch(maxPlayers, playerName);
                    sendResponse(response.toString( ), con);

                    break;
                case "JOIN_MATCH":
//                    response.put("ID_PARTIDA", "1");
//                    response.put("MATRIZ", "matriz");
//                    sendResponse(response.toString(), con);
                    String matchID = msg.getString("match_id");
                    playerName = msg.getString("player_name");
                    response = joinMatch(matchID, playerName);
                    sendResponse(response.toString(), con);

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
                default:
                    sendResponse("Palabra clave no encontrada", con);
            }

            System.out.println("Cantidad de juegos creados " + gamesList.getSize());

            try {
                con.close();
            } catch (IOException e) {
                System.out.println("Error closing the connection " + e.getMessage());
            }

            System.out.println("\n");
        }

    }


    /**
     * Método para crear una nueva partida
     * @param maxPlayers Cantidad máxima de jugadores para la partida
     * @param playerName Nombre del jugador que crea la partida
     * @return Objeto Json para enviarlo al cliente
     */
    private JSONObject createMatch(int maxPlayers, String playerName){
        JSONObject jsonObject = new JSONObject();
        String serializedGame = "";
        String serializedPlayer = "";
        Game newGame = new Game(maxPlayers);
        gamesList.addLast(newGame);
        Player newPlayer = new Player(playerName);
        newPlayer.setTokenlist(generateTokens());
        newGame.addPlayer(newPlayer);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            serializedGame = objectMapper.writeValueAsString(newGame);
            serializedPlayer = objectMapper.writeValueAsString(newPlayer);
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("game", serializedGame);
            jsonObject.put("player", serializedPlayer);
            jsonObject.put("game_id", newGame.getGameID());
            jsonObject.put("player_id", newPlayer.getPlayer_ID());
            return jsonObject;
        } catch (JsonProcessingException e) {
            jsonObject.put("status", "FAILED");
            return jsonObject;
        }

    }

    private JSONObject joinMatch(String id, String name) {
        JSONObject obj = new JSONObject();
        Game game;
        String serializedGame = "";
        String serializedPlayer = "";
        for (int i=0; i<gamesList.getSize(); i++) {
            if (gamesList.get(i).getGameID().equals(id)) {
                game = gamesList.get(i);
                Player player = new Player(name);
                player.setTokenlist(generateTokens());

                ObjectMapper mapper = new ObjectMapper();

                try {
                    serializedGame = mapper.writeValueAsString(game);
                    serializedPlayer = mapper.writeValueAsString(player);
                    obj.put("status", game.addPlayer(player)); //Status define si el jugador pudo ingresar o no a la partida
                    obj.put("player_id", player.getPlayer_ID());
                    obj.put("game", serializedGame);
                    obj.put("player", serializedPlayer);

                    return obj;
                } catch (JsonProcessingException e) {
                    obj.put("status", "FAILED");
                    return obj;
                }
            }
        }
        return obj;
    }

    private LinkedList<Token> generateTokens() {

        LinkedList<Token> tokenList = new LinkedList<>();
        int ind;
        Random random = new Random();

        for (int i = 0; i <= 6; i++) {
            ind = random.nextInt(100);
            Node<Token> random_token = totalTokenList.acces_index(ind);
            tokenList.addLast(random_token.getValue());
        }

        return tokenList; //TODO generar lista de tokens para el jugador
    }


    public LinkedList<Token> getTokenList() {
        return totalTokenList;
    }


    /**
    *Se completa la lista que contiene todas las fichas disponibles para el juego, se agrupan las fichas que comparten la frecuencia en la que aparecen
     * @author Brayan
    *
    */
    public void fillTokenList(){
        Token A = new Token("/res/images/token/A.png", 1, "A");
        Token E = new Token("/res/images/token/E.png", 1, "E");
        Token O = new Token("/res/images/token/O.png", 1, "O");
        Token I = new Token("/res/images/token/I.png", 1, "I");
        Token S = new Token("/res/images/token/S.png", 1, "S");
        Token N = new Token("/res/images/token/N.png", 1, "N");
        Token R = new Token("/res/images/token/R.png", 1, "R");
        Token U = new Token("/res/images/token/U.png", 1, "U");
        Token D = new Token("/res/images/token/D.png", 2, "D");
        Token L = new Token("/res/images/token/L.png", 1, "L");
        Token T = new Token("/res/images/token/T.png", 1, "T");
        Token C = new Token("/res/images/token/C.png", 3, "C");
        Token G = new Token("/res/images/token/G.png", 2, "G");
        Token B = new Token("/res/images/token/B.png", 3, "B");
        Token M = new Token("/res/images/token/M.png", 3, "M");
        Token P = new Token("/res/images/token/P.png", 3, "P");
        Token H = new Token("/res/images/token/H.png", 4, "H");
        Token F = new Token("/res/images/token/F.png", 4, "F");
        Token V = new Token("/res/images/token/V.png", 4, "V");
        Token Y = new Token("/res/images/token/Y.png", 4, "Y");
        Token CH = new Token("/res/images/token/CH.png", 5, "CH");
        Token Q = new Token("/res/images/token/Q.png", 5, "Q");
        Token J = new Token("/res/images/token/J.png", 8, "J");
        Token LL = new Token("/res/images/token/LL.png", 8, "LL");
        Token Ñ = new Token("/res/images/token/Ñ.png", 8, "Ñ");
        Token RR = new Token("/res/images/token/RR.png", 8, "RR");
        Token X = new Token("/res/images/token/X.png", 8, "X");
        Token Z = new Token("/res/images/token/Z.png", 10, "Z");
        Token Bonus = new Token("*****", 0, "");


        //Se añaden las fichas A y E a la lista
        for (int ae = 1;ae <= 12; ae++){
            totalTokenList.addLast(A);
            totalTokenList.addFirst(E);
        }
        //Se añade la ficha O a la lista
        for (int o = 1;o <= 9;o++){
            this.totalTokenList.addFirst(O);
        }
        //Se añaden las fichas I y S a la lista
        for (int is = 1;is <= 6;is++){
            this.totalTokenList.addFirst(I);
            this.totalTokenList.addFirst(S);
        }
        //Se añaden las fichas N,R,U,D a la lista
        for (int nrud = 1;nrud <= 5;nrud++){
            this.totalTokenList.addFirst(N);
            this.totalTokenList.addFirst(R);
            this.totalTokenList.addFirst(U);
            this.totalTokenList.addFirst(D);
        }
        //Se añaden las fichas L,T,C a la lista
        for (int ltc = 1;ltc <= 4;ltc++){
            this.totalTokenList.addFirst(L);
            this.totalTokenList.addFirst(T);
            this.totalTokenList.addFirst(C);
        }
        //Se añaden las fichas G,B,M,P,H a la lista
        for (int gbmph = 1;gbmph <= 2;gbmph++){
            this.totalTokenList.addFirst(G);
            this.totalTokenList.addFirst(B);
            this.totalTokenList.addFirst(M);
            this.totalTokenList.addFirst(P);
            this.totalTokenList.addFirst(H);
        }
        //Se añaden las fichas F,V,Y,CH,Q,J,LL,Ñ,RR,X,Z a la lista
        for (int fvychqjllñrrxz = 1;fvychqjllñrrxz <= 1;fvychqjllñrrxz++){
            this.totalTokenList.addFirst(F);
            this.totalTokenList.addFirst(V);
            this.totalTokenList.addFirst(Y);
            this.totalTokenList.addFirst(CH);
            this.totalTokenList.addFirst(Q);
            this.totalTokenList.addFirst(J);
            this.totalTokenList.addFirst(LL);
            this.totalTokenList.addFirst(Ñ);
            this.totalTokenList.addFirst(RR);
            this.totalTokenList.addFirst(X);
            this.totalTokenList.addFirst(Z);

        }
        for (int bonus = 1;bonus <= 2;bonus++){
            this.totalTokenList.addFirst(Bonus);
        }

    }

    public static void main(String[] args) {
        Server server = new Server(6307);
        System.out.println("Servidor iniciado...");
        server.connectionListener();

    }
}
