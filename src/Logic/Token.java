package Logic;

public class Token {
    private String imagePath;
    private int score;
    private String letter;

    /**
     * Constructor por defecto
     */
    public Token(){}

    /**
     * Constructor de la clase
     * @param imagePath  Ruta de la imagen que se va a agregar
     * @param score Puntuación de la letra
     * @param letter letra que se va a agregar
     */
    public Token(String imagePath, int score, String letter) {
        this.imagePath = imagePath;
        this.score = score;
        this.letter = letter;
    }


    /**
     * @return Retorna la ruta de la imagen
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @return Retorna la puntuación de la letra
     */
    public int getScore() {
        return score;
    }

    /**
     * @return Retorna la letra
     */
    public String getLetter() {
        return letter;
    }
}
