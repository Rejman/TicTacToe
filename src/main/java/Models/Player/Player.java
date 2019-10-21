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

    //sign represents the player
    protected Sign value;

    /**
     *
     * @param name
     * @param value
     */
    public Player(String name, Sign value, Game game){
        this.game = game;
        this.name = name;
        this.value = value;
    }
    public void setGame(Game game){
        this.game = game;
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
    public void setValue(Sign sign){
        this.value = sign;
    }

    public String toString(){
        return name;
    }


}
