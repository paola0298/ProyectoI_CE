package Logic;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class DictionaryTest {

    public static boolean search(String word) {
        try {
            boolean found = false;
            FileInputStream inputStream = new FileInputStream("res/dictionary.txt");
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            while(!found) {
                if (StringUtils.stripAccents(reader.readLine()).equals(word)) {
                    found = true;
                } else if (reader.readLine() == null) {
                    break;
                }
            }
            inputStream.close();
            streamReader.close();
            reader.close();

            return found;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void appendWord(String word) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("res/dictionary.txt", true));
            writer.append(word + "\n");
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        boolean found = search("Hola");
        appendWord("Hola");
    }

}
