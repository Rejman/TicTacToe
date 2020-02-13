package RL;

import Controllers.SymulationPanelController;
import IO.Serialize;
import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import RL.Policy.State;
import RL.Policy.Tree.Leaf;
import Tools.Stoper;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class Symulation extends Task<Void> {

    private Stoper stoper;
    private CheckBox autoSave;

    public void setAutoSave(CheckBox autoSave) {
        this.autoSave = autoSave;
    }

    private Button button;

    public void setButton(Button button) {
        this.button = button;
    }

    private double DECAYGAMMA = 0.9;
    private double LR = 0.2;

    private double expRate;
    private int rounds;

    private int cross = 0;
    private int circle = 0;
    private int draw = 0;

    //cross is always first
    public Computer crossPlayer;
    public Computer circlePlayer;

    private Game game;

    public Symulation(int size, int full, double expRate, int rounds) {
        this.stoper = new Stoper();
        this.game = new Game(size, full);
        crossPlayer = new Computer("firstPlayer", Sign.CROSS, this.game);
        circlePlayer = new Computer("secondPlayer", Sign.CIRCLE, this.game);
        this.expRate = expRate;
        this.rounds = rounds;

    }
    public Symulation(Game game, double expRate, int rounds){
        this.stoper = new Stoper();
        this.game = game;
        crossPlayer = new Computer("firstPlayer", Sign.CROSS, this.game);
        circlePlayer = new Computer("secondPlayer", Sign.CIRCLE, this.game);
        this.expRate = expRate;
        this.rounds = rounds;
    }

    public Policy getFirstPlayerPolicy() {
        return crossPlayer.getPolicy();
    }

    public Policy getSecondPlayerPolicy() {
        return circlePlayer.getPolicy();
    }

    private void resetStatistics() {
        cross = 0;
        circle = 0;
        draw = 0;
    }

    public void test(int rounds, double exp_rate) {
        resetStatistics();
        this.rounds = rounds;
        Verdict verdict;
        for (int i = 0; i < rounds; i++) {

            while (true) {

                crossPlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    //giveReward(verdict);
                    break;
                }

                circlePlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    //giveReward(verdict);
                    break;
                }
            }
            if (verdict == Verdict.CROSS) cross++;
            if (verdict == Verdict.CIRCLE) circle++;
            if (verdict == Verdict.DRAW) draw++;

            game.reset();
            crossPlayer.resetMoves();
            circlePlayer.resetMoves();
            
        }
        showStatistics();
    }
    public void train(){
        this.train("");
    }
    public void dynamicTrain(Policy policy) {
        stoper.start();
        //this.game.addMove(4,Sign.CIRCLE);
        /*crossPlayer.setPolicy(new Policy(Sign.CROSS, rounds, expRate));
        circlePlayer.setPolicy(new Policy(Sign.CIRCLE, rounds, expRate));*/
        switch (policy.getSign()){
            case CROSS:
                crossPlayer.setPolicy(policy);
                circlePlayer.setPolicy(new Policy(Sign.CIRCLE, policy.getRounds(), policy.getExpRate()));
                break;
            case CIRCLE:
                circlePlayer.setPolicy(policy);
                crossPlayer.setPolicy(new Policy(Sign.CROSS, policy.getRounds(), policy.getExpRate()));
        }

        resetStatistics();


        for (int i = 0; i < rounds; i++) {
            Verdict verdict;
            while (true) {

                crossPlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }

                circlePlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }
            }
            if (verdict == Verdict.CROSS) cross++;
            if (verdict == Verdict.CIRCLE) circle++;
            if (verdict == Verdict.DRAW) draw++;

            game.reset();
            crossPlayer.resetMoves();
            circlePlayer.resetMoves();
        }
        stoper.stop();
        System.out.println(stoper.getMinutes()+" minutes");
    }
    public void train(String baseFileName) {
        stoper.start();
        //this.game.addMove(4,Sign.CIRCLE);
        if(baseFileName.equals("")){
            crossPlayer.setPolicy(new Policy(Sign.CROSS, rounds, expRate));
            circlePlayer.setPolicy(new Policy(Sign.CIRCLE, rounds, expRate));
        }else{
            crossPlayer.setPolicy(Serialize.loadPolicy(Serialize.pathToFile(baseFileName,Sign.CROSS)));
            circlePlayer.setPolicy(Serialize.loadPolicy(Serialize.pathToFile(baseFileName,Sign.CIRCLE)));
        }

        resetStatistics();


        for (int i = 0; i < rounds; i++) {
            Verdict verdict;
            while (true) {

                crossPlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }

                circlePlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }
            }
            if (verdict == Verdict.CROSS) cross++;
            if (verdict == Verdict.CIRCLE) circle++;
            if (verdict == Verdict.DRAW) draw++;

            game.reset();
            crossPlayer.resetMoves();
            circlePlayer.resetMoves();
        }
        stoper.stop();
        System.out.println(stoper.getMinutes()+" minutes");
    }

    public void giveReward(Verdict verdict) {

        switch (verdict) {
            case CROSS:
                setReward(1, crossPlayer);
                setReward(0, circlePlayer);
                break;
            case CIRCLE:
                setReward(0, crossPlayer);
                setReward(1, circlePlayer);
                break;
            default:
                setReward(0.1, crossPlayer);
                setReward(0.5, circlePlayer);
                break;
        }

    }

    public void setReward(double reward, Computer computer) {

        ArrayList<Leaf> moves = computer.getMoves();

        for (int i = moves.size() -1; i > 0; i--) {
            Leaf move = moves.get(i);
            Leaf parent = moves.get(i - 1);

            double newValue = LR * (DECAYGAMMA * reward - move.getValue()) + move.getValue();

            move.setValue(newValue);
            reward = newValue;

            parent.addChild(move);
        }
    }

    public void showStatistics() {

        System.out.println("In " + this.rounds + " games:");
        System.out.println("\tcirle won " + circle + " times\t(" + (circle * 100) / rounds + "%)");
        System.out.println("\tcross won " + cross + " times\t(" + (cross * 100) / rounds + "%)");
        System.out.println("\tdraw was " + draw + " times\t(" + (draw * 100) / rounds + "%)");
    }

    public static void main(String[] args) {
        System.out.println("Symulation tests");
        Symulation symulation = new Symulation(3, 3, 0.3,10);
        symulation.train();
        symulation.test(10, 0.0);

    }


    @Override
    protected Void call() throws Exception {
        stoper.start();
        crossPlayer.setPolicy(new Policy(Sign.CROSS, rounds, expRate));
        circlePlayer.setPolicy(new Policy(Sign.CIRCLE, rounds, expRate));
        System.out.println(rounds);


        for (int i = 0; i < rounds; i++) {
            Verdict verdict;
            while (true) {
                crossPlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }

                circlePlayer.move(expRate);

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }
            }
            if (verdict == Verdict.CROSS) cross++;
            if (verdict == Verdict.CIRCLE) circle++;
            if (verdict == Verdict.DRAW) draw++;

            game.reset();
            crossPlayer.resetMoves();
            circlePlayer.resetMoves();
            updateProgress(i,rounds);
        }
        stoper.stop();
        return null;
    }

    @Override
    protected void failed() {
        System.out.println("Failed");
    }

    @Override
    protected void succeeded() {
        System.out.println("Learning time: "+stoper.getMinutes()+" minutes");
        button.setDisable(false);
        if(autoSave.isSelected()) button.fire();

    }


}
