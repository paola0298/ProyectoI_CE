package Logic;

import Structures.LinkedList;
import Structures.Node;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class Player{
    private String playerID;
    private String name;
    private LinkedList<Token> tokenlist = new LinkedList<>();


    public Player(String name) {
        this.playerID = RandomStringUtils.randomAlphanumeric(6);
        this.name = name;
    }

    /**
     * Este método se encarga de tomar una cantidad de fichas aleatorias de la lista con todas las fichas que posee el servidor y son guardadas en la lista de fichas de cada jugador
     * @param tokens
     */
    public void assign_tokens(LinkedList<Token> tokens){ //TODO El servidor es el que le va a asignar las fichas al jugador
        int ind;
        Random random = new Random();

        for(int i = 0; i <= 6; i++){
            ind = random.nextInt(100);
            Node<Token> random_token = tokens.acces_index(ind);
            tokenlist.addLast(random_token.getValue());

        }
    }

    public String getPlayer_ID() {
        return playerID;
    }

    public void setPlayer_ID(String player_ID) {
        playerID= player_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Token> getTokenlist() {
        return tokenlist;
    }

    public void setTokenlist(LinkedList<Token> tokenlist) {
        this.tokenlist = tokenlist;
    }

    public boolean getScore() {
        return false;
    }
}
