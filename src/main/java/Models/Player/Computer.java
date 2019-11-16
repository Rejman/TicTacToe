package Models.Player;

import Models.Game.*;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;

import java.util.*;

public class Computer extends Player {
    //object that chooses random movement
    private Random generator = new Random();
    //REINFORCEMENT_LEARNING_VARIABLES
    private ArrayList<String> states;
    private Policy policy;
    private Leaf lastMove;
    private ArrayList<Leaf> moves;

    /**
     * @param name
     * @param value
     * @param game
     */
    public Computer(String name, Sign value, Game game) {
        super(name, value, game);
        states = new ArrayList<String>();
        policy = new Policy(this.value,0,0);
        lastMove = policy.getTree();
        moves = new ArrayList<>();
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public void addState(String state) {
        states.add(state);
    }

    public ArrayList<Leaf> getMoves() {
        return moves;
    }

    public void resetStates() {
        states.clear();
        lastMove = policy.getTree();

    }
    public void resetMoves(){
        moves.clear();
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
        lastMove = policy.getTree();
    }


    public Policy getPolicy() {
        return this.policy;
    }

    /**
     * Performs random movement
     *
     * @return field that was chosen
     */
/*    public int randomMove() {
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
    }*/

    public int move(double exp_rate){

        Leaf nextMove = new Leaf("",0.0);

        ArrayList<Integer> emptyFields = game.getEmptyFields();
        int action = 0;
        Random generator = new Random();
        System.out.println("Dostępne ruchy: ");
        for (Leaf leaf:lastMove.getChildren()
             ) {
            System.out.println(leaf.getState()+" "+leaf.getValue());
        }

        if(generator.nextDouble()<=exp_rate){
            if(emptyFields.isEmpty()) return 0;
            int randomId = generator.nextInt(emptyFields.size());
            int field = emptyFields.get(randomId);

            ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
            nextResultMatrix.add(field,this.value);
            String nextResultMatrixHash = nextResultMatrix.getHash();
            //!
            Leaf newLeaf = new Leaf(nextResultMatrixHash, 0.0);
            if(lastMove.getChild(newLeaf)==null){
                //gdy wylosowany ruch jest wylosowany po raz pierwszy
                lastMove.addChild(newLeaf);
            }
            nextMove = lastMove.getChild(newLeaf);


            action =  field;
        }else{

            double value_max = -999;

            for (Integer field:emptyFields
            ) {
                ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
                nextResultMatrix.add(field,this.value);
                String nextResultMatrixHash = nextResultMatrix.getHash();
                double value=0.0;
                Leaf move = new Leaf(nextResultMatrixHash,0.0);
                if(!lastMove.getChildren().isEmpty()){
                    if(lastMove.getChild(move) == null){
                        value = 0.0;
                    }else{
                        value = lastMove.getChild(move).getValue();
                    }
                }

                //!!!
                if(value>=value_max){
                    //!
                    Leaf newLeaf = new Leaf(nextResultMatrixHash, 0.0);
                    if(lastMove.getChild(newLeaf)==null){
                        //gdy wybrany ruch jest wybrany po raz pierwszy
                        lastMove.addChild(newLeaf);
                    }
                    nextMove = lastMove.getChild(newLeaf);

                    value_max = value;
                    action = field;
                }
            }


        }
        lastMove = nextMove;
        //dodanie ostatiego ruchu do swojej pamięci
        this.moves.add(lastMove);

        System.out.println("Najlepszy ruch: ");
        lastMove.showTreeTheBestWay(lastMove.getLevel()+1);
        System.out.println("Last move:"+this.value);
        System.out.println(lastMove.toBoardString());
        game.addMove(action, value);
        return action;
    }

}
