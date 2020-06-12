package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import Tools.Stoper;
import javafx.concurrent.Task;

import java.util.ArrayList;

public class DynamicSymulation extends Task<Void> {

    private Stoper stoper;

    private double DECAYGAMMA = 0.9;
    private double LR = 0.2;

    private double expRate;
    private int rounds;

    private int cross = 0;
    private int circle = 0;
    private int draw = 0;

    private Sign firstMove;

    //cross is always first
    public Computer crossPlayer;
    public Computer circlePlayer;

    private Game game;

    public DynamicSymulation(Game game, double expRate, int rounds, Sign firstMove) {
        this.stoper = new Stoper();
        this.game = game;
        crossPlayer = null;
        circlePlayer = null;
        this.expRate = expRate;
        this.rounds = rounds;
        this.firstMove = firstMove;

        Game newGame = new Game(this.game.getSize(),this.game.getFull());
        newGame.setGameStatus(this.game.getResultMatrix());
        this.game = newGame;

        this.crossPlayer = new Computer("dynfirstPlayer", Sign.CROSS, this.game);
        this.circlePlayer = new Computer("dynsecondPlayer", Sign.CIRCLE, this.game);
        this.crossPlayer.setPolicy(new Policy(Sign.CROSS, rounds, expRate,this.game.getSize(),this.game.getFull()));
        this.circlePlayer.setPolicy(new Policy(Sign.CIRCLE, rounds, expRate,this.game.getSize(),this.game.getFull()));
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
    public Policy getFirstPlayerPolicy() {
        return crossPlayer.getPolicy();
    }

    public Policy getSecondPlayerPolicy() {
        return circlePlayer.getPolicy();
    }

    public Policy getNewPolicy() {
        if(this.firstMove==Sign.CROSS) return this.getFirstPlayerPolicy();
        else if(this.firstMove==Sign.CIRCLE) return this.getSecondPlayerPolicy();
        else return null;

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
    @Override
    protected Void call() throws Exception {

        System.out.println("dynamic learning");
        System.out.println("start");
        stoper.start();


        System.out.println(rounds);

        //Test.startMoves(game);

        for (int i = 0; i < rounds; i++) {
            System.out.println(i);
            Verdict verdict;
            while (true) {

                if(this.firstMove==Sign.CIRCLE) circlePlayer.move(expRate);
                else crossPlayer.move(expRate);

                verdict = this.game.getVerdict();
                //System.out.println(verdict);
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }
                if(this.firstMove==Sign.CIRCLE) this.crossPlayer.move(expRate);
                else this.circlePlayer.move(expRate);


                verdict = this.game.getVerdict();
                if (verdict != Verdict.NOBODY) {
                    giveReward(verdict);
                    break;
                }
            }


            this.game.reset();
            this.crossPlayer.resetMoves();
            this.circlePlayer.resetMoves();
            updateProgress(i,rounds);
        }
        stoper.stop();
        //System.out.println(stoper.getTime()+" minutes");
        return null;
    }

    @Override
    protected void failed() {
        System.out.println("Failed");
    }

    @Override
    protected void succeeded() {
        System.out.println("Learning time: "+stoper.getTime()+" minutes");


    }
}
