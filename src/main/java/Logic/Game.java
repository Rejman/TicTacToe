package Logic;

import java.util.*;


public class Game {

    private int numberOfFields;
    private int full;
    private int numberOfMovesWitchoutVerdict;
    private int numberOfRows;

    private int numberOfMoves;
    private int lastMove;
    private Player playerOne;
    private Player playerTwo;
    private boolean isEnded;
    private Sign verdict;

    private ResultMatrix resultMatrix;

    private List<Integer> emptyFields;

    public boolean isEnded(){
        return isEnded;
    }
    public Game(int degree, int full, Player one, Player two) {

        this.full = full;
        this.numberOfMovesWitchoutVerdict = full*2-1;
        this.numberOfRows = degree;
        this.numberOfFields = degree*degree;
        this.playerOne = one;
        this.playerTwo = two;
        this.playerOne.setGame(this);
        this.playerTwo.setGame(this);
        this.resultMatrix = new ResultMatrix(numberOfRows);
        setEmptyFields();

    }

    private void setEmptyFields(){
        this.numberOfMoves = 0;
        emptyFields = new ArrayList<Integer>();
        // set available moves
        for (int i = 0; i < numberOfFields; i++) {
            emptyFields.add(i);
        }
    }

    public void reset(){
        isEnded = false;
        setEmptyFields();
        resultMatrix.clearMatrix();
    }

    public void addMove(int field, Sign sign){
        if(!isEnded){
            emptyFields.remove(new Integer(field));
            numberOfMoves++;
            resultMatrix.add(field, sign);
            lastMove = field;
            if(numberOfMoves>=numberOfMovesWitchoutVerdict){
                this.verdict = getVerdict();
                if(verdict!=Sign.NONE || emptyFields.isEmpty()) isEnded = true;
            }
        }
    }
    public List<Integer> getEmptyFields() {
        return emptyFields;
    }

    public Sign getVerdict(){

        Sign winnerSign;

        Sign[] date = resultMatrix.findColumn(lastMove);
        winnerSign = checkIfWin(date);

        if(winnerSign==Sign.NONE){
            date = resultMatrix.findRow(lastMove);
            winnerSign = checkIfWin(date);
        }
        if(winnerSign==Sign.NONE){
            List<Sign> list = resultMatrix.findGrowingDiagonal(lastMove);
            Sign[] temp = new Sign[list.size()];
            for(int i=0;i<temp.length;i++){
                temp[i] = list.get(i);
            }
            winnerSign = checkIfWin(temp);
        }
        if(winnerSign==Sign.NONE){
            List<Sign> list = resultMatrix.findFallingDiagonal(lastMove);
            Sign[] temp = new Sign[list.size()];
            for(int i=0;i<temp.length;i++){
                temp[i] = list.get(i);
            }
            winnerSign = checkIfWin(temp);
        }
        return winnerSign;
    }

    private Sign checkIfWin(Sign[] date) {

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
