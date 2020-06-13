package Controllers;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Gui.*;
import Models.Player.Computer;
import Models.Player.Human;
import IO.Serialize;
import RL.DynamicLearning;
import RL.Policy.Policy;
import RL.Policy.State;
import RL.Policy.Tree.Leaf;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private ChoiceBox<Sign> signChoiceBox;

    @FXML
    private Label verdictLabel;
    @FXML
    private ProgressIndicator loadProgress;
    @FXML
    void play(ActionEvent event) {
        if(lastLoadedPolicy==null){
            loadPolicy();
            return;
        }


        //int size = sizeOfGameBoardSpinner.getValueFactory().getValue();
        //int full = winningNumberOfSignsSpinner.getValueFactory().getValue();
        int size = lastLoadedPolicy.getSize();
        int full = lastLoadedPolicy.getFull();
        Sign sign = signChoiceBox.getSelectionModel().getSelectedItem();
        String policyName = policyChoiceBox.getSelectionModel().getSelectedItem();
        boolean computerFirst;
        if(sign==Sign.CROSS) computerFirst = false;
        else computerFirst = true;

        Game newGame = new Game(size, full);

        State.degree = size;
        Human human = new Human("You", sign, newGame);
        Computer computer;
        if(sign==Sign.CIRCLE) computer = new Computer("computer", Sign.CROSS, newGame);
        else computer = new Computer("computer", Sign.CIRCLE, newGame);


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
        Sign sign = signChoiceBox.getSelectionModel().getSelectedItem();
        if(sign==Sign.CROSS){
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
                System.out.println("Koniec");

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
        signChoiceBox.getItems().add(Sign.CIRCLE);
        signChoiceBox.getItems().add(Sign.CROSS);
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

        Sign sign = signChoiceBox.getSelectionModel().getSelectedItem();
        switch (sign){
            case CROSS:
                policy = Serialize.loadPolicy(Serialize.pathToFile(policyName, Sign.CROSS));
                break;
            case CIRCLE:
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
}
