package Models.Player;

import Models.Game.*;
import RL.DynamicLearning;
import RL.Policy.Policy;
import RL.Policy.State;
import RL.Policy.Tree.Leaf;
import RL.Symulation;

import java.util.*;

public class Computer extends Player {
    //object that chooses random movement
    private Random generator = new Random();
    private Policy policy;
    private Leaf lastMove;
    private Leaf nextMove;
    private ArrayList<Leaf> moves;

    public Leaf getLastMove() {
        return lastMove;
    }

    public void setLastMove(Leaf lastMove) {
        this.lastMove = lastMove;
    }

    public Leaf getNextMove() {
        return nextMove;
    }

    public void setNextMove(Leaf nextMove) {
        this.nextMove = nextMove;
    }

    public void setMoves(ArrayList<Leaf> moves) {
        this.moves = moves;
    }

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
        moves.add(0,policy.getTree());
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
        return move(exp_rate, false);
    }
    public int move(double exp_rate, boolean trueGame){
/*        if(trueGame){
            Policy newPolicy = DynamicLearning.train(game,0.3,10000,value);
            System.out.println("POLITYKA: "+policy.getTree().getChildren());
            this.setPolicy(newPolicy);

        }*/


        double value =0.0;
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

                value = 0.0;
                Leaf newLeaf = lastMove.getChild(new Leaf(nextResultMatrixHash));
                if(newLeaf != null){
                    value = newLeaf.getValue();
                }else{
                    continue;
                    //lastMove.addChild(new Leaf(nextResultMatrixHash, value));
                }
                //!!!
                if(value>=valueMax){
                    //!
                    newLeaf = new Leaf(nextResultMatrixHash);
                    nextMove = lastMove.getChild(newLeaf);
                    valueMax = value;
                    action = field;
                    //System.out.println("\t\t\t" + game.getNumberOfFields());
                }
            }


        }
        //if(trueGame) DynamicLearning.train(this,game);

        //gdy ruch ma wartość 0.0 (czyli gdy go nie rozpoznano w polityce)
        if(value==0.0) {

/*            System.out.print("NIE ROZPOZNANO: "+nextMove+" wylosowano: ");

            action = randomMove(emptyFields);
            System.out.println(action);*/
            System.out.println("NIEZNANY");
            if(trueGame){
                Policy newPolicy = DynamicLearning.train(game,0.3,10000,this.value);
                System.out.println("POLITYKA: "+newPolicy.getTree().getChildren());
                this.setPolicy(newPolicy);
                action = this.move(exp_rate);
            }else{
                action = randomMove(emptyFields);
            }

        }

/*        if(moves.size()==1){
            //pierwszy ruch
            System.out.println("pierwszu");
            Set<String> alters = State.allternatveState(nextMove.getState());
            int index = new Random().nextInt(alters.size());

            Iterator<String> iter = alters.iterator();
            for (int i = 0; i < index; i++) {
                iter.next();
            }

            nextMove.setState(iter.next());

            String oppositeSign;
            if(this.value == Sign.CROSS) oppositeSign="X";
            else oppositeSign="O";

            action = nextMove.getState().indexOf(oppositeSign);
        }*/

        if(trueGame) showMoves();
        lastMove = nextMove;
        this.moves.add(lastMove);

        game.addMove(action, this.value);



        return action;
    }

    void showMoves(){
        System.out.println("Ruchy: "+lastMove.getChildren().size());
        for(int i=0;i<lastMove.getChildren().size();i++){
            Leaf l = lastMove.getChildren().get(i);
            Set<String> states = State.allternatveState(l.getState());
            System.out.println("Ruch "+(i+1)+"________________________________"+l.getValue());
            System.out.println(State.showAsBoards(states));;
        }
    }

}
