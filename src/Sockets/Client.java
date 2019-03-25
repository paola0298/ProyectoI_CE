package Sockets;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * La clase Client es la que se conecta con el servidor y envía y recibe mensajes.
 * @author marlon
 * @version 1.0
 */
public class Client {
    private int port;
    private String host;
    private DataInputStream is;
    private DataOutputStream os;
    private Socket clientSocket;

    /**
     * @param host Dirección IP del servidor.
     * @param port Puerto en el cual el servidor está escuchando.
     */
    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    /**
     * @param message Mensaje a enviar al servidor.
     * @return Mensaje recibido del servidor.
     */
    public String connect(String message) {
        try {
            this.clientSocket = new Socket(this.host, this.port);
        } catch (IOException e) {
            System.out.println("Error opening socket ");
        }
        //Opening connection streams
        try {
            this.os = new DataOutputStream(this.clientSocket.getOutputStream());
            this.is = new DataInputStream(this.clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error opening streams");
        }
        //Reading and sending in connection streams
        try {
            if (os != null && is != null) {
                if (message != null) {
                    os.writeUTF(message);
                    return is.readUTF();
                } else {
                    System.out.println("Message must not be null");
                    os.writeUTF("null");
                }

            }
        } catch (IOException e) {
            System.out.println("Error handling streams");
        }
        return null;
    }

    public static void main(String[] args) {
        Client client = new Client("192.168.100.24", 6307);
        Client client1 = new Client("192.168.100.24", 6307);
        boolean flag = true;
        while (flag) {
            String msg = JOptionPane.showInputDialog(null, "Message");
            String response = client.connect(msg);
            if (response == null) {
                flag = false;
                System.out.println(1);
            } else if (response.equals("")) {
                flag = false;
                System.out.println("2");
            } else {
                System.out.println(response);
            }
        }
    }

}