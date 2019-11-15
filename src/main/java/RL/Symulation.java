package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;

import java.util.ArrayList;

public class Symulation {

    private int cross = 0;
    private int circle = 0;
    private int draw = 0;

    //cross is always first
    private Computer crossPlayer;
    private Computer circlePlayer;

    private Game game;

    private int rounds = 0;

    public Symulation(int size, int full) {
        this.game = new Game(size, full);
        crossPlayer = new Computer("firstPlayer", Sign.CROSS, this.game);
        circlePlayer = new Computer("secondPlayer", Sign.CIRCLE, this.game);

    }

    public Policy getFirstPlayerPolicy() {
        return crossPlayer.getPolicy();
    }

    public Policy getSecondPlayerPolicy() {
        return circlePlayer.getPolicy();
    }

    private void resetStatistics() {
        rounds = 0;
        cross = 0;
        circle = 0;
        draw = 0;
    }

    public void play(int rounds, double exp_rate) {
        resetStatistics();
        this.rounds = rounds;

        for (int i = 0; i < rounds; i++) {


            Verdict verdict = Verdict.NOBODY;
            while (verdict == Verdict.NOBODY) {
                crossPlayer.move(exp_rate);
                circlePlayer.move(exp_rate);
                verdict = game.getVerdict();
            }
            if (verdict == Verdict.CROSS) cross++;
            if (verdict == Verdict.CIRCLE) circle++;
            if (verdict == Verdict.DRAW) draw++;

            game.reset();
        }
        showStatistics();
    }

    public void train(int rounds, double exp_rate) {
        crossPlayer.setPolicy(new Policy(Sign.CROSS, rounds, exp_rate));
        circlePlayer.setPolicy(new Policy(Sign.CIRCLE, rounds, exp_rate));
        resetStatistics();
        this.rounds = rounds;

        for (int i = 0; i < rounds; i++) {
            if (rounds % 10000 == 0) System.out.println((i * 100) / rounds + " %");
            Verdict verdict;
            while (true) {

                crossPlayer.move(exp_rate);
                crossPlayer.addState(game.getResultMatrix().getHash());

                verdict = game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }

                circlePlayer.move(exp_rate);
                circlePlayer.addState(game.getResultMatrix().getHash());

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
            crossPlayer.resetStates();
            circlePlayer.resetStates();
        }
        crossPlayer.getPolicy().getTree().showTree();
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

    public static void main(String[] args) {
        Symulation symulation = new Symulation(3, 3);
        symulation.train(10000, 0.3);
        symulation.play(10, 0.0);
        symulation.getFirstPlayerPolicy().getTree().showTree();
    }

    public void setReward(double reward, Computer computer) {

        double decayGamma = 0.9;
        double lr = 0.2;

        ArrayList<String> states = computer.getStates();
        //resetowanie drzewa do pozycji początkowej

        Leaf level = computer.getPolicy().getTree();
        ArrayList<Leaf> moves = computer.getMoves();
        for (int i = 0; i < moves.size(); i++) {
            Leaf leaf = moves.get(i);
            System.out.println("move nr"+(i+1)+"-> "+leaf);
            System.out.println("level -> "+level);
            if (level.getChild(leaf) == null) {
                leaf.setValue(0.0);

                level.addChild(leaf);
            }
            double value = lr * (decayGamma * reward - leaf.getValue());
            value += leaf.getValue();

            leaf.setValue(value);
            reward = value;

            level.addChild(leaf);
            level = level.getChild(leaf);
        }

    }

    public void showStatistics() {

        System.out.println("In " + this.rounds + " games:");
        System.out.println("\tcirle won " + circle + " times\t(" + (circle * 100) / rounds + "%)");
        System.out.println("\tcross won " + cross + " times\t(" + (cross * 100) / rounds + "%)");
        System.out.println("\tdraw was " + draw + " times\t(" + (draw * 100) / rounds + "%)");
    }



}
