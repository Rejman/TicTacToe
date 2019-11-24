package RL.Policy.Tree;

import java.io.Serializable;
import java.util.*;

public class Leaf implements Serializable {

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
            return children.get(children.indexOf(leaf));
        }
        return null;
    }

    public void addChild(Leaf leaf) {
        int id = children.indexOf(leaf);
        if (id >= 0) {
            System.out.println("nadpisanie");
            //System.out.println("nadpisanie liscia nową oceną");
            children.set(id, leaf);
        } else {
            System.out.println("Nowy stan "+state);
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
        for (int i = 0; i < 9; i++) {
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

    public void showTree(int limit) {


        int level = getLevel();
        if (level <= limit) {
            String space = "";
            for (int i = 0; i < level; i++) space += "\t";
            System.out.println(space + "(" + level + ")" + state + " = " + value);
            System.out.println(space + state.substring(0, 3));
            System.out.println(space + state.substring(3, 6));
            System.out.println(space + state.substring(6, 9));

            if (!children.isEmpty()) {
                for (Leaf leaf : children
                ) {
                    leaf.showTree(limit);
                }
            }
        }
    }

    public static void main(String[] args) {


        /*Leaf root = new Leaf("---------", 0);

        root.addChild(new Leaf("---X-----", 0.3));
        root.addChild(new Leaf("--X------", 0.3));
        root.addChild(new Leaf("-X-------", 0.3));
        root.addChild(new Leaf("---X-----", 0.4));

        Leaf child = root.getChild(new Leaf("--X------", 0.8));
        child.addChild(new Leaf("-OX------", 0.5));
        //root.showTree(0);
        Leaf test = root.getChild(new Leaf("-X-------", 10.2));

        test.setState("XXXXXXXXX");

        root.showTree(55);*/

        String state = "-X-O-----";
        showInRows(state);

    }

    public static void showInRows(String state){
        System.out.println(state.substring(0,3));
        System.out.println(state.substring(3,6));
        System.out.println(state.substring(6));
    }

}
