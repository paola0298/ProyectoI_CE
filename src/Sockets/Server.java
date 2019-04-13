package Sockets;

import Logic.Game;
import Logic.Player;
import Logic.Token;
import Logic.WordDictionary;
import Structures.ExpertRequests;
import Structures.LinkedList;
import Structures.Node;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
                    sendResponse(response.toString(), con);
                    break;

                case "JOIN_MATCH":
                    response = joinMatch(msg.getString("match_id"), msg.getString("player_name"));
                    sendResponse(response.toString(), con);
                    break;

                case "CHECK_WORD":
                    String word = msg.getString("word");
                    String matchId = msg.getString("match_id");
                    int score = msg.getInt("score");
                    String game = msg.getString("game");
                    String player = msg.getString("player");

                    response = checkWord(game, word, matchId, score, player);
                    sendResponse(response.toString(), con);
                    break;

                case "CALL_EXPERT":
                    response = callExpert(msg.getString("match_id"),
                            msg.getString("expert_phone"),
                            msg.getString("word_to_check"),
                            msg.getInt("word_score"),
                            msg.getString("game"),
                            msg.getString("player"));
                    sendResponse(response.toString(), con);
                    break;

                case "EXPERT_SERVICE":
                    if (msg.getString("status").equals("STANDBY")) {
                        response = expertService();
                    } else {
                        response = checkExpertAnswers(msg.getString("requests"));
                    }
                    sendResponse(response.toString(), con);
                    break;

                case "DID_EXPERT_ANSWER":
                    response = expertAnswered(msg.getString("match_id"));
                    sendResponse(response.toString(), con);
                    break;

                case "CHECK_TURN":
                    response = checkTurn(msg.getString("match_id"), msg.getString("player_id"));
                    sendResponse(response.toString(), con);
                    break;

                case "PASS_TURN":
                    response = passTurn(msg.getString("match_id"));
                    sendResponse(response.toString(), con);
                    break;

                case "DISCONNECT":
                    response = disconnect(msg.getString("player_id"), msg.getString("match_id"));
                    sendResponse(response.toString(), con);
                    break;

                default:
                    sendResponse("Palabra clave no encontrada", con);
            }

//            System.out.println("Cantidad de juegos creados " + gamesList.getSize());
            try {
                con.close();
                System.out.println("Conexión finalizada");
            } catch (IOException e) {
                System.out.println("Error closing the connection " + e.getMessage());
            }
        }

    }

    /**
     * Método que le pasa el turno al siguiente jugador
     * @param gameId Identificador de la partida
     * @return objeto json con la información
     */
    private JSONObject passTurn(String gameId) {
        JSONObject obj = new JSONObject();
        Game actualGame = findGame(gameId);
        actualGame.nextPlayer();

        obj.put("status", "SUCCESS");

        return obj;
    }

    /**
     * Busca un juego, con base en el id del juego
     * @param gameId  identificador del juego
     * @return Objeto Game
     */
    private Game findGame(String gameId) {
        Game game = null;
        for (int i=0; i<gamesList.getSize(); i++) {
            if (gamesList.get(i).getGameID().equals(gameId)) {
                game = gamesList.get(i);
            }
        }
        return game;
    }

    /**
     * Método que verifica si el experto respondió
     * @param matchId Identificador de la partida
     * @return Objeto json con la respuesta
     */
    private JSONObject expertAnswered(String matchId) {
        JSONObject obj = new JSONObject();
        Game actualGame = findGame(matchId);
        ObjectMapper mapper = new ObjectMapper();

        if (actualGame.didExpertAnswered()) {
            try {
                obj.put("status", "VALID");
                obj.put("expert_answer", actualGame.getExpertAnswer());

                if (actualGame.getExpertAnswer().equals("S") || actualGame.getExpertAnswer().equals("s")) {

                    Game dataGame = mapper.readValue(actualGame.getGameString(), Game.class);
                    Player playerData = mapper.readValue(actualGame.getPlayerString(), Player.class);

                    LinkedList<Token> tokenList = playerData.getTokenlist();

                    actualGame.setGrid(dataGame.getGrid());
                    Player actualPlayer = actualGame.getActualPlayer();
                    actualPlayer.addScore(actualGame.getWordScore());
                    actualPlayer.setTokenlist(tokenList);

                    actualGame.nextPlayer();

                    String gameSer = mapper.writeValueAsString(actualGame);
                    String playerSer = mapper.writeValueAsString(actualPlayer);

                    obj.put("game", gameSer);
                    obj.put("player", playerSer);

                } else {
                    obj.put("status", "INVALID");
                    actualGame.nextPlayer();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            obj.put("status", "CONTACTING");
        }
        return obj;
    }

    /**
     * Método para crear una nueva partida
     * @param maxPlayers Cantidad máxima de jugadores para la partida
     * @param playerName Nombre del jugador que crea la partida
     * @return Objeto Json para enviarlo al cliente
     */
    private JSONObject createMatch(int maxPlayers, String playerName){
        JSONObject jsonObject = new JSONObject();
        String serializedGame;
        String serializedPlayer;
        Game newGame = new Game(maxPlayers);
        Player newPlayer = new Player(playerName);
        newPlayer.setTokenlist(generateTokens());
        newGame.addPlayer(newPlayer);
        gamesList.addLast(newGame);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            serializedGame = objectMapper.writeValueAsString(newGame);
            serializedPlayer = objectMapper.writeValueAsString(newPlayer);
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("game", serializedGame);
            jsonObject.put("player", serializedPlayer);
            jsonObject.put("game_id", newGame.getGameID());
            jsonObject.put("player_id", newPlayer.getplayerId());
            System.out.println("Players: " + newGame.getPlayers());
            System.out.println("Serialized game: " + serializedGame);
            return jsonObject;
        } catch (JsonProcessingException e) {
            jsonObject.put("status", "FAILED");
            return jsonObject;
        }

    }

    /**
     * Este método agrega un nuevo jugador a la partida
     * @param matchId Identificador del juego al que se desea unir
     * @param playerName Nombre del jugador a agregar
     * @return Objeto Json para enviarlo al cliente
     */
    private JSONObject joinMatch(String matchId, String playerName) {
        JSONObject obj = new JSONObject();
        Game game = findGame(matchId);
        Player newPlayer = new Player(playerName);
        newPlayer.setTokenlist(generateTokens());
        ObjectMapper mapper = new ObjectMapper();

        if (game != null) {
            if (game.addPlayer(newPlayer)) {
                try {
                    obj.put("status", "SUCCESS");
                    obj.put("player_id", newPlayer.getplayerId());
                    String gameSer = mapper.writeValueAsString(game);
                    String playerSer = mapper.writeValueAsString(newPlayer);
                    obj.put("game", gameSer);
                    obj.put("player", playerSer);

                } catch (JsonProcessingException e) {
                    obj.put("status", "FAILED");
                }
            } else {
                obj.put("status", "FAILED");
            }
        } else {
            obj.put("status", "FAILED");
        }

        return obj;
    }

    /**
     * Verifica que la palabra exista en el diccionario
     * @param playerGame  Objeto serializado del juego
     * @param word palabra que se va a verificar
     * @param matchId Identificador de la partida
     * @param score Puntuación de la palabra formada
     * @param playerInstance Objeto serializado del jugador actual
     * @return Mensaje en formato json
     */
    private JSONObject checkWord(String playerGame, String word, String matchId, int score, String playerInstance){
        JSONObject obj = new JSONObject();
        Game actualGame = findGame(matchId);
        ObjectMapper mapper = new ObjectMapper();

        try {

            Game gameData = mapper.readValue(playerGame, Game.class);
            Player playerData = mapper.readValue(playerInstance, Player.class);
            LinkedList<Token> tokenList = playerData.getTokenlist();

            System.out.println("Word to search: \"" + word + "\"");
            if (WordDictionary.search(word)){
                System.out.println("Word found");

                actualGame.setGrid(gameData.getGrid());

                Player actualPlayer = actualGame.getActualPlayer();
                actualPlayer.addScore(score);
                actualPlayer.setTokenlist(tokenList);

                actualGame.nextPlayer();

                try {

                    String gameSer = mapper.writeValueAsString(actualGame);
                    String playerSer = mapper.writeValueAsString(actualPlayer);

                    obj.put("status", "VALID");
                    obj.put("game", gameSer);
                    obj.put("player", playerSer);

                } catch (JsonProcessingException e) {
                    obj.put("status", "FAILED");
                }
            } else {
                obj.put("status", "NOT_FOUND");
            }

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            obj.put("status", "FAILED");
        }

        return obj;
    }


    /**
     * Método que se pone en contacto con el experto
     * @param matchId Id de la partida actual
     * @param expertPhone Teléfono del experto
     * @param wordToCheck Palabra a verificar
     * @return Json objet con la respuesta
     */
    private JSONObject callExpert(String matchId, String expertPhone, String wordToCheck, int wordScore, String gameString, String playerString) {
        JSONObject obj = new JSONObject();
        Game actualGame = findGame(matchId);

        actualGame.setContactExpert(true);
        actualGame.setExpertPhone(expertPhone);
        actualGame.setWordToCheck(wordToCheck);
        actualGame.setWordScore(wordScore);
        actualGame.setGameString(gameString);
        actualGame.setPlayerString(playerString);
        obj.put("status", "CONTACTING");

        return obj;
    }

    private JSONObject expertService() {
        JSONObject obj = new JSONObject();
        ExpertRequests requests = checkExpertRequests();
        ObjectMapper mapper = new ObjectMapper();

        if (requests.getSize() == 0) {
            obj.put("status", "STANDBY");
        } else {
            try {
                String requestsSer = mapper.writeValueAsString(requests);
                obj.put("status", "CONTACT");
                obj.put("requests", requestsSer);
            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//                System.out.println("Error en ExpertService()");
                obj.put("status", "INT_ERROR");
                obj.put("error", e.getMessage());
            }
        }
        return obj;
    }

    private ExpertRequests checkExpertRequests() {
        ExpertRequests requests = new ExpertRequests();

        for (int i=0; i<gamesList.getSize(); i++) {
            Game game = gamesList.get(i);
            if (game.isContactExpert()) {
                String expertPhone = game.getExpertPhone();
                String wordToCheck = game.getWordToCheck();
                String matchId = game.getGameID();
                requests.addRequest(matchId, expertPhone, wordToCheck);
            }
        }
        return requests;
    }

    private JSONObject checkExpertAnswers(String requests) {
        JSONObject obj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ExpertRequests requestsList = mapper.readValue(requests, ExpertRequests.class);
            for (int i=0; i<requestsList.getSize(); i++) {
                String[] request = requestsList.getRequest(i);
                Game actualGame = findGame(request[0]);
                actualGame.setContactExpert(false);
                actualGame.setReceivedAnswer(true);
                actualGame.setExpertAnswer(request[3]);
            }
            obj.put("status", "STANDBY");
        } catch (IOException e) {
//            e.printStackTrace();
            obj.put("status", "INT_ERROR");
            obj.put("error", e.getMessage());
        }
        return obj;
    }


    /**
     * Verifica si ya es el turno de un jugador específico
     * @param gameId  Identificador de la partida
     * @param playerId Indetificador del jugador
     * @return Mensaje en formato json
     */
    private JSONObject checkTurn(String gameId, String playerId) {
        JSONObject obj = new JSONObject();
        Game actualGame = findGame(gameId);
        Player actualPlayer = actualGame.getActualPlayer();
        ObjectMapper mapper = new ObjectMapper();

        if (actualPlayer.getplayerId().equals(playerId)) {
            if (actualGame.getPlayers().getSize() == 1) {
                obj.put("status", "FAILED");
            } else {
                obj.put("status", "SUCCESS");
                try {
                    String gameSer = mapper.writeValueAsString(actualGame);
                    String playerSer = mapper.writeValueAsString(actualPlayer);
                    obj.put("game", gameSer);
                    obj.put("player", playerSer);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } else{
            obj.put("status", "FAILED");
        }

        return obj;
    }

    /**
     * Método que elimina a un jugador de la partida
     * @return Json object con la respuesta
     */
    private JSONObject disconnect(String playerId, String matchId) {
        JSONObject obj = new JSONObject();

        Game actualGame = findGame(matchId);
        for (int i=0; i<actualGame.getPlayers().getSize(); i++) {
            Player actualPlayer = actualGame.getPlayers().get(i);
            if (actualPlayer.getplayerId().equals(playerId)) {

                if (actualGame.getActualPlayer() == actualPlayer) {
                    actualGame.nextPlayer();
                }

                actualGame.removePlayer(actualPlayer);
            }
        }
        return obj;
    }

    /** Genera una lista de tokens
     * @return  Lista de token
     */
    private LinkedList<Token> generateTokens() {

        LinkedList<Token> tokenList = new LinkedList<>();
        int ind;
        Random random = new Random();

        for (int i = 0; i <= 6; i++) {
            ind = random.nextInt(100);
            Node<Token> random_token = tokenInstances.acces_index(ind);
            tokenList.addLast(random_token.getValue());
        }

        return tokenList;
    }

    /**
     * Agrega una nueva palabra al diccionario
     * @param newWord  Palabra que se desea agregar
     */
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
        Token Bonus = new Token("res/images/token/wildcard.png", 0, "");


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

    /**
     * @return Retorna la lista de los tokens
     */
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
        letters.addLast(new Token("/res/images/token/CH.png", 5, "CH"));
        letters.addLast(new Token("/res/images/token/LL.png", 8, "LL"));
        letters.addLast(new Token("/res/images/token/Ñ.png", 8, "Ñ"));
        letters.addLast(new Token("/res/images/token/RR.png", 8, "RR"));

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
        int port = 9810;
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
