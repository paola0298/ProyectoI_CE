package GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DragAndDropImage extends Application {

    private String cwd = System.getProperty("user.dir");
    private HBox tokenBox;
    private ImageView letterSelected;

    @Override
    public void start(Stage stage) throws Exception {

        StackPane mainLayout = new StackPane();

        GridPane matrixContainer = new GridPane();
        matrixContainer.setStyle("-fx-background-color: white;\n" +
                "    -fx-background-radius: 5.0;\n" +
                "    -fx-background-insets: 5.0 5.0 5.0 5.0;\n" +
                "    -fx-padding: 10;\n" +
                "    -fx-hgap: 10;\n" +
                "    -fx-vgap: 10;");

        matrixContainer.setOnDragDropped(dragEvent -> {
            System.out.println("DROPED");
            tokenBox.getChildren().remove(letterSelected);
        });
        tokenBox = new HBox();
        tokenBox.setAlignment(Pos.CENTER);
        tokenBox.setSpacing(5);
        tokenLoader();


        BorderPane gameScreenContainer = new BorderPane();

        gameScreenContainer.setBottom(tokenBox);
        gameScreenContainer.setCenter(matrixContainer);

        //Aquí añaden su panel al contenedor principal.
        mainLayout.getChildren().addAll(gameScreenContainer);
        gameScreenContainer.toFront();
        Scene scene = new Scene(mainLayout, 1280, 900);
//        scene.getStylesheets().add("file:///" + cwd + "/res/styles.css");
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.setTitle("Scrabble TEC");
        stage.show();

    }

    private void tokenLoader(){

        ImageView aLetter = loadImageView("/res/tokenImages/A.png");
        aLetter.setOnDragDetected(event -> {
            System.out.println("Drag detected");
            this.letterSelected = aLetter;

            /* allow MOVE transfer mode */
            Dragboard db = aLetter.startDragAndDrop(TransferMode.MOVE);

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageLoader(cwd + "/res/tokenImages/A.png"));
            db.setContent(content);

            event.consume();
        });
        ImageView bLetter = loadImageView("/res/tokenImages/B.png");
        ImageView cLetter = loadImageView("/res/tokenImages/C.png");
        ImageView dLetter = loadImageView("/res/tokenImages/D.png");
        ImageView eLetter = loadImageView("/res/tokenImages/R.png");
        ImageView fLetter = loadImageView("/res/tokenImages/F.png");
        ImageView gLetter = loadImageView("/res/tokenImages/G.png");

        tokenBox.getChildren().addAll(aLetter, bLetter, cLetter, dLetter,
                eLetter, fLetter, gLetter); //agregar las fichas

    }

    private ImageView loadImageView(String path){
        Image tokenImage = imageLoader(cwd + path);
        ImageView addTokenImage = new ImageView(tokenImage);
        addTokenImage.setFitHeight(80);
        addTokenImage.setFitWidth(80);

        return addTokenImage;
    }

    private Image imageLoader(String path){
        try{
            FileInputStream i = new FileInputStream(path);
            return new Image(i);
        }catch (FileNotFoundException e){
            System.out.println("Couldn't load images!");
        }
        System.out.println("Returning null");
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
