package Logic;

public class Token {
    String imagePath;
    int score;
    String words;

    public Token(String imagePath, int score, String words) {
        this.imagePath = imagePath;
        this.score = score;
        this.words = words;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getScore() {
        return score;
    }

    public String getWords() {
        return words;
    }
}
