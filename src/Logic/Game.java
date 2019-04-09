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
     * @author HazelMartinez
     * @since 22/03/2019
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

    /**
     *
     * @return allowedUsers
     */
    public int getAllowedUsers() {
        return allowedUsers;
    }

    /**
     *
     * @param allowedUsers
     */
    public void setAllowedUsers(int allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    /**
     *
     * @return idGame
     */
    public String getIdGame() {
        return idGame;
    }

    /**
     *
     * @param idGame
     */
    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    /**
     *
     * @return PlayerList
     */
    public LinkedList<Player> getPlayersList() {
        return PlayersList;
    }

    /**
     *
     * @param playersList
     */
    public void setPlayersList(LinkedList<Player> playersList) {
        PlayersList = playersList;
    }

    /**
     *
     * @return userInTurn
     */
    public int getUserInTurn() {
        return userInTurn;
    }

    /**
     *
     * @param userInTurn
     */
    public void setUserInTurn(int userInTurn) {
        this.userInTurn = userInTurn;
    }

    /**
     * @author HazelMartinez
     * @since 22/03/2019
     * @param player
     */
    public void AddPlayer(Player player){
        //This method add a player in the gameList
        PlayersList.insert(player);
    }

    /**
     *@author HazelMartinez
     * @since 22/03/2019
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
     *@author HazelMartinez
     * @since 22/03/2019
     * @param userName
     * @return The user Name that is going to play
     */
    public boolean VerifyPlayerTurn(String userName){
        //This method is for know who is going to play
        //userInTurn is a number, this indicates who is going to play
        return PlayersList.returnValue(userInTurn).getName().equals(userName);

    }

}

