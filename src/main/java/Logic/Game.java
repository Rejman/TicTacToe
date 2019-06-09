package Logic;

import java.util.*;


public class Game {
    public static final int NUMBER_OF_FIELDS = 9;
    public static final int numberOfWin = 3;
    public static int numbuerOfRows;
    public int move = 0;

    private Sign[][] resultMatrix;


    private List<Integer> emptyFields;

    public Game() {
        Judge.numberOfWin = numberOfWin;
        numbuerOfRows = (int) Math.sqrt(NUMBER_OF_FIELDS);
        Judge.numberOfRows = numbuerOfRows;

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

    public void addMove(int field, Sign value){
        emptyFields.remove(new Integer(field));
        int row = field/numbuerOfRows;
        int column = field%numbuerOfRows;
        resultMatrix[row][column] = value;

            Sign verdict = Judge.getVerdict(field, resultMatrix);
            switch (verdict){
                case CROSS:
                    System.out.println("Cross won");
                    reset();
                    break;
                case CIRCLE:
                    System.out.println("Circle won");
                    reset();
                    break;
            }

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
