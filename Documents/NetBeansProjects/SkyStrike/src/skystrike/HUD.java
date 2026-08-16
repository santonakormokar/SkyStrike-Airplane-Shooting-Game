package skystrike;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * HUD (Observer)
 * --------------
 * Displays health hearts, score, coins, and difficulty in the top-left
 * corner. It never reads GameManager directly while drawing — it just
 * caches whatever GameManager last pushed via onStatsChanged(). That
 * means GameManager can change score/coins/health from anywhere
 * (collision detection, coin pickups, difficulty select) and the HUD
 * updates automatically next frame, with zero calls from those places
 * back into HUD itself.
 */
public class HUD implements GameObserver {

    private int health = 3;
    private int maxHealth = 3;
    private int score = 0;
    private int coins = 0;
    private GameManager.Difficulty difficulty = GameManager.Difficulty.EASY;

    @Override
    public void onStatsChanged(int health, int maxHealth, int score, int coins, GameManager.Difficulty difficulty) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.score = score;
        this.coins = coins;
        this.difficulty = difficulty;
    }

    public void draw(Graphics2D g) {
        drawHearts(g);

        g.setFont(g.getFont().deriveFont(Font.BOLD, 15f));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 14, 58);
        g.drawString("Coins: " + coins, 14, 78);
        g.drawString("Difficulty: " + difficulty, 14, 98);
    }

    private void drawHearts(Graphics2D g) {
        int size = 18;
        int gap = 6;
        int startX = 14;
        int y = 14;

        for (int i = 0; i < maxHealth; i++) {
            int x = startX + i * (size + gap);
            boolean filled = i < health;
            drawHeart(g, x, y, size, filled);
        }
    }

    private void drawHeart(Graphics2D g, int x, int y, int size, boolean filled) {
        g.setColor(filled ? new Color(230, 50, 60) : new Color(255, 255, 255, 90));
        int lobe = size / 2;
        g.fillOval(x, y, lobe, lobe);
        g.fillOval(x + lobe, y, lobe, lobe);
        int[] xs = { x, x + size, x + lobe };
        int[] ys = { y + lobe / 2, y + lobe / 2, y + size };
        g.fillPolygon(xs, ys, 3);
    }
}