package Models.Player;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;

import java.util.List;
import java.util.Random;

public class Computer extends Player {
    //object that chooses random movement
    private Random generator = new Random();
    /**
     * @param name
     * @param value
     * @param game
     */
    public Computer(String name, Sign value, Game game) {
        super(name, value, game);
    }
    /**
     * Performs random movement
     * @return field that was chosen
     */
    public int randomMove(){
        if(game.getVerdict()!= Verdict.NOBODY) return -1;

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
}
