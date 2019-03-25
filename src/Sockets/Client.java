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

        sendData(message);
        return getData();
    }

    /**
     * Este método se usa para enviar información al servidor.
     * @param message Mensaje a enviar al servidor.
     */
    private void sendData(String message) {
        try {
            DataOutputStream os = new DataOutputStream(this.clientSocket.getOutputStream());
            os.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Error sending data: " + e.getMessage());
        }
    }

    /**
     * Este método recibe la información enviada del servidor.
     * @return Información enviada del servidor.
     */
    private String getData() {
        try {
            DataInputStream is = new DataInputStream(this.clientSocket.getInputStream());
            return is.readUTF();
        } catch (IOException e) {
            System.out.println("Error getting data: " + e.getMessage());
            return null;
        }
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
