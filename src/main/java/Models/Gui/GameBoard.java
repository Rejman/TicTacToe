package Models.Gui;

import Models.Game.Game;
import Models.Game.Position;
import Models.Game.ResultMatrix;
import Models.Game.Sign;
import RL.Policy.Tree.Leaf;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.List;

public abstract class GameBoard extends StackPane {


    public final static int SIZE = 600;
    private int sizeOfField = 100;
    protected GridPane gridPane = new GridPane();
    protected List<Field> allFields;
    protected Game game;
    protected Label verdictLabel;

    public Label getVerdictLabel(){
        return verdictLabel;
    }
    public void setVerdictLabel(Label label){
        this.verdictLabel = label;
        label.setText("");
    }
    public void lock(){
        gridPane.setDisable(true);
    }
    public void unlock(){
        gridPane.setDisable(false);
    }
    public GameBoard(Game game) {
        verdictLabel = new Label();
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.game = game;
        sizeOfField = SIZE / game.getSize();
        Field.setSize(sizeOfField,15,20);
        buildGridPane(game.getSize());
        gridPane.setGridLinesVisible(true);
        gridPane.setCursor(Cursor.HAND);
        this.getChildren().add(gridPane);
    }

    private void buildGridPane(int sizeOfBoard) {
        allFields = new ArrayList<Field>();
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {

                Field field = new Field();
                field.addLigtingEffect();
                addMauseAction(field);
                allFields.add(field);
                gridPane.add(field, j, i);

            }
        }
    }


    private void addMauseAction(final Field field) {
        field.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                click(field);
            }

        });
    }
    public void showRates(ArrayList<Integer> rates){
        hideRates();
        int id = 0;
        for (Field field:allFields
             ) {
            field.drawNumber(rates.get(id));
            id++;
        }
    }
    public void hideRates(){
        ResultMatrix resultMatrix = game.getResultMatrix();
        int id =0;
        for (Field field:allFields
        ) {
            field.clear();
            Position pos = Position.convertToPositon(id,resultMatrix.getDegree());
            Sign sign = resultMatrix.getValues()[pos.row][pos.column];
            switch (sign){
                case CIRCLE:
                    field.drawCircle(Color.BLACK);
                    break;
                case CROSS:
                    field.drawCircle(Color.WHITE);
            }
            id++;

        }
    }
    public static StackPane draw(int size, String boardHash, int sizeOfBoard){
        StackPane stackPane = new StackPane();

        GridPane gridPane = new GridPane();
        stackPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        int sizeOfField = size / sizeOfBoard;
        Field.setSize(sizeOfField,45,30);
        int numberOfSignInBoardHash = 0;
        char[] table = boardHash.toCharArray();

        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {

                Field field = new Field();
                field.addLigtingEffect();
                switch (table[numberOfSignInBoardHash]){
                    case 'O':
                        field.drawCircle(Color.BLACK);
                        break;
                    case 'X':
                        field.drawCircle(Color.WHITE);
                        break;
                }
                gridPane.add(field, j, i);
                numberOfSignInBoardHash++;

            }
        }
        gridPane.setGridLinesVisible(true);
        stackPane.getChildren().add(gridPane);
        //stackPane.setAlignment(Pos.CENTER);
        //stackPane.setPrefWidth(USE_COMPUTED_SIZE);

        //gridPane.setPrefHeight();
        //gridPane.setPrefWidth(100);
        //stackPane.setPrefWidth(100);
        return stackPane;

    }
    protected abstract void click(Field field);

    protected void addSignToField(Field field, Sign value) {
        Field.setSize(sizeOfField,45,30);
        switch (value) {
            case CROSS:
                field.drawCircle(Color.WHITE);
                break;
            case CIRCLE:
                field.drawCircle(Color.BLACK);
                break;
        }

    }


}

