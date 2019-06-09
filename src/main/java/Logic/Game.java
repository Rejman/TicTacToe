package Logic;

import java.util.*;


public class Game {
    public static final int NUMBER_OF_FIELDS = 9;
    public static int numbuerOfRows;

    private boolean[][] resultMatrix;


    private List<Integer> emptyFields;

    public Game() {
        numbuerOfRows = (int) Math.sqrt(NUMBER_OF_FIELDS);
        resultMatrix = new boolean[numbuerOfRows][numbuerOfRows];

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
    }

    public void addMove(int field, boolean value){
        emptyFields.remove(new Integer(field));
        int row = field/numbuerOfRows;
        int column = field%numbuerOfRows;
        resultMatrix[row][column] = value;
    }
    public List<Integer> getEmptyFields(){
        return emptyFields;
    }

}
