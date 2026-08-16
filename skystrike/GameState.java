package skystrike;

import java.awt.Graphics2D;

/**
 * GameState (State pattern)
 * --------------------------
 * Each screen of the game — menu, playing, paused, game over, victory —
 * is one implementation of this interface. GamePanel just holds "the
 * current state" and forwards update(), draw(), and key events to it.
 * GamePanel itself never asks "am I in the menu?" with an if/else chain —
 * whichever state is active simply behaves the way that state should,
 * and transitions to a new state by calling gamePanel.setState(...).
 */
public interface GameState {

    /** Called once when this state becomes active. */
    void onEnter();

    /** Called once per frame while this state is active. */
    void update();

    void draw(Graphics2D g);

    void handleKeyPressed(int keyCode);

    void handleKeyReleased(int keyCode);

    /** Default no-op: only states with clickable UI (Playing, Pause) need to override this. */
    default void handleMouseClick(int x, int y) { }
}