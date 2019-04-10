package Logic;
/**
 * Clase Game
<<<<<<< HEAD
 *
=======
>>>>>>> c734b6c850b5fcca2544391a380c2ab310678c0c
 * @author HazelMartinez
 * @version 1.0
 * @since 22/03/2019
 */

import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

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

    public void tokens() { //Que hace este método?

    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getMaxPlayers() {
        return maxPlayers;

    }

    /**
     *
     * @return PlayerList
     */
    public LinkedList<Player> getPlayersList() {
        return players;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Token[][] getGrid() {
        return grid;
    }

    public void setGrid(Token[][] grid) {
        this.grid = grid;
    }


    public String getActualPlayer() {
        return actualPlayer;
    }

    /**
     *@author HazelMartinez
     * @since 22/03/2019
     * @param player
     */
    public void deletePlayer(Player player) {
        players.deleteElement(player);

    }

    /**
     *
     *@author HazelMartinez
     * @since 22/03/2019
     * @return The user Name that is going to play
     */
    public boolean VerifyPlayerTurn(String player_id) {

//        return players.retornarValor(userInTurn).getName().equals(userName);

        //This method is for know who is going to play
        return (actualPlayer.equals(player_id));

    }


    public void setActualPlayer(String actualPlayer) {
        this.actualPlayer = actualPlayer;
    }


}