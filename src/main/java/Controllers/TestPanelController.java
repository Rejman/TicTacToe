package Controllers;

import RL.BaseSymulation;
import RL.Policy.MultipleSymulation;
import RL.Policy.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class TestPanelController {

    @FXML
    private Spinner<?> sizeOfGameBoardSpinner;

    @FXML
    private Spinner<?> stepsSpinner;
    @FXML
    private Spinner<?> winningNumberOfSignsSpinner;

    @FXML
    private Slider startSlider;

    @FXML
    private Slider expRateSlider;
    @FXML
    private Label startLabel;

    @FXML
    private Label endLabel;
    @FXML
    private Label expRateLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private GridPane gridPanel;
    @FXML
    void test(ActionEvent event) throws IOException, InterruptedException {
        String size =  sizeOfGameBoardSpinner.getValue().toString();
        String number = winningNumberOfSignsSpinner.getValue().toString();
        String steps = stepsSpinner.getValue().toString();
        System.out.println("steps: "+steps);
        double expRate = expRateSlider.getValue();
        int start = (int)startSlider.getValue();

        expRate = expRate/100;
        start = start * 1000;

        MultipleSymulation multipleSymulation = new MultipleSymulation(Integer.parseInt(size), Integer.parseInt(number), expRate, start,Integer.parseInt(steps));
        System.out.println(expRate);
        State.degree = Integer.parseInt(size);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(multipleSymulation.progressProperty());
        Thread thread = new Thread(multipleSymulation);

        thread.setDaemon(true);
        thread.start();

        gridPanel.setDisable(true);
    }
    private void buildSliders(){

        expRateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                //cappuccino.setOpacity(new_val.doubleValue());
                //roundsSlider.setValue(Math.round(new_val.doubleValue()));
                expRateLabel.setText(String.format("%.2f", new_val)+ "%");
            }
        });
        startSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                //cappuccino.setOpacity(new_val.doubleValue());
                startSlider.setValue(Math.round(new_val.doubleValue()));
                startLabel.setText(""+(int)startSlider.getValue()+" 000");
            }
        });

        expRateSlider.setValue(30.0);
    }
    private void buildSpinners(){
        SpinnerValueFactory sizeSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);
        SpinnerValueFactory numberSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50);
        SpinnerValueFactory stepsSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99999999);

        sizeOfGameBoardSpinner.setValueFactory(sizeSVF);
        winningNumberOfSignsSpinner.setValueFactory(numberSVF);
        stepsSpinner.setValueFactory(stepsSVF);

        sizeSVF.setValue(3);
        numberSVF.setValue(3);
        stepsSVF.setValue(1);

    }
    @FXML
    void initialize() {
        buildSpinners();
        buildSliders();
        MultipleSymulation.gridPane = this.gridPanel;
    }

}
