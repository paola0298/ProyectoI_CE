package Logic;

import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

public class Player{
    private String playerID;
    private String name;
    private int score;
    private LinkedList<Token> tokenlist;

    public Player(){

    }

    public Player(String name) {
        this.playerID = RandomStringUtils.randomAlphanumeric(6);
        this.name = name;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlayer_ID() {
        return playerID;
    }

    public void setPlayer_ID(String player_ID) {
        playerID= player_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Token> getTokenlist() {
        return tokenlist;
    }

    public void setTokenlist(LinkedList<Token> tokenlist) {
        this.tokenlist = tokenlist;
    }
}
