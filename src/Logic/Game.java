package Logic;

import Structures.CircularList;
import org.apache.commons.lang3.RandomStringUtils;

public class Game {
    private String gameID;
    private int maxPlayers;
    private Token[][] grid;
    private CircularList<Player> players;

    public Game(){}

    public Game(int maxPlayers) {
//        this.players = new CircularList<>();
        this.players = new CircularList<>();
        this.gameID = RandomStringUtils.randomAlphanumeric(6);
        this.maxPlayers = maxPlayers;
        this.grid = new Token[15][15];
    }

    public boolean addPlayer(Player player) {
        if (players.getSize() < maxPlayers) {
            players.add(player);
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    public void banPlayer(Player player) {
        players.remove(player);
        maxPlayers--;
    }

    public Player getActualPlayer() {
        return players.getCurrent();
    }

    public void nextPlayer() {
        players.next();
    }


    public String getGameID() {
        return gameID;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Token[][] getGrid() {
        return grid;
    }

    public CircularList<Player> getPlayers() {
        return players;
    }

}
