import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
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
        TilePane another = this.getTilePane();
        pane.setTop(other);
        pane.setCenter(another);
        return pane;
    }

    private TilePane getTilePane() {
        TilePane tilepane = new TilePane();
        setImagetoTilePane(tilepane);
        return tilepane;
    }

    private BorderPane getBorderPane() {
        BorderPane borderpane = new BorderPane();
        Label label1 = new Label("tere");
        borderpane.setTop(label1);
        return borderpane;
    }

    //Method to set board game tiles to board
    private void setImagetoTilePane(TilePane tilepane) {
        Image tile = new Image("File:images/square.png");

        //set preferred column nr to boardwidth
        tilepane.setPrefColumns(9);

        //ignores root size, ensures that preferred size of tilepane is used
        tilepane.setMaxWidth(Region.USE_PREF_SIZE);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilepane.getChildren().add(new ImageView(tile));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
