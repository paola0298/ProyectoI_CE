package Test;

import Logic.Game;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JacksonTest {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Game game = new Game(4);
        try {
            mapper.writeValue(new File("testGame.json"), game);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
