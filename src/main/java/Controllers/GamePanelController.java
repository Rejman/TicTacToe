package Controllers;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Gui.*;
import Models.Player.Computer;
import Models.Player.Human;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.File;

import static Models.Gui.GameType.*;

public class GamePanelController {


    private final int GOMOKU_SIZE = 15;
    private final int GOMOKU_FULL = 5;
    private final int TICTACTOE_VALUE = 3;
    private boolean lock = false;

    @FXML
    private Button playButton;
    @FXML
    private TitledPane customSettingsPanel;
    @FXML
    private ChoiceBox<GameType> gameTypeChoiceBox;

    @FXML
    private ChoiceBox<String> opponentChoiceBox;

    @FXML
    private ChoiceBox<Sign> signChoiceBox;

    @FXML
    private CheckBox iWantStartCheckBox;

    @FXML
    private Spinner<Integer> sizeOfGameBoardSpinner;

    @FXML
    private Spinner<Integer> winningNumberOfSignsSpinner;

    @FXML
    private Label verdictLabel;

    @FXML
    void play(ActionEvent event) {
        playButton.setText("RESET");

        int size = sizeOfGameBoardSpinner.getValueFactory().getValue();
        int full = winningNumberOfSignsSpinner.getValueFactory().getValue();
        Sign sign = signChoiceBox.getSelectionModel().getSelectedItem();
        boolean computerFirst;
        if(iWantStartCheckBox.isSelected()) computerFirst = false;
        else computerFirst = true;

        Game newGame = new Game(size, full);

        Human human = new Human("You", sign, newGame);
        Computer computer;
        if(sign==Sign.CIRCLE) computer = new Computer("computer", Sign.CROSS, newGame);
        else computer = new Computer("random", Sign.CIRCLE, newGame);

        HumanVsComputer gameBoard = new HumanVsComputer(newGame, human, computer, computerFirst);
        gameBoard.setVerdictLabel(verdictLabel);
        borderStackPane.getChildren().clear();
        borderStackPane.getChildren().add(gameBoard);



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
    private void buildOpponentChoiceBox(){
        opponentChoiceBox.getItems().add("Random moves");
        opponentChoiceBox.getSelectionModel().select(0);
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
        buildGameTypeChoiceBox();
        buildOpponentChoiceBox();
        buildSignChoiceBox();
        buildSpinners();

        lock = true;
        sizeOfGameBoardSpinner.getValueFactory().setValue(TICTACTOE_VALUE);
        winningNumberOfSignsSpinner.getValueFactory().setValue(TICTACTOE_VALUE);
        lock = false;


        borderStackPane.setMinWidth(GameBoard.SIZE);
        borderStackPane.setMinHeight(GameBoard.SIZE);
        listFilesForFolder(new File("policy"));

    }


    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
