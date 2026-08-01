package skystrike;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/** Title screen. Press ENTER to start a new game. */
public class MenuState implements GameState {

    private final GamePanel gamePanel;

    public MenuState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void onEnter() { }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 110));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 42f));
        drawCentered(g, "Skystrike", GamePanel.HEIGHT / 2 - 40);

        g.setFont(g.getFont().deriveFont(Font.PLAIN, 18f));
        
    }

    private void drawCentered(Graphics2D g, String text, int y) {
        int w = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (GamePanel.WIDTH - w) / 2, y);
    }

    @Override
    public void handleKeyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            gamePanel.setState(new PlayingState(gamePanel));
        }
    }

    @Override
    public void handleKeyReleased(int keyCode) { }
}