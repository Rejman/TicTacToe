package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import Tools.Logger;
import Tools.Stoper;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;


public class BaseSymulation extends Symulation {

    private CheckBox autoSave;
    public void setAutoSave(CheckBox autoSave) {
        this.autoSave = autoSave;
    }
    private Button button;
    public void setButton(Button button) {
        this.button = button;
    }

    public BaseSymulation(int size, int full, double expRate, int rounds) {
        this.stoper = new Stoper();
        this.game = new Game(size, full);
        crossPlayer = new Computer("firstPlayer", Sign.CROSS, this.game);
        circlePlayer = new Computer("secondPlayer", Sign.CIRCLE, this.game);
        this.expRate = expRate;
        this.rounds = rounds;

    }
    public BaseSymulation(Game game, double expRate, int rounds){
        this.stoper = new Stoper();
        this.game = game;
        crossPlayer = new Computer("firstPlayer", Sign.CROSS, this.game);
        circlePlayer = new Computer("secondPlayer", Sign.CIRCLE, this.game);
        this.expRate = expRate;
        this.rounds = rounds;
    }

    @Override
    protected Void call() throws Exception {
        stoper.start();

        crossPlayer.setPolicy(new Policy(Sign.CROSS, rounds, expRate,game.getSize(),game.getFull()));
        circlePlayer.setPolicy(new Policy(Sign.CIRCLE, rounds, expRate,game.getSize(),game.getFull()));
        Logger.add(game.getSize()+"x"+game.getFull()+" ");
        Logger.add(expRate+" ");
        Logger.add(rounds+" ");

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
        System.out.println("Base Symulation failed");
    }

    @Override
    protected void succeeded() {
        Logger.add((int)stoper.getSeconds()+"\n");
        System.out.println("Learning time: "+stoper.getTime()+" minutes");
        System.out.println("Learning time: "+stoper.getSeconds()+" seconds");
        button.setDisable(false);
        if(autoSave.isSelected()) button.fire();
    }


}
