package Models.Gui;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import Models.Player.Human;
import Models.Player.Player;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public abstract class GameBoard extends StackPane {


    private final int SIZE = 600;
    private int sizeOfImage = 100;
    protected GridPane gridPane = new GridPane();
    protected List<ImageView> allFields;
    protected Game game;

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image blank = new Image("/img/empty.png");

    public GameBoard(Game game) {

        this.game = game;
        sizeOfImage = SIZE / game.getSize();
        buildGridPane(game.getSize());
        gridPane.setGridLinesVisible(true);
        gridPane.setCursor(Cursor.HAND);
        this.getChildren().add(gridPane);
    }

    private void buildGridPane(int sizeOfBoard) {
        allFields = new ArrayList<ImageView>();
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {

                ImageView field = new ImageView();
                field.setFitWidth(sizeOfImage);
                field.setFitHeight(sizeOfImage);
                clearField(field);
                addMauseAction(field);
                allFields.add(field);
                gridPane.add(field, j, i);

            }
        }
    }

    private void clearField(ImageView imageView) {
        imageView.setImage(blank);
    }

    private void addMauseAction(final ImageView imageView) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {

                click(imageView);
            }

        });
    }

    protected abstract void click(ImageView imageView);

    protected void addImageOnBoard(ImageView imageView, Sign value) {
        switch (value) {
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

}

