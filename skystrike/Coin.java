package skystrike;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Coin
 * ----
 * A falling pickup. Unlike enemies, coins don't fight back — the player
 * just has to fly into one to collect it. Coins award coins only; score
 * comes exclusively from destroying enemies (see PlayingState.onEnemyDestroyed).
 */
public class Coin {

    private float x, y;
    private final float speed;
    private static final int SIZE = 20;
    private static final int VALUE = 5;
    private boolean collected = false;

    public Coin(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update() {
        y += speed;
    }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), SIZE, SIZE);
    }

    public int getValue() {
        return VALUE;
    }

    /** Marks this coin as picked up, so it's removed instead of continuing to fall. */
    public void collect() { collected = true; }
    public boolean isCollected() { return collected; }

    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 205, 45));
        g.fillOval(Math.round(x), Math.round(y), SIZE, SIZE);
        g.setColor(new Color(200, 150, 20));
        g.drawOval(Math.round(x), Math.round(y), SIZE, SIZE);

        g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
        g.setColor(new Color(140, 100, 10));
        FontMetrics fm = g.getFontMetrics();
        String label = "$";
        int textWidth = fm.stringWidth(label);
        g.drawString(label, Math.round(x) + SIZE / 2 - textWidth / 2, Math.round(y) + SIZE / 2 + 5);
    }
}