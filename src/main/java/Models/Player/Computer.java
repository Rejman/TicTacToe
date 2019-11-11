package Models.Player;

import Models.Game.*;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Computer extends Player {
    //object that chooses random movement
    private Random generator = new Random();
    //REINFORCEMENT_LEARNING_VARIABLES
    private ArrayList<String> states;
    private Policy policy;

    /**
     * @param name
     * @param value
     * @param game
     */
    public Computer(String name, Sign value, Game game) {
        super(name, value, game);
        states = new ArrayList<String>();
        policy = new Policy(this.value,0,0);
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

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }


    public Policy getPolicy() {
        return this.policy;
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
        Leaf theBestLeaf = null;

        ArrayList<Integer> emptyFields = game.getEmptyFields();
        int action = 0;
        Random generator = new Random();
        //Random generatorInt = new Random();
        if(generator.nextDouble()<=exp_rate){
            if(emptyFields.isEmpty()) return 0;
            int randomId = generator.nextInt(emptyFields.size());
            int field = emptyFields.get(randomId);

            HashMap<Leaf,Double> leaves = policy.getCurrentLeaf().getLeaves();

            ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
            nextResultMatrix.add(field,this.value);
            String nextResultMatrixHash = nextResultMatrix.getHash();

            Leaf newLeaf = new Leaf(nextResultMatrixHash);
            leaves.put(newLeaf,0.0);
            action =  field;
        }else{

            double value_max = -999;

            for (Integer field:emptyFields
            ) {
                HashMap<Leaf,Double> leaves = policy.getCurrentLeaf().getLeaves();

                ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
                nextResultMatrix.add(field,this.value);
                String nextResultMatrixHash = nextResultMatrix.getHash();
                double value=0.0;
                Leaf leaf = new Leaf(nextResultMatrixHash);
                if(leaves.get(leaf) == null){

                    value = 0.0;
                }else{

                    value = leaves.get(leaf);
                }

                if(value>=value_max){
                    theBestLeaf = leaf;
                    value_max = value;
                    action = field;
                }
            }


        }
        policy.setCurrentLeaf(theBestLeaf);
        game.addMove(action, value);
        return action;
    }

}
