package Logic;

import javafx.scene.image.ImageView;
import java.util.*;


public class Game {
    public static final int NUMBER_OF_FIELDS = 9;

    private int[] resultTable;

    private Random generator = new Random();

    private List<Integer> emptyFields;

    private Set<Integer> player1Steps;
    private Set<Integer> player2Steps;

    public Set<Integer> getPlayer1Steps() {
        return player1Steps;
    }

    public Set<Integer> getPlayer2Steps() {
        return player2Steps;
    }

    public Game() {

        resultTable = new int[NUMBER_OF_FIELDS];

        player1Steps = new LinkedHashSet<Integer>();
        player2Steps = new LinkedHashSet<Integer>();

        emptyFields = new ArrayList<Integer>();
        setEmptyFields();


    }
    public boolean move(int position, Player player) {

        if(emptyFields.indexOf(position) != (-1)){
            emptyFields.remove(new Integer(position));

            saveResult(player, position);

            return true;
        } else {
        System.out.println("Not empty");
        return false;
        }

    }
    private void setEmptyFields(){
        emptyFields.clear();
        // set available moves
        for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
            emptyFields.add(i);
        }
    }
    public int randomMove(Player player){
        int numberOfEmptyFields = emptyFields.size();
        if (numberOfEmptyFields > 0) {

            int randomId = generator.nextInt(numberOfEmptyFields);
            int field = emptyFields.get(randomId);
            emptyFields.remove(randomId);

            saveResult(player, field);

            return field;
        }
        return -1;
    }
    public void reset(){
        setEmptyFields();
        player1Steps.clear();
        player2Steps.clear();
    }

    private int findMin(Integer[] moves){
        int min = moves[0];
        for(int i=1;i<moves.length;i++){
            int temp = moves[i];
            if(temp>min) min=temp;
        }
        return min;
    }

    private void saveResult(Player player, int field){
        if(player == Player.ONE){
            player1Steps.add(field);
            resultTable[field] = 1;
        }
        if(player == Player.TWO){
            player2Steps.add(field);
            resultTable[field] = 2;
        }
        for (int elem:resultTable
             ) {
            System.out.print(elem+", ");
        }
        System.out.println("");
    }
}
