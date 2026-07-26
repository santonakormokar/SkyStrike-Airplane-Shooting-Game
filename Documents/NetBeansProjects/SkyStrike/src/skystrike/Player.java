package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Player
 * ------
 * The player-controlled aircraft. Implements Aircraft's move() step with
 * simple directional flags rather than reading the keyboard directly —
 * that keeps Player decoupled from input handling. In the Command
 * pattern will call these same setMoving...() setters from key bindings,
 * so nothing here will need to change.
 */
public class Player extends Aircraft {

    private final int panelWidth, panelHeight;
    private boolean movingLeft, movingRight, movingUp, movingDown;

    public Player(float x, float y, int panelWidth, int panelHeight) {
        super(x, y, 48, 48, 5f, 3);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    public void setMovingLeft(boolean v) { movingLeft = v; }
    public void setMovingRight(boolean v) { movingRight = v; }
    public void setMovingUp(boolean v) { movingUp = v; }
    public void setMovingDown(boolean v) { movingDown = v; }

    @Override
    protected void move() {
        if (movingLeft) x -= speed;
        if (movingRight) x += speed;
        if (movingUp) y -= speed;
        if (movingDown) y += speed;

        // Keep the player fully on screen.
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + width > panelWidth) x = panelWidth - width;
        if (y + height > panelHeight) y = panelHeight - height;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(230, 60, 60));
        g.fillRoundRect(Math.round(x), Math.round(y), width, height, 12, 12);

        g.setColor(Color.WHITE);
        int cx = Math.round(x + width / 2f);
        int nose = Math.round(y);
        int wingY = Math.round(y + height * 0.4f);
        g.fillPolygon(
                new int[]{cx, Math.round(x + width * 0.15f), Math.round(x + width * 0.85f)},
                new int[]{nose, wingY, wingY},
                3
        );
    }
}