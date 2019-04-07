package Logic;

public class Token {
    String imagePath;
    int score;
    String letter;

    public Token(String imagePath, int score, String letter) {
        this.imagePath = imagePath;
        this.score = score;
        this.letter = letter;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getScore() {
        return score;
    }

    public String getLetter() {
        return letter;
    }
}
