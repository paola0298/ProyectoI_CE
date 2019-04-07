package Logic;

import Structures.LinkedList;
import Structures.Node;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class Player{
    private String Player_ID;
    private String name;
    private LinkedList<Token> tokenlist = new LinkedList<>();


    public Player(String name) {
        this.Player_ID = RandomStringUtils.randomAlphanumeric(6);
        this.name = name;
    }

    public void assign_tokens(LinkedList<Token> tokens){ //TODO El servidor es el que le va a asignar las fichas al jugador
        int ind;
        Random random = new Random();

        for(int i = 0; i <= 6; i++){
            ind = random.nextInt(100);
            Node<Token> random_token = tokens.acces_index(ind);
            tokenlist.addLast(random_token.getValue());

        }


    }

}
