package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
public class Heart {

    private float x, y;
    private final float speed;
    private static final int SIZE = 20;

    public Heart(float x, float y, float speed) {
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

    private boolean collected = false;
    public void collect() { collected = true; }
    public boolean isCollected() { return collected; }

    public void draw(Graphics2D g) {
        g.setColor(new Color(230, 50, 60));
        int lobe = SIZE / 2;
        g.fillOval(Math.round(x), Math.round(y), lobe, lobe);
        g.fillOval(Math.round(x) + lobe, Math.round(y), lobe, lobe);
        int[] xs = { Math.round(x), Math.round(x) + SIZE, Math.round(x) + lobe };
        int[] ys = { Math.round(y) + lobe / 2, Math.round(y) + lobe / 2, Math.round(y) + SIZE };
        g.fillPolygon(xs, ys, 3);
    }
}
