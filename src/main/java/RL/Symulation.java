package RL;

import Models.Game.Game;
import Models.Game.Verdict;
import Models.Player.Computer;
import RL.Policy.Policy;
import RL.Policy.Tree.Leaf;
import Tools.Stoper;
import javafx.concurrent.Task;

import java.util.ArrayList;

public abstract class Symulation extends Task<Void> {
    protected Stoper stoper;
    protected double DECAYGAMMA = 0.9;
    protected double LR = 0.2;
    protected double expRate;
    protected int rounds;

    //cross is always first
    public Computer crossPlayer;
    public Computer circlePlayer;
    protected Game game;
    @Override
    protected Void call() throws Exception {
        return null;
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
    public Policy getFirstPlayerPolicy() {
        return crossPlayer.getPolicy();
    }

    public Policy getSecondPlayerPolicy() {
        return circlePlayer.getPolicy();
    }



}
