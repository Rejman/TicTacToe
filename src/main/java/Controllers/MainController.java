package Controllers;

import Logic.Game;
import Logic.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class MainController {

    private int sizeOfBoard;
    private final int SIZE_OF_IMAGE = 100;

    @FXML
    private Label playerMovesLabel;
    @FXML
    private Label computerMovesLabel;

    @FXML
    private GridPane gameBoard;

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image blank = new Image("/img/empty.png");

    private List<ImageView> allFields;

    private Game game;

    @FXML
    void initialize() {

        allFields = new ArrayList<ImageView>();


        game = new Game();
        sizeOfBoard = (int) Math.sqrt(Game.NUMBER_OF_FIELDS);
        //wypełnienie pola gry obrazkami
        buildFields();
        randomMove(Player.TWO, cross);

    }

    private void buildFields() {

        for (int i = 0; i < sizeOfBoard; i++) {

            for (int j = 0; j < sizeOfBoard; j++) {

                ImageView field = new ImageView();
                field.setFitWidth(SIZE_OF_IMAGE);
                field.setFitHeight(SIZE_OF_IMAGE);
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

                boolean correct = game.move(id, Player.ONE);
                if(correct){
                    playerMovesLabel.setText(game.getPlayer1Steps().toString());
                    addImageOnBoard(id, circle);
                    randomMove(Player.TWO, cross);
                }

            }

        });
    }

    private void addImageOnBoard(int id, Image sign){
        ImageView imageView = (ImageView) gameBoard.getChildren().get(id);
        imageView.setImage(sign);
    }

    private void randomMove(Player player, Image sign){
        int id = game.randomMove(player);
        if(id!=(-1)) addImageOnBoard(id, sign);

        computerMovesLabel.setText(game.getPlayer2Steps().toString());

    }

    private void clearFields(){

        for (ImageView image:allFields
             ) {
            image.setImage(blank);
        }
    }
    @FXML
    void resetGame(ActionEvent event) {
        clearFields();
        game.reset();
        playerMovesLabel.setText("empty");
        computerMovesLabel.setText("empty");
        randomMove(Player.TWO, cross);
    }

}

