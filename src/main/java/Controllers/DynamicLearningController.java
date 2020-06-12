package Controllers;

import RL.DynamicSymulation;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class DynamicLearningController {

    public Thread thread;

    @FXML
    private ProgressBar progressBar;
    private Stage parent;

    public Stage getParent() {
        return parent;
    }

    public void setParent(Stage parent) {
        this.parent = parent;
    }

    private DynamicSymulation symulation;

    public void setSymulation(DynamicSymulation symulation) {
        this.symulation = symulation;
    }


    public void start() throws InterruptedException {
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(symulation.progressProperty());
        thread = new Thread(symulation);

        thread.setDaemon(true);
        thread.start();

    }
    @FXML
    void initialize() {
    }
}
