package Logic;

//TODO actualizar la librería a la última versión
//import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class Player {
    String Player_ID;
    String Name;
    int Player_Number;

    public void create_ID (){
        this.Player_ID = RandomStringUtils.randomAlphanumeric(6);

    }

    public String getPlayer_ID() {
        return Player_ID;
    }

    public void setPlayer_ID(String player_ID) {
        Player_ID = player_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPlayer_Number() {
        return Player_Number;
    }

    public void setPlayer_Number(int player_Number) {
        Player_Number = player_Number;
    }
}
