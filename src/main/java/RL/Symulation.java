package RL;

import Controllers.SymulationPanelController;
import IO.Serialize;
import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class Symulation extends Task<Void> {

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
    private Computer crossPlayer;
    private Computer circlePlayer;

    private Game game;

    public Symulation(int size, int full, double expRate, int rounds) {
        this.game = new Game(size, full);
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

        for (int i = 0; i < rounds; i++) {

            Verdict verdict = Verdict.NOBODY;
            while (verdict == Verdict.NOBODY) {
                crossPlayer.move(exp_rate);
                circlePlayer.move(exp_rate);
                verdict = game.getVerdict();
            }
            switch (verdict) {
                case CIRCLE:
                    cross++;
                    break;
                case CROSS:
                    circle++;
                    break;
                case DRAW:
                    draw++;
            }
            game.reset();
        }
        showStatistics();
    }

    public void train() {
        crossPlayer.setPolicy(new Policy(Sign.CROSS, rounds, expRate));
        circlePlayer.setPolicy(new Policy(Sign.CIRCLE, rounds, expRate));
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

        for (int i = moves.size() - 1; i > 0; i--) {
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

        Symulation symulation = new Symulation(3, 3, 0.3,100000);
        symulation.train();
        symulation.test(10, 0.0);

    }


    @Override
    protected Void call() throws Exception {
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

        return null;
    }

    @Override
    protected void failed() {
        System.out.println("Failed");
    }

    @Override
    protected void succeeded() {

        button.setDisable(false);
        if(autoSave.isSelected()) button.fire();

    }
}
