package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import OI.Serialize;

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

    public static void main(String[] args) {

        Symulation symulation1 = new Symulation(3,3);

        symulation1.train(500000,0.3);
        //symulation1.resetStatistics();
        symulation1.play(200,0);
        //symulation1.play(200,0);
        Serialize.savePolicy("FirstPlayerCross",symulation1.getFirstPlayerPolicy());


    }
    public Symulation(int size, int full) {
        this.game = new Game(size, full);
        crossPlayer = new Computer("firstPlayer", Sign.CROSS, this.game);
        circlePlayer = new Computer("secondPlayer", Sign.CIRCLE, this.game);
    }
    public HashMap<String, Double> getFirstPlayerPolicy() {
        return (HashMap<String, Double>) crossPlayer.getPolicy().clone();
    }

    public HashMap<String, Double> getSecondPlayerPolicy() {
        return (HashMap<String, Double>) circlePlayer.getPolicy().clone();
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
    private void resetStatistics(){
        rounds = 0;
        cross = 0;
        circle = 0;
        draw = 0;
    }
    public void train(int rounds, double exp_rate){
        resetStatistics();
        this.rounds = rounds;
        for(int i=0;i<rounds;i++){
            if(rounds%10000==0) System.out.println((i*100)/rounds+" %");
            Verdict verdict;
            while(true){

                crossPlayer.move(exp_rate);
                crossPlayer.addState(game.getResultMatrix().getHash());

                verdict = game.getVerdict();
                if(verdict!=Verdict.NOBODY){
                    giveReward(verdict);
                    break;
                }

                circlePlayer.move(exp_rate);
                circlePlayer.addState(game.getResultMatrix().getHash());

                verdict = game.getVerdict();
                if(verdict!=Verdict.NOBODY){
                    giveReward(verdict);
                    break;
                }
            }
            if(verdict == Verdict.CROSS) cross++;
            if(verdict == Verdict.CIRCLE) circle++;
            if(verdict == Verdict.DRAW) draw++;

            game.reset();
            crossPlayer.resetStates();
            circlePlayer.resetStates();
        }

    }
    public void giveReward(Verdict verdict){

        switch (verdict){
            case CROSS:
                setReward(1,crossPlayer);
                setReward(0,circlePlayer);
                break;
            case CIRCLE:
                setReward(0,crossPlayer);
                setReward(1,circlePlayer);
                break;
            default:
                setReward(0.1,crossPlayer);
                setReward(0.5,circlePlayer);
                break;
        }

    }
    public void setReward(double reward, Computer computer) {
        double decayGamma = 0.9;
        double lr = 0.2;
        ArrayList<String> states = computer.getStates();
        HashMap<String,Double> policy = computer.getPolicy();
        for (int i = states.size() - 1; i >= 0; i--) {
            String state = states.get(i);
            if (policy.get(state) == null) {
                policy.put(state, 0.0);
            }
            double value =lr*(decayGamma*reward - policy.get(state));
            value += policy.get(state);
            policy.put(state, value);
            reward = value;
        }
    }
    public void showStatistics(){

        System.out.println("In "+this.rounds+" games:");
        System.out.println("\tcirle won "+circle+" times\t("+(circle*100)/rounds+"%)");
        System.out.println("\tcross won "+cross+" times\t("+(cross*100)/rounds+"%)");
        System.out.println("\tdraw was "+draw+" times\t("+(draw*100)/rounds+"%)");
    }

}
