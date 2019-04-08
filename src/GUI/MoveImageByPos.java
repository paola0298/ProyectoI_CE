//package GUI;
//
//import Structures.LinkedList;
//import javafx.application.Application;
//import javafx.geometry.HPos;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.ClipboardContent;
//import javafx.scene.input.Dragboard;
//import javafx.scene.input.TransferMode;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//
//public class MoveImageByPos extends Application {
//
//
//    private String cwd = System.getProperty("user.dir");
//    private HBox tokenBox;
//    private ImageView letterSelected;
//    private GridPane matrixContainer;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//
//        StackPane mainLayout = new StackPane();
//
//        matrixContainer = new GridPane();
//        matrixContainer.setStyle("-fx-background-color: blue;\n" +
//                "    -fx-background-insets: 5.0 5.0 5.0 5.0;\n");
//
//        matrixContainer.setAlignment(Pos.CENTER);
//
//        matrixContainer.setOnMouseClicked(event->{
//            putImageOnContainer((int)event.getSceneX(), (int)event.getSceneY());
//
//        });
//
//        matrixContainer.setGridLinesVisible(true);
//
//        addColumnsAndRows();
//
//        tokenBox = new HBox();
//        tokenBox.setAlignment(Pos.CENTER);
//        tokenBox.setSpacing(5);
//        tokenLoader();
//
//
//        BorderPane gameScreenContainer = new BorderPane();
//
//        gameScreenContainer.setBottom(tokenBox);
//        gameScreenContainer.setCenter(matrixContainer);
//
//        //Aquí añaden su panel al contenedor principal.
//        mainLayout.getChildren().addAll(gameScreenContainer);
//        gameScreenContainer.toFront();
//        Scene scene = new Scene(mainLayout, 1280, 900);
////        scene.getStylesheets().add("file:///" + cwd + "/res/styles.css");
//        stage.setMinWidth(640);
//        stage.setMinHeight(480);
//        stage.setScene(scene);
//        stage.setTitle("Scrabble TEC");
//        stage.show();
//
//    }
//
//    private void addColumnsAndRows() {
//        for (int i = 0; i < 15; i++) {
//            ColumnConstraints colConst = new ColumnConstraints();
//            colConst.setPercentWidth(100.0 / 15);
//            matrixContainer.getColumnConstraints().add(colConst);
//
//            RowConstraints rowConst = new RowConstraints();
//            rowConst.setPercentHeight(100.0 / 15);
//            matrixContainer.getRowConstraints().add(rowConst);
//        }
//    }
//
//    private void tokenLoader(){
//
//        ImageView aLetter = loadImageView("/res/tokenImages/A.png");
//        aLetter.setOnMouseClicked(mouseEvent -> {
//            letterSelected = aLetter;
//        });
//        ImageView bLetter = loadImageView("/res/tokenImages/B.png");
//        ImageView cLetter = loadImageView("/res/tokenImages/C.png");
//        ImageView dLetter = loadImageView("/res/tokenImages/D.png");
//        ImageView eLetter = loadImageView("/res/tokenImages/R.png");
//        ImageView fLetter = loadImageView("/res/tokenImages/F.png");
//        ImageView gLetter = loadImageView("/res/tokenImages/G.png");
//
//        tokenBox.getChildren().addAll(aLetter, bLetter, cLetter, dLetter,
//                eLetter, fLetter, gLetter); //agregar las fichas
//
//    }
//
//    private ImageView loadImageView(String path){
//        Image tokenImage = imageLoader(cwd + path);
//        ImageView addTokenImage = new ImageView(tokenImage);
//        addTokenImage.setFitHeight(80);
//        addTokenImage.setFitWidth(80);
//
//        return addTokenImage;
//    }
//
//    private Image imageLoader(String path){
//        try{
//            FileInputStream i = new FileInputStream(path);
//            return new Image(i);
//        }catch (FileNotFoundException e){
//            System.out.println("Couldn't load images!");
//        }
//        System.out.println("Returning null");
//        return null;
//    }
//
//    private void putImageOnContainer(int xPos, int yPos){
//        System.out.println("PosX " + xPos);
//        System.out.println("PosY " + yPos);
//        LinkedList<Integer> rowColumnList = new LinkedList<>();
////        getRowAndColumn(rowColumnList, xPos, yPos);
//        ImageView image = new ImageView(letterSelected.getImage());
//        image.setFitHeight(50);
//        image.setFitWidth(50);
//        matrixContainer.getChildren().add(image);
//        GridPane.setHalignment(image, HPos.CENTER);
//        tokenBox.getChildren().remove(letterSelected);
//
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//
//}
