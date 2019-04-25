import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainClass extends Application {

    List<Image> images = getImages();

    private UserBoard userBoard;
    private Board realBoard;
    private int bombsRightNow;
    private int openedSquaresNow;
    private List<List<Integer>> checked;
    private int allFreeSpaces;

    @Override
    public void start(Stage primaryStage){
        BorderPane root = getMainPane();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        startGame();
    }

    private void startGame(){
        int boardHeight = 8;
        int boardWidth = 10;
        int bombCount = 15;
        userBoard = new UserBoard(boardHeight, boardWidth);
        realBoard = new Board(boardHeight, boardWidth);
        checked = new ArrayList<>();
        bombsRightNow = 0;
        openedSquaresNow = 0;
        allFreeSpaces = boardHeight * boardWidth - bombCount;

        List<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        realBoard.setNumbers();
    }

    private BorderPane getMainPane() {
        BorderPane pane = new BorderPane();
        BorderPane other = this.getBorderPane();
        GridPane another = this.getGridPane();
        pane.setTop(other);
        pane.setCenter(another);
        return pane;
    }

    private GridPane getGridPane() {
        GridPane gridpane = new GridPane();
        setImageToGridPane(gridpane);
        return gridpane;
    }

    private BorderPane getBorderPane() {
        BorderPane borderpane = new BorderPane();
        Label label1 = new Label("tere");
        borderpane.setTop(label1);
        return borderpane;
    }

    private List<Image> getImages() {
        List<Image> images = new ArrayList<>();

        images.add(new Image ("File:images/Originalsquare.png"));
        images.add(new Image ("File:images/One.png"));
        images.add(new Image ("File:images/Two.png"));
        images.add(new Image ("File:images/Three.png"));
        images.add(new Image ("File:images/Four.png"));
        images.add(new Image ("File:images/Five.png"));
        images.add(new Image ("File:images/Six.png"));
        images.add(new Image ("File:images/Seven.png"));
        images.add(new Image ("File:images/Eight.png"));
        images.add(new Image ("File:images/Square.png"));
        images.add(new Image ("File:images/Bomb.png"));
        images.add(new Image ("File:images/SelectedBomb.png"));
        images.add(new Image ("File:images/WrongBombLocation.png"));
        images.add(new Image ("File:images/Flag.png"));
        images.add(new Image ("File:images/Question.png"));
        images.add(new Image ("File:images/SelectedQuestion.png"));

        return images;
    }


    //Method to set board game tiles to board
    private void setImageToGridPane(GridPane gridpane) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Label label = new Label();
                ImageView view = new ImageView(images.get(0));
                ImageView view2 = new ImageView(images.get(13));
                ImageView view3 = new ImageView(images.get(14));
                label.setGraphic(view);
                gridpane.add(label, i, j);

                label.setOnMousePressed(event -> {
                    if(event.isPrimaryButtonDown()) {
                        int x = GridPane.getRowIndex(label);
                        int y = GridPane.getColumnIndex(label);
                        userBoard.getBoard()[x][y] = realBoard.getBoard()[x][y];
                        if (realBoard.getBoard()[x][y] == -1) {
                            System.out.println("Game Over");
                            System.exit(1);
                            //TODO how to
                        } else if (realBoard.getBoard()[x][y] == 0) {
                            openAround(x, y, userBoard, realBoard, checked);
                        }
                        openedSquaresNow = userBoard.countOpened();
                        printM_ind(" ", userBoard.getBoard());

                    } else if(event.isSecondaryButtonDown()){
                        if(label.getGraphic().equals(view)){
                            label.setGraphic(view2);
                            System.out.println("1");
                        } else if(label.getGraphic().equals(view2)){
                            label.setGraphic(view3);
                            System.out.println("2");
                        }else if(label.getGraphic().equals(view3)){
                            label.setGraphic(view);
                            System.out.println("2");
                        }
                    }

                    if (openedSquaresNow == allFreeSpaces) { //if there is the same number of opened squares as originally planned
                        System.out.println("You won!");
                    }

                    event.consume();
                });


            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void openAround(int x, int y, UserBoard userBoard, Board realBoard, List<List<Integer>> checked) {
        int[] numbers = realBoard.getNumbersAround(x, y);

        List<Integer> uus = new ArrayList<>();
        uus.add(x);
        uus.add(y);
        if (!checked.contains(uus)) checked.add(uus);

        if (numbers[0] != -9) userBoard.getBoard()[x - 1][y - 1] = realBoard.getBoard()[x - 1][y - 1];
        if (numbers[1] != -9) userBoard.getBoard()[x - 1][y] = realBoard.getBoard()[x - 1][y];
        if (numbers[2] != -9) userBoard.getBoard()[x - 1][y + 1] = realBoard.getBoard()[x - 1][y + 1];
        if (numbers[3] != -9) userBoard.getBoard()[x][y - 1] = realBoard.getBoard()[x][y - 1];
        if (numbers[4] != -9) userBoard.getBoard()[x][y + 1] = realBoard.getBoard()[x][y + 1];
        if (numbers[5] != -9) userBoard.getBoard()[x + 1][y - 1] = realBoard.getBoard()[x + 1][y - 1];
        if (numbers[6] != -9) userBoard.getBoard()[x + 1][y] = realBoard.getBoard()[x + 1][y];
        if (numbers[7] != -9) userBoard.getBoard()[x + 1][y + 1] = realBoard.getBoard()[x + 1][y + 1];

        //TODO here and for cycle with values x and y
        List<Integer> uus2 = new ArrayList<>();
        uus2.add(x - 1);
        uus2.add(y - 1);
        if (numbers[0] == 0 && !checked.contains(uus2)) openAround(x - 1, y - 1, userBoard, realBoard, checked);

        List<Integer> uus3 = new ArrayList<>();
        uus3.add(x - 1);
        uus3.add(y);
        if (numbers[1] == 0 && !checked.contains(uus3)) openAround(x - 1, y, userBoard, realBoard, checked);

        List<Integer> uus4 = new ArrayList<>();
        uus4.add(x - 1);
        uus4.add(y + 1);
        if (numbers[2] == 0 && !checked.contains(uus4)) openAround(x - 1, y + 1, userBoard, realBoard, checked);

        List<Integer> uus5 = new ArrayList<>();
        uus5.add(x);
        uus5.add(y - 1);
        if (numbers[3] == 0 && !checked.contains(uus5)) openAround(x, y - 1, userBoard, realBoard, checked);

        List<Integer> uus6 = new ArrayList<>();
        uus6.add(x);
        uus6.add(y + 1);
        if (numbers[4] == 0 && !checked.contains(uus6)) openAround(x, y + 1, userBoard, realBoard, checked);

        List<Integer> uus7 = new ArrayList<>();
        uus7.add(x + 1);
        uus7.add(y-1);
        if (numbers[5] == 0 && !checked.contains(uus7)) openAround(x + 1, y-1, userBoard, realBoard, checked);

        List<Integer> uus8 = new ArrayList<>();
        uus8.add(x + 1);
        uus8.add(y);
        if (numbers[6] == 0 && !checked.contains(uus8)) openAround(x + 1, y, userBoard, realBoard, checked);

        List<Integer> uus9 = new ArrayList<>();
        uus9.add(x+1);
        uus9.add(y +1);
        if (numbers[7] == 0 && !checked.contains(uus9)) openAround(x+1, y + 1, userBoard, realBoard, checked);
    }

    public static String toStringJ(int[] var0, int var1, String var2) {
        String var3 = "";
        String var4 = "%" + var1 + "d";

        for (int var5 = 0; var5 < var0.length; ++var5) {
            if (var5 == 0) {
                var3 = var3 + String.format(var4, var0[var5]);
            } else {
                var3 = var3 + var2 + String.format(var4, var0[var5]);
            }
        }

        return var3;
    }



    public static void printM_ind(String var0, int[][] var1) {
        int var2 = -2147483648;
        int[][] var3 = var1;
        int var4 = var1.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            int[] var6 = var3[var5];
            int[] var7 = var6;
            int var8 = var6.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                int var10 = var7[var9];
                int var11 = ("" + var10).length();
                if (var11 > var2) {
                    var2 = var11;
                }
            }
        }

        int[] var12 = new int[var1[0].length];

        for (var4 = 0; var4 < var1[0].length; var12[var4] = var4++) {
        }

        System.out.println(var0 + "   " + toStringJ(var12, var2, ".") + ".");

        for (var4 = 0; var4 < var1.length; ++var4) {
            String var13;
            if (var4 < 10) {
                var13 = var0 + " " + var4 + ". ";
            } else {
                var13 = var0 + var4 + ". ";
            }

            System.out.print(var13 + toStringJ(var1[var4], var2, " "));
            System.out.println();
        }

    }
}
