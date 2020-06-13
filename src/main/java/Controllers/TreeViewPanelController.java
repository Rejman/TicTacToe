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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TreeViewPanelController {

    private final int SCROLL_PANEL_SIZE = 500;
    private final int PARENT_SIZE = 200;
    private final int CHILD_SIZE = 80;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.sizeToScene();
    }
    @FXML
    private BorderPane borderPane;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private AnchorPane parentPanel;
    @FXML
    private Label generalInfoLabel;
    @FXML
    private FlowPane childrenPanel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Button backButton;
    @FXML
    private Label numberOfChildrenLabel;
    @FXML
    private Label roundsLabel;

    @FXML
    private Label expRateLabel;
    @FXML
    private Label levelLabel;
    private Policy policy;
    private ArrayList<Leaf> history = new ArrayList<>();

    public void setPolicy(Policy policy){
        this.policy = policy;

        Leaf root = policy.getTree();
        loadTree(root);

        history.add(root);
        signLabel.setText(policy.getSign().toString());
        roundsLabel.setText(policy.getRounds()+"");
        expRateLabel.setText((int)(policy.getExpRate()*100)+" %");

    }
    @FXML
    private Label signLabel;
    @FXML
    void initialize() {
        //childrenPanel.prefWidthProperty().bind(mainStackPane.widthProperty());
        childrenPanel.setPrefWidth(SCROLL_PANEL_SIZE);
        childrenPanel.setPrefHeight(SCROLL_PANEL_SIZE);
        backButton.setPrefWidth(PARENT_SIZE);


    }
    private void loadTree(Leaf leaf){


        System.out.println("Load "+leaf.getState());
        ratingLabel.setText(String.format("%.5f", leaf.getValue()));
        levelLabel.setText(level+"");
        StackPane parent = GameBoard.draw(PARENT_SIZE,leaf.getState(),policy.getSize());


        childrenPanel.getChildren().clear();

        ArrayList<Leaf> children = (ArrayList<Leaf>) leaf.getChildren().clone();
        Collections.sort(children);
        numberOfChildrenLabel.setText(children.size()+"");



        for(int i=0;i<children.size();i++){
            Leaf child = children.get(i);

            StackPane gameBoard = GameBoard.draw(CHILD_SIZE,child.getState(),policy.getSize());
            addMauseAction(gameBoard, child);
            String value = String.format("%.5f", child.getValue());

            VBox childInfo = new VBox();
            childInfo.setAlignment(Pos.CENTER);
            childInfo.setPadding(new Insets(10, 10, 10, 10));
            childInfo.getChildren().add(gameBoard);
            Label rateLabel = new Label(value);
            childInfo.getChildren().add(rateLabel);

            childrenPanel.getChildren().add(childInfo);

        }
        //borderPane.setCenter(parent);
        parentPanel.getChildren().clear();
        parentPanel.getChildren().add(parent);
        stage.sizeToScene();


    }

    private int level = 1;

    private void addMauseAction(final StackPane gameBoard, Leaf leaf) {
        gameBoard.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Lighting lighting = new Lighting();
                gameBoard.setEffect(lighting);
            }
        });
        gameBoard.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gameBoard.setEffect(null);
            }
        });
        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {


            public void handle(MouseEvent event) {
                backButton.setDisable(false);
                addPrefLeaf(leaf);
                loadTree(leaf);
            }


        });
    }

    private void addPrefLeaf(Leaf leaf){
        this.history.add(leaf);
        level++;

    }
    @FXML
    void back(ActionEvent event) {


        if(level>1){
            history.remove(history.size()-1);
            level--;
            Leaf leaf = history.get(level-1);

            this.loadTree(leaf);
        }
        if(level<=1) backButton.setDisable(true);

    }

}
