package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import RL.Policy.Symulation;
import Tools.Stoper;


public class DynamicSymulation extends Symulation {

    private Sign firstMove;

    public DynamicSymulation(Game game, double expRate, int rounds, Sign firstMove) {
        this.stoper = new Stoper();
        this.game = game;
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

    public Policy getNewPolicy() {
        if(this.firstMove==Sign.CROSS) return this.getFirstPlayerPolicy();
        else if(this.firstMove==Sign.CIRCLE) return this.getSecondPlayerPolicy();
        else return null;

    }

    @Override
    protected Void call() throws Exception {
        stoper.start();

        for (int i = 0; i < rounds; i++) {
            System.out.println(i);
            Verdict verdict;
            while (true) {

                if(this.firstMove==Sign.CIRCLE) circlePlayer.move(expRate);
                else crossPlayer.move(expRate);

                verdict = this.game.getVerdict();

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
        return null;
    }

    @Override
    protected void failed() {
        System.out.println("Dynamic learning failed");
    }
    @Override
    protected void succeeded() {
        System.out.println("Learning time: "+stoper.getTime()+" minutes");

    }
}
