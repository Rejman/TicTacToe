package RL.Policy;

import Models.Game.Sign;
import RL.Policy.Tree.Leaf;

import java.io.Serializable;

public class Policy implements Serializable {

    private int size;
    private int full;
    private Sign sign;
    private int rounds;
    private double expRate;
    private Leaf root;

    public Policy(Sign sign, int rounds, double expRate, int size, int full) {
        this.sign = sign;
        this.rounds = rounds;
        this.expRate = expRate;
        String rootValue = "";
        for(int i=0;i<(size*size);i++){
            rootValue+="-";
        }
        this.root = new Leaf(rootValue, 0.0);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
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
