package Logic;


import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class Player {
    String Player_ID;
    String Name;
    LinkedList<Token> tokenlist;

    public Player(String name) {
        create_ID();
        Name = name;
    }

    public void create_ID (){
        this.Player_ID = RandomStringUtils.randomAlphanumeric(6);

    }
    public void assign_tokens(LinkedList<Token> tokensList){
        int ind;
        Random random = new Random();
        ind = random.nextInt(100)+1;
        System.out.println(ind);

    }
    public static void main(String [] args ){
        int ind;
        Random random = new Random();
        ind = random.nextInt(100)+1;
        System.out.println(ind);



    }
}
