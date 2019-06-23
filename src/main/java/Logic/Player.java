package Logic;
import java.util.Random;
import java.util.*;

public class Player {

    private String name;
    private Game game;
    private Set<Integer> steps;
    private Random generator = new Random();
    private Sign value;

    public Player(String name, Sign value){
        steps = new HashSet<Integer>();
        this.name = name;
        this.value = value;
    }
    public void setGame(Game game){
        this.game = game;
    }

    public boolean move(int field) {

        if(game.isEnded()) return false;

        List<Integer> emptyFields = game.getEmptyFields();
        if(emptyFields.indexOf(field) != (-1)){
            game.addMove(field, value);
            steps.add(field);
            return true;
        } else {
            System.out.println("Not empty");
            return false;
        }

    }
    public int randomMove(){
        if(game.isEnded()) return -1;
        List<Integer> emptyFields = game.getEmptyFields();
        int numberOfEmptyFields = emptyFields.size();
        if (numberOfEmptyFields > 0) {

            int randomId = generator.nextInt(numberOfEmptyFields);
            int field = emptyFields.get(randomId);
            steps.add(field);
            game.addMove(field, value);
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
