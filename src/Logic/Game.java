package Logic;

import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

public class Game {
    private String gameID;
    private int maxPlayers;
    private Token[][] grid;
    private String actualPlayer;
    private LinkedList<Player> players;

    public Game(int maxPlayers) {
        this.players = new LinkedList<>();
        this.gameID = RandomStringUtils.randomAlphanumeric(6);
        this.maxPlayers = maxPlayers;
        this.grid = new Token[15][15];
        this.actualPlayer = "-";
    }

    public boolean addPlayer(Player player) {
        if (players.getSize() < maxPlayers) {
            players.addLast(player);
            return true;
        } else {
            return false;
        }
    }

    public void removePlayer() {

    }

    public void tokens(){ //Que hace este método?

    }
    /**
     * Con este método se obtiene el ID de la partida
     * */
    public String getGameID() {
        return gameID;
    }

    /**
     * Con este método se modifica el ID de la partida
     * */
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    /**
     * Con este método se obtiene el máximo de jugadores
     * */
    public int getMaxPlayers() {
        return maxPlayers;
    }
    /**
     * Con este método se modifica el máximo de jugadores
     * */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    /**
     * Con este método se obtiene la matriz donde se agregan las fichas
     * */
    public Token[][] getGrid() {
        return grid;
    }
    /**
     * Con este método se modifica la matriz principal del juego
     * */
    public void setGrid(Token[][] grid) {
        this.grid = grid;
    }

    public String getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(String actualPlayer) {
        this.actualPlayer = actualPlayer;
    }
}
