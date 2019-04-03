package Logic;


import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

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
    public void create_tokens(){



    }

}
