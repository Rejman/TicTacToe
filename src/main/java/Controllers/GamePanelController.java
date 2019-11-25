package Controllers;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Gui.*;
import Models.Player.Computer;
import Models.Player.Human;
import IO.Serialize;
import RL.Policy.Policy;
import RL.Policy.State;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ArrayList;

import static Models.Gui.GameType.*;

public class GamePanelController {


    private Policy lastLoadedPolicy = null;
    private final int GOMOKU_SIZE = 15;
    private final int GOMOKU_FULL = 5;
    private final int TICTACTOE_VALUE = 3;
    private boolean lock = false;
    @FXML
    private Button infoButton;

    @FXML
    private Button deleteButton;
    @FXML
    private Button playButton;
    @FXML
    private TitledPane customSettingsPanel;
    @FXML
    private ChoiceBox<GameType> gameTypeChoiceBox;

    @FXML
    private ChoiceBox<String> policyChoiceBox;

    @FXML
    private ChoiceBox<Sign> signChoiceBox;

    @FXML
    private Spinner<Integer> sizeOfGameBoardSpinner;

    @FXML
    private Spinner<Integer> winningNumberOfSignsSpinner;

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
        System.out.println("Ok");
        int size = sizeOfGameBoardSpinner.getValueFactory().getValue();
        int full = winningNumberOfSignsSpinner.getValueFactory().getValue();
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


        HumanVsComputer gameBoard = new HumanVsComputer(newGame, human, computer, computerFirst);
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
                lastLoadedPolicy = Serialize.loadPolicy(Serialize.pathToFile(policyName, finalSign));
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("Koniec");
                playButton.setDisable(false);
                playButton.setText("RESET");
                playButton.fire();
            }
        };
        //loadProgressBar.progressProperty().bind(load.progressProperty());
        loadProgress.progressProperty().bind(load.progressProperty());
        loadProgress.setVisible(true);
        Thread thread = new Thread(load);
        thread.start();
    }
    @FXML
    private StackPane stackPane;
    @FXML
    private StackPane borderStackPane;

    private void buildGameTypeChoiceBox(){
        gameTypeChoiceBox.getItems().add(TICTACTOE);
        gameTypeChoiceBox.getItems().add(GOMOKU);
        gameTypeChoiceBox.getItems().add(CUSTOM);
        gameTypeChoiceBox.getSelectionModel().select(0);


        gameTypeChoiceBox.getSelectionModel()
                .selectedIndexProperty()
                .addListener((v, oldValue, newValue) -> {

                    GameType value = gameTypeChoiceBox.getItems().get((int) newValue);
                    lock = true;
                    switch (value){
                        case GOMOKU:
                            sizeOfGameBoardSpinner.getValueFactory().setValue(GOMOKU_SIZE);
                            winningNumberOfSignsSpinner.getValueFactory().setValue(GOMOKU_FULL);
                            break;
                        case TICTACTOE:
                            sizeOfGameBoardSpinner.getValueFactory().setValue(TICTACTOE_VALUE);
                            winningNumberOfSignsSpinner.getValueFactory().setValue(TICTACTOE_VALUE);
                            break;
                        case CUSTOM:
                            customSettingsPanel.setExpanded(true);
                            break;

                    }
                    lock = false;

                });
    }
    private void buildPolicyChoiceBox(){


        ArrayList<String> list = listFilesForFolder(new File("policy"));
        for(int i=0;i<list.size();i++){
            policyChoiceBox.getItems().add(list.get(i));
        }
        policyChoiceBox.getSelectionModel().select(0);
    }
    private void buildSpinners(){
        SpinnerValueFactory sizeSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);
        SpinnerValueFactory numberSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);

        sizeOfGameBoardSpinner.setValueFactory(sizeSVF);
        winningNumberOfSignsSpinner.setValueFactory(numberSVF);

        sizeOfGameBoardSpinner.getValueFactory()
                .valueProperty()
                .addListener((v, oldValue, newValue)->{
                    if(!lock){
                        gameTypeChoiceBox.getSelectionModel().select(CUSTOM);
                    }


        });
        winningNumberOfSignsSpinner.getValueFactory()
                .valueProperty()
                .addListener((v, oldValue, newValue)->{
                    if(!lock){
                        gameTypeChoiceBox.getSelectionModel().select(CUSTOM);
                    }
                });
    }

    private void buildSignChoiceBox(){
        signChoiceBox.getItems().add(Sign.CIRCLE);
        signChoiceBox.getItems().add(Sign.CROSS);
        signChoiceBox.getSelectionModel().select(0);
    }
    @FXML
    void initialize() {
        loadProgress.setVisible(false);

        buildGameTypeChoiceBox();
        buildPolicyChoiceBox();
        buildSignChoiceBox();
        buildSpinners();

        lock = true;
        sizeOfGameBoardSpinner.getValueFactory().setValue(TICTACTOE_VALUE);
        winningNumberOfSignsSpinner.getValueFactory().setValue(TICTACTOE_VALUE);
        lock = false;


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
    void deletePolicy(ActionEvent event) {
        String policyName = policyChoiceBox.getSelectionModel().getSelectedItem();

        boolean deleted = Serialize.deletePolicy(policyName);
        if(deleted==true) policyChoiceBox.getItems().remove(policyName);

    }



    @FXML
    void infoPolicy(ActionEvent event) {
        String policyName = policyChoiceBox.getSelectionModel().getSelectedItem();
        Policy crossPolicy = Serialize.loadPolicy(Serialize.pathToFile(policyName, Sign.CROSS));
        //System.out.println(crossPolicy.getSign().toString());
        System.out.println(crossPolicy.getRounds());
        System.out.println(crossPolicy.getExpRate());
        String message = "rounds: "+crossPolicy.getRounds()+"\n";
        message+="expRate: "+crossPolicy.getExpRate();
        State.degree = 4;
        //System.out.println("Uwaga "+State.degree);
        crossPolicy.getTree().showTree(3);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Policy details");
        alert.setHeaderText("Information of \""+policyName+"\" policy");
        alert.setContentText(message);

        alert.showAndWait();
    }
}
