package Sockets;

import Logic.Token;
import Structures.LinkedList;
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
    public LinkedList<Token> TokenList = new LinkedList<Token>();
    private ServerSocket serverSocket;
    private boolean isRunning = true;

    public LinkedList<Token> getTokenList() {
        TokenList.printList();
        return TokenList;
    }

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

            JSONObject jsonMessage = new JSONObject(receiveDataFromClient(con));

            System.out.println("JSON received: " + jsonMessage.toString());

            JSONObject response = new JSONObject();

            switch (jsonMessage.get("action").toString()) {
                case "CREATE_MATCH":
                    response.put("CODE", "123456");
                    sendResponse(response.toString(), con);
                    break;
                case "JOIN_MATCH":
                    response.put("ID_PARTIDA", "1");
                    response.put("MATRIZ", "matriz");
                    sendResponse(response.toString(), con);
                    break;
                case "CHECK_WORD":
                    response.put("WORD_STATUS", "VALID");
                    sendResponse(response.toString(), con);
                    break;
                case "CALL_EXPERT":
                    response.put("WORD_STATUS", "INVALID");
                    sendResponse(response.toString(), con);
                    break;
                case "CHECK_TURN":
                    response.put("YOUR_TURN", "NO");
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


        //Se añaden las fichas A y E a la lista
        for (int ae = 0;ae <= 12; ae++){
            TokenList.addLast(A);
            TokenList.addFirst(E);
        }
        //Se añade la ficha O a la lista
        for (int o = 0;o <= 9;o++){
            this.TokenList.addFirst(O);
        }
        //Se añaden las fichas I y S a la lista
        for (int is = 0;is <= 6;is++){
            this.TokenList.addFirst(I);
            this.TokenList.addFirst(S);
        }
        //Se añaden las fichas N,R,U,D a la lista
        for (int nrud = 0;nrud <= 5;nrud++){
            this.TokenList.addFirst(N);
            this.TokenList.addFirst(R);
            this.TokenList.addFirst(U);
            this.TokenList.addFirst(D);
        }
        //Se añaden las fichas L,T,C a la lista
        for (int ltc = 0;ltc <= 4;ltc++){
            this.TokenList.addFirst(L);
            this.TokenList.addFirst(T);
            this.TokenList.addFirst(C);
        }
        //Se añaden las fichas G,B,M,P,H a la lista
        for (int gbmph = 0;gbmph <= 2;gbmph++){
            this.TokenList.addFirst(G);
            this.TokenList.addFirst(B);
            this.TokenList.addFirst(M);
            this.TokenList.addFirst(P);
            this.TokenList.addFirst(H);
        }
        //Se añaden las fichas F,V,Y,CH,Q,J,LL,Ñ,RR,X,Z a la lista
        for (int fvychqjllñrrxz = 0;fvychqjllñrrxz <= 1;fvychqjllñrrxz++){
            this.TokenList.addFirst(G);
            this.TokenList.addFirst(B);
            this.TokenList.addFirst(M);
            this.TokenList.addFirst(P);
            this.TokenList.addFirst(H);
        }
        TokenList.printList();



    }

    public static void main(String[] args) {



        Server server = new Server(6307);
        System.out.println("Servidor iniciado...");
        server.connectionListener();

    }




//
//    case "NO_TOKENS":
//    String match_ID = msg.getString("current_match_id");
//    String player_ID = msg.getString("current_player_id");
//    //  response = pickWinner(match_ID, player_ID);

    // sendResponse(response.toString(), con);






    //private JSONObject pickWinner(String Match_ID, String Player_ID) {
//        JSONObject obj = new JSONObject();
//        Game actualGame = ;
//        Player possible_Winner = actualGame.access_by_id(Player_ID);
//        if (actualGame.hasTop_Points(possible_Winner)) {
//            obj.put("noTokensresult", "WINNER");
//
//        } else {
//            obj.put("noTokensresult", "LOST");
//        }
//        return obj;
    //}

}
