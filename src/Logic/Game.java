package Logic;
/**
 * Clase Game
 * @author HazelMartinez
 * @version 1.0
 * @since 22/03/2019
 */

import Structures.LinkedList;

import java.util.List;
import java.util.Random;

public class Game {
    private int allowedUsers;
    /**
     *This Variable represents the number of players allowed in the game 2-4.
     */
    private String idGame;
    /**
     *idGame is the code, is used to enter in the game.
     */
    private LinkedList<Player> PlayersList;
    /**
     * This List is for control which user is going to play.
     */
    private int userInTurn;
    /**
     * userInTurn is the number of Node in the list where the game knows who plays.
     */

    /**
     *
     * @param allowedUsers
     * @param idGame
     * @param userInTurn
     */
    public Game(int allowedUsers, String idGame, int userInTurn) {
        this.allowedUsers = allowedUsers;
        this.idGame = idGame;
        this.userInTurn = userInTurn;
        this.PlayersList = new LinkedList<Player>();
    }

    public int getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(int allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public LinkedList<Player> getPlayersList() {
        return PlayersList;
    }

    public void setPlayersList(LinkedList<Player> playersList) {
        PlayersList = playersList;
    }

    public int getUserInTurn() {
        return userInTurn;
    }

    public void setUserInTurn(int userInTurn) {
        this.userInTurn = userInTurn;
    }

    /**
     *-
     * @param player
     */
    public void AddPlayer(Player player){
        //This method add a player in the gameList
        PlayersList.insert(player);
    }

    /**
     *
     * @param player
     */
    public void DeletePlayer(Player player){
        //This method delete a player in the game
        for(int index = 0; index < PlayersList.getSize(); index++){
            if(PlayersList.returnValue(index).getName() == player.getName()){
                PlayersList.deleteNode(player);
            }
        }
    }

    /**
     *
     * @param userName
     * @return The user Name that is going to play
     */
    public boolean VerifyPlayerTurn(String userName){
        //This method is for know who is going to play
        return PlayersList.returnValue(userInTurn).getName().equals(userName);

    }

    /**
     *
     * @param playerList
     * @return a Player
     */
    public Player returnPlayer(List<Player> playerList){
        //This method sends a Player to the server for start the game
        Random rand = new Random();
        return PlayersList.returnValue(rand.nextInt((PlayersList.getSize())));
    }
}

