package RL.Policy.Tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class Leaf {
    private String state;
    private HashMap<Leaf,Double> leaves;

    public Leaf(String state) {
        this.state = state;
        this.leaves = new HashMap<Leaf,Double>();
    }

    public HashMap<Leaf, Double> getLeaves() {
        return leaves;
    }

    public void setLeaves(HashMap<Leaf, Double> leaves) {
        this.leaves = leaves;
    }

    public boolean isHasChildren(){
        return leaves.isEmpty();
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaf leaf = (Leaf) o;
        return state.equals(leaf.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return state;
    }

    public static void main(String[] args) {
        System.out.println("tree test");

        Leaf root = new Leaf("---X-----");

        System.out.println(root);

        HashMap<Leaf,Double> children = root.getLeaves();
        children.put(new Leaf("O--X-----"), 0.3);
        children.put(new Leaf("-O-X-----"), 0.3);
        children.put(new Leaf("--OX-----"), 0.3);
        children.put(new Leaf("---XO----"), 0.3);
        children.put(new Leaf("---X-O---"), 0.3);
        children.put(new Leaf("---X--O--"), 0.3);
        children.put(new Leaf("test"), 0.4);


        System.out.println(children);

        System.out.println(children.get(new Leaf("test")));


    }
}
