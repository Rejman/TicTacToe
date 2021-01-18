package Controllers;

import IO.Serialize;
import RL.BaseSymulation;
import Tools.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LearningController {


    private Thread thread;
    @FXML
    private TextField fileNameTextField;
    @FXML
    private CheckBox autoSaveCheckBox;
    @FXML
    private Button saveButton;

    @FXML
    private ProgressBar progressBar;
    private Stage parent;

    public void setParent(Stage parent) {
        this.parent = parent;
    }

    public BooleanProperty getFinishProperty() {
        return saveButton.visibleProperty();
    }

    private BaseSymulation baseSymulation;

    public BaseSymulation getBaseSymulation() {
        return baseSymulation;
    }

    public void setBaseSymulation(BaseSymulation baseSymulation) {
        this.baseSymulation = baseSymulation;
        this.baseSymulation.setButton(saveButton);
        this.baseSymulation.setAutoSave(autoSaveCheckBox);
    }

    @FXML
    void save(ActionEvent event) {
        String filename = fileNameTextField.getText();
        Task<Void> save = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Serialize.savePolicy(filename, baseSymulation.getFirstPlayerPolicy());
                Serialize.savePolicy(filename, baseSymulation.getSecondPlayerPolicy());
                return null;
            }

            @Override
            protected void succeeded() {
                parent.close();
            }
        };
        progressBar.progressProperty().bind(save.progressProperty());
        Thread thread = new Thread(save);
        thread.start();
    }

    public void start() throws InterruptedException {
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(baseSymulation.progressProperty());
        thread = new Thread(baseSymulation);

        thread.setDaemon(true);
        thread.start();


    }
    @FXML
    void cancel(ActionEvent event) {
        thread.stop();
        parent.close();
    }
    @FXML
    void autoSaveCheckBoxClick(ActionEvent event) {
        if(autoSaveCheckBox.isSelected()) saveButton.setVisible(false);
        else saveButton.setVisible(true);
    }
    @FXML
    void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        fileNameTextField.setText(dtf.format(now));
    }
}
