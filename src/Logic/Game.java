package Logic;

import Structures.LinkedList;
import Structures.Node;
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

    public void setActualPlayer(String actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public Player access_by_id(String player_id) {
        Player element = null;
        for (int i = 0; i < players.getSize(); i++) {
            element = players.get(i);
            if (element.getPlayer_ID() != player_id) {
            } else {
                break;
            }
        }
        return element;
    }
    public boolean hasTop_Points(Player poss_winner) {
        return poss_winner == hasTop_Points_aux() && poss_winner.getTokenlist().getHead() == null;
    }


    private Player hasTop_Points_aux() {
        Node<Player> tmp = players.getHead();
        Player winner = tmp.getValue();
        for (int i = 0; i <= players.getSize(); i++) {
            if (tmp.getNext() == null) {
                if (winner.getScore() < tmp.getValue().getScore()) {
                    winner = tmp.getValue();
                }
            } else if (winner.getScore() < tmp.getNext().getValue().getScore()) {
                winner = tmp.getNext().getValue();
                tmp = tmp.getNext();
            } else {
                tmp = tmp.getNext();
            }

        }
        return winner;
    }
}
