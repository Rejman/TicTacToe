package Controllers;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Gui.Field;
import Models.Gui.GameBoard;
import Models.Gui.HumanVsComputer;
import Models.Gui.HumanVsHuman;
import Models.Player.Computer;
import Models.Player.Human;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GamePanelController {


    @FXML
    private HBox buttonHBox;

    @FXML
    private StackPane stackPane;
    @FXML
    private StackPane borderStackPane;
    @FXML
    void initialize() {
        Field cross = new Field();
        Field circle = new Field();
        cross.drawCross(Color.GREEN);
        circle.drawCircle(Color.BLUE);
        circle.addLigtingEffect();
        cross.addLigtingEffect();

        buttonHBox.getChildren().add(cross);
        buttonHBox.getChildren().add(circle);
        borderStackPane.setMinWidth(GameBoard.SIZE);
        borderStackPane.setMinHeight(GameBoard.SIZE);
        start();
    }
    void start() {

    }
}
