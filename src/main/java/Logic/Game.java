package Logic;

import java.util.*;


public class Game {
    public static final int NUMBER_OF_FIELDS = 9;
    public static final int numberOfWin = 3;
    public static int numbuerOfRows;
    private int lastMove;
    private Sign verdict = Sign.NONE;

    private Sign[][] resultMatrix;


    private List<Integer> emptyFields;

    public Game() {

        numbuerOfRows = (int) Math.sqrt(NUMBER_OF_FIELDS);

        resultMatrix = new Sign[numbuerOfRows][numbuerOfRows];
        setResultMatrix();

        emptyFields = new ArrayList<Integer>();
        setEmptyFields();


    }

    private void setEmptyFields(){
        emptyFields.clear();
        // set available moves
        for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
            emptyFields.add(i);
        }
    }

    public void reset(){
        setEmptyFields();
        setResultMatrix();
    }

    public void nextMove(int field, Sign value){

        lastMove = field;
        emptyFields.remove(new Integer(field));
        int row = field/numbuerOfRows;
        int column = field%numbuerOfRows;
        resultMatrix[row][column] = value;
        this.verdict = Judge.getVerdict(lastMove, resultMatrix);

    }
    public Sign getVerdict(){
        System.out.println("Last move "+lastMove);
        System.out.println(this.verdict);
        return verdict;
    }
    public List<Integer> getEmptyFields(){
        return emptyFields;
    }


    private void setResultMatrix(){
        for(int i=0;i<numbuerOfRows;i++){
            for(int j=0;j<numbuerOfRows;j++){
                resultMatrix[i][j] = Sign.NONE;
            }
        }
    }
}
