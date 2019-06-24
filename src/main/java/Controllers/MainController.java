package Controllers;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Gui.GameBoard;
import Models.Gui.HumanVsComputer;
import Models.Gui.HumanVsHuman;
import Models.Player.Computer;
import Models.Player.Human;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainController {


    Stage mainStage;
    @FXML
    private StackPane gameStackPane;
    @FXML
    private StackPane mainStackPane;
    private StackPane settingsPane;

    private Human playerOne;
    private Computer playerTwo;


    public void setMainStage(Stage stage){
        this.mainStage = stage;
        settingsPane = gameStackPane;
    }
    @FXML
    void initialize() {

    }
    @FXML
    void newGame(ActionEvent event) {
        Game game = new Game(12,5);
        playerOne = new Human("Person1", Sign.CIRCLE, game);
        playerTwo = new Computer("Person2", Sign.CROSS, game);

        HumanVsComputer gameBoard = new HumanVsComputer(game, playerOne, playerTwo, false);

        gameStackPane.getChildren().add(gameBoard);
        mainStage.setTitle("test");
        mainStage.sizeToScene();

    }
    @FXML
    void settings(ActionEvent event) {
        gameStackPane = settingsPane;
    }
}
