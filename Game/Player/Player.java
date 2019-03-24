package Game.Player;

public class Player {
    String game_ID;
    String player_ID;
    int personal_score;

    public Player() {
        //this.game_ID

    }

    public String getGame_ID() {
        return game_ID;
    }

    public void setGame_ID(String game_ID) {
        this.game_ID = game_ID;
    }

    public String getPlayer_ID() {
        return player_ID;
    }

    public void setPlayer_ID(String player_ID) {
        this.player_ID = player_ID;
    }

    public int getPersonal_score() {
        return personal_score;
    }

    public void setPersonal_score(int personal_score) {
        this.personal_score = personal_score;
    }
}

