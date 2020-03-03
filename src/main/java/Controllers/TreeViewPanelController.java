package Controllers;


import Models.Gui.GameBoard;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TreeViewPanelController {

    @FXML
    private StackPane mainStackPane;
    @FXML
    private StackPane parentPanel;

    @FXML
    private VBox childrenPanel;
    private Policy policy;
    public void setPolicy(Policy policy){
        this.policy = policy;
        Leaf root = policy.getTree();
        String state = root.getState();
        System.out.println(policy.getSize());
        StackPane test = GameBoard.draw(200,state,policy.getSize());

        parentPanel.getChildren().add(test);
        for (Leaf leaf:root.getChildren()
             ) {
            childrenPanel.getChildren().add(GameBoard.draw(100,leaf.getState(),policy.getSize()));
        }
    }
}
