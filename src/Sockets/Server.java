package Sockets;

import Logic.Game;
import Logic.Player;
import Logic.Token;
import Logic.WordDictionary;
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
    private LinkedList<Token> tokenInstances = new LinkedList<>();
    private int[][] letterInfo;

    //TODO estos atributos deben ir dentro de la clase Game, para que sea específico de cada partida.
    String expertAnswer;
    boolean contact_expert = false;
    boolean waiting_ex_response = false;
    boolean receivedAnswer = false;
    String expert_phone = "";
    String expert_word = "";
    String player_id = "";


    /**
     * @param port Puerto en el cual el servidor esta escuchando
     */
    public Server(int port){
        fillTokenList();
//        init();
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
            String playerName;

//            System.out.println("JSON received: " + msg.toString());

            JSONObject response;

            switch (msg.get("action").toString()) {
                case "CREATE_MATCH":
                    int maxPlayers = Integer.parseInt(msg.getString("max_players"));
                    playerName = msg.getString("player_name");
                    response = createMatch(maxPlayers, playerName);
                    sendResponse(response.toString( ), con);

                    break;
                case "JOIN_MATCH":
                    String matchID = msg.getString("match_id");
                    playerName = msg.getString("player_name");
                    response = joinMatch(matchID, playerName);
                    sendResponse(response.toString(), con);

                    break;
                case "CHECK_WORD":
                    response = checkWord(); //TODO actualizar el metodo segun los datos que se necesiten
                    break;
                case "CALL_EXPERT":
                    response = callExpert(msg); //TODO actualizar el metodo segun los datos que se necesiten
                    sendResponse(response.toString(), con);
                    break;

                case "EXPERT_SERVICE":
                    response = expertService();
                    sendResponse(response.toString(), con);
                    break;

                case "DISCONNECT":
                    response = disconnect();
                    sendResponse(response.toString(), con);
                    break;
                default:
                    sendResponse("Palabra clave no encontrada", con);
            }

            System.out.println("Cantidad de juegos creados " + gamesList.getSize());
            try {
                con.close();
                System.out.println("Conexión finalizada");
            } catch (IOException e) {
                System.out.println("Error closing the connection " + e.getMessage());
            }
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
            jsonObject.put("player_id", newPlayer.getplayerId());
            return jsonObject;
        } catch (JsonProcessingException e) {
            jsonObject.put("status", "FAILED");
            return jsonObject;
        }

    }

    /**
     * Este método agrega un nuevo jugador a la partida
     * @param id Identificador del jugador a agregar
     * @param name Nombre del jugador a agregar
     * @return Objeto Json para enviarlo al cliente
     */
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
                    obj.put("player_id", player.getplayerId());
                    obj.put("game", serializedGame);
                    obj.put("player", serializedPlayer);

                    return obj;
                } catch (JsonProcessingException e) {
                    obj.put("status", "FAILED");
                    return obj;
                }
            }
        }
        obj.put("status", false);
        return obj;
    }

    private JSONObject checkWord(){ return new JSONObject(); }

    private JSONObject callExpert(JSONObject msg) {
        JSONObject obj = new JSONObject();

//        if (msg.getString("status").equals("WAITING")) {
//            if (!expertAnswer.equals("")) {
//                response.put("status", "ANSWERED");
//                response.put("WORD_STATUS", expertAnswer);
//                contact_expert = false;
//                expert_phone = "";
//                expert_word = "";
//                player_id = "";
//            } else {
//                response.put("status", "CONTACTING");
//            }
//        } else {
//            contact_expert = true;
//            expert_phone = msg.getString("phone");
//            expert_word = msg.getString("word");
//            player_id = msg.getString("player_id");
//
//            response.put("status", "CONTACTING");
//        }
//
//        sendResponse(response.toString(), con);
//        System.out.println("Esperando a contactar experto");
//        break;
//        case "CHECK_TURN":
//        response = checkTurn(); //TODO actualizar el metodo segun los datos que se necesiten
//        sendResponse(response.toString(), con);
//        break;
//        case "EXPERT_SERVICE":
//        response = expertService(); //TODO actualizar el metodo segun los datos que se necesiten
//
//        if (!contact_expert && !waiting_ex_response) {
//            response.put("status", "NO");
//
//        } else if (contact_expert && !waiting_ex_response){
//            contact_expert = false;
//            waiting_ex_response = true;
//            response.put("status", "SEND_SMS");
//            response.put("phone", expert_phone);
//            response.put("word", expert_word);
//        } else {
//            if (msg.getString("status").equals("WAITING")) {
//                response.put("status", "WAITING");
//            } else if (msg.getString("status").equals("ANSWERED")) {
//                waiting_ex_response = false;
//                expertAnswer = msg.getString("expert_answer");
//            }
//        }
        if (!receivedAnswer) {
            expert_phone = msg.getString("phone");
            expert_word = msg.getString("word");
            player_id = msg.getString("player_id");
            contact_expert = true;
            obj.put("status", "CONTACTING");
        } else {
            obj.put("status", "ANSWERED");
            obj.put("word_status", expertAnswer);
        }


        return obj;
    }

    private JSONObject checkTurn() {
        return new JSONObject();
    }

    private JSONObject expertService() {
        JSONObject obj = new JSONObject();

        if (!contact_expert) {
            obj.put("status", "NO");
        } else {
            obj.put("status", "SEND_SMS");
        }

        return obj;
    }

    private JSONObject disconnect() { return new JSONObject(); }

    private LinkedList<Token> generateTokens() {

        LinkedList<Token> tokenList = new LinkedList<>();
        int ind;
        Random random = new Random();

        for (int i = 0; i <= 6; i++) {
            ind = random.nextInt(100);
            Node<Token> random_token = tokenInstances.acces_index(ind);
            tokenList.addLast(random_token.getValue());
        }

        return tokenList; //TODO generar lista de tokens para el jugador
    }

    public void addWordDictonary(String newWord){
        if(!WordDictionary.search(newWord)){
            WordDictionary.addWord(newWord);
        }else{
            System.out.println("The word already exist");
        }
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
            tokenInstances.addLast(A);
            tokenInstances.addFirst(E);
        }
        //Se añade la ficha O a la lista
        for (int o = 1;o <= 9;o++){
            this.tokenInstances.addFirst(O);
        }
        //Se añaden las fichas I y S a la lista
        for (int is = 1;is <= 6;is++){
            this.tokenInstances.addFirst(I);
            this.tokenInstances.addFirst(S);
        }
        //Se añaden las fichas N,R,U,D a la lista
        for (int nrud = 1;nrud <= 5;nrud++){
            this.tokenInstances.addFirst(N);
            this.tokenInstances.addFirst(R);
            this.tokenInstances.addFirst(U);
            this.tokenInstances.addFirst(D);
        }
        //Se añaden las fichas L,T,C a la lista
        for (int ltc = 1;ltc <= 4;ltc++){
            this.tokenInstances.addFirst(L);
            this.tokenInstances.addFirst(T);
            this.tokenInstances.addFirst(C);
        }
        //Se añaden las fichas G,B,M,P,H a la lista
        for (int gbmph = 1;gbmph <= 2;gbmph++){
            this.tokenInstances.addFirst(G);
            this.tokenInstances.addFirst(B);
            this.tokenInstances.addFirst(M);
            this.tokenInstances.addFirst(P);
            this.tokenInstances.addFirst(H);
        }
        //Se añaden las fichas F,V,Y,CH,Q,J,LL,Ñ,RR,X,Z a la lista
        for (int fvychqjllñrrxz = 1;fvychqjllñrrxz <= 1;fvychqjllñrrxz++){
            this.tokenInstances.addFirst(F);
            this.tokenInstances.addFirst(V);
            this.tokenInstances.addFirst(Y);
            this.tokenInstances.addFirst(CH);
            this.tokenInstances.addFirst(Q);
            this.tokenInstances.addFirst(J);
            this.tokenInstances.addFirst(LL);
            this.tokenInstances.addFirst(Ñ);
            this.tokenInstances.addFirst(RR);
            this.tokenInstances.addFirst(X);
            this.tokenInstances.addFirst(Z);

        }
        for (int bonus = 1;bonus <= 2;bonus++){
            this.tokenInstances.addFirst(Bonus);
        }

    }

    public LinkedList<Token> getTokenList() {
        return tokenInstances;
    }

    private void init() {
        LinkedList<Token> letters = new LinkedList<>();
        this.letterInfo = new int[][]{
                {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,5,1,1,1,1,4,4,8,4,10,5,8,8,8}, //Valores
                {12,2,4,5,12,1,2,2,6,1,1,4,2,5,9,2,1,5,6,4,5,1,2,1,1,1,1,1,1,1} //Repeticiones
        };

        char letter = 'A';
        for (int i=0; i<26; i++) {
            letters.addLast(new Token("/res/images/token/"+ letter + ".png", letterInfo[0][i], String.valueOf(letter)));
            letter += 1;
        }
        letters.addLast(new Token("-", 5, "CH"));
        letters.addLast(new Token("-", 8, "LL"));
        letters.addLast(new Token("-", 8, "Ñ"));
        letters.addLast(new Token("-", 8, "RR"));

        this.tokenInstances = letters;
    }

    private LinkedList<Token> randomTokens(int count) {
        LinkedList<Token> list = new LinkedList<>();
        Random random = new Random();

        for (int i=0; i<count; i++) {
            int range = random.nextInt(100);
            int iter;
            int type;
            if (range<=31) {

                iter = random.nextInt(count(12));
                type = 12;
            } else if (32<range && range<=54) {

                iter = random.nextInt(count(9));
                type = 9;
            } else if (54<range && range<=69) {

                iter = random.nextInt(count(6));
                type = 6;
            } else if (69<range && range<=82) {

                iter = random.nextInt(count(5));
                type = 5;
            } else if (82<range && range<=92) {

                iter = random.nextInt(count(4));
                type = 4;
            } else if (92<range && range<=97) {

                iter = random.nextInt(count(2));
                type = 2;
            } else {

                iter = random.nextInt(count(1));
                type = 1;
            }

            list.addLast(findOccurrence(iter, type));
        }
        return list;
    }

    private int count(int element) {
        int e = 0;
        for(int i:letterInfo[1]) {
            if (i == element) {
                e++;
            }
        }
        return e;
    }


    private Token findOccurrence(int iter, int type) {
        int index = 0;
        int c = 0;
        for (int element:letterInfo[1]) {
            if (c == iter) {
                break;
            } else if (element == type) {
                c++;
            }
            index++;
        }
        return tokenInstances.get(index);
    }

    public static void main(String[] args) {
        int port = 6307;
        Server server = new Server(port);
//        System.out.println("Servidor iniciado en puerto " + port);
        server.connectionListener();
//      server.init();
//      LinkedList<Token> list = server.randomTokens(7);
//        System.out.println("-------------------");
//      for (int i=0; i<list.getSize(); i++) {
//          System.out.println("Token: " + list.get(i).getLetter());
//      }

    }
}
