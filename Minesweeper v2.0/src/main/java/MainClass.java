import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainClass extends Application {
    private Map<String, String> highScores; //saves high scores
    private List<Image> images = getImages(); //stores list of images used in game
    private UserBoard userBoard; //used for storing values of a board which is visible to a player
    private Board realBoard; //used for storing values of board which has the real values of the game
    private int openedSquaresNow; //how many opened squares are at the moment
    private List<List<Integer>> checked; //used in method openAround where it saves all checked squares at that time
    private int allFreeSpaces; //saves how many spaces there are (all except bombs)
    private BorderPane top; //pane for time counter
    private AnimationTimer clock; //used for time
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
        level = "beginner";
    }

    //Sets up all the necessary elements to display game board
    private void generateGame(int height, int width, int bombCount) {
        //Loading high scores from file
        highScores = readFromFile();

        //AnimationTimer needs to be initialized in the beginning, so we can use the variable immediately
        clock = addTimeCounter();
        clock.stop();

        this.boardHeight = height;
        this.boardWidth = width;
        this.bombCount = bombCount;
        userBoard = new UserBoard(boardHeight, boardWidth, bombCount);
        setRealBoard(boardHeight, boardWidth, bombCount);
        checked = new ArrayList<>();
        openedSquaresNow = 0;
        allFreeSpaces = boardHeight * boardWidth - bombCount;

        BorderPane root = new BorderPane();
        BorderPane mainPane = getMainPane();

        addBombCounter(0);

        MenuBar menuBar = getMenuBar();

        root.setTop(menuBar);
        root.setCenter(mainPane);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    private MenuBar getMenuBar() {
        Menu menu1 = new Menu("Game");
        Menu menu2 = new Menu("Help");
        Menu subMenu = new Menu("Levels");
        MenuItem menuItem1 = new MenuItem("New");
        menuItem1.setAccelerator(KeyCombination.keyCombination("N")); //Adds keyboard shortcut
        menuItem1.setOnAction(e -> {
            //if new is selected, a new game is generated using the current board parameters
            clock.stop();
            generateGame(boardHeight, boardWidth, bombCount);
        });
        RadioMenuItem choice1Item = new RadioMenuItem("Beginner");
        choice1Item.setAccelerator(KeyCombination.keyCombination("B"));
        choice1Item.setOnAction(e -> {
            level = "beginner";
            clock.stop();
            generateGame(9, 9, 10);
        });
        RadioMenuItem choice2Item = new RadioMenuItem("Intermediate");
        choice2Item.setAccelerator(KeyCombination.keyCombination("I"));
        choice2Item.setOnAction(e -> {
            level = "intermediate";
            clock.stop();
            generateGame(16, 16, 40);
        });
        RadioMenuItem choice3Item = new RadioMenuItem("Expert");
        choice3Item.setAccelerator(KeyCombination.keyCombination("E"));
        choice3Item.setOnAction(e -> {
            level = "expert";
            clock.stop();
            generateGame(16, 30, 99);
        });
        RadioMenuItem choice4Item = new RadioMenuItem("Custom");
        choice4Item.setAccelerator(KeyCombination.keyCombination("C"));
        choice4Item.setOnAction(e -> {
            level = "custom";
            clock.stop();
            generateCustomBoard();
        });

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(choice1Item, choice2Item, choice3Item, choice4Item);

        MenuItem menuItem5 = new MenuItem("High scores");
        menuItem5.setAccelerator(KeyCombination.keyCombination("H"));
        menuItem5.setOnAction(e -> getHighScoreWindow());

        MenuItem menuItem6 = new MenuItem("Exit");
        menuItem6.setAccelerator(KeyCombination.keyCombination("Q"));
        menuItem6.setOnAction(e -> stage.close());

        subMenu.getItems().addAll(choice1Item, choice2Item, choice3Item, choice4Item);
        menu1.getItems().addAll(menuItem1, subMenu, menuItem5, menuItem6);

        MenuItem menuItem2 = new MenuItem("How to");
        menuItem2.setOnAction(e -> getHowToWindow());
        MenuItem menuItem3 = new MenuItem("About");
        menuItem3.setOnAction(e -> getAboutWindow());
        menu2.getItems().addAll(menuItem2, menuItem3);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2);

        return menuBar;
    }

    private void getHighScoreWindow() {
        Stage stage = new Stage();
        stage.setMinHeight(200);
        stage.setMinWidth(300);
        stage.setMaxHeight(300);
        stage.setMaxWidth(500);
        stage.setTitle("High Scores");

        String[] line1 = highScores.get("beginner").split(";");
        String[] line2 = highScores.get("intermediate").split(";");
        String[] line3 = highScores.get("expert").split(";");

        VBox vBox1 = new VBox(10, new Label("Beginner: "), new Label("Intermediate: "), new Label("Expert: "));
        VBox vBox2 = new VBox(10, new Label(line1[0]), new Label(line2[0]), new Label(line3[0]));
        VBox vBox3 = new VBox(10, new Label(line1[1]), new Label(line2[1]), new Label(line3[1]));
        HBox hbox = new HBox(vBox1, vBox2, vBox3);

        Button okButton = new Button("OK");
        Button resetButton = new Button("Reset Scores");
        okButton.setMinWidth(50);
        resetButton.setMinWidth(50);
        HBox buttonHBox = new HBox(20, resetButton, okButton);

        Scene scene = new Scene(new VBox(hbox, buttonHBox));
        stage.setScene(scene);
        stage.show();

        //Binding vBoxes with stages width and height in order to have
        vBox1.layoutXProperty().bind(stage.widthProperty().divide(3).subtract(vBox1.getWidth()));
        vBox2.layoutXProperty().bind(stage.widthProperty().divide(2).subtract(vBox2.getWidth() / 2.0));
        vBox3.layoutXProperty().bind(stage.widthProperty().divide(1.3));
        buttonHBox.layoutXProperty().bind(stage.widthProperty().divide(2).subtract(buttonHBox.getWidth() / 2.0));

        vBox1.layoutYProperty().bind(stage.heightProperty().divide(2).subtract(vBox1.getHeight()));
        vBox2.layoutYProperty().bind(stage.heightProperty().divide(2).subtract(vBox2.getHeight()));
        vBox3.layoutYProperty().bind(stage.heightProperty().divide(2).subtract(vBox3.getHeight()));
        buttonHBox.layoutYProperty().bind(stage.heightProperty().divide(1.5).subtract(buttonHBox.getHeight() / 2.0));

        okButton.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                stage.close();
            }
        });

        resetButton.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                Stage stage2 = new Stage();
                Label text = new Label("Are you sure?");
                text.setPadding(new Insets(20, 20, 20, 28));
                Button button1 = new Button("Yes");
                button1.setDefaultButton(true);
                Button button2 = new Button("No");
                button2.setCancelButton(true);
                HBox Hbox = new HBox(20, button1, button2);
                Hbox.setPadding(new Insets(0, 20, 20, 20));
                Scene scene2 = new Scene(new VBox(text, Hbox));
                stage2.setScene(scene2);
                stage2.setResizable(false);
                stage2.show();

                button1.setOnAction(event2 -> {
                    if (event.isPrimaryButtonDown()) {
                        setDefaultHighScore();
                        stage2.close();
                        stage.close();
                        writeToFile();
                        getHighScoreWindow();
                    }
                });

                button2.setOnAction(event2 -> {
                    if (event.isPrimaryButtonDown()) {
                        stage2.close();
                    }
                });
            }
        });
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
        okButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(50);

        VBox vbox3 = new VBox(okButton, cancelButton);
        vbox3.setPadding(new Insets(20, 20, 20, 10));

        HBox hbox = new HBox();

        hbox.getChildren().addAll(vbox, vbox2, vbox3);

        Scene scene = new Scene(hbox);
        custom.setScene(scene);
        custom.show();
        custom.setResizable(false);

        cancelButton.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                custom.close();
            }
        });

        final int[] x = new int[1];
        final int[] y = new int[1];
        final int[] bombs = new int[1];

        okButton.setOnAction(event -> {
            clock.stop();

            //if textField values are not numeric, then use current boardHeight and boardWidth and bombCount of 10
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

            //minimum bomb count is 10, maximum is the number of squares on the board minus 20
            if (bombs[0] < 10) bombs[0] = 10;
            else if (bombs[0] > (x[0] * y[0] - 20)) bombs[0] = (x[0] * y[0] - 20);


            generateGame(x[0], y[0], bombs[0]);
            custom.close();
        });
    }

    //Generates about window
    private void getAboutWindow() {
        Stage about = new Stage();

        Label label = new Label();
        label.setText("Classic Minesweeper game where the objective is to clear a rectangular board " +
                "containing hidden bombs without detonating any of them, " +
                "with help from clues about the number of neighboring mines in each field.");
        label.setPadding(new Insets(10));
        label.setMaxWidth(400);
        label.setWrapText(true);

        Label label2 = new Label();
        label2.setText("Authors: Laima Anna Dalbina and Eva Ibrus");
        label2.setPadding(new Insets(10));

        Label label3 = new Label();
        label3.setText("Course: Object-oriented Programming, 2018/19 Spring");
        label3.setPadding(new Insets(10));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, label2, label3);
        Scene scene = new Scene(vbox);
        about.setScene(scene);
        about.show();
        about.setResizable(false);
    }

    //generates how to window
    private void getHowToWindow() {
        Stage howTo = new Stage();
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));

        Label first = new Label();
        first.setText("Each Minesweeper game starts out with a grid of unmarked squares. " +
                "It's your job to use the numbers to figure out which of the blank squares have bombs and which are safe to click." +
                "\n\nIf you click on a square containing a bomb, you lose. " +
                "If you manage to click all the squares (without clicking on any bombs) you win. " +
                "\n\nClicking a square which doesn't have a bomb reveals the number of neighbouring squares containing bombs. " +
                "\n\nTo open a square, point at the square and click on it. To mark a square you think is a bomb, point and right-click." +
                "Right-clicking twice will give you a question mark symbol which can be useful if you are unsure about a square. " +
                "\n\nThe upper left corner contains the number of bombs left to find." +
                "\n\nThe upper right corner contains a time counter. The timer will max out at 999 seconds. ");
        first.setMaxWidth(400);
        first.setWrapText(true);

        Button okButton = new Button("OK");
        okButton.setMinWidth(50);

        okButton.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                howTo.close();
            }
        });

        vbox.getChildren().addAll(first, okButton);
        Scene scene = new Scene(vbox);

        howTo.setScene(scene);
        howTo.show();
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Sets or resets real board
    private void setRealBoard(int boardHeight, int boardWidth, int bombCount) {
        realBoard = new Board(boardHeight, boardWidth, bombCount);
        List<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        realBoard.setNumbers();
    }

    //Sets main game pane with squares (center) and timer (top)
    private BorderPane getMainPane() {
        BorderPane pane = new BorderPane();
        top = this.getBorderPane();
        top.setStyle("-fx-background-color: #bdbdbd;");

        GridPane mainSquarePane = new GridPane();
        setImageToGridPane(mainSquarePane);

        pane.setTop(top);
        pane.setCenter(mainSquarePane);

        return pane;
    }

    //Generates borderPane for timer and score
    private BorderPane getBorderPane() {
        BorderPane borderpane = new BorderPane();
        //adds borderPane to the right, where time counter will be placed
        BorderPane counter = new BorderPane();
        borderpane.setRight(counter);

        //shows three zeroes when board has been generated
        counter.setLeft(new ImageView(images.get(16)));
        counter.setCenter(new ImageView(images.get(16)));
        counter.setRight(new ImageView(images.get(16)));
        return borderpane;
    }

    //Loads images from file into list
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

    //Checks whether there is flag in userBoard and puts in into realBoard
    private void checkFlags() {
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
        for (int i = 0; i < boardWidth; i++)
            for (int j = 0; j < boardHeight; j++) {
                final String[] compare = {"notOpened"};

                Label label = new Label();
                ImageView view = new ImageView(images.get(0)); //unopened square
                ImageView view2 = new ImageView(images.get(13)); //flag
                ImageView view3 = new ImageView(images.get(14)); //question
                label.setGraphic(view);
                gridpane.add(label, i, j);

                label.setOnMousePressed(event -> {

                    //Event handler for not allowing to open square if it has a flag
                    EventHandler<MouseEvent> filter = new EventHandler<>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println(event.getEventType());
                            if (event.isPrimaryButtonDown()) {
                                event.consume();
                            } else if (event.isSecondaryButtonDown()) {
                                label.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
                            }
                        }
                    };

                    if (event.isPrimaryButtonDown()) {
                        if (firstClick[0]) { //if it is the first click then it generates new board until the clicked square is not a bomb
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
                            userBoard.getBoard()[x][y] = -6;
                        } else if (realBoard.getBoard()[x][y] == 0) {
                            openAround(x, y, userBoard, realBoard, checked);
                        }
                        openedSquaresNow = userBoard.countOpened();

                        Node result;
                        ObservableList<Node> children = gridpane.getChildren(); //list where are all squares (Nodes) in it

                        for (Node child : children) {
                            result = child;
                            int value = userBoard.getBoard()[GridPane.getRowIndex(result)][GridPane.getColumnIndex(result)]; //value which a certain node has in userBoard
                            Label label1 = (Label) result;
                            ImageView view5 = new ImageView(images.get(0));

                            //disables right click on all squares that have been opened
                            if (value == 0 || value == 1 || value == 2 || value == 3 || value == 4 || value == 5 ||
                                    value == 6 || value == 7 || value == 8) {
                                label1.addEventFilter(MouseEvent.MOUSE_PRESSED, ev -> {
                                    if (ev.getButton() == MouseButton.SECONDARY) {
                                        ev.consume();
                                    }
                                });
                            }

                            //sets the appropriate image according to userBoard
                            if (value == 0) view5 = new ImageView(images.get(9));
                            else if (value == 1) view5 = new ImageView(images.get(1));
                            else if (value == 2) view5 = new ImageView(images.get(2));
                            else if (value == 3) view5 = new ImageView(images.get(3));
                            else if (value == 4) view5 = new ImageView(images.get(4));
                            else if (value == 5) view5 = new ImageView(images.get(5));
                            else if (value == 6) view5 = new ImageView(images.get(6));
                            else if (value == 7) view5 = new ImageView(images.get(7));
                            else if (value == 8) view5 = new ImageView(images.get(8));
                            else if (value == -5) view5 = new ImageView(images.get(13)); //flag
                            else if (value == -3) view5 = new ImageView(images.get(14)); //question mark
                            else if (value == -7) view5 = new ImageView(images.get(0)); //unopened square

                            //if you click on a square that has a bomb on it, the game stops and
                            //all the squares on the board will be opened
                            if (value == -6) {

                                Label lost = new Label("You lost!");
                                lost.setFont(new Font(25));
                                top.setCenter(lost);

                                clock.stop();

                                ObservableList<Node> children2 = gridpane.getChildren();
                                realBoard.getBoard()[GridPane.getRowIndex(result)][GridPane.getColumnIndex(result)] = -8;
                                checkFlags();

                                for (Node child2 : children2) {
                                    result = child2;
                                    int value2 = realBoard.getBoard()[GridPane.getRowIndex(result)][GridPane.getColumnIndex(result)]; //value of realBoard node
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
                                    else if (value2 == -1) view6 = new ImageView(images.get(10)); //bomb
                                    else if (value2 == -8) view6 = new ImageView(images.get(11)); //selected bomb
                                    else if (value2 == -5) view6 = new ImageView(images.get(12)); //marked bomb in wrong location

                                    label2.setGraphic(view6);

                                    //disables all mouse events when a bomb has been clicked
                                    label2.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);
                                }
                                break;
                            }
                            label1.setGraphic(view5);
                        }

                    } else if (event.isSecondaryButtonDown()) {
                        switch (compare[0]) {
                            case "notOpened":
                                label.setGraphic(view2);
                                userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -5;
                                compare[0] = "flag";
                                bombsMarked[0]++;
                                label.addEventFilter(MouseEvent.MOUSE_PRESSED, filter);
                                break;
                            case "flag":
                                label.setGraphic(view3);
                                userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -3;
                                compare[0] = "question";
                                bombsMarked[0]--;
                                break;
                            case "question":
                                label.setGraphic(view);
                                userBoard.getBoard()[GridPane.getRowIndex(label)][GridPane.getColumnIndex(label)] = -7;
                                compare[0] = "notOpened";
                                break;
                        }
                        addBombCounter(bombsMarked[0]);
                    }

                    //if there is the same number of opened squares as originally planned
                    if (openedSquaresNow == allFreeSpaces) {
                        //disable mouse clicks on whole grid when game has been won
                        gridpane.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);

                        Label winning = new Label("You won!");
                        winning.setFont(new Font(25));
                        top.setCenter(winning);

                        if (!level.equals("custom")) {
                            String[] eraldi = highScores.get(level).split(";");
                            if (Integer.parseInt(eraldi[1]) > Integer.parseInt(clock.toString())) {
                                setHighScore(clock.toString());
                            }
                        }
                        clock.stop();
                    }
                });
            }
    }

    // if the person has new high score
    private void setHighScore(String time) {
        Stage highscoreWindow = new Stage();

        Label text = new Label("You have the fastest time for " + level + " level");
        Label text2 = new Label("Please enter your name: ");

        TextField nameTextField = new TextField();
        nameTextField.setPrefWidth(200);
        nameTextField.setText("Anonymous");
        HBox hBox = new HBox(nameTextField);
        hBox.setPadding(new Insets(0, 0, 0, 50));

        Button okButton = new Button("OK");
        okButton.setMinWidth(50);
        HBox hBox2 = new HBox(okButton);
        hBox2.setPadding(new Insets(0, 0, 0, 125));

        VBox vbox = new VBox(5);

        vbox.getChildren().addAll(text, text2, hBox, hBox2);

        Scene scene = new Scene(vbox, 300, 110);

        highscoreWindow.setScene(scene);
        highscoreWindow.show();

        text.setPadding(new Insets(0, 0, 0, (300 - text.getWidth()) / 2.0));
        text2.setPadding(new Insets(0, 0, 0, (300 - text2.getWidth()) / 2.0));

        nameTextField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                highScores.put(level, nameTextField.getText() + ";" + time);
                writeToFile();
                highscoreWindow.close();
            }
        });
        okButton.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                highScores.put(level, nameTextField.getText() + ";" + time);
                writeToFile();
                highscoreWindow.close();
            }
        });
    }

    private void setDefaultHighScore() {
        highScores = new HashMap<>();
        highScores.put("beginner", "Anonymous;999");
        highScores.put("intermediate", "Anonymous;999");
        highScores.put("expert", "Anonymous;999");
    }

    private Map<String, String> readFromFile() {
        Map<String, String> highScores = new HashMap<>();
        boolean inprogress = true;

        while (inprogress) {
            try (BufferedReader br = new BufferedReader(new FileReader("highScores.txt"))) {
                String line = br.readLine();
                while (line != null) {
                    String[] sep = line.split(";");
                    highScores.put(sep[0], sep[1] + ";" + sep[2]);
                    line = br.readLine();
                }
                inprogress = false;
            } catch (IOException e) {
                //if file isn't found, a new file is created with default high score values
                setDefaultHighScore();
                writeToFile();
            }
        }
        return highScores;
    }

    private void writeToFile() {

        try (BufferedWriter br = new BufferedWriter(new FileWriter("highScores.txt"))) {
            br.write("beginner;" + highScores.get("beginner") + "\n");
            br.write("intermediate;" + highScores.get("intermediate") + "\n");
            br.write("expert;" + highScores.get("expert"));

        } catch (IOException e) {
            e.printStackTrace();
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
                    else bp.setRight(view);
                } else {
                    //first digit is zero
                    bp.setLeft(new ImageView(images.get(16)));
                    if (i == 0) bp.setCenter(view);
                    else bp.setRight(view);
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

                //if counter reaches 999 seconds, the clock will stop
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

    private static void openAround(int x, int y, UserBoard userBoard, Board realBoard, List<List<Integer>> checked) {
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

    //method for printing
    private static String toStringJ(int[] var0, int var1, String var2) {
        StringBuilder var3 = new StringBuilder();
        String var4 = "%" + var1 + "d";

        for (int var5 = 0; var5 < var0.length; ++var5) {
            if (var5 == 0) {
                var3.append(String.format(var4, var0[var5]));
            } else {
                var3.append(var2).append(String.format(var4, var0[var5]));
            }
        }

        return var3.toString();
    }

    //method for printing
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
