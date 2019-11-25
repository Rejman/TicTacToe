package RL.Policy;

import java.util.HashSet;
import java.util.Set;

public abstract class State {
    public static int degree = 3;
    public static String turnLeft(String state){
        String[] rows = new String[degree];
        String newState = new String();
        int index = 0;
        for(int i=0;i<degree*degree;i+=degree){
            rows[index] = state.substring(i,i+degree);
            index++;

        }
        for(int i=degree-1;i>=0;i--){
            for (String row:rows
            ) {
                newState+=row.charAt(i);
            }
        }
        return newState;
    }
    public static String mirror(String state){
        String[] rows = new String[degree];
        String newState = new String();
        int index = 0;
        for(int i=0;i<degree*degree;i+=degree){
            rows[index] = state.substring(i,i+degree);
            index++;

        }
        for (String row:rows
             ) {
            for(int i=degree-1;i>=0;i--){
                newState+=row.charAt(i);
            }
        }
        return newState;
    }
    public static Set<String> allternatveState(String state){
        Set<String> allState = new HashSet<>();

        for(int i=0;i<=3;i++){
            allState.add(state);
            allState.add(mirror(state));
            state = turnLeft(state);
        }
        /*String[] array = new String[allState.size()];
        allState.toArray(array);*/

        return allState;
    }
    public static void showAsBoard(String state){
        String[] rows = new String[degree];
        String newState = new String();
        int index = 0;
        for(int i=0;i<degree*degree;i+=degree){
            System.out.println(state.substring(i,i+degree));
            index++;
        }
    }

    public static void main(String[] args) {

        String state1 = "XO--------------";
        Set<String> alters = allternatveState(state1);
        for (String s:alters
             ) {
            showAsBoard(s);
            System.out.println("__________");
        }
        String state2 = "----OX----------";
        System.out.println(alters.contains(state2));
        showAsBoard(state2);
    }
}
