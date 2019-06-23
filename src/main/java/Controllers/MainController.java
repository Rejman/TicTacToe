package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {


    @FXML
    private StackPane gameStackPane;
    @FXML
    private StackPane mainStackPane;
    private GameBoardController gameBoardController;

    private void loadBoardGame(){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/GameBoard.fxml"));
        try {
            this.gameStackPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gameBoardController = loader.getController();
    }

    @FXML
    void initialize() throws IOException {
        loadBoardGame();

    }
    @FXML
    void newGame(ActionEvent event) {
        gameBoardController.resetGame();
    }
}
