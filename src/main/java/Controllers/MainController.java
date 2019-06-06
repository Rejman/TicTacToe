package Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class MainController {

    private final int SIZE_OF_BOARD = 3;
    private final int NUMBER_OF_FIELDS = 9;
    private Random generator = new Random();

    @FXML
    private GridPane gameBoard;

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image blank = new Image("/img/empty.png");

    private List<Integer> emptyFields;
    private List<ImageView> allFields;

    //history of moves
    private Set<Integer> computerSteps;
    private Set<Integer> playerSteps;

    @FXML
    void initialize() {

        computerSteps = new LinkedHashSet<Integer>();
        playerSteps = new LinkedHashSet<Integer>();
        allFields = new ArrayList<ImageView>();
        emptyFields = new ArrayList<Integer>();
        // set available moves
        for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
            emptyFields.add(i);
        }
        //wypełnienie pola gry obrazkami
        buildFields();

    }

    private void buildFields() {

        for (int i = 0; i < SIZE_OF_BOARD; i++) {

            for (int j = 0; j < SIZE_OF_BOARD; j++) {

                ImageView field = new ImageView();

                clearField(field);
                addMauseAction(field);
                allFields.add(field);
                gameBoard.add(field, j, i);

            }
        }
        //add lines between fields
        gameBoard.setGridLinesVisible(true);
        gameBoard.setCursor(Cursor.HAND);

    }

    private void clearField(ImageView imageView) {
        imageView.setImage(blank);
    }

    private void addMauseAction(final ImageView imageView) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                int id = allFields.indexOf(imageView);

                if (emptyFields.indexOf(id) != (-1)) {
                    move(id, cross);
                    randomMove(circle);
                } else {
                    System.out.println("Not empty");
                }
            }
        });
    }

    private void move(int id, Image sign) {

        playerSteps.add(id);
        emptyFields.remove(new Integer(id));
        ImageView imageView = (ImageView) gameBoard.getChildren().get(id);
        imageView.setImage(sign);

    }

    private void randomMove(Image sign) {

        int numberOfEmptyFields = emptyFields.size();
        if (numberOfEmptyFields > 0) {

            int randomId = generator.nextInt(numberOfEmptyFields);
            int field = emptyFields.get(randomId);
            emptyFields.remove(randomId);
            computerSteps.add(field);
            ImageView imageView = allFields.get(field);
            imageView.setImage(sign);
        }
    }


}

