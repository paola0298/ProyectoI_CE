package Logic;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class WordDictionary {

    /**
     * Método para buscar una palabra en el diccionario
     * @param wordToSearch Palabra a buscar en el diccionario
     * @return true si la encontró, false en caso contrario
     */
    public static boolean search(String wordToSearch) {
        try {

            FileInputStream inputStream = new FileInputStream("res/dictionary.txt");
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                line = StringUtils.stripAccents(line);
                if (line.equals(wordToSearch)) {
                    reader.close();
                    return true;
                }


            }

            reader.close();
            return false;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Método que agrega una palabra al diccionario existente
     * @param word palabra que se va a agregar
     */
    public static void addWord(String word) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("res/dictionary.txt", true));
            writer.append(word).append("\n");
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void main(String[] args) {
        System.out.println(WordDictionary.search("te"));
    }

}
