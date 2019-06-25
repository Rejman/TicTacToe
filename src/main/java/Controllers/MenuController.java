package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.Stage;

public class MenuController {

    private Stage mainStage;

    @FXML
    private CheckMenuItem histroyOfMatchesCheckMenuItem;

    @FXML
    private CheckMenuItem MovementsCheckMenuItem;

    @FXML
    void about(ActionEvent event) {
        System.out.println("About");
    }

    @FXML
    void exit(ActionEvent event) {
        System.out.println("Exit program");
    }

    @FXML
    void newGame(ActionEvent event) {

    }

    @FXML
    void newSymulation(ActionEvent event) {
        System.out.println("new symulation");
    }
    @FXML
    void initialize() {


    }
    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
}
