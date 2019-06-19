package Controllers;

import Logic.Game;
import Logic.Player;
import Logic.Sign;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class MainController {

    private final int SIZE_OF_IMAGE = 100;
    private int sizeOfBoard;

    @FXML
    private ListView<Integer> playerListView;
    @FXML
    private ListView<Integer> computerListView;

    @FXML
    private GridPane gameBoard;
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image blank = new Image("/img/empty.png");

    private List<ImageView> allFields;

    private Game game;
    private Player player;
    private Player computer;
    @FXML
    void initialize() {

        game = new Game();
        this.player = new Player("You", Sign.CROSS, game);
        this.computer = new Player("Computer",Sign.CIRCLE, game);

        player1Label.setText(player.getName());
        player2Label.setText(computer.getName());

        sizeOfBoard = Game.numberOfRows;
        buildFields();

        //computer start
        randomMove(computer);

    }

    private void buildFields() {

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
                deleteAllEffects();

                boolean correct = player.move(id);
                if(correct){
                    playerListView.getItems().add(id);
                    addImageOnBoard(id, player.getValue());

                    Sign verdict = game.getVerdict();
                    System.out.println(verdict.toString());

                    randomMove(computer);
                }
                //Sign verdict = game.getVerdict();
                //System.out.println(verdict.toString());

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

    private void randomMove(Player player){
        int id = player.randomMove();
        if(id!=(-1)) addImageOnBoard(id, player.getValue());

        computerListView.getItems().add(id);

    }

    private void clearFields(){

        for (ImageView image:allFields
             ) {
            image.setImage(blank);
        }
    }
    @FXML
    void resetGame(ActionEvent event) {
        resetGame();

    }
    void resetGame(){
        clearFields();
        game.reset();
        playerListView.getItems().clear();
        computerListView.getItems().clear();
        player.resetSteps();
        computer.resetSteps();
        randomMove(computer);
        setMauseActions();
    }
    @FXML
    void showComputerMove(MouseEvent event) {
        int id = computerListView.getSelectionModel().getSelectedItem();
        highlightField(id);
    }

    @FXML
    void showPlayerMove(MouseEvent event) {
        int id = playerListView.getSelectionModel().getSelectedItem();
        highlightField(id);
    }

    private void highlightField(int field){
        deleteAllEffects();
        Lighting lighting = new Lighting();
        allFields.get(field).setEffect(lighting);
    }

    private void deleteAllEffects(){
        for (ImageView field:allFields
             ) {
            field.setEffect(null);
        }
    }
}

