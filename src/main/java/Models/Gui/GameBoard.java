package Models.Gui;

import Models.Game.Game;
import Models.Game.Sign;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
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

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image blank = new Image("/img/empty.png");

    public GameBoard(Game game) {

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
                addMauseAction(field);
                allFields.add(field);
                gridPane.add(field, j, i);

            }
        }
    }

    private void clearField(ImageView imageView) {
        imageView.setImage(blank);
    }

    private void addMauseAction(final Field field) {
        field.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {

                click(field);
            }

        });
    }

    protected abstract void click(Field field);

    protected void addSignToField(Field field, Sign value) {

        switch (value) {
            case CROSS:
                field.drawCross(Color.GREEN);

                break;
            case CIRCLE:
                field.drawCircle(Color.BLUE);

                break;
        }
        
    }


}

