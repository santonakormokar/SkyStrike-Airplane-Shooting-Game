package skystrike;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * KeyInputHandler (Command pattern's invoker)
 * ---------------------------------------------
 * Now that GameState exists, this class got simpler: it doesn't decide
 * what a key means anymore — it just forwards every press/release to
 * whichever GameState is currently active, and that state decides
 * (MenuState only cares about ENTER, PlayingState builds Move/Shoot/Pause
 * Commands, GameOverState only cares about ENTER, etc).
 */
public class KeyInputHandler extends KeyAdapter {

    private final GamePanel gamePanel;

    public KeyInputHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.getCurrentState().handleKeyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.getCurrentState().handleKeyReleased(e.getKeyCode());
    }
}