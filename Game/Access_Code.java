package Game;
import java.util.Random;

/**This class is responsible of the creation of a random hexadecimal number of six digits
 * It is saved in a String variable called code
 * Made by: Brayan Rodríguez Villalobos
 * 23/March/2019
 */

public class Access_Code  {

    String code;
    public Access_Code() {
        this.create_code();


    }

    /**
     * This method creates the hexadecimal number and saves it in "code"
     * @return code
     */
    public String create_code() {
        int data;
        Random value = new Random();
        data = value.nextInt((16777215-1048576)+1)+1048576;
        this.code = Integer.toHexString(data);
        System.out.println(this.code);
        return this.code;


    }

    public String getCode() {

        return code;
    }
}
