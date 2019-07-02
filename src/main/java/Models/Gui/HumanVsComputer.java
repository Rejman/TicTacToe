package Models.Gui;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import Models.Player.Human;
import com.sun.prism.paint.Color;
import javafx.scene.Cursor;

public class HumanVsComputer extends GameBoard {

    private Human human;
    private Computer computer;

    public HumanVsComputer(Game game, Human player, Computer computer, boolean computerFirst) {

        super(game);
        this.human = player;
        this.computer = computer;

        if(computerFirst){
            int field = computer.randomMove();
            Field temp = (Field) gridPane.getChildren().get(field);
            addSignToField(temp, computer.getValue());
        }
    }

    protected void click(Field field) {

        int numberOfField = allFields.indexOf(field);

        if (game.isFree(numberOfField)) {
            if (game.getVerdict() == Verdict.NOBODY) {

                human.move(numberOfField);
                addSignToField(field, human.getValue());

            }
            if (game.getVerdict() == Verdict.NOBODY) {
                int id = computer.randomMove();
                Field temp = (Field) gridPane.getChildren().get(id);
                addSignToField(temp, computer.getValue());
            }
            if (game.getVerdict() != Verdict.NOBODY) {
                System.out.println(game.getVerdict());

                modifyVerdictLabel();
                gridPane.setCursor(Cursor.DEFAULT);
            }
        }
    }
    private void modifyVerdictLabel(){
        Verdict verdict = game.getVerdict();
        Sign sign = Sign.NONE;

        if(verdict==Verdict.CROSS) sign = Sign.CROSS;
        if(verdict==Verdict.CIRCLE) sign = Sign.CIRCLE;

        if(human.getValue()==sign){
            this.verdictLabel.setText(human.getName()+" WON");

        }
        if(computer.getValue()==sign) this.verdictLabel.setText(computer.getName()+" WON");
        if(verdict==Verdict.TIE) this.verdictLabel.setText("TIE");


    }
}
