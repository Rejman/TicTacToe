package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;

public class Symulation {

    private int cross = 0;
    private int circle = 0;
    private int draw = 0;

    private Game game;

    private int rounds = 0;

    public static void main(String[] args) {

        Game ticTacToe = new Game(3,3);
        Symulation symulation1 = new Symulation(ticTacToe);
        symulation1.play(400);
        symulation1.showStatistics();


    }

    public Symulation(Game game) {
        this.game = game;
    }

    public void play(int rounds){
        this.rounds = rounds;

        Computer player1 = new Computer("p1", Sign.CROSS, game);
        Computer player2 = new Computer("p2", Sign.CIRCLE, game);
        for(int i=0;i<rounds;i++){


            Verdict verdict = Verdict.NOBODY;
            while(verdict==Verdict.NOBODY){
                player1.randomMove();
                player2.randomMove();
                verdict = game.getVerdict();
            }
            if(verdict == Verdict.CROSS) cross++;
            if(verdict == Verdict.CIRCLE) circle++;
            if(verdict == Verdict.DRAW) draw++;

            game.reset();
        }



    }

    public void showStatistics(){

        System.out.println("In "+this.rounds+" games:");
        System.out.println("\tcirle won "+circle+" times\t("+(circle*100)/rounds+"%)");
        System.out.println("\tcross won "+cross+" times\t("+(cross*100)/rounds+"%)");
        System.out.println("\tdraw was "+draw+" times\t("+(draw*100)/rounds+"%)");
    }
}
