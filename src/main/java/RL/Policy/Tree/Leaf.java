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
    public Leaf getChild(Leaf leaf){
        int id = children.indexOf(leaf);
        if(id>=0) return children.get(id);
        return null;
    }
    public Leaf getChild(String state){
        int id = children.indexOf(new Leaf(state,0.0));
        if(id>=0) return children.get(id);
        return null;
    }
    public void addChild(Leaf leaf){
        int id = children.indexOf(leaf);
        if(id>=0){
            System.out.println("nadpisanie liscia nową oceną");
            children.set(id,leaf);
        }else{
            System.out.println("dodanie nowego liścia bo go nie było");
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

    public int getLevel(){
        int level = 9;
        for(int i=0;i<9;i++){
            if(state.substring(i,i+1).equals("-")){
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
    public String toBoardString(){
        String baord = "";
        baord+=state.substring(0,3)+"\n";
        baord+=state.substring(3,6)+"\n";
        baord+=state.substring(6,9)+"\n";
        baord+="------------------------";
        return baord;
    }
    public void showTree(){
        //System.out.println();
        int level = getLevel();
        for(int i=0;i<level;i++) System.out.print("\t");
        //System.out.println(level+" "+state+" -> ("+leaves.size()+")"+leaves);
        System.out.println("("+level+")"+state+" = "+value);
        if(!children.isEmpty()){
            for (Leaf leaf:children
            ) {
                leaf.showTree();
            }
        }


    }
    public static void main(String[] args) {


        Leaf root = new Leaf("---------", 0);

        root.addChild(new Leaf("---X-----",0.3));
        root.addChild(new Leaf("--X------",0.3));
        root.addChild(new Leaf("-X-------",0.3));
        root.addChild(new Leaf("---X-----",0.4));

        Leaf child = root.getChild(new Leaf("--X------",0.8));
        child.addChild(new Leaf("-OX------", 0.5));
        root.showTree();


    }

}
