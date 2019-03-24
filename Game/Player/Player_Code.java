package Game.Player;

public class Player_Code {//extends Player {
    String player_ID;
    String game_ID;

    public Player_Code(int number) {
        //create_code(number);

    }
    public void create_code(int number){
        player_ID = game_ID + "@" + Integer.toString(number);
        System.out.println(player_ID);



    }


    public void setGame_ID(String game_ID) {

        this.game_ID = game_ID;
    }
}
