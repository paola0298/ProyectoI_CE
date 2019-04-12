package Logic;
import Structures.Node;
import org.apache.commons.lang3.RandomStringUtils;
import Structures.CircularList;
import Logic.Player;


public class Game {
    private String gameID;
    private int maxPlayers;
    private Token[][] grid;
    private String actualPlayer;
    private CircularList <Player> players;


    public Game(){}

    public Game(int maxPlayers) {
        this.players = new CircularList<>();
        this.gameID = RandomStringUtils.randomAlphanumeric(6);
        this.maxPlayers = maxPlayers;
        this.grid = new Token[15][15];
        this.actualPlayer = "-";
    }

    public boolean addPlayer(Player player) {
        if (players.getSize() < maxPlayers) {
            players.addLast(player);////////////////////////////////////////////////////////Brayan
            return true;
        } else {
            return false;
        }
    }
    public void removePlayer() {

    }

    public CircularList<Player> getPlayers() {
        return players;
    }

    public String getGameID() {
        return gameID;
    }

    public Token[][] getGrid() {
        return grid;
    }

    public Player access_by_id(String player_id){
        Player element = null;
        for (int i = 0; i <= players.getSize(); i++){
            element = players.get(i);
            if (element.getplayerId() != player_id){
                System.out.println("IF");
            } else{
                System.out.println("ELSE");
                break;
            }

        }
        System.out.println(element);return element;
//
    }
    public boolean hasTop_Points(Player poss_winner){
        for (int i = 0; i <= players.getSize(); i++){
            Player element = players.get(i);
            if (poss_winner.getScore() <= players.getCurrent().getScore()){
                return false;

            }else{
                break;
            }


        }return true;

    }
    public static void main (String [] args){
        Game juego = new Game(3);
        Player brayan = new Player("Brayan");
        brayan.setScore(20);
        String brayanid = brayan.getplayerId();
        Player hazel = new Player("Hazel");
        hazel.setScore(15);

        juego.addPlayer(brayan);
        juego.addPlayer(hazel);
        juego.access_by_id(brayanid);






    }

}
