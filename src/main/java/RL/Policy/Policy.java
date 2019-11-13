package RL.Policy;

import Models.Game.Sign;
import RL.Policy.Tree.Leaf;

import java.io.Serializable;
import java.util.HashMap;

public class Policy implements Serializable {

    private Sign sign;
    private int rounds;
    private double expRate;
    private Leaf tree;

    private Leaf currentLeaf;

    public Leaf getCurrentLeaf() {
        return currentLeaf;
    }

    public void setCurrentLeaf(Leaf currentLeaf) {
        this.currentLeaf = currentLeaf;
    }

    public Policy(Sign sign, int rounds, double expRate) {
        this.sign = sign;
        this.rounds = rounds;
        this.expRate = expRate;
        this.tree = new Leaf("root", 0.0);
        this.currentLeaf = tree;
    }

    public Leaf getTree() {
        return tree;
    }

    public void setTree(Leaf tree) {
        this.tree = tree;
        this.currentLeaf = this.tree;
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
