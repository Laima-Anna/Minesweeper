import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainClass extends Application {
    private Map<String,String> highScores= new HashMap<>();
    private List<Image> images = getImages();
    private UserBoard userBoard;
    private Board realBoard;
    private int openedSquaresNow;
    private List<List<Integer>> checked;
    private int allFreeSpaces;
    private BorderPane top;
    private AnimationTimer clock;
    private int boardHeight;
    private int boardWidth;
    private int bombCount;
    private String level;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = new Stage();

        //Program is started by default with beginner mode
        generateGame(9, 9, 10);
        level="beginner";
    }

    //Sets up all the necessary elements to display game board
    private void generateGame(int height, int width, int bombcount) {
        startGame(height, width, bombcount);

        BorderPane root = new BorderPane();
        BorderPane root2 = getMainPane();

        addBombCounter(0);

        MenuBar menuBar = getMenuBar();

        root.setTop(menuBar);
        root.setCenter(root2);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    private void startGame(int height, int width, int bombcount) {
        //AnimationTimer needs to be initialized in the beginning, so we can use the variable immediately
        clock = addTimeCounter();
        clock.stop();

        this.boardHeight = height;
        this.boardWidth = width;
        this.bombCount = bombcount;
        userBoard = new UserBoard(boardHeight, boardWidth, bombCount);
        setRealBoard(boardHeight, boardWidth, bombCount);
        checked = new ArrayList<>();
        openedSquaresNow = 0;
        allFreeSpaces = boardHeight * boardWidth - bombCount;

    }

    private MenuBar getMenuBar() {
        Menu menu1 = new Menu("Game");
        Menu menu2 = new Menu("Help");
        Menu subMenu = new Menu("Levels");
        MenuItem menuItem1 = new MenuItem("New");
        menuItem1.setAccelerator(KeyCombination.keyCombination("N"));
        menuItem1.setOnAction(e -> {
            //if new is selected, a new game is generated using the current board parameters
            clock.stop();
            generateGame(boardHeight, boardWidth, bombCount);
        });
        RadioMenuItem choice1Item = new RadioMenuItem("Beginner");
        choice1Item.setAccelerator(KeyCombination.keyCombination("B"));
        choice1Item.setOnAction(e -> {
            clock.stop();
            generateGame(9, 9, 10);
            level="beginner";
        });
        RadioMenuItem choice2Item = new RadioMenuItem("Intermediate");
        choice2Item.setAccelerator(KeyCombination.keyCombination("I"));
        choice2Item.setOnAction(e -> {
            clock.stop();
            generateGame(16, 16, 40);
            level="intermediate";
        });
        RadioMenuItem choice3Item = new RadioMenuItem("Expert");
        choice3Item.setAccelerator(KeyCombination.keyCombination("E"));
        choice3Item.setOnAction(e -> {
            level="expert";
            clock.stop();
            generateGame(16, 30, 99);
        });
        RadioMenuItem choice4Item = new RadioMenuItem("Custom");
        choice4Item.setAccelerator(KeyCombination.keyCombination("C"));
        choice4Item.setOnAction(e -> {
            generateCustomBoard();
        });
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(choice1Item, choice2Item, choice3Item, choice4Item);

        MenuItem menuItem5 = new MenuItem("High scores");
        menuItem5.setAccelerator(KeyCombination.keyCombination("H"));
        menuItem5.setOnAction(e -> {
            BorderPane root = new BorderPane();
            Stage stage = new Stage();
            stage.setTitle("High scores");
            stage.setScene(new Scene(root, 400, 200));
            stage.show();

        });
        MenuItem menuItem6 = new MenuItem("Exit");
        menuItem6.setAccelerator(KeyCombination.keyCombination("Q"));
        menuItem6.setOnAction(e -> {
            stage.close();
        });

        subMenu.getItems().addAll(choice1Item, choice2Item, choice3Item, choice4Item);
        menu1.getItems().addAll(menuItem1, subMenu, menuItem5, menuItem6);

        MenuItem menuItem2 = new MenuItem("How to");
        menuItem2.setOnAction(e -> {
            System.out.println("Menu Item 1 Selected");
        });
        MenuItem menuItem3 = new MenuItem("About");
        menuItem3.setOnAction(e -> {
            System.out.println("Menu Item 1 Selected");
        });
        menu2.getItems().addAll(menuItem2, menuItem3);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2);

        return menuBar;
    }

    //Method that shows a separate window where you can input custom board measurements
    private void generateCustomBoard() {
        Stage custom = new Stage();

        Label height = new Label("Height: ");
        Label width = new Label("Width: ");
        Label mines = new Label("Mines: ");

        VBox vbox = new VBox(10, height, width, mines);
        vbox.setPadding(new Insets(20, 10, 20, 20));

        TextField heightField = new TextField();
        heightField.setPrefWidth(40);
        TextField widthField = new TextField();
        widthField.setPrefWidth(40);
        TextField minesField = new TextField();
        minesField.setPrefWidth(40);

        VBox vbox2 = new VBox(heightField, widthField, minesField);
        vbox2.setPadding(new Insets(20, 20, 20, 10));

        Button okButton = new Button("OK");
        okButton.setMinWidth(50);
        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(50);

        VBox vbox3 = new VBox(okButton, cancelButton);
        vbox3.setPadding(new Insets(20, 20, 20, 10));

        HBox hbox = new HBox();

        hbox.getChildren().addAll(vbox, vbox2, vbox3);

        Scene scene = new Scene(hbox);

        custom.setScene(scene);
        custom.show();

        cancelButton.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                custom.close();
            }
        });

        final int[] x = new int[1];
        final int[] y = new int[1];
        final int[] bombs = new int[1];

        okButton.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                clock.stop();

                //if textfield values are not numeric, then use current boardheight and boardwidth and bombcount of 10
                if (isNumeric(heightField.getText())) x[0] = Integer.parseInt(heightField.getText());
                else x[0] = boardHeight;

                if (isNumeric(widthField.getText())) y[0] = Integer.parseInt(widthField.getText());
                else y[0] = boardWidth;

                if (isNumeric(minesField.getText())) bombs[0] = Integer.parseInt(minesField.getText());
                else bombs[0] = 10;

                //minimum height is 9 squares and maximum height 18
                if (x[0] < 9) x[0] = 9;
                else if (x[0] > 18) x[0] = 18;

                //minimum width is 9 squares and maximum width 35
                if (y[0] < 9) y[0] = 9;
                else if (y[0] > 35) y[0] = 35;

                //minimum bombcount is 10, maximum is the number of squares on the board minus 20
                if (bombs[0] < 10) bombs[0] = 10;
                else if (bombs[0] > (x[0] * y[0] - 20)) bombs[0] = (x[0] * y[0] - 20);

            }
            generateGame(x[0], y[0], bombs[0]);
            custom.close();
        });

    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void setRealBoard(int boardHeight, int boardWidth, int bombCount) {
        realBoard = new Board(boardHeight, boardWidth, bombCount);
        List<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        realBoard.setNumbers();
        //printM_ind(" ", realBoard.getBoard());
    }

    private BorderPane getMainPane() {
        BorderPane pane = new BorderPane();
        top = this.getBorderPane();
        top.setStyle("-fx-background-color: #bdbdbd;");
        GridPane another = this.getGridPane();
        pane.setTop(top);
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
        //adds borderpane to the right, where time counter will be placed
        BorderPane counter = new BorderPane();
        borderpane.setRight(counter);

        //shows three zeroes when board has been generated
        counter.setLeft(new ImageView(images.get(16)));
        counter.setCenter(new ImageView(images.get(16)));
        counter.setRight(new ImageView(images.get(16)));
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
        images.add(new Image("File:images/0.png"));
        images.add(new Image("File:images/1.png"));
        images.add(new Image("File:images/2.png"));
        images.add(new Image("File:images/3.png"));
        images.add(new Image("File:images/4.png"));
        images.add(new Image("File:images/5.png"));
        images.add(new Image("File:images/6.png"));
        images.add(new Image("File:images/7.png"));
        images.add(new Image("File:images/8.png"));
        images.add(new Image("File:images/9.png"));
        images.add(new Image("File:images/Counter.png"));
        images.add(new Image("File:images/Minus.png"));

        return images;
    }

    void checkFlags() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (userBoard.getBoard()[i][j] == -5 && realBoard.getBoard()[i][j] != -1)
                    realBoard.getBoard()[i][j] = -5;
            }
        }
    }

    //Method to set board game tiles to board
    private void setImageToGridPane(GridPane gridpane) {
        final int[] bombsMarked = {0};
        final boolean[] firstClick = {true};
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                final String[] compare = {"notOpened"};

                Label label = new Label();
                ImageView view = new ImageView(images.get(0));
                ImageView view2 = new ImageView(images.get(13));
                ImageView view3 = new ImageView(images.get(14));
                label.setGraphic(view);
                gridpane.add(label, i, j);

                label.setOnMousePressed(event -> {
                    if (event.isPrimaryButtonDown()) {
                        if (firstClick[0]) {
                            //time starts counting when first square is opened
                            clock = addTimeCounter();
                            clock.start();
                            while (realBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] == -1) {
                                setRealBoard(boardHeight, boardWidth, bombCount);
                            }
                            firstClick[0] = false;
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

                        //printM_ind(" ", userBoard.getBoard());
                        //printM_ind(" ", realBoard.getBoard());
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

                                clock.stop();

                                ObservableList<Node> childrens2 = gridpane.getChildren();
                                realBoard.getBoard()[GridPane.getRowIndex(result)][GridPane.getColumnIndex(result)] = -8;
                                checkFlags();

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
                                    else if (value2 == -5) view6 = new ImageView(images.get(12));

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
                            bombsMarked[0]++;
                        } else if (compare[0].equals("flag")) {
                            label.setGraphic(view3);
                            userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -3;
                            compare[0] = "question";
                            bombsMarked[0]--;
                        } else if (compare[0].equals("question")) {
                            label.setGraphic(view);
                            userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -7;
                            compare[0] = "notOpened";
                        }
                        addBombCounter(bombsMarked[0]);
                    }

                    if (openedSquaresNow == allFreeSpaces) { //if there is the same number of opened squares as originally planned
                        System.out.println("You won!");
                        if(level.equals("beginner")){
                            String a = highScores.get("beginner");
                            String[] eraldi = a.split(",");
                            if(Integer.parseInt(eraldi[1])<Integer.parseInt(clock.toString())){

                            }
                        }
                        clock.stop();
                    }

                    //event.consume();
                });
            }
        }
    }

    //Method that adds flagged bomb counter to the left
    private void addBombCounter(int bombsMarked) {
        BorderPane bp = new BorderPane();
        ImageView viewCounter = new ImageView(images.get(26));
        bp.getChildren().add(viewCounter);

        int allBombs = realBoard.getBombCount();
        int bombsLeft = allBombs - bombsMarked;
        String bombs = String.valueOf(bombsLeft);

        top.setLeft(setImagesToCounters(bombs, bp));
    }

    //matches integers with the correct picture for counters
    private ImageView counterImages(int a) {
        ImageView view = new ImageView(images.get(16));
        if (a == 1) view = new ImageView(images.get(17));
        else if (a == 2) view = new ImageView(images.get(18));
        else if (a == 3) view = new ImageView(images.get(19));
        else if (a == 4) view = new ImageView(images.get(20));
        else if (a == 5) view = new ImageView(images.get(21));
        else if (a == 6) view = new ImageView(images.get(22));
        else if (a == 7) view = new ImageView(images.get(23));
        else if (a == 8) view = new ImageView(images.get(24));
        else if (a == 9) view = new ImageView(images.get(25));
        return view;
    }

    //sets correct images to the counters
    private BorderPane setImagesToCounters(String numbers, BorderPane bp) {

        for (int i = 0; i < numbers.length(); i++) {
            ImageView view;
            if (numbers.charAt(i) == '-') {
                view = new ImageView(images.get(27));
            } else {
                int a = Integer.parseInt("" + numbers.charAt(i));
                view = counterImages(a);
            }

            //if number is 3 digits long, sets image views either to the left, center or right
            if (numbers.length() == 3) {
                if (i == 0) bp.setLeft(view);
                else if (i == 1) bp.setCenter(view);
                else bp.setRight(view);
            }
            //if number is 2 digits long
            else if (numbers.length() == 2) {
                //if nr is negative, don't show 0 on the left
                if (numbers.charAt(0) == '-') {
                    if (i == 0) bp.setCenter(view);
                    else if (i == 1) bp.setRight(view);
                } else {
                    //first digit is zero
                    bp.setLeft(new ImageView(images.get(16)));
                    if (i == 0) bp.setCenter(view);
                    else if (i == 1) bp.setRight(view);
                }
            }
            //if number is 1 digit long, the first two numbers shown are both zeroes
            else if (numbers.length() == 1) {
                bp.setLeft(new ImageView(images.get(16)));
                bp.setCenter(new ImageView(images.get(16)));
                bp.setRight(view);
            }
        }
        return bp;
    }

    //adds elapsed time counter to top right
    private AnimationTimer addTimeCounter() {
        long startTime = System.currentTimeMillis();
        BorderPane timeCounter = new BorderPane();

        AnimationTimer timer = new AnimationTimer() {
            String time;

            @Override
            public void handle(long now) {

                long elapsedTime = System.currentTimeMillis() - startTime;
                time = Long.toString(elapsedTime / 1000);
                if (Integer.parseInt(time) == 999) this.stop();

                top.setRight(setImagesToCounters(time, timeCounter));
            }

            @Override
            public String toString() {
                return time;
            }
        };
        return timer;
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
}
