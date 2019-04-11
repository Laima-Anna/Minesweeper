import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = getMainPane();

        Scene scene = new Scene(root, 500, 300);
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
        Label label2 = new Label("tsfsdfsgsd");
        tilepane.getChildren().add(label2);

        return tilepane;
    }

    private BorderPane getBorderPane() {
        BorderPane borderpane = new BorderPane();
        Label label1 = new Label("tere");
        borderpane.setTop(label1);
        return borderpane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
 //TODO mänguruudustik - eva
