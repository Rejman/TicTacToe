package Controllers;


import Models.Game.Verdict;
import Models.Gui.Field;
import Models.Gui.GameBoard;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    private Label generalInfoLabel;
    @FXML
    private VBox childrenPanel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Button backButton;
    @FXML
    private Label numberOfChildrenLabel;

    @FXML
    private Label levelLabel;
    private Policy policy;
    private ArrayList<Leaf> history = new ArrayList<>();

    public void setPolicy(Policy policy){
        this.policy = policy;
        generalInfoLabel.setText(policy.getSign().toString());
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
        ratingLabel.setText(String.format("%.5f", leaf.getValue()));
        levelLabel.setText(level+"");
        StackPane parent = GameBoard.draw(PARENT_SIZE,leaf.getState(),policy.getSize());
        parentPanel.getChildren().add(parent);

        childrenPanel.getChildren().clear();

        ArrayList<Leaf> children = (ArrayList<Leaf>) leaf.getChildren().clone();
        Collections.sort(children);
        numberOfChildrenLabel.setText(children.size()+"");


        HBox row = new HBox();
        row.setSpacing(10);
        for(int i=0;i<children.size();i++){
            Leaf child = children.get(i);

            StackPane gameBoard = GameBoard.draw(CHILD_SIZE,child.getState(),policy.getSize());
            addMauseAction(gameBoard, child);
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
    private int level = -1;
    private void addMauseAction(final StackPane gameBoard, Leaf leaf) {
        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                history.add(leaf);
                level++;
                levelLabel.setText(history.size()+"");
                loadTree(leaf);
            }

        });
    }
    @FXML
    void back(ActionEvent event) {
        if(level<0){
            backButton.setDisable(true);
        }else{
            backButton.setDisable(false);

            Leaf leaf = history.get(level);
            //loadTree(leaf);
            this.loadTree(leaf);
            //history.remove(level);
            level--;
        }

    }

}
