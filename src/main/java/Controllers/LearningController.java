package Controllers;
import IO.Serialize;
import RL.Symulation;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LearningController {

    Stage parent;

    public void setParent(Stage parent) {
        this.parent = parent;
    }

    public BooleanProperty getFinishProperty(){
        return saveButton.visibleProperty();
    }
    private Symulation symulation;

    public Symulation getSymulation() {
        return symulation;
    }

    public void setSymulation(Symulation symulation) {
        this.symulation = symulation;
        this.symulation.setButton(saveButton);
    }
    @FXML
    private TextField fileNameTextField;

    @FXML
    private Button saveButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    void save(ActionEvent event) {
        String filename = fileNameTextField.getText();
        System.out.println("test");
        Serialize.savePolicy(filename, symulation.getFirstPlayerPolicy());
        Serialize.savePolicy(filename, symulation.getSecondPlayerPolicy());
        parent.close();


    }
    public void start() throws InterruptedException {
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(symulation.progressProperty());
        Thread thread = new Thread(symulation);

        thread.setDaemon(true);
        thread.start();

    }
    @FXML
    void initialize() {

    }
}
