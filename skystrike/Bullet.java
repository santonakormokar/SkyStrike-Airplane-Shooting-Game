package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
public class Bullet {

    private float x, y;
    private final float dx, dy;
    private final int width, height;
    private final int damage;
    private final boolean fromPlayer;

    public Bullet(float x, float y, float dx, float dy, int width, int height, int damage, boolean fromPlayer) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.fromPlayer = fromPlayer;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D g) {
        g.setColor(fromPlayer ? new Color(255, 230, 60) : new Color(255, 70, 70));
        g.fillRoundRect(Math.round(x), Math.round(y), width, height, 4, 4);
    }

    public boolean isOffScreen(int panelWidth, int panelHeight) {
        return y + height < 0 || y > panelHeight || x + width < 0 || x > panelWidth;
    }

    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), width, height);
    }

    public int getDamage() { return damage; }
    public boolean isFromPlayer() { return fromPlayer; }
}
