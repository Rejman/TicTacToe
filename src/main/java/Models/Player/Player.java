package Models.Player;

import Models.Game.Game;
import Models.Game.Sign;

import java.util.HashSet;
import java.util.Set;

public abstract class Player {

    //name of player
    private String name;
    //reference to game object
    protected Game game;
    //steps which was made by the player
    protected Set<Integer> steps;

    //sign represents the player
    protected Sign value;

    /**
     *
     * @param name
     * @param value
     */
    public Player(String name, Sign value, Game game){
        this.game = game;
        this.steps = new HashSet<Integer>();
        this.name = name;
        this.value = value;
    }



    /**
     * player forgets all movements
     */
    public void resetSteps(){
        steps.clear();
    }

    /**
     * @return name of the player
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return sign that represents the player
     */
    public Sign getValue(){
        return value;
    }


}
