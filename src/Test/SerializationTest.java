package Test;

import Logic.Game;
import Logic.Player;
import Logic.Token;
import Structures.LinkedList;
import Structures.Node;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Random;

public class SerializationTest {
    private static LinkedList<Token> tokenInstances = new LinkedList<>();

    private static void init() {
        LinkedList<Token> letters = new LinkedList<>();
        int[][] letterInfo = new int[][]{
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

        tokenInstances = letters;
    }

    private static LinkedList<Token> generateTokens() {


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

    public static void main(String[] args) {
        init();
        generateTokens();

        Game game = new Game(4);
        Player player = new Player("Marlon");
        player.setTokenlist(generateTokens());
        game.addPlayer(player);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String serializedGame = "";

        try {

            serializedGame = mapper.writeValueAsString(game);

            System.out.println("Game:\n" + serializedGame);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {

            Game gameObject = mapper.readValue(serializedGame, Game.class);

            System.out.println(gameObject);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
