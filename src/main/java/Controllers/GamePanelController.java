package Controllers;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Gui.GameBoard;
import Models.Gui.HumanVsHuman;
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
        int size = 15;
        int full = 5;
        Game game = new Game(size,full);
        Human one = new Human("one", Sign.CROSS,game);
        Human two = new Human("two", Sign.CIRCLE,game);
        HumanVsHuman gameBoard = new HumanVsHuman(game,one,two);

        borderStackPane.getChildren().clear();
        borderStackPane.getChildren().add(gameBoard);

    }
}
