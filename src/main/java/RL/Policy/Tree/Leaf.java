package RL.Policy.Tree;

import RL.Policy.State;
import javafx.scene.layout.StackPane;

import java.io.Serializable;
import java.util.*;

public class Leaf implements Serializable, Comparable {

    private String state;
    private double value;
    private ArrayList<Leaf> children;

    public Leaf(String state, double value) {
        this.state = state;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public Leaf(String state) {
        this(state, 0.0);
    }

    public Leaf getChild(Leaf leaf) {

        boolean isExist = children.contains(leaf);
        if (isExist) {
            //System.out.println("istnieje");
            return children.get(children.indexOf(leaf));
        }
        return null;
    }

    public void addChild(Leaf leaf) {
        //System.out.println("Dzieci : "+children.toString());
        //System.out.println("Szukana wartość: "+leaf.getState());
        int id = children.indexOf(leaf);
        //System.out.println(id);
        if (id >= 0) {
            //System.out.println("nadpisanie");
            //System.out.println("nadpisanie liscia nową oceną");
            children.set(id, leaf);
        } else {
            //System.out.println("Nowy stan "+state);
            //System.out.println("dodanie nowego liścia bo go nie było");
            children.add(leaf);
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ArrayList<Leaf> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Leaf> children) {
        this.children = children;
    }

    public int getLevel() {
        int level = state.length();
        for (int i = 0; i < state.length(); i++) {
            if (state.substring(i, i + 1).equals("-")) {
                level--;
            }
        }
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaf leaf = (Leaf) o;
        Set<String> alters = State.alternativeStates(((Leaf) o).getState());

        return alters.contains(this.state);

    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return state;
    }


    @Override
    public int compareTo(Object o) {
        Leaf leaf = (Leaf) o;
        return Double.compare(leaf.getValue(),this.getValue());

    }
}
