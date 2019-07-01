package Models.Gui;

import Models.Game.Game;
import Models.Game.Verdict;
import Models.Player.Human;
import javafx.scene.Cursor;

public class HumanVsHuman extends GameBoard {

    private Human playerOne;
    private Human playerTwo;
    private Human player;

    public HumanVsHuman(Game game, Human playerOne, Human playerTwo) {
        super(game);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.player = playerOne;
    }

    protected void click(Field field) {

        int numberOfField = allFields.indexOf(field);

        if (game.isFree(numberOfField)) {
            if (game.getVerdict() == Verdict.NOBODY) {

                player.move(numberOfField);
                addSignToField(field, player.getValue());
                changeBeginer();

            }

            if (game.getVerdict() != Verdict.NOBODY) {
                System.out.println(game.getVerdict());
                gridPane.setCursor(Cursor.DEFAULT);
            }
        }

    }

    public void changeBeginer() {
        if (playerOne.equals(player)) {
            player = playerTwo;
        } else {
            player = playerOne;
        }
    }
}
