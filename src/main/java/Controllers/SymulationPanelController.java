package Controllers;
import IO.Serialize;
import RL.Policy.Policy;
import RL.Symulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SymulationPanelController {


    @FXML
    private Spinner<?> roundsSpinner;

    @FXML
    private Spinner<?> sizeOfGameBoardSpinner;

    @FXML
    private Spinner<?> winningNumberOfSignsSpinner;

    @FXML
    private Spinner<?> expRateSpinner;

    @FXML
    private Label verdictLabel;

    @FXML
    private Button trainButton;

    @FXML
    void train(ActionEvent event) throws IOException, InterruptedException {


        String size =  sizeOfGameBoardSpinner.getValue().toString();
        String number = winningNumberOfSignsSpinner.getValue().toString();
        String expRate = expRateSpinner.getValue().toString();
        String rounds = roundsSpinner.getValue().toString();

        symulation = new Symulation(Integer.parseInt(size), Integer.parseInt(number), Double.parseDouble(expRate)/100, Integer.parseInt(rounds));
        System.out.println(Double.parseDouble(expRate)/100);

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Learning.fxml"));
        StackPane stackPane = loader.load();
        LearningController learningController = loader.getController();
        learningController.setSymulation(symulation);
        Scene scene = new Scene(stackPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Learning");
        stage.setResizable(false);
        learningController.setParent(stage);
        //stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);

        learningController.start();
        stage.show();



    }

    private Symulation symulation;
    @FXML
    void initialize() {
        buildSpinners();

    }
    private void buildSpinners(){
        SpinnerValueFactory sizeSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);
        SpinnerValueFactory numberSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);
        SpinnerValueFactory expRateSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100);
        SpinnerValueFactory roundsSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999999,500000,10000);

        sizeOfGameBoardSpinner.setValueFactory(sizeSVF);
        winningNumberOfSignsSpinner.setValueFactory(numberSVF);
        expRateSpinner.setValueFactory(expRateSVF);
        roundsSpinner.setValueFactory(roundsSVF);

        expRateSVF.setValue(30);
        sizeSVF.setValue(3);
        numberSVF.setValue(3);


    }
/*    public static void runSaveAlert(String filename, Policy policyCross, Policy policyCircle){
        TextInputDialog dialog = new TextInputDialog(filename);
        dialog.setTitle("Save trained policy");
        dialog.setHeaderText("Save trained policy");
        dialog.setContentText("Please enter file name:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            Serialize.savePolicy(result.get(), policyCircle);
            Serialize.savePolicy(result.get(), policyCross);
        }

    }*/

}
