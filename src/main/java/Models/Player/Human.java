package Models.Player;

import Models.Game.Game;
import Models.Game.Sign;

public class Human extends Player {
    /**
     * @param name
     * @param value
     * @param game
     */
    public Human(String name, Sign value, Game game) {
        super(name, value, game);
    }
    /**
     * Performs movement to identified field
     * @param field
     */
    public void move(int field) {

        game.addMove(field, value);
        steps.add(field);

    }
}
