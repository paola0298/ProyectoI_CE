package Logic;

import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

public class Player{
    private String playerId;
    private String name;
    private int score;
    private LinkedList<Token> tokenlist;

    public Player(){}

    public Player(String name) {
        this.playerId = RandomStringUtils.randomAlphanumeric(6);
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getplayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Token> getTokenlist() {
        return tokenlist;
    }

    public void setTokenlist(LinkedList<Token> tokenlist) {
        this.tokenlist = tokenlist;
    }

}
