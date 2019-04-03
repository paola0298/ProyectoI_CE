package Logic;

import org.apache.commons.lang3.RandomStringUtils;

public class Game {
    String idGame;

    public void create_ID (){
        this.idGame = RandomStringUtils.randomAlphanumeric(6);
    }
    public void tokens(){

    }
}
