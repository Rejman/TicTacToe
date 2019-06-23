package Controllers;

import Logic.Game;
import Logic.Player;
import Logic.Sign;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class MainController {

    private final int SIZE_OF_IMAGE = 50;
    private int full = 5;
    private int numberOfRows = 10;
    private Game game;
    private Player human;
    private Player ai;
    private boolean newRound = true;

    @FXML
    private GridPane gameBoard;

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image blank = new Image("/img/empty.png");

    private List<ImageView> allFields;

    @FXML
    void initialize() throws InterruptedException {
        human = new Player("You", Sign.CIRCLE);
        ai = new Player("AI", Sign.CROSS);

        game = new Game(numberOfRows,full,human,ai);
        buildFields(numberOfRows);
    }

    private void buildFields(int sizeOfBoard) {
        allFields = new ArrayList<ImageView>();
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
                boolean correct = human.move(id);
                if(correct){
                    addImageOnBoard(id,human.getValue());
                    randomMove(ai);
                }
                if(game.isEnded()){
                    System.out.println(game.getVerdict());
                }

            }

        });
    }
    private void delMauseActions(){
        for (ImageView field:allFields
             ) {
            field.setOnMouseClicked(null);
        }
        gameBoard.setCursor(Cursor.DEFAULT);
    }
    private void setMauseActions(){
        for (ImageView field:allFields
        ) {
            addMauseAction(field);
        }
        gameBoard.setCursor(Cursor.HAND);
    }
    private void addImageOnBoard(int id, Sign value){
        ImageView imageView = (ImageView) gameBoard.getChildren().get(id);
        switch (value){
            case CROSS:
                imageView.setImage(cross);
                break;
            case CIRCLE:
                imageView.setImage(circle);
                break;
                default:
                    imageView.setImage(blank);
        }
    }


    private void clearFields(){

        for (ImageView image:allFields
             ) {
            image.setImage(blank);
        }
    }
    @FXML
    void newGame(ActionEvent event) {
        resetGame();

    }
    void resetGame(){
        Player one = new Player("CirclePlayer", Sign.CIRCLE);
        Player two = new Player("CrossPlayer", Sign.CROSS);

        game = new Game(3,3,one,two);

    }



    private void showEndGameAlert(Sign sign){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game over");
        if(sign == Sign.NONE) alert.setHeaderText("Tie");
        else alert.setHeaderText("Player \""+sign+"\" win.");
        alert.setContentText("New game.");

        alert.showAndWait();
    }

    private void randomMove(Player player) {
        int move = player.randomMove();
        if (move >= 0) {
            addImageOnBoard(move, player.getValue());
        }
    }

}

