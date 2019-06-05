package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class RootController {

    @FXML
    private StackPane mainStackPane;
    @FXML
    void initialize() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Main.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
            mainStackPane.getChildren().clear();
            mainStackPane.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
