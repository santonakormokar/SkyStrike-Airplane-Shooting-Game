package skystrike;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class VictoryState implements GameState {

    private final GamePanel gamePanel;
    private final int finalScore;
    private final int finalCoins;

    public VictoryState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.finalScore = GameManager.getInstance().getScore();
        this.finalCoins = GameManager.getInstance().getCoins();
    }

    @Override
    public void onEnter() { }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0, 50, 20, 170));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 38f));
        drawCentered(g, "VICTORY!", GamePanel.HEIGHT / 2 - 50);

        g.setFont(g.getFont().deriveFont(Font.PLAIN, 18f));
        drawCentered(g, "Score: " + finalScore + "   Coins: " + finalCoins, GamePanel.HEIGHT / 2);
        drawCentered(g, "Press ENTER to return to menu", GamePanel.HEIGHT / 2 + 40);
    }

    private void drawCentered(Graphics2D g, String text, int y) {
        int w = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (GamePanel.WIDTH - w) / 2, y);
    }

    @Override
    public void handleKeyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            gamePanel.setState(new MenuState(gamePanel));
        }
    }

    @Override
    public void handleKeyReleased(int keyCode) { }
}
