package RL.Policy.Tree;

import Models.Game.Sign;
import RL.Policy.Policy;

import java.io.Serializable;
import java.util.*;

public class Leaf implements Serializable {
    private String state;
    private int level;
    private HashMap<Leaf,Double> leaves;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Leaf(String state) {
        this.state = state;
        this.leaves = new HashMap<Leaf,Double>();
        //test
        //leaves.put(new Leaf("d"),0.0);
    }
    public Leaf(String state, int level) {
        this.state = state;
        this.leaves = new HashMap<Leaf,Double>();
        this.level = level;
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
    public String toBoardString(){
        String baord = "";
        baord+=state.substring(0,3)+"\n";
        baord+=state.substring(3,6)+"\n";
        baord+=state.substring(6,9)+"\n";
        baord+="------------------------";

        //System.out.println("\t"+leaves);
        return baord;
    }
    public void showTree(){
        //System.out.println();
        for(int i=0;i<level;i++) System.out.print("\t");
        //System.out.println(level+" "+state+" -> ("+leaves.size()+")"+leaves);
        System.out.println("("+level+")"+state);
        Set keys = this.leaves.keySet();

        if(keys.size()>0){

            for (Object key:keys
            ) {
                Leaf leaf = (Leaf) key;
                leaf.showTree();

            }
        }

    }
    public static void main(String[] args) {
        System.out.println("tree test");

        Leaf root = new Leaf("---------", 0);

        //System.out.println(root);

        HashMap<Leaf,Double> children = root.getLeaves();
        children.put(new Leaf("O--X-----",1), 0.3);
        children.put(new Leaf("-O-X-----",1), 0.3);
        children.put(new Leaf("--OX-----",1), 0.3);
        children.put(new Leaf("---XO----",1), 0.3);
        children.put(new Leaf("---X-O---",1), 0.3);
        children.put(new Leaf("---X--O--",1), 0.3);
        children.put(new Leaf("---------",1), 0.4);

        Set<Leaf> keys = children.keySet();

        ArrayList<Leaf> testLeaves = new ArrayList<>();
        for (Leaf key: keys
             ) {
            testLeaves.add(key);
        }
        Leaf cos = testLeaves.get(0);
        cos.getLeaves().put(new Leaf("XXX---X--", 2),0.3);
        root.showTree();

    }

}
