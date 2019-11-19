package Controllers;
import IO.Serialize;
import RL.Policy.Policy;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class LoadPolicyWindowController {

    private Task<Void> task;

    public void setTask(Task<Void> task) {
        this.task = task;
    }

    @FXML
    private ProgressBar progressBar;

    @FXML
    void initialize() {

    }
    public void start() throws InterruptedException {

        progressBar.progressProperty().bind(task.progressProperty());
        Thread thread = new Thread(task);

        thread.setDaemon(true);
        thread.start();


    }




}
