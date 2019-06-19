package Logic;
import java.util.Random;
import java.util.*;

public class Player {

    private String name;
    private Game game;
    private Set<Integer> steps;
    private Random generator = new Random();
    private Sign value;

    public Player(String name, Sign value, Game game) {

        steps = new HashSet<Integer>();
        this.name = name;
        this.game = game;
        this.value = value;

    }

    public boolean move(int position) {

        List<Integer> emptyFields = game.getEmptyFields();
        if(emptyFields.indexOf(position) != (-1)){
            game.nextMove(position, value);
            steps.add(position);
            return true;
        } else {
            System.out.println("Not empty");
            return false;
        }

    }
    public int randomMove(){
        List<Integer> emptyFields = game.getEmptyFields();
        int numberOfEmptyFields = emptyFields.size();
        if (numberOfEmptyFields > 0) {

            int randomId = generator.nextInt(numberOfEmptyFields);
            int field = emptyFields.get(randomId);
            steps.add(field);
            game.nextMove(field, value);
            return field;
        }
        return -1;
    }
    public void resetSteps(){
        steps.clear();
    }
    public String getName(){
        return this.name;
    }
    public Sign getValue(){
        return value;
    }
}
