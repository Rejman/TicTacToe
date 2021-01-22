package Models.Gui;

import Models.Game.*;
import Models.Player.Computer;
import Models.Player.Human;
import javafx.scene.Cursor;

import java.awt.event.MouseEvent;

public class HumanVsComputer extends GameBoard {

    private Human human;
    private Computer computer;

    public HumanVsComputer(Game game, Human player, Computer computer, boolean computerFirst) {

        super(game);
        this.human = player;
        this.computer = computer;

        //Test.startMoves(game);

        updateFields();
        if(computerFirst){
            int field = this.computer.move(0, true);
            Field temp = (Field) gridPane.getChildren().get(field);
            addSignToField(temp, this.computer.getValue());
        }
    }
    private void updateFields(){
        ResultMatrix resultMatrix = this.game.getResultMatrix();
        Sign[][] values = resultMatrix.getValues();
        for(int i=0;i<values.length;i++){
            for(int j=0;j<values.length;j++){
                if(values[i][j]==Sign.CROSS){
                    Field temp = (Field) gridPane.getChildren().get(Position.convertToNumber(new Position(i,j), resultMatrix.getDegree()));
                    addSignToField(temp, Sign.CROSS);
                }else if(values[i][j]==Sign.CIRCLE){
                    Field temp = (Field) gridPane.getChildren().get(Position.convertToNumber(new Position(i,j), resultMatrix.getDegree()));
                    addSignToField(temp, Sign.CIRCLE);
                }
            }
        }
    }
    protected void click(Field field) {
        //field.setDisable(true);
        lock();
        int numberOfField = allFields.indexOf(field);

        if (game.isFree(numberOfField)) {
            if (game.getVerdict() == Verdict.NOBODY) {

                human.move(numberOfField);
                addSignToField(field, human.getValue());

            }
            if (game.getVerdict() == Verdict.NOBODY) {

                int id = -1;
                while(id<0){
                    System.out.println("PETLA id:"+id);
                    id = computer.move(0, true);
                    if(id == -10){
                        System.out.println("STOP");
                        return;
                    }
                }

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

    public void computerMove(){

        int id = computer.move(0, true);
        Field temp = (Field) gridPane.getChildren().get(id);
        addSignToField(temp, computer.getValue());



    }

    private void modifyVerdictLabel(){
        /*Policy updatePolicy = computer.getPolicy();
        Serialize.savePolicy("new",updatePolicy);*/
        Verdict verdict = game.getVerdict();
        Sign sign = Sign.NONE;

        if(verdict==Verdict.CROSS) sign = Sign.CROSS;
        if(verdict==Verdict.CIRCLE) sign = Sign.CIRCLE;

        if(human.getValue()==sign){
            this.verdictLabel.setText(human.getName()+" won");

        }
        if(computer.getValue()==sign) this.verdictLabel.setText(computer.getName()+" won");
        if(verdict==Verdict.DRAW) this.verdictLabel.setText("draw");


    }
}
