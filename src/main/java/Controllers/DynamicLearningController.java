package Controllers;

import IO.Serialize;
import RL.Policy.DynamicSymulation;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import RL.Symulation;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Date;

public class DynamicLearningController {

    private Leaf lastMove = null;
    public Policy newPolicy = null;
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

    public DynamicSymulation getSymulation() {
        return symulation;
    }

    public void setSymulation(DynamicSymulation symulation) {
        this.symulation = symulation;
    }


    public void start() throws InterruptedException {
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(symulation.progressProperty());
        thread = new Thread(symulation);

        thread.setDaemon(true);
        thread.start();

        /*Thread thread2 = new Thread(() ->{
            while(thread.isAlive()){

            }
            System.out.println("KONIEC!!!!!!!");
            this.lastMove =
            parent.close();
        });
        thread2.setDaemon(true);
        thread2.start();*/

    }
    public void close_window(){
        thread.stop();
        parent.close();
    }
    @FXML
    void initialize() {

    }
}
