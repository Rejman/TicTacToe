package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import Models.Player.Player;

import java.util.HashMap;

public class Symulation {

    private int cross = 0;
    private int circle = 0;
    private int draw = 0;

    private Computer playerOne;
    private Computer playerTwo;

    public HashMap<String, Double> firstPlayerPolicy = null;
    public HashMap<String, Double> secondPlayerPolicy = null;

    private Game game;

    private int rounds = 0;

    public static void main(String[] args) {

        Game ticTacToe = new Game(3,3);
        Symulation symulation1 = new Symulation(ticTacToe);
        symulation1.train(50000);
        symulation1.showStatistics();
        HashMap<String, Double> firstPlayer = symulation1.firstPlayerPolicy;

        Computer randomPlayer = new Computer("Random", Sign.CIRCLE, ticTacToe);
        Computer smartPlayer = new Computer("Random", Sign.CROSS, ticTacToe);
        smartPlayer.setPolicy(firstPlayer);
        Symulation symulation2 = new Symulation(ticTacToe, smartPlayer, randomPlayer);
        symulation2.play(100);
        symulation2.showStatistics();


    }

    public Symulation(Game game) {
        this.game = game;
        playerOne = new Computer("cross player", Sign.CROSS, this.game);
        playerTwo = new Computer("circle player", Sign.CIRCLE, this.game);
    }
    public Symulation(Game game, Computer playerOne, Computer playerTwo) {
        this.game = game;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.playerOne.setGame(this.game);
        this.playerTwo.setGame(this.game);

    }


    public void play(int rounds){
        this.rounds = rounds;

        for(int i=0;i<rounds;i++){


            Verdict verdict = Verdict.NOBODY;
            while(verdict==Verdict.NOBODY){
                playerOne.move(1);
                playerTwo.move(1);
                verdict = game.getVerdict();
            }
            if(verdict == Verdict.CROSS) cross++;
            if(verdict == Verdict.CIRCLE) circle++;
            if(verdict == Verdict.DRAW) draw++;

            game.reset();
        }
    }

    public void train(int rounds){
        this.rounds = rounds;
        firstPlayerPolicy = new HashMap<String, Double>();
        secondPlayerPolicy = new HashMap<String, Double>();

        for(int i=0;i<rounds;i++){


            Verdict verdict;
            while(true){

                playerOne.move(0.3);
                playerOne.addState(game.getResultMatrix().getHash());

                verdict = game.getVerdict();
                if(verdict!=Verdict.NOBODY){
                    giveReward(verdict);
                    break;
                }

                playerTwo.move(0.3);
                playerTwo.addState(game.getResultMatrix().getHash());

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
            playerOne.resetStates();
            playerTwo.resetStates();
        }
        firstPlayerPolicy = playerOne.getPolicy();
        secondPlayerPolicy = playerTwo.getPolicy();

    }
    public void giveReward(Verdict verdict){

        switch (verdict){
            case CROSS:
                this.playerOne.setReward(1);
                this.playerTwo.setReward(0);
                break;
            case CIRCLE:
                this.playerOne.setReward(0);
                this.playerTwo.setReward(1);
                break;
            default:
                this.playerOne.setReward(0.1);
                this.playerTwo.setReward(0.5);
                break;
        }

    }
    public void showStatistics(){

        System.out.println("In "+this.rounds+" games:");
        System.out.println("\tcirle won "+circle+" times\t("+(circle*100)/rounds+"%)");
        System.out.println("\tcross won "+cross+" times\t("+(cross*100)/rounds+"%)");
        System.out.println("\tdraw was "+draw+" times\t("+(draw*100)/rounds+"%)");
    }
}
