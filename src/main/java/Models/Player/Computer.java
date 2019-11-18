package Models.Player;

import Models.Game.*;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;

import java.util.*;

public class Computer extends Player {
    //object that chooses random movement
    private Random generator = new Random();
    private Policy policy;
    private Leaf lastMove;
    private Leaf nextMove;
    private ArrayList<Leaf> moves;

    /**
     * @param name
     * @param value
     * @param game
     */
    public Computer(String name, Sign value, Game game) {
        super(name, value, game);
        policy = new Policy(this.value,0,0);
        //lastMove = policy.getTree();
        moves = new ArrayList<>();
        resetMoves();
    }

    public ArrayList<Leaf> getMoves() {
        return moves;
    }

    public void resetMoves(){
        moves.clear();
        lastMove = policy.getTree();
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
        resetMoves();
    }

    public Policy getPolicy() {
        return this.policy;
    }
    private int randomMove(ArrayList<Integer> emptyFields){
        emptyFields = game.getEmptyFields();

        int randomId = generator.nextInt(emptyFields.size());
        int field = emptyFields.get(randomId);

        ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
        nextResultMatrix.add(field,this.value);
        String nextResultMatrixHash = nextResultMatrix.getHash();
        //!
        nextMove = new Leaf(nextResultMatrixHash);
        if(lastMove.getChild(nextMove)==null){
            //gdy wylosowany ruch jest wylosowany po raz pierwszy
            lastMove.addChild(nextMove);
        }
        nextMove = lastMove.getChild(nextMove);

        return field;
    }
    public int move(double exp_rate){

        nextMove = new Leaf("");

        ArrayList<Integer> emptyFields = game.getEmptyFields();

        int action = 0;

        Random generator = new Random();
        if(generator.nextDouble()<=exp_rate){
            //if move is random
            action =  randomMove(emptyFields);

        }else{
            //if move is form policy
            double valueMax = Integer.MIN_VALUE;

            for (Integer field:emptyFields
            ) {
                ResultMatrix nextResultMatrix = game.getResultMatrix().clone();
                nextResultMatrix.add(field,this.value);
                String nextResultMatrixHash = nextResultMatrix.getHash();

                double value = 0.0;
                Leaf newLeaf = lastMove.getChild(new Leaf(nextResultMatrixHash));
                if(newLeaf != null){
                    value = newLeaf.getValue();
                }else{
                    lastMove.addChild(new Leaf(nextResultMatrixHash, value));
                }
                //!!!
                if(value>=valueMax){
                    //!
                    newLeaf = new Leaf(nextResultMatrixHash);
                    nextMove = lastMove.getChild(newLeaf);

                    valueMax = value;
                    action = field;
                }
            }


        }
        lastMove = nextMove;

        this.moves.add(lastMove);

        game.addMove(action, value);
        return action;
    }

}
