package Controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Stack;

import RL.Policy.Policy;
import Tools.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainController {

    private Stage mainStage;
    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
    @FXML
    private StackPane mainStackPane;

    @FXML
    private StackPane stackPane;

    @FXML
    void about(ActionEvent event) {

    }

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void newGame(ActionEvent event) throws IOException {

        loadNewView("GamePanel");
    }

    @FXML
    void newSymulation(ActionEvent event) throws IOException {
        loadNewView("SymulationPanel");
    }

    @FXML
    void initialize() {
        stackPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/TestPanel.fxml"));
        try {
            stackPane.getChildren().add((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadNewView(String name){
        stackPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/"+name+".fxml"));
        try {
            stackPane.getChildren().add((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage.sizeToScene();
    }
}
