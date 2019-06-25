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
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class GamePanelController {


    @FXML
    private StackPane stackPane;
    @FXML
    private StackPane borderStackPane;
    @FXML
    void initialize() {
        borderStackPane.setMinWidth(GameBoard.SIZE);
        borderStackPane.setMinHeight(GameBoard.SIZE);
        start();
    }
    void start() {
        int size = 5;
        int full = 3;
        Game game = new Game(size,full);
        Human one = new Human("one", Sign.CROSS,game);
        Computer two = new Computer("two", Sign.CIRCLE,game);
        GameBoard gameBoard = new HumanVsComputer(game,one,two,true);

        borderStackPane.getChildren().clear();
        borderStackPane.getChildren().add(gameBoard);

    }
}
