package Sockets;

import Logic.Game;
import Logic.Player;
import Structures.LinkedList;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random; //This Library is for choose and player for start the game

/**
 * @author Paola
 * @version 1.0
 *
 * La clase Server realiza la conexión con el cliente, además envía y recibe mensajes
 */
public class Server {

    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private LinkedList<Game> gameList = new LinkedList<Game>(); //Lista de partidas, Hazel

    /**
     * @param port Puerto en el cual el servidor esta escuchando
     */
    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating the socket " + e.getMessage());
        }
    }

    /**
     * @return La conexion con el cliente
     */
    public Socket clientConnection() {
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
    public String receiveDataFromClient(Socket con) {
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
     * @param con      Conexion con el cliente
     */
    public void sendResponse(String response, Socket con) {
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
    public void connectionListener() {
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

        while (this.isRunning) {

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

    public LinkedList<Game> getGameList() {
        return gameList;
    }

    public void setGameList(LinkedList<Game> gameList) {
        this.gameList = gameList;
    }

    /**
     * @param player
     * @param idGame
     * @author HazelMartinez
     * @version 1.0
     * @since 2019/05/04
     */
    public void addPlayerToExistingGame(Player player, String idGame) {
        //This method adds a new player to a existing game
        //Recives the idGame for know where to add the player.
        for (int index = 0; index < gameList.getSize(); index++) {
            if (gameList.returnValue(index).getIdGame() == idGame) {
                //If the idGame is found add the player to the gameList
                gameList.returnValue(index).getPlayersList().insert(player);
            }
        }
    }

    /**
     * @author HazelMartinez
     * @version 1.0
     * @since 07/04/2019
     *
     * @param gameNumber
     * @return user Name
     */
    public String choosePlayerStart(int gameNumber){
        //This method choose a random player for start the game
        //Recives a gameNumber, this is the number of gameList
        Random rand = new Random();
        int sizelist = gameList.returnValue(gameNumber).getPlayersList().getSize();
        //Obtein the size of the list for have the random number between 0 and the size
        int randomInt = rand.nextInt((sizelist)); //generate the random number
        return gameList.returnValue(gameNumber).getPlayersList().returnValue(randomInt).getName();
        //return the UserName
    }

    public static void main(String[] args){
        Server server = new Server(6307);
        System.out.println("Servidor iniciado...");
        server.connectionListener();
    }
}
