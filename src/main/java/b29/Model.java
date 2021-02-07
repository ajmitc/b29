package b29;

import b29.game.Game;

public class Model {
    private Game game;

    public Model() {
        this.game = null;
    }

    public void updateGame() {

    }

    public Game getGame() {
        return game;
    }

    public void setGame( Game game ) {
        this.game = game;
    }
}
