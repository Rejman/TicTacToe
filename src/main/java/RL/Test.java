package RL;

import Models.Game.Game;
import Models.Game.ResultMatrix;
import Models.Game.Sign;

public class Test {

    public static void startMoves(Game game){
        ResultMatrix resultMatrix = new ResultMatrix(3);
        resultMatrix.add(3, Sign.CIRCLE);
        game.setGameStatus(resultMatrix);
        //System.out.println(game.getResultMatrix().getHash());
    }

}
