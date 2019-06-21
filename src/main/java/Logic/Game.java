package Logic;

import java.util.*;


public class Game {
    public static final int NUMBER_OF_FIELDS = 9;
    private static int full = 3;
    public static int numberOfRows;
    public Sign verdict = null;

    private int numberOfMoves;
    private Position lastMove;
    private Player one;
    private Player two;

    private ResultMatrix resultMatrix;

    private List<Integer> emptyFields;

    public Game() {
        
        setNumberOfRows();
        resultMatrix = new ResultMatrix(numberOfRows);
        setEmptyFields();

    }
    public Game(Player one, Player two) {

        this.one = one;
        this.two = two;
        this.one.setGame(this);
        this.two.setGame(this);
        
        setNumberOfRows();
        resultMatrix = new ResultMatrix(numberOfRows);
        setEmptyFields();

    }

    private void setNumberOfRows(){
        numberOfRows = (int) Math.sqrt(NUMBER_OF_FIELDS);
    }
    private void setEmptyFields(){
        this.numberOfMoves = 0;
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

        numberOfMoves++;
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
    public Player play(Player one, Player two){

        one.setGame(this);
        two.setGame(this);
        Sign verdict = null;

        while(!emptyFields.isEmpty()){
            one.randomMove();
            two.randomMove();

            if(this.numberOfMoves>=full*2-1){
                verdict = getVerdict();
                if(verdict!=Sign.NONE){
                    break;
                }
            }
        }

        if(one.getValue()==verdict) return one;
        if(two.getValue()==verdict) return two;
        else return null;

    }
}
