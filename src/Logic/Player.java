package Logic;



import Structures.LinkedList;

import Structures.Node;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class Player{
    String Player_ID;
    String Name;
    LinkedList tokenlist = new LinkedList();

    public Player(String name) {
        create_ID();
        Name = name;
    }

    public void create_ID (){
        this.Player_ID = RandomStringUtils.randomAlphanumeric(6);

    }
    public void assign_tokens(LinkedList<Token> tokens){
        int ind;
        Random random = new Random();

        for(int i = 0; i <= 6; i++){
            ind = random.nextInt(100);
            Node<Token> random_token = tokens.acces_index(ind);
            tokenlist.addLast(random_token.getValue());

        }


    }
    public static void main(String [] args ){





    }
}
