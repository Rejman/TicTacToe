package RL;

import Models.Game.Game;
import Models.Game.Sign;
import RL.Policy.Policy;

public abstract class DynamicLearning {

    public static Policy train(Game oldGame,double expRate, int rounds, Sign sign){
        System.out.println("dynamic learning");
        Game newGame = new Game(oldGame.getSize(),oldGame.getFull());
        newGame.setGameStatus(oldGame.getResultMatrix());

        BaseSymulation baseSymulation = new BaseSymulation(newGame, expRate, rounds);
        if(sign==Sign.CIRCLE){
            //symulation.train(Sign.CIRCLE);
        }else{
            //symulation.train(Sign.CROSS);
        }


        if(sign==Sign.CROSS) return baseSymulation.getFirstPlayerPolicy();
        else if(sign==Sign.CIRCLE) return baseSymulation.getSecondPlayerPolicy();
        else return null;


    }
}
