package Models.Gui;

import Models.Game.Game;
import Models.Game.Verdict;
import Models.Player.Computer;
import Models.Player.Human;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;

public class HumanVsComputerBoard extends GameBoard {

    private Human human;
    private Computer computer;

    public HumanVsComputerBoard(Game game, Human player, Computer computer) {
        super(game);
        this.human = player;
        this.computer = computer;
    }

    protected void click(ImageView imageView) {

        int numberOfField = allFields.indexOf(imageView);

        if (game.isFree(numberOfField)) {
            if (game.getVerdict() == Verdict.NOBODY) {

                human.move(numberOfField);
                addImageOnBoard(imageView, human.getValue());

            }
            if (game.getVerdict() == Verdict.NOBODY) {
                int field = computer.randomMove();
                ImageView temp = (ImageView) gridPane.getChildren().get(field);
                addImageOnBoard(temp, computer.getValue());
            }
            if (game.getVerdict() != Verdict.NOBODY) {
                System.out.println(game.getVerdict());
                gridPane.setCursor(Cursor.DEFAULT);
            }
        }
    }
}
