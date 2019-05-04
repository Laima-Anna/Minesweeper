import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
        uus7.add(y - 1);
        if (numbers[5] == 0 && !checked.contains(uus7)) openAround(x + 1, y - 1, userBoard, realBoard, checked);

        List<Integer> uus8 = new ArrayList<>();
        uus8.add(x + 1);
        uus8.add(y);
        if (numbers[6] == 0 && !checked.contains(uus8)) openAround(x + 1, y, userBoard, realBoard, checked);

        List<Integer> uus9 = new ArrayList<>();
        uus9.add(x + 1);
        uus9.add(y + 1);
        if (numbers[7] == 0 && !checked.contains(uus9)) openAround(x + 1, y + 1, userBoard, realBoard, checked);
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

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = getMainPane();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        startGame();
    }

    private void startGame() {
        int boardHeight = 9;
        int boardWidth = 9;
        int bombCount = 15;
        userBoard = new UserBoard(boardHeight, boardWidth);
        setRealBoard(boardHeight,boardWidth,bombCount);
        checked = new ArrayList<>();
        bombsRightNow = 0;
        openedSquaresNow = 0;
        allFreeSpaces = boardHeight * boardWidth - bombCount;



    }

    private void setRealBoard(int boardHeight, int boardWidth, int bombCount){
        realBoard = new Board(boardHeight, boardWidth);
        List<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        realBoard.setNumbers();
        printM_ind(" ", realBoard.getBoard());
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

        images.add(new Image("File:images/Originalsquare.png"));
        images.add(new Image("File:images/One.png"));
        images.add(new Image("File:images/Two.png"));
        images.add(new Image("File:images/Three.png"));
        images.add(new Image("File:images/Four.png"));
        images.add(new Image("File:images/Five.png"));
        images.add(new Image("File:images/Six.png"));
        images.add(new Image("File:images/Seven.png"));
        images.add(new Image("File:images/Eight.png"));
        images.add(new Image("File:images/Square.png"));
        images.add(new Image("File:images/Bomb.png"));
        images.add(new Image("File:images/SelectedBomb.png"));
        images.add(new Image("File:images/WrongBombLocation.png"));
        images.add(new Image("File:images/Flag.png"));
        images.add(new Image("File:images/Question.png"));
        images.add(new Image("File:images/SelectedQuestion.png"));

        return images;
    }

    //Method to set board game tiles to board
    private void setImageToGridPane(GridPane gridpane) {
        final boolean[] firstClick = {true};
        final String[] compare = {"notOpened"};
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {


                Label label = new Label();
                ImageView view = new ImageView(images.get(0));
                ImageView view2 = new ImageView(images.get(13));
                ImageView view3 = new ImageView(images.get(14));
                label.setGraphic(view);
                gridpane.add(label, i, j);

                //TODO - keyboard event and mousepressed event
                label.setOnMousePressed(event -> {
                    if (event.isPrimaryButtonDown()) {
                        if(firstClick[0]) {
                            while (realBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] == -1) {
                                setRealBoard(9,9,15);
                            }
                            firstClick[0] =false;
                        }

                        int x = GridPane.getRowIndex(label);
                        int y = GridPane.getColumnIndex(label);
                        userBoard.getBoard()[x][y] = realBoard.getBoard()[x][y];
                        if (realBoard.getBoard()[x][y] == -1) {
                            System.out.println("Game Over");
                            userBoard.getBoard()[x][y] = -6;
                        } else if (realBoard.getBoard()[x][y] == 0) {
                            openAround(x, y, userBoard, realBoard, checked);
                        }
                        openedSquaresNow = userBoard.countOpened();


                        Node result = null;
                        ObservableList<Node> childrens = gridpane.getChildren();

                        printM_ind(" ", userBoard.getBoard());
                        for (int m = 0; m < childrens.size(); m++) {
                            result = childrens.get(m);
                            int value = userBoard.getBoard()[GridPane.getRowIndex(result)][GridPane.getColumnIndex(result)];
                            Label label1 = (Label) result;
                            ImageView view5 = new ImageView(images.get(0));

                            if (value == 0) view5 = new ImageView(images.get(9));
                            else if (value == 1) view5 = new ImageView(images.get(1));
                            else if (value == 2) view5 = new ImageView(images.get(2));
                            else if (value == 3) view5 = new ImageView(images.get(3));
                            else if (value == 4) view5 = new ImageView(images.get(4));
                            else if (value == 5) view5 = new ImageView(images.get(5));
                            else if (value == 6) view5 = new ImageView(images.get(6));
                            else if (value == 7) view5 = new ImageView(images.get(7));
                            else if (value == 8) view5 = new ImageView(images.get(8));
                            else if (value == -5) view5 = new ImageView(images.get(13));
                            else if (value == -3) view5 = new ImageView(images.get(14));
                            else if (value == -7) view5 = new ImageView(images.get(0));
                            else if (value == -6) {

                                ObservableList<Node> childrens2 = gridpane.getChildren();
                                realBoard.getBoard()[GridPane.getRowIndex(result)][GridPane.getColumnIndex(result)] = -8;
                                printM_ind(" ", realBoard.getBoard());
                                for (int m2 = 0; m2 < childrens2.size(); m2++) {
                                    result = childrens2.get(m2);
                                    int value2 = realBoard.getBoard()[GridPane.getRowIndex(result)][GridPane.getColumnIndex(result)];
                                    Label label2 = (Label) result;
                                    ImageView view6 = new ImageView(images.get(0));

                                    if (value2 == 0) view6 = new ImageView(images.get(9));
                                    else if (value2 == 1) view6 = new ImageView(images.get(1));
                                    else if (value2 == 2) view6 = new ImageView(images.get(2));
                                    else if (value2 == 3) view6 = new ImageView(images.get(3));
                                    else if (value2 == 4) view6 = new ImageView(images.get(4));
                                    else if (value2 == 5) view6 = new ImageView(images.get(5));
                                    else if (value2 == 6) view6 = new ImageView(images.get(6));
                                    else if (value2 == 7) view6 = new ImageView(images.get(7));
                                    else if (value2 == 8) view6 = new ImageView(images.get(8));
                                    else if (value2 == -1) view6 = new ImageView(images.get(10));
                                    else if (value2 == -8) view6 = new ImageView(images.get(11));

                                    //TODO if flag is wrong then show correct picture - Laima

                                    label2.setGraphic(view6);

                                }

                                break;
                            }
                            label1.setGraphic(view5);

                        }
                    } else if (event.isSecondaryButtonDown()) {
                        if (compare[0].equals("notOpened")) {
                            label.setGraphic(view2);
                            userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -5;
                            compare[0] = "flag";
                        } else if (compare[0].equals("flag")) {
                            label.setGraphic(view3);
                            userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -3;
                            compare[0] = "question";
                        } else if (compare[0].equals("question")) {
                            label.setGraphic(view);
                            userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -7;
                            compare[0] = "notOpened";
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
}
