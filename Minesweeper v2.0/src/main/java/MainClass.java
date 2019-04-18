import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
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
        setImagetoGridPane(gridpane);
        return gridpane;
    }

    private BorderPane getBorderPane() {
        BorderPane borderpane = new BorderPane();
        Label label1 = new Label("tere");
        borderpane.setTop(label1);
        return borderpane;
    }

    //Method to set board game tiles to board
    private void setImagetoGridPane(GridPane gridpane) {
        Image tile = new Image("File:images/square.png");

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ImageView imageview = new ImageView(tile);
                gridpane.add(imageview, i, j);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
