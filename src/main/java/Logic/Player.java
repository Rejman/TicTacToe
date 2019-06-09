package Logic;

import javafx.scene.image.Image;

import java.util.*;

public class Player {
    private String name;
    private Game game;
    private Set<Integer> steps;
    private Random generator = new Random();
    private boolean value;

    public Player(String name, boolean value, Game game) {
        steps = new HashSet<Integer>();
        this.name = name;
        this.game = game;
        this.value = value;

    }
    public boolean getValue(){
        return value;
    }
    public boolean move(int position) {

        List<Integer> emptyFields = game.getEmptyFields();
        if(emptyFields.indexOf(position) != (-1)){
            game.addMove(position, value);
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
            game.addMove(field, value);
            steps.add(field);
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
}
