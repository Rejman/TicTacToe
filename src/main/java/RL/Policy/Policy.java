package RL.Policy;

import Models.Game.Sign;
import RL.Policy.Tree.Leaf;

import java.io.Serializable;

public class Policy implements Serializable {

    private Sign sign;
    private int rounds;
    private double expRate;
    private Leaf root;

    public Policy(Sign sign, int rounds, double expRate) {
        this.sign = sign;
        this.rounds = rounds;
        this.expRate = expRate;

        this.root = new Leaf("-------------------------------------", 0.0);
    }

    public Leaf getTree() {
        return root;
    }

    public void setTree(Leaf root) {
        this.root = root;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public double getExpRate() {
        return expRate;
    }

    public void setExpRate(double expRate) {
        this.expRate = expRate;
    }


}
