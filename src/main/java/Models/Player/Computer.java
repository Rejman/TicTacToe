package Models.Player;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Computer extends Player {
    //object that chooses random movement
    private Random generator = new Random();
    //REINFORCEMENT_LEARNING_VARIABLES
    private double lr = 0.2;
    private double exp_rate = 0.3;
    private double decay_gamma = 0.9;
    private ArrayList<String> states;
    private HashMap<String, Double> policy;

    /**
     * @param name
     * @param value
     * @param game
     */
    public Computer(String name, Sign value, Game game) {
        super(name, value, game);
        states = new ArrayList<String>();
        policy = new HashMap<String, Double>();
    }

    public void addState(String state) {
        states.add(state);
    }

    public void resetStates() {
        states.clear();
    }

    public void setPolicy(HashMap<String, Double> policy) {
        policy = (HashMap<String, Double>) policy;
    }

    public HashMap<String, Double> getPolicy() {
        return (HashMap<String, Double>) policy;
    }

    public void setReward(double reward) {
        for (int i = states.size() - 1; i >= 0; i--) {
            String state = states.get(i);
            if (policy.get(state) == null) {
                policy.put(state, 0.0);
            }
            double value = policy.get(state);
            value += lr * (decay_gamma * reward - value);
            policy.put(state, value);
            reward = value;
        }
    }

    /**
     * Performs random movement
     *
     * @return field that was chosen
     */
    public int randomMove() {
        if (game.getVerdict() != Verdict.NOBODY) return -1;

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
