package Models.Player;

import Models.Game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Computer extends Player {
    //object that chooses random movement
    private Random generator = new Random();
    //REINFORCEMENT_LEARNING_VARIABLES
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

    public ArrayList<String> getStates() {
        return states;
    }

    public void addState(String state) {
        states.add(state);
    }

    public void resetStates() {
        states.clear();
    }

    public void setPolicy(HashMap<String, Double> policy) {
        this.policy = (HashMap<String, Double>) policy;
    }

    public HashMap<String, Double> getPolicy() {
        return (HashMap<String, Double>) policy;
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
            return field;
        }
        return -1;
    }

    public int move(double exp_rate){
        ArrayList<Integer> emptyFields = game.getEmptyFields();
        int action = 0;
        Random generator = new Random();
        //Random generatorInt = new Random();
        if(generator.nextDouble()<=exp_rate){
            if(emptyFields.isEmpty()) return 0;
            int randomId = generator.nextInt(emptyFields.size());
            int field = emptyFields.get(randomId);

            action =  field;
        }else{

            double value_max = -999;

            for (Integer field:emptyFields
            ) {

                ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
                nextResultMatrix.add(field,this.value);
                String nextResultMatrixHash = nextResultMatrix.getHash();
                double value=0.0;
                if(policy.get(nextResultMatrixHash) == null){

                    value = 0.0;
                }else{

                    value = policy.get(nextResultMatrixHash);
                }

                if(value>=value_max){

                    value_max = value;
                    action = field;
                }
            }


        }
        game.addMove(action, value);
        return action;
    }

}
