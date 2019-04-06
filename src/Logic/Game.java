package Logic;
/**
 * Clase Game
 * @author HazelMartinez
 * @version 1.0
 * @since 22/03/2019
 */

import Structures.Lista;

public class Game {
        private int allowedUsers;
        //This Variable represents the number of players allowed in the game 2-4.
        private String idGame;
        //idGame is the code, is used to enter in the game.
        private Lista<Player> PlayersList;
        //This List is for control which user is going to play.
        private int userInTurn;
        //userInTurn is the number of Node in the list where the game knows who plays.

    public Game(int allowedUsers, String idGame, int userInTurn) {
        this.allowedUsers = allowedUsers;
        this.idGame = idGame;
        this.userInTurn = userInTurn;
        this.PlayersList = new Lista<Player>();
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

    public Lista<Player> getPlayersList() {
        return PlayersList;
    }

    public void setPlayersList(Lista<Player> playersList) {
        PlayersList = playersList;
    }

    public int getUserInTurn() {
        return userInTurn;
    }

    public void setUserInTurn(int userInTurn) {
        this.userInTurn = userInTurn;
    }
    public void AddPlayer(Player player){
        PlayersList.insertar(player); //add player
    }


    public void DeletePlayer(Player player){
        for(int index = 0; index < PlayersList.getSize(); index++){
            if(PlayersList.retornarValor(index).getName() == player.getName()){
                //PlayersList.retornarValor(index).getPlayersList().insertar(player);
                PlayersList.eliminar();
            }
        }
    }

    public boolean VerifyPlayerTurn(String userName){
        return PlayersList.retornarValor(userInTurn).getName().equals(userName);

    }
}

