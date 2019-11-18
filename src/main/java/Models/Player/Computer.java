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
        lastMove = policy.getTree();
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
        lastMove = policy.getTree();
    }


    public Policy getPolicy() {
        return this.policy;
    }

    public int move(double exp_rate){

        Leaf nextMove = new Leaf("???");

        ArrayList<Integer> emptyFields = game.getEmptyFields();

        int action = 0;

        Random generator = new Random();
        /*System.out.println("Dostępne ruchy: ");
        for (Leaf leaf:lastMove.getChildren()
             ) {
            System.out.println(leaf.getState()+" "+leaf.getValue());
        }*/

        if(generator.nextDouble()<=exp_rate){
            //gdy ruch ma być losowy
            //if(emptyFields.isEmpty()) return 0;
            int randomId = generator.nextInt(emptyFields.size());
            int field = emptyFields.get(randomId);

            ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
            nextResultMatrix.add(field,this.value);
            String nextResultMatrixHash = nextResultMatrix.getHash();
            //!
            Leaf randomLeaf = new Leaf(nextResultMatrixHash, 0.0);
            if(lastMove.getChild(randomLeaf)==null){
                //gdy wylosowany ruch jest wylosowany po raz pierwszy
                lastMove.addChild(randomLeaf);
            }
            nextMove = lastMove.getChild(randomLeaf);
            action =  field;
        }else{
            //gdy ruch jest wybierany z polityki
            double value_max = Integer.MIN_VALUE;

            for (Integer field:emptyFields
            ) {
                ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
                nextResultMatrix.add(field,this.value);
                String nextResultMatrixHash = nextResultMatrix.getHash();
                double value=0.0;

                Leaf move = new Leaf(nextResultMatrixHash);
                //if(!lastMove.getChildren().isEmpty()){
                    if(lastMove.getChild(move) == null){
                        //lastMove.addChild(move);
                        value = 0.0;
                    }else{
                        value = lastMove.getChild(move).getValue();
                    }
                //}

                //!!!
                if(value>=value_max){
                    //!
                    Leaf newLeaf = new Leaf(nextResultMatrixHash);
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

        //System.out.println("Najlepszy ruch: ");
        //lastMove.showTreeTheBestWay(lastMove.getLevel()+1);
        //System.out.println("Last move:"+this.value);
        //System.out.println(lastMove.toBoardString());
        game.addMove(action, value);
        return action;
    }

}
