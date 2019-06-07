package Logic;

import javafx.scene.image.ImageView;
import java.util.*;


public class Game {
    private final int NUMBER_OF_FIELDS = 9;
    private Random generator = new Random();

    private List<Integer> emptyFields;
    private List<ImageView> allFields;

    private Set<Integer> player1Steps;
    private Set<Integer> player2Steps;

    public Set<Integer> getPlayer1Steps() {
        return player1Steps;
    }

    public Set<Integer> getPlayer2Steps() {
        return player2Steps;
    }

    public Game() {

        player1Steps = new LinkedHashSet<Integer>();
        player2Steps = new LinkedHashSet<Integer>();

        allFields = new ArrayList<ImageView>();
        emptyFields = new ArrayList<Integer>();
        // set available moves
        for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
            emptyFields.add(i);
        }

    }
    public boolean move(int position, Player player) {

        if(emptyFields.indexOf(position) != (-1)){
            emptyFields.remove(new Integer(position));
            if(player == Player.ONE) player1Steps.add(position);
            if(player == Player.TWO) player2Steps.add(position);
            return true;
        } else {
        System.out.println("Not empty");
        return false;
        }

    }
    public int randomMove(Player player){
        int numberOfEmptyFields = emptyFields.size();
        if (numberOfEmptyFields > 0) {

            int randomId = generator.nextInt(numberOfEmptyFields);
            int field = emptyFields.get(randomId);
            emptyFields.remove(randomId);

            if(player == Player.ONE) player1Steps.add(field);
            if(player == Player.TWO) player2Steps.add(field);
            return field;
        }
        return -1;
    }
}
