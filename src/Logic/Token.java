package Logic;

public class Token {
    private String imagePath;
    private int score;
    private String letter;

    public Token(){}

    public Token(String imagePath, int score, String letter) {
        this.imagePath = imagePath;
        this.score = score;
        this.letter = letter;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setLetter(String letter) {
        this.letter = letter;

    }

    public String getLetter() {
        return letter;
    }
}
