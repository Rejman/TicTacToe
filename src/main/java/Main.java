import Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Main.fxml"));
        StackPane stackPane = loader.load();
        MainController mainController = loader.getController();
        Scene scene = new Scene(stackPane);

        primaryStage.setScene(scene);
        mainController.setMainStage(primaryStage);
        primaryStage.setTitle("Tic Tac Toe (Reinforcement Learning)");
        primaryStage.show();
    }
}
