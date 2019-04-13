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
    private boolean contactExpert = false;
    private boolean receivedAnswer = false;
    private String expertPhone = "";
    private String wordToCheck = "";
    private String expertAnswer = "";
    private String gameString = "";
    private String playerString = "";
    private int wordScore = 0;

    /**
     * Constructor por defecto
     */
    public Game(){}

    /**
     * Constructor de la clase
     * @param maxPlayers  Cantidad máxima de jugadores
     */
    public Game(int maxPlayers) {
        this.players = new LinkedList<>();
        this.gameID = RandomStringUtils.randomAlphanumeric(6);
        this.maxPlayers = maxPlayers;
        this.grid = new Token[15][15];
        this.actualPlayer = null;
    }

    /**
     * Método para agregar un jugador a la partida
     * @param player  Nuevo jugador
     * @return true si se pudo unir, false en caso contrario
     */
    public boolean addPlayer(Player player) {
        if (this.players.getSize() < this.maxPlayers) {
            if (this.players.getSize() == 0) {
                actualPlayer = player;
            }
            this.players.addLast(player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método para eliminar un jugador de la partida
     * @param player el jugador a eliminar
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Se obtiene el siguiente jugador que tiene el turno
     */
    public void nextPlayer() {
        System.out.println("[Game]");
//        System.out.println("List: " + players);
        System.out.println("ActualPlayer game instance: " + actualPlayer);
        Player nextPlayer = players.nextOf(this.actualPlayer);
//        System.out.println("Next player: " + nextPlayer);
//        System.out.println("List: " + players);
        this.actualPlayer = nextPlayer;

    }

    /**
     * @return Retorna el jugador actual de la partida
     */
    public Player getActualPlayer() {
        return this.actualPlayer;
    }

    /**
     * @return Retorna la lista con todos los jugadores de la partida
     */
    public LinkedList<Player> getPlayers() {
        return players;
    }

    /**
     * @return Retorna el id del juego
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * @return Retorna la matriz/cuadricula del juego
     */
    public Token[][] getGrid() {
        return grid;
    }

    public boolean didExpertAnswered() {
        return receivedAnswer;
    }

    public String getExpertAnswer() {
        return expertAnswer;
    }

    /**
     * Establece quien es el jugador actual
     * @param actualPlayer jugador actual
     */
    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }


    /**
     * Obtiene el objeto jugador a partir del id
     * @param playerId identificador del jugador
     * @return Objeto tipo Player
     */
    public Player getPlayer(String playerId) {
        Player player = null;

        for (int i=0; i<players.getSize(); i++) {
            if (players.get(i).getplayerId().equals(playerId)) {
                player = players.get(i);
            }
        }

        return player;
    }

    /**
     * @param grid Establece la matriz de la partida
     */
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

    public boolean isContactExpert() {
        return contactExpert;
    }

    public void setContactExpert(boolean contactExpert) {
        this.contactExpert = contactExpert;
    }

    public void setReceivedAnswer(boolean receivedAnswer) {
        this.receivedAnswer = receivedAnswer;
    }

    public String getExpertPhone() {
        return expertPhone;
    }

    public void setExpertPhone(String expertPhone) {
        this.expertPhone = expertPhone;
    }

    public String getWordToCheck() {
        return wordToCheck;
    }

    public void setWordToCheck(String wordToCheck) {
        this.wordToCheck = wordToCheck;
    }

    public void setExpertAnswer(String expertAnswer) {
        this.expertAnswer = expertAnswer;
    }

    public int getWordScore() {
        return wordScore;
    }

    public void setWordScore(int wordScore) {
        this.wordScore = wordScore;
    }

    public String getGameString() {
        return gameString;
    }

    public void setGameString(String gameString) {
        this.gameString = gameString;
    }

    public String getPlayerString() {
        return playerString;
    }

    public void setPlayerString(String playerString) {
        this.playerString = playerString;
    }
}
