package Logic;
/**
 * Clase Game
 * @author HazelMartinez
 * @version 1.0
 * @since 22/03/2019
 */

import Structures.LinkedList;
<<<<<<< HEAD
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
=======
import Structures.Lista;

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
>>>>>>> Se agrega a la linkedList el metodo insert, deleteNode, retornar un valor, 
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

    public String getGameID() {
        return gameID;
    }

<<<<<<< HEAD
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getMaxPlayers() {
        return maxPlayers;
=======
    public LinkedList<Player> getPlayersList() {
        return PlayersList;
    }

    public void setPlayersList(LinkedList<Player> playersList) {
        PlayersList = playersList;
>>>>>>> Se agrega a la linkedList el metodo insert, deleteNode, retornar un valor, 
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Token[][] getGrid() {
        return grid;
    }

<<<<<<< HEAD

    public void setGrid(Token[][] grid) {
        this.grid = grid;
    }

    public String getActualPlayer() {
        return actualPlayer;
    }


    public boolean DeletePlayer(Player playerToDelete){

//        for(int index = 0; index < players.getSize(); index++){
//
////            if(players.(index).getName() == player.getName()){
//            if (players.get(index).getPlayer_ID().equals(playerToDelete.getPlayer_ID())) {
//                    players.de
//            }
//                //players.retornarValor(index).getplayers().insertar(player);
//                players.eliminar();
//            }
//        }

        return players.deleteElement(playerToDelete);

=======
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
>>>>>>> Se agrega a la linkedList el metodo insert, deleteNode, retornar un valor, 
    }

    /**
     *
     * @param userName
     * @return The user Name that is going to play
     */
    public boolean VerifyPlayerTurn(String userName){
<<<<<<< HEAD

//        return players.retornarValor(userInTurn).getName().equals(userName);

=======
        //This method is for know who is going to play
        return PlayersList.returnValue(userInTurn).getName().equals(userName);

    }
>>>>>>> Se agrega a la linkedList el metodo insert, deleteNode, retornar un valor, 

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


    public void setActualPlayer(String actualPlayer) {
        this.actualPlayer = actualPlayer;
    }
}
