package Models;

public class Game {
    private int[] moves;
    private static int index = 0;

    public Game() {
        moves = new int[9];
    }

    public boolean check(int positon){
        moves[index] = positon;
        index++;
        return true;
    }

    public int[] getAllMoves(){
        return moves;
    }
    public int[] getFirstPlayerMoves(){
        int[] temp = new int[5];
        int j=0;
        for(int i=0;i<9;i++){
            if(i%2==0) temp[j]=moves[i];
            j++;
        }
        return temp;
    }
    public int[] getSecondPlayerMoves(){
        int[] temp = new int[5];
        int j=0;
        for(int i=0;i<9;i++){
            if(i%2!=0) temp[j]=moves[i];
            j++;
        }
        return temp;
    }
}
