import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage){
        BorderPane root = getMainPane();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
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

    //Method to set board game tiles to board
    private void setImageToGridPane(GridPane gridpane) {
        Image tile = new Image("File:images/square.png");

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ImageView imageview = new ImageView(tile);
                Label label = new Label();
                label.setGraphic(imageview);
                gridpane.add(label, i, j);
                label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("Tile pressed ");
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

        //TODO here
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
}
