package RL.Policy.Tree;

import RL.Policy.State;
import javafx.scene.layout.StackPane;

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
        Set<String> alters = State.allternatveState(((Leaf) o).getState());

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

    public void showTree(int limit) {

        //State.degree = 4;
        int level = getLevel();
        if (level <= limit) {
            String space = "";
            for (int i = 0; i < level; i++) space += "\t";
            System.out.println(space + "(" + level + ") value =  "+ String.format("%.5f", value));
            int degree = State.degree;
            System.out.println(state);
            System.out.println(degree);
            String[] rows = new String[degree];
            String newState = new String();
            int index = 0;
            for(int i=0;i<degree*degree;i+=degree){
                System.out.println(space+state.substring(i,i+degree));
                index++;
            }

            if (!children.isEmpty()) {
                for (Leaf leaf : children
                ) {
                    leaf.showTree(limit);
                }
            }
        }
    }

    public static void main(String[] args) {

        State.degree = 4;
        Leaf root = new Leaf("---------", 0);

        root.addChild(new Leaf("---X-----", 0.3));
        root.addChild(new Leaf("--X------", 0.3));
        //root.addChild(new Leaf("---------", 0.3));
        //root.addChild(new Leaf("---X-----", 0.4));

        Set<String> alter = State.allternatveState("---X-----");
        System.out.println("test:" + alter.contains("--X------"));
        System.out.println(root.getChildren().indexOf("-------X-"));
        root.showTree(55);

        System.out.println("Porównywanie ");
        Leaf leaf1 = new Leaf("X--------");
        State.showAsBoard(leaf1.getState());
        Leaf leaf2 = new Leaf("-X-------");
        System.out.println("Z");
        State.showAsBoard(leaf2.getState());
        System.out.println(leaf1.equals(leaf2));


    }

    public static void showInRows(String state){
        System.out.println(state.substring(0,3));
        System.out.println(state.substring(3,6));
        System.out.println(state.substring(6));
    }

}
