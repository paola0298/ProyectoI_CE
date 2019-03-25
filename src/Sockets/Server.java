package Sockets;

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


    /**
     * @param port Puerto en el cual el servidor esta escuchando
     */
    public Server(int port){
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

    public static void main(String[] args) {

        Server server = new Server(6307);
        System.out.println("Servidor iniciado...");
        server.connectionListener();
    }



}
