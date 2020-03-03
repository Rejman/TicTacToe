package Controllers;


import Models.Gui.GameBoard;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TreeViewPanelController {

    private final int PARENT_SIZE = 200;
    private final int CHILD_SIZE = 80;
    private final int NUMBER_IN_ROW = 5;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private StackPane parentPanel;

    @FXML
    private VBox childrenPanel;
    private Policy policy;

    public void setPolicy(Policy policy){
        this.policy = policy;
        loadTree(policy.getTree());

    }
    @FXML
    void initialize() {
        //System.out.println( mainStackPane.getPadding().toString());
        //mainStackPane.setPrefHeight(PARENT_SIZE);
        childrenPanel.setPrefWidth(NUMBER_IN_ROW*(CHILD_SIZE+10)+40);
        childrenPanel.setPrefHeight(PARENT_SIZE);
    }
    private void loadTree(Leaf leaf){
        StackPane parent = GameBoard.draw(PARENT_SIZE,leaf.getState(),policy.getSize());
        parentPanel.getChildren().add(parent);

        ArrayList<Leaf> children = (ArrayList<Leaf>) leaf.getChildren().clone();
        Collections.sort(children);

        HBox row = new HBox();
        row.setSpacing(10);
        for(int i=0;i<children.size();i++){
            Leaf child = children.get(i);

            StackPane gameBoard = GameBoard.draw(CHILD_SIZE,child.getState(),policy.getSize());
            String value = String.format("%.5f", child.getValue());

            VBox childInfo = new VBox();
            childInfo.getChildren().add(gameBoard);
            childInfo.getChildren().add(new Label(value));

            row.getChildren().add(childInfo);
            if((i+1)%NUMBER_IN_ROW==0){
                childrenPanel.getChildren().add(row);
                row = new HBox();
                row.setSpacing(10);
            }

        }
        childrenPanel.getChildren().add(row);

    }
}
