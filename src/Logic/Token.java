package Logic;

public class Token {
    private String imagePath;
    private int score;
    private String letter;
    private boolean isActive;

    public Token(){}

    public Token(String imagePath, int score, String letter) {
        this.imagePath = imagePath;
        this.score = score;
        this.letter = letter;
        this.isActive = false;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
