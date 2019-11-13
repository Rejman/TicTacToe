package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;

import java.util.ArrayList;
import java.util.HashMap;

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
        return  crossPlayer.getPolicy();
    }
    public Policy getSecondPlayerPolicy() {
        return  circlePlayer.getPolicy();
    }
    private void resetStatistics() {
        rounds = 0;
        cross = 0;
        circle = 0;
        draw = 0;
    }
    public void play(int rounds, double exp_rate){
        resetStatistics();
        this.rounds = rounds;

        for(int i=0;i<rounds;i++){


            Verdict verdict = Verdict.NOBODY;
            while(verdict==Verdict.NOBODY){
                crossPlayer.move(exp_rate);
                circlePlayer.move(exp_rate);
                verdict = game.getVerdict();
            }
            if(verdict == Verdict.CROSS) cross++;
            if(verdict == Verdict.CIRCLE) circle++;
            if(verdict == Verdict.DRAW) draw++;

            game.reset();
        }
        showStatistics();
    }
    public void train(int rounds, double exp_rate) {
        crossPlayer.setPolicy(new Policy(Sign.CROSS,rounds,exp_rate));
        circlePlayer.setPolicy(new Policy(Sign.CIRCLE,rounds,exp_rate));
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
        Symulation symulation = new Symulation(3,3);
        symulation.train(10000,0.3);
        symulation.play(10,0.0);
    }
    public void setReward(double reward, Computer computer) {

        /*double decayGamma = 0.9;
        double lr = 0.2;

        ArrayList<String> states = computer.getStates();
        //resetowanie drzewa do pozycji początkowej

        HashMap<Leaf, Double> leaves = computer.getPolicy().getTree().getLeaves();

        for (int i = states.size() - 1; i >= 0; i--) {
            String state = states.get(i);
            Leaf leaf = new Leaf(state);

            //tu jest dodawanie losowych wyborów
            if (leaves.get(leaf) == null) {
                leaves.put(leaf, 0.0);

            }
            double value = lr * (decayGamma * reward - leaves.get(leaf));
            value += leaves.get(leaf);
            leaves.put(leaf, value);
            reward = value;
            computer.getPolicy().setCurrentLeaf(leaf);
            leaves = leaf.getLeaves();

        }
        //System.out.println(leaves);
        System.out.println(computer.getPolicy().getCurrentLeaf().getLeaves());*/
        //computer.getPolicy().getCurrentLeaf().setLeaves(leaves);
    }

    public void showStatistics() {

        System.out.println("In " + this.rounds + " games:");
        System.out.println("\tcirle won " + circle + " times\t(" + (circle * 100) / rounds + "%)");
        System.out.println("\tcross won " + cross + " times\t(" + (cross * 100) / rounds + "%)");
        System.out.println("\tdraw was " + draw + " times\t(" + (draw * 100) / rounds + "%)");
    }

}
