package Controllers;


import Models.Gui.GameBoard;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TreeViewPanelController {

    @FXML
    private StackPane mainStackPane;
    private Policy policy;
    public void setPolicy(Policy policy){
        this.policy = policy;
        String root = policy.getTree().getState();
        StackPane test = GameBoard.draw(100,root,3);

        mainStackPane.getChildren().add(test);
    }
}
