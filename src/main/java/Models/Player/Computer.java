package Models.Player;

import Controllers.DynamicLearningController;
import Models.Game.*;
import Models.Gui.HumanVsComputer;
import RL.DynamicSymulation;
import RL.Policy.Policy;
import RL.Policy.State;
import RL.Policy.Tree.Leaf;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;

public class Computer extends Player {
    //object that chooses random movement
    public HumanVsComputer game_board = null;
    private Random generator = new Random();
    private Policy policy;
    private Leaf lastMove;
    private Leaf nextMove;
    private ArrayList<Leaf> moves;

    public static boolean dynamicLearning = true;

    private ProgressBar progressBar;

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

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * @param name
     * @param value
     * @param game
     */
    public Computer(String name, Sign value, Game game) {
        super(name, value, game);

        policy = new Policy(this.value,0,0, game.getSize(), game.getFull());
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
        this.policy.setSize(game.getSize());
        this.policy.setFull(game.getFull());

        resetMoves();
    }

    public Policy getPolicy() {
        return this.policy;
    }
    private int randomMove(ArrayList<Integer> emptyFields){

        if(emptyFields.size()<=0) emptyFields = game.getEmptyFields();

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
        ArrayList<Integer> selectedFields;
        if(trueGame){
            selectedFields = this.selectMovements(true, true);
        }else{
            selectedFields = this.selectMovements(true, false);
        }


        int action = 0;

        Random generator = new Random();
        if(generator.nextDouble()<=exp_rate){
            //if move is random
            action =  randomMove(selectedFields);

        }else{
            //if move is form policy
            double valueMax = Integer.MIN_VALUE;

            for (Integer field:selectedFields
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

        //gdy ruch ma wartość 0.0 (czyli gdy go nie rozpoznano w polityce)
        if(value==0.0) {

            //System.out.println("NIEZNANY");

            if(trueGame && selectedFields.size()>0 && dynamicLearning){

                //DynamicLearningTask dynamicLearningTask = new DynamicLearningTask(game,0.3,10000,this.value, progressBar);
                DynamicSymulation symulation = new DynamicSymulation(game, this.value);

                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/DynamicLearning.fxml"));
                StackPane stackPane = null;
                try {
                    stackPane = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DynamicLearningController dynamicLearningController = loader.getController();
                dynamicLearningController.setSymulation(symulation);
                Scene scene = new Scene(stackPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Dynamic Learning");
                stage.setResizable(false);
                dynamicLearningController.setParent(stage);
                //stage.setAlwaysOnTop(true);
                stage.initStyle(StageStyle.UNDECORATED);

                try {
                    dynamicLearningController.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stage.show();
                Thread thread2 = new Thread(() ->{
                    while(dynamicLearningController.thread.isAlive()){

                    }
                    System.out.println("KONIEC!!!!!!!");
                    ArrayList<Leaf> newChildren = symulation.getNewPolicy().getTree().getChildren();
                    this.lastMove.setChildren(newChildren);
                    this.game_board.computerMove();
                    dynamicLearningController.thread.stop();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            dynamicLearningController.getParent().close();
                        }
                    });



                });
                thread2.setDaemon(true);
                thread2.start();

                return -10;
            }
            action = randomMove(selectedFields);
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

        if(trueGame == true && game_board != null){
            game_board.unlock();
        }
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
    private ArrayList<Integer> selectMovements(boolean on, boolean showInfo){
        ArrayList<Integer> selected = new ArrayList<>();
        ResultMatrix actualResultMatrix = game.getResultMatrix();
        if(on == false){
            return game.getEmptyFields();
        }
        if(showInfo)    System.out.println("HEADERS     "+"COLUMN"+"\t"+"ROW "+"\t"+"FDIAG"+"\t"+"GDIAG");
        for (Integer field:game.getEmptyFields()
             ) {
            boolean okRow = false;
            Sign[] row = actualResultMatrix.findRow(field);
            okRow = canSbWin(row);

            boolean okColumn = false;
            Sign[] column = actualResultMatrix.findColumn(field);
            okColumn = canSbWin(column);

            boolean okFDiag = false;
            List fallingDiagonal = actualResultMatrix.findFallingDiagonal(field);
            if(fallingDiagonal.size()>=game.getFull()){
                okFDiag = canSbWin(fallingDiagonal);
            }

            boolean okGDiag = false;
            List growingDiagonal = actualResultMatrix.findGrowingDiagonal(field);
            if(growingDiagonal.size()>=game.getFull()){
                okGDiag = canSbWin(growingDiagonal);
            }
            if(okColumn || okRow || okFDiag || okGDiag) selected.add(field);
            if(showInfo)    System.out.println("Field: "+field+" -> "+okColumn+"\t"+okRow+"\t"+okFDiag+"\t"+okGDiag);

        }

        return selected;
    }
    private boolean canSbWin(Sign[] line){


        int x = 0;
        int o = 0;
        for (Sign sign:line
             ) {
            switch (sign){
                case CIRCLE:
                    x++;
                    break;
                case CROSS:
                    o++;
                    break;
            }
        }
        return !(x>0 && o>0);
    }
    private boolean canSbWin(List line){
        int x = 0;
        int o = 0;
        for (Object elem:line
        ) {
            Sign sign = (Sign) elem;
            switch (sign){
                case CIRCLE:
                    x++;
                    break;
                case CROSS:
                    o++;
                    break;
            }
        }
        return !(x>0 && o>0);
    }



}
