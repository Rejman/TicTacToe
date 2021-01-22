package Controllers;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Gui.*;
import Models.Player.Computer;
import Models.Player.Human;
import IO.Serialize;
import RL.DynamicLearning;
import RL.DynamicSymulation;
import RL.Policy.Policy;
import RL.Policy.State;
import RL.Policy.Tree.Leaf;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static Models.Gui.GameType.*;

public class GamePanelController {

    @FXML
    private GridPane dynamicSettingsGridPanel;
    @FXML
    private Slider roundsSlider;

    @FXML
    private Label roundsLabel;

    @FXML
    private Slider expRateSlider;

    @FXML
    private Label expRateLabel;

    @FXML
    private ToggleButton dynamicLearningButton;
    @FXML
    public ProgressBar dynamicProgressBar;

    private Policy lastLoadedPolicy = null;
    private final int GOMOKU_SIZE = 15;
    private final int GOMOKU_FULL = 5;
    private final int TICTACTOE_VALUE = 3;
    private final int FourOnFour_VALUE = 4;
    private boolean lock = false;

    @FXML
    private Button resetButton;

    @FXML
    private Button playButton;

    @FXML
    private ChoiceBox<String> policyChoiceBox;

    @FXML
    private ChoiceBox<String> signChoiceBox;

    @FXML
    private Label verdictLabel;
    @FXML
    private ProgressIndicator loadProgress;
    private void setDynamicLearningSettings(){
        double expRate =expRateSlider.getValue();
        expRate = expRate/100;
        int rounds = (int) roundsSlider.getValue();
        rounds = rounds*100;

        DynamicSymulation.expRate = expRate;
        DynamicSymulation.rounds = rounds;
    }
    @FXML
    void play(ActionEvent event) {
        this.setDynamicLearningSettings();
        if(lastLoadedPolicy==null){
            loadPolicy();
            return;
        }


        //int size = sizeOfGameBoardSpinner.getValueFactory().getValue();
        //int full = winningNumberOfSignsSpinner.getValueFactory().getValue();
        System.out.println("TO jest polityka dla komutera: "+lastLoadedPolicy.getSign());
        int size = lastLoadedPolicy.getSize();
        int full = lastLoadedPolicy.getFull();
        String color = signChoiceBox.getSelectionModel().getSelectedItem();
        Sign sign;
        String policyName = policyChoiceBox.getSelectionModel().getSelectedItem();
        boolean computerFirst;
        if(color.equals("WHITE")){
            computerFirst = false;
            sign = Sign.CROSS;
        }
        else{
            computerFirst = true;
            sign = Sign.CIRCLE;
        }

        Game newGame = new Game(size, full);

        State.degree = size;
        Human human = new Human("You", sign, newGame);
        Computer computer;
        if(sign==Sign.CIRCLE){
            computer = new Computer("computer", Sign.CROSS, newGame);
        }
        else{
            computer = new Computer("computer", Sign.CIRCLE, newGame);
        }


        computer.setPolicy(lastLoadedPolicy);
        computer.setProgressBar(this.dynamicProgressBar);


        HumanVsComputer gameBoard = new HumanVsComputer(newGame, human, computer, computerFirst);
        // dodanie planszy komputerowemu graczowi
        computer.game_board = gameBoard;

        gameBoard.setVerdictLabel(verdictLabel);
        borderStackPane.getChildren().clear();
        borderStackPane.getChildren().add(gameBoard);

    }
    private void loadPolicy(){
        String policyName = policyChoiceBox.getSelectionModel().getSelectedItem();

        if(policyName == null){
            lastLoadedPolicy = null;
            return;
        }
        String color = signChoiceBox.getSelectionModel().getSelectedItem();
        Sign sign;
        //tu ma byc odwrotnie -> (CROSS) a sign = CIRCLE
        if(color.equals("WHITE")){
            sign=Sign.CIRCLE;
        }else{
            sign=Sign.CROSS;
        }
        Sign finalSign = sign;
        Task<Void> load = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                playButton.setDisable(true);
                if(policyChoiceBox.getItems().isEmpty()){
                    lastLoadedPolicy = null;
                }else{
                    lastLoadedPolicy = Serialize.loadPolicy(Serialize.pathToFile(policyName, finalSign));
                }
                return null;
            }

            @Override
            protected void succeeded() {

                playButton.setDisable(false);
                resetButton.setVisible(true);
                resetButton.fire();
            }
        };

        loadProgress.progressProperty().bind(load.progressProperty());
        loadProgress.setVisible(true);
        Thread thread = new Thread(load);

        thread.start();

    }
    @FXML
    private StackPane stackPane;
    @FXML
    private StackPane borderStackPane;

    private void buildPolicyChoiceBox() throws IOException {
        policyChoiceBox.getItems().clear();
        ArrayList<String> list = new ArrayList<>();
        try{
            list = listFilesForFolder(new File("policy"));
        }catch(Exception ex){
            Files.createDirectory(Paths.get("policy"));
        }
        for(int i=0;i<list.size();i++){
            policyChoiceBox.getItems().add(list.get(i));
        }
        policyChoiceBox.getSelectionModel().select(0);
    }

    private void buildSignChoiceBox(){
        /*signChoiceBox.getItems().add(Sign.CIRCLE);
        signChoiceBox.getItems().add(Sign.CROSS);*/
        signChoiceBox.getItems().add("BLACK");
        signChoiceBox.getItems().add("WHITE");
        signChoiceBox.getSelectionModel().select(0);
    }
    @FXML
    void initialize() throws IOException {

        loadProgress.setVisible(false);

        buildPolicyChoiceBox();
        buildSignChoiceBox();

        borderStackPane.setMinWidth(GameBoard.SIZE);
        borderStackPane.setMinHeight(GameBoard.SIZE);
        borderStackPane.setMinHeight(GameBoard.SIZE);
        listFilesForFolder(new File("policy"));
        buildSliders();


    }

    public ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> list = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                String name = fileEntry.getName();
                int id = name.indexOf(".CROSS");
                if(id>=0){
                    list.add(name.substring(0,id));
                }
            }
        }
        return list;
    }
    @FXML
    void deletePolicy(ActionEvent event) throws IOException {
        String policyName = policyChoiceBox.getSelectionModel().getSelectedItem();

        boolean deleted = Serialize.deletePolicy(policyName);
        if(deleted==true){
            buildPolicyChoiceBox();
        }

    }



    @FXML
    void infoPolicy(ActionEvent event) throws IOException {
        String policyName = policyChoiceBox.getSelectionModel().getSelectedItem();
        Policy policy =null;

        String color = signChoiceBox.getSelectionModel().getSelectedItem();
        switch (color){
            case "WHITE":
                policy = Serialize.loadPolicy(Serialize.pathToFile(policyName, Sign.CROSS));
                break;
            case "BLACK":
                policy = Serialize.loadPolicy(Serialize.pathToFile(policyName, Sign.CIRCLE));
                break;
        }

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/TreeViewPanel.fxml"));
        StackPane stackPane = loader.load();
        TreeViewPanelController treeViewPanelController = loader.getController();
        Scene scene = new Scene(stackPane);

        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        treeViewPanelController.setStage(primaryStage);
        treeViewPanelController.setPolicy(policy);

        primaryStage.setTitle(policyName+" - tree view");
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    private void buildSliders(){

        roundsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                //cappuccino.setOpacity(new_val.doubleValue());
                roundsSlider.setValue(Math.round(new_val.doubleValue()));
                roundsLabel.setText(""+(int)roundsSlider.getValue()+"00");
            }
        });

        expRateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                //cappuccino.setOpacity(new_val.doubleValue());
                //roundsSlider.setValue(Math.round(new_val.doubleValue()));
                expRateLabel.setText(String.format("%.2f", new_val)+ "%");
            }
        });

        roundsSlider.setValue(10);
        expRateSlider.setValue(30.0);
    }
    @FXML
    void offOnDynamicLearning(ActionEvent event) {
        boolean selected = dynamicLearningButton.isSelected();
        if(selected){
            dynamicLearningButton.setText("ON");
            dynamicSettingsGridPanel.setDisable(false);
            Computer.dynamicLearning = true;
        }
        else{
            dynamicLearningButton.setText("OFF");
            dynamicSettingsGridPanel.setDisable(true);
            Computer.dynamicLearning = false;
        }
    }
}
