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
    public static Set<String> alternativeStates(String state){
        Set<String> allStates = new HashSet<>();

        for(int i=0;i<=3;i++){
            allStates.add(state);
            allStates.add(mirror(state));
            state = turnLeft(state);
        }
        return allStates;
    }

    public static String showAsBoards(Set<String> states){
        String[] rows = new String[degree];
        String newState = new String();
        int index = 0;
        String board = "";
        for(int i=0;i<degree*degree;i+=degree){
            String line = "";
            //System.out.println(state.substring(i,i+degree));
            for (String s:states
                 ) {
                line+=s.substring(i,i+degree);
                line+="\t";
            }
            index++;
            board+=line+"\n";
        }
        return board;
    }

}
