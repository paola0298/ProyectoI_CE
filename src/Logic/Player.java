package Logic;

import Structures.LinkedList;
import org.apache.commons.lang3.RandomStringUtils;

public class Player{
    private String playerId;
    private String name;
    private int score;
    private LinkedList<Token> tokenlist;

    /**
     * Constructor por defecto
     */
    public Player(){ }

    /**
     * Constructor de la clase
     * @param name  El nombre del jugador
     */
    public Player(String name) {
        this.playerId = RandomStringUtils.randomAlphanumeric(6);
        this.name = name;
        this.score = 0;
    }

    /**
     * @return Retorna la puntuación del jugador
     */
    public int getScore() {
        return score;
    }

    /**
     * Le establece una puntuación al jugador
     * @param score Nueva puntuación
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Le suma un valor a la puntuación actual
     * @param scoreToAdd  puntos que se van a agregar
     */
    public void addScore(int scoreToAdd) {
        this.score += scoreToAdd;
    }

    /**
     * @return Retorna el id del jugador
     */
    public String getplayerId() {
        return playerId;
    }

    /**
     * @return Retorna el nombre del jugador
     */
    public String getName() {
        return name;
    }

    /**
     * @return Retorna la lista de tokens del jugador
     */
    public LinkedList<Token> getTokenlist() {
        return tokenlist;
    }

    /**
     * @param tokenlist Establece la lista de tokens del jugador
     */
    public void setTokenlist(LinkedList<Token> tokenlist) {
        this.tokenlist = tokenlist;
    }

}
