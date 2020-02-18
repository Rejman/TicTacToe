package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Player.Computer;
import Models.Player.Player;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;

import java.util.ArrayList;

public abstract class DynamicLearning {

    public static Policy train(Game oldGame,double expRate, int rounds, Sign sign){
        System.out.println("dynamic learning");
        Game newGame = new Game(oldGame.getSize(),oldGame.getFull());
        newGame.setGameStatus(oldGame.getResultMatrix());

        Symulation symulation = new Symulation(newGame, expRate, rounds);

        symulation.train();

        if(sign==Sign.CROSS) return symulation.getFirstPlayerPolicy();
        else if(sign==Sign.CIRCLE) return symulation.getSecondPlayerPolicy();
        else return null;

    }
}
