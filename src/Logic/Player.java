package Logic;

//TODO actualizar la librería a la última versión
//import org.apache.commons.lang.RandomStringUtils;

import org.apache.commons.lang3.RandomStringUtils;


public class Player {
    String Player_ID;
    String Name;


    public void create_ID (){
        this.Player_ID = RandomStringUtils.randomAlphanumeric(6);

    }

}
