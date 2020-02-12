package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Player.Computer;
import Models.Player.Player;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;

import java.util.ArrayList;

public abstract class DynamicLearning {
    public static void train(Computer player, Game oldGame){
        System.out.println("dynamic learning");
        //backup
        /*Leaf oldLastMove = player.getLastMove();
        Leaf oldNextMove = player.getNextMove();
        ArrayList<Leaf> oldMoves = player.getMoves();*/
        //
        Game newGame = new Game(oldGame.getSize(),oldGame.getFull());
        newGame.setResultMatrix(oldGame.getResultMatrix().clone());

        //Symulation symulation = new Symulation(3,3,0.3,10);
        Symulation symulation = new Symulation(newGame, 0.3,100);
        symulation.dynamicTrain(player.getPolicy());
        System.out.println(player.getName()+" "+player.getLastMove().toString());
        //symulation.train();
        //symulation.dynamicTrain(player.getPolicy());
    }
}
