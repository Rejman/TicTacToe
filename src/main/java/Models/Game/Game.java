package Models.Game;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private int numberOfFields;
    private int full;
    private int numberOfMovesWithoutVerdict;
    private int size;
    private int numberOfMove;
    private int lastMove;
    private Verdict verdict = Verdict.NOBODY;
    private ResultMatrix resultMatrix;
    private ArrayList<Integer> emptyFields;

    public Game(int size, int full) {

        this.size = size;
        this.full = full;
        this.numberOfMovesWithoutVerdict = full*2-1;
        this.numberOfFields = size*size;
        this.resultMatrix = new ResultMatrix(size);
        setEmptyFields();

    }
    public int getNumberOfFields() {
        return numberOfFields;
    }
    public int getFull() {
        return full;
    }

    public ResultMatrix getResultMatrix() {
        return resultMatrix;
    }

    public void setResultMatrix(ResultMatrix resultMatrix) {
        this.resultMatrix = resultMatrix;
        setEmptyFields();
    }

    /**
     * @return degree of result matrix
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Unlocks all empty fields (movements)
     * reset number of movements
     */
    private void setEmptyFields(){
        this.numberOfMove = 0;
        emptyFields = new ArrayList<Integer>();
        for (int i = 0; i < numberOfFields; i++) {
            emptyFields.add(i);
        }
    }

    /**
     * reset game to initial settings
     */
    public void reset(){
        if(started==null){
            verdict = Verdict.NOBODY;
            setEmptyFields();
            resultMatrix.clearMatrix();
        }else{
            verdict = Verdict.NOBODY;
            setEmptyFields();
            resultMatrix.clearMatrix();
            //Test.startMoves(this);
            this.setGameStatus(started);
        }

    }

    /**
     * This method adds new sign
     * to result matrix and new movement
     * Removes one empty field
     * Runs judge and settings game status (isEnded)
     * @param field number where add a sign
     * @param sign it define what type of sign is adding
     */
    public void addMove(int field, Sign sign){
        if(verdict==Verdict.NOBODY){
            emptyFields.remove(new Integer(field));
            numberOfMove++;
            resultMatrix.add(field, sign);
            lastMove = field;
            if(numberOfMove>=numberOfMovesWithoutVerdict){
                this.verdict = setVerdict();
            }
        }
    }
    public void addMoveWithoutVerdict(int field, Sign sign){
        emptyFields.remove(new Integer(field));
        numberOfMove++;
        resultMatrix.add(field, sign);
    }

    private ResultMatrix started = null;
    public void setGameStatus(ResultMatrix resultMatrix){
        started = resultMatrix.clone();
        Sign[][] values = started.getValues();
        for(int i=0;i<started.degree;i++){
            for(int j=0;j<started.degree;j++){
                switch (values[i][j]){
                    case CIRCLE:
                        addMove(Position.convertToNumber(new Position(i,j),started.degree), Sign.CIRCLE);
                        break;
                    case CROSS:
                        addMove(Position.convertToNumber(new Position(i,j),started.degree), Sign.CROSS);
                        break;
                    case NONE:
                        break;
                }
            }
        }
    }

    /**
     * @return Return all available movements
     */
    public ArrayList<Integer> getEmptyFields() {
        return emptyFields;
    }

    /**
     * Judges game
     * @return Sign that won the game
     */
    private Verdict setVerdict(){

        Verdict winner;

        Sign[] date = resultMatrix.findColumn(lastMove);
        winner = checkLine(date);

        if(winner!=Verdict.NOBODY) return winner;

        date = resultMatrix.findRow(lastMove);
        winner = checkLine(date);

        if(winner!=Verdict.NOBODY) return winner;

        List<Sign> list = resultMatrix.findGrowingDiagonal(lastMove);
        Sign[] temp = new Sign[list.size()];
        for(int i=0;i<temp.length;i++){
            temp[i] = list.get(i);
        }
        winner = checkLine(temp);

        if(winner!=Verdict.NOBODY) return winner;

        list = resultMatrix.findFallingDiagonal(lastMove);
        temp = new Sign[list.size()];
        for(int i=0;i<temp.length;i++){
            temp[i] = list.get(i);
        }
        winner = checkLine(temp);

        if( winner==Verdict.NOBODY && emptyFields.isEmpty()) return Verdict.DRAW;

        return winner;
    }

    /**
     *
     * @param date date to table to check that somebody won in this line
     * @return Sign that won game
     */
    private Verdict checkLine(Sign[] date) {

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
            if (cross == full) return Verdict.CROSS;
            if (circle == full) return Verdict.CIRCLE;
        }
        return Verdict.NOBODY;
    }

    /**
     * @return true if game is over
     */
    public Verdict getVerdict(){
        return verdict;
    }
    public boolean isFree(int field){
        int id = emptyFields.indexOf(new Integer(field));

        if(id>=0) return true;
        else return false;
    }
}
