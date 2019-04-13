package Logic;

import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

public class Game {
    private String gameID;
    private int maxPlayers;
    private Token[][] grid;
    private Player actualPlayer;
    private LinkedList<Player> players;

    //Expert related attributes
    boolean contactExpert = false;
    private boolean receivedAnswer = false;
    String expertPhone = "";
    String wordToCheck = "";
    String expertAnswer = "";

    public Game(){}

    public Game(int maxPlayers) {
        this.players = new LinkedList<>();
        this.gameID = RandomStringUtils.randomAlphanumeric(6);
        this.maxPlayers = maxPlayers;
        this.grid = new Token[15][15];
        this.actualPlayer = null;
    }

    public boolean addPlayer(Player player) {
        if (players.getSize() < maxPlayers) {
            players.addLast(player);
            if (players.getSize() == 1) {
                actualPlayer = player;
            }
            return true;
        } else {
            return false;
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void nextPlayer() {
        actualPlayer = players.nextOf(actualPlayer);
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public String getGameID() {
        return gameID;
    }

    public Token[][] getGrid() {
        return grid;
    }

    public boolean didExpertAnswered() {
        return receivedAnswer;
    }

    public String getExpertAnswer() {
        return expertAnswer;
    }

}
