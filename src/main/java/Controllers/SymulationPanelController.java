package Controllers;
import RL.BaseSymulation;
import RL.Policy.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SymulationPanelController {



    @FXML
    private Slider roundsSlider;
    @FXML
    private Label roundsLabel;
    @FXML
    private Spinner<?> sizeOfGameBoardSpinner;

    @FXML
    private Spinner<?> winningNumberOfSignsSpinner;

    @FXML
    private Slider expRateSlider;

    @FXML
    private Label expRateLabel;
    @FXML
    private Label verdictLabel;

    @FXML
    private Button trainButton;

    @FXML
    void train(ActionEvent event) throws IOException, InterruptedException {

        String size =  sizeOfGameBoardSpinner.getValue().toString();
        String number = winningNumberOfSignsSpinner.getValue().toString();

        double expRate = expRateSlider.getValue();
        int rounds = (int)roundsSlider.getValue();
        expRate = expRate/100;
        rounds = rounds * 1000;
        baseSymulation = new BaseSymulation(Integer.parseInt(size), Integer.parseInt(number), expRate, rounds);
        System.out.println(expRate);
        State.degree = Integer.parseInt(size);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Learning.fxml"));
        StackPane stackPane = loader.load();
        LearningController learningController = loader.getController();
        learningController.setBaseSymulation(baseSymulation);
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

    private BaseSymulation baseSymulation;
    @FXML
    void initialize() {
        buildSpinners();
        buildSliders();

    }
    private void buildSpinners(){
        SpinnerValueFactory sizeSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);
        SpinnerValueFactory numberSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);
        //SpinnerValueFactory roundsSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999999,500000,10000);

        sizeOfGameBoardSpinner.setValueFactory(sizeSVF);
        winningNumberOfSignsSpinner.setValueFactory(numberSVF);
        sizeSVF.setValue(3);
        numberSVF.setValue(3);

    }
    private void buildSliders(){

        roundsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                //cappuccino.setOpacity(new_val.doubleValue());
                roundsSlider.setValue(Math.round(new_val.doubleValue()));
                roundsLabel.setText(""+(int)roundsSlider.getValue()+" 000");
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
