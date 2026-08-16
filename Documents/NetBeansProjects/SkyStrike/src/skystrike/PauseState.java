package skystrike;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * PauseState
 * ----------
 * Wraps a specific PlayingState instance without discarding it — update()
 * does nothing (frozen), but draw() still renders the paused gameplay
 * underneath a dark overlay, then "PAUSED" on top. Clicking the resume
 * icon (same top-right spot the pause icon used) hands control straight
 * back to the same PlayingState, so nothing about the run is lost.
 */
public class PauseState implements GameState {

    private final GamePanel gamePanel;
    private final PlayingState pausedState;

    public PauseState(GamePanel gamePanel, PlayingState pausedState) {
        this.gamePanel = gamePanel;
        this.pausedState = pausedState;
    }

    @Override
    public void onEnter() { }

    @Override
    public void update() {
        // Frozen on purpose: no player/enemy/bullet updates while paused.
    }

    @Override
    public void draw(Graphics2D g) {
        pausedState.draw(g);

        g.setColor(new Color(0, 0, 0, 140));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 36f));
        String text = "PAUSED";
        int w = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (GamePanel.WIDTH - w) / 2, GamePanel.HEIGHT / 2);

        drawResumeButton(g);
    }

    /** Draws a small play/resume triangle in the same corner the pause icon used. */
    private void drawResumeButton(Graphics2D g) {
        Rectangle b = GamePanel.PAUSE_BUTTON_BOUNDS;
        g.setColor(new Color(255, 255, 255, 200));
        g.fillRoundRect(b.x, b.y, b.width, b.height, 8, 8);

        g.setColor(new Color(30, 30, 30));
        int cx = b.x + b.width / 2;
        int cy = b.y + b.height / 2;
        int[] xs = { cx - 5, cx - 5, cx + 7 };
        int[] ys = { cy - 8, cy + 8, cy };
        g.fillPolygon(xs, ys, 3);
    }

    @Override
    public void handleKeyPressed(int keyCode) { }

    @Override
    public void handleKeyReleased(int keyCode) { }

    @Override
    public void handleMouseClick(int x, int y) {
        if (GamePanel.PAUSE_BUTTON_BOUNDS.contains(x, y)) {
            gamePanel.setState(pausedState);
        }
    }
}