package Controllers;
import IO.Serialize;
import RL.Policy;
import RL.Symulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Optional;

public class SymulationPanelController {

    @FXML
    private TitledPane customSettingsPanel;

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
    void train(ActionEvent event) {
        String size =  sizeOfGameBoardSpinner.getValue().toString();
        String number = winningNumberOfSignsSpinner.getValue().toString();
        String expRate = expRateSpinner.getValue().toString();
        String rounds = roundsSpinner.getValue().toString();

        symulation = new Symulation(Integer.parseInt(size), Integer.parseInt(number));
        //System.out.println(Double.parseDouble(expRate)/100);
        symulation.train(Integer.parseInt(rounds),Double.parseDouble(expRate)/100);
        runSaveAlert("filename", symulation.getFirstPlayerPolicy(), symulation.getSecondPlayerPolicy());


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
        //roundsSVF.setValue(5000);


    }
    private void runSaveAlert(String filename, Policy policyCross, Policy policyCircle){
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

    }

}
