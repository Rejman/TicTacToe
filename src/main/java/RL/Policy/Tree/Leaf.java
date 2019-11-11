package RL.Policy.Tree;

import java.util.ArrayList;

public class Leaf {
    String state;
    ArrayList<Leaf> leaves;
    Double value;

    public Leaf(String state, Double value) {
        this.state = state;
        this.value = value;
        this.leaves = new ArrayList<Leaf>();
    }

    public boolean isHasChildren(){
        return leaves.isEmpty();
    }
    public void addChild(Leaf leaf){
        leaves.add(leaf);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Leaf> getLeaves() {
        return leaves;
    }

    public void setLeaves(ArrayList<Leaf> leaves) {
        this.leaves = leaves;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
