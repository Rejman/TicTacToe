package Logic;

import javafx.geometry.Pos;
import javafx.util.Pair;

import java.util.*;


public class Game {
    public static final int NUMBER_OF_FIELDS = 9;
    public static int numberOfRows;
    public static int full = 3;
    private Position lastMove;

    private ResultMatrix resultMatrix;

    private List<Integer> emptyFields;

    public Game() {

        setNumberOfRows();
        resultMatrix = new ResultMatrix(numberOfRows);
        setEmptyFields();

    }
    private void setNumberOfRows(){
        numberOfRows = (int) Math.sqrt(NUMBER_OF_FIELDS);
    }
    private void setEmptyFields(){
        emptyFields = new ArrayList<Integer>();
        // set available moves
        for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
            emptyFields.add(i);
        }
    }

    public void reset(){
        setEmptyFields();
        resultMatrix.clearMatrix();
    }

    public void nextMove(int field, Sign value){

        lastMove = Position.convertToPositon(field, numberOfRows);
        emptyFields.remove(new Integer(field));
        resultMatrix.values[lastMove.row][lastMove.column] = value;

    }
    public List<Integer> getEmptyFields() {
        return emptyFields;
    }

    public Sign getVerdict(){

        Sign winner;
        Sign[] date = resultMatrix.findColumn(lastMove);
        winner = checkTheMostSign(date);

        if(winner==Sign.NONE){
            date = resultMatrix.findRow(lastMove);
            winner = checkTheMostSign(date);
        }
        if(winner==Sign.NONE){
            List<Sign> list = resultMatrix.findGrowingDiagonal(lastMove);
            Sign[] temp = new Sign[list.size()];
            for(int i=0;i<temp.length;i++){
                temp[i] = list.get(i);
            }
            winner = checkTheMostSign(temp);
        }
        if(winner==Sign.NONE){
            List<Sign> list = resultMatrix.findFallingDiagonal(lastMove);
            Sign[] temp = new Sign[list.size()];
            for(int i=0;i<temp.length;i++){
                temp[i] = list.get(i);
            }
            winner = checkTheMostSign(temp);
        }
        return winner;
    }
    private Sign checkTheMostSign(Sign[] date) {
        System.out.println("Sprawdzam: ");
        for(int i=0;i<date.length;i++){
            System.out.print(date[i]+", ");
        }
        System.out.println();
        int circle = 0;
        int cross = 0;

        for (Sign sign:date
             ) {
            switch (sign) {
                case CIRCLE:
                    circle++;
                    cross = 0;
                    break;
                case CROSS:
                    circle = 0;
                    cross++;
                    break;
                default:
                    cross = 0;
                    circle = 0;
            }
            if (cross == full) return Sign.CROSS;
            if (circle == full) return Sign.CIRCLE;
        }
        return Sign.NONE;
    }


}
