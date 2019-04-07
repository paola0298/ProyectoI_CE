package SMS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsSender {
    public String sendSms(String word, String name) {
        try {
            // Construct data
            String apiKey = "apikey=" + "rYtc5Ks0rAo-O4IkqDvJkOh2XGOabUCuJufHw1xJu4";
            String message = "&message=" + "Buenas, el jugador " + name + ", desea saber si la siguiente palabra puede " +
                    "ser añadida al diccionario de palabras. La palabra es= " + word + ".";
            String sender = "&sender=" + "AeroTEC";
            String numbers = "&numbers=50683216963";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }

    }

    public static void main(String[] args) {
        SmsSender smsSender = new SmsSender();
        String returnn = smsSender.sendSms("Hola", "Paola");
        System.out.println(returnn);
    }

}