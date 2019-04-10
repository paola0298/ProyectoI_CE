package Test;

public class StringsManipulation {

    public static void main(String[] args) {
        char letter = 'A';

        for (int i=0; i<26; i++) {
            System.out.println(letter);
            letter += 1;
        }
    }
}
