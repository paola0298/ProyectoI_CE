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
        System.out.println("[Game]");
//        System.out.println("List: " + players);
        System.out.println("ActualPlayer game instance: " + actualPlayer);
        Player nextPlayer = players.nextOf(this.actualPlayer);
//        System.out.println("Next player: " + nextPlayer);
//        System.out.println("List: " + players);
        this.actualPlayer = nextPlayer;

    }

    public Player getActualPlayer() {
        return this.actualPlayer;
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

<<<<<<< HEAD
    public String getExpertAnswer() {
        return expertAnswer;
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public Player getPlayer(String playerId) {
        Player player = null;

        for (int i=0; i<players.getSize(); i++) {
            if (players.get(i).getplayerId().equals(playerId)) {
                player = players.get(i);
            }
        }

        return player;
    }

    public void setGrid(Token[][] grid) {
        this.grid = grid;
    }


    public static void main(String[] args) {
        Player player = new Player("Paola");
        Player player1 = new Player("Marlon");

        Game game = new Game(2);
        game.addPlayer(player);
        game.addPlayer(player1);

        System.out.println("Actual player " + game.getActualPlayer().getName());
        game.nextPlayer();
        System.out.println("Actual player " + game.getActualPlayer().getName());

        System.out.println(game.getPlayers());
    }

=======





//
//    public Player access_by_id(String player_id){
//        Player element = null;
//        for (int i = 0; i <= players.getSize(); i++){
//            element = players.get(i);
//            if (element.getplayerId() != player_id){
//                System.out.println("IF");
//            } else{
//                System.out.println("ELSE");
//                break;
//            }
//
//        }
//        System.out.println(element);return element;
////
//    }
//    public boolean hasTop_Points(Player poss_winner){
//        for (int i = 0; i <= players.getSize(); i++){
//            Player element = players.get(i);
//            if (poss_winner.getScore() <= players.getCurrent().getScore()){
//                return false;
//
//            }else{
//                break;
//            }
//
//
//        }return true;
//
//    }
>>>>>>> Añado cambios que van en connection
}
