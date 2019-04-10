package Logic;
/**
 * Clase Game
 * @author HazelMartinez
 * @version 1.0
 * @since 22/03/2019
 */

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

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getMaxPlayers() {
        return maxPlayers;
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

    }

    public boolean VerifyPlayerTurn(String userName){

//        return players.retornarValor(userInTurn).getName().equals(userName);


    }


    public void setActualPlayer(String actualPlayer) {
        this.actualPlayer = actualPlayer;
    }
}
