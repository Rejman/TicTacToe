package Controllers;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class MainController {

    private Random generator = new Random();
    private final int SIZE = 3;
    @FXML
    private GridPane mainGridPane;

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image empty = new Image("/img/empty.png");

    private List<Integer> emptyFields;
    private List<ImageView> fields;

    private Set<Integer> computerSteps;
    private Set<Integer> playerSteps;

    @FXML
    void initialize() {
        computerSteps = new LinkedHashSet<Integer>();
        playerSteps = new LinkedHashSet<Integer>();
        fields = new ArrayList<ImageView>();
        emptyFields = new ArrayList<Integer>();
        emptyFields.add(0);
        emptyFields.add(1);
        emptyFields.add(2);
        emptyFields.add(3);
        emptyFields.add(4);
        emptyFields.add(5);
        emptyFields.add(6);
        emptyFields.add(7);
        emptyFields.add(8);

        buildFields();

    }

    private void buildFields(){
        final int WIDTH = 100;
        final int HEIGHT = 100;

        for(int i=0; i<SIZE; i++){

            for(int j=0;j<SIZE; j++){

                final ImageView imageView = new ImageView();
                imageView.setFitWidth(WIDTH);
                imageView.setFitWidth(HEIGHT);
                imageView.setImage(empty);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        int id = mainGridPane.getChildren().indexOf(imageView);
                        click(id);
                        move(circle);
                        statistics();
                    }
                });
                fields.add(imageView);
                mainGridPane.add(imageView, j, i);
            }
        }
        mainGridPane.setGridLinesVisible(true);
        mainGridPane.setCursor(Cursor.HAND);

    }

    private boolean click(int id){
        int field = emptyFields.indexOf(id);
        if(field>=0){
            playerSteps.add(id);
            emptyFields.remove(new Integer(id));
            ImageView imageView = (ImageView) mainGridPane.getChildren().get(id);
            imageView.setImage(cross);
            return true;
        }
        else{
            System.out.println("Fields is not empty");
            return false;
        }
    }

    private void move(Image shape) {

        int moves = emptyFields.size();
        if(moves>0){

            int id = generator.nextInt(moves);
            int field = emptyFields.get(id);
            computerSteps.add(field);
            ImageView imageView = (ImageView) mainGridPane.getChildren().get(field);
            imageView.setImage(shape);
            emptyFields.remove(id);
        }
    }

    private void statistics(){
        System.out.println("Ruchy gracza: ");
        System.out.println(playerSteps);
        System.out.println("Ruchy komputera: ");
        System.out.println(computerSteps);

    }
}

