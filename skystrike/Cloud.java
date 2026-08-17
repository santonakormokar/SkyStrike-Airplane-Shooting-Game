package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;

public class Cloud {

    private float x, y;
    private final int size;
    private final float speed;

    public Cloud(float x, float y, int size, float speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    public void update(int panelHeight, int panelWidth) {
        y += speed;
        if (y - size > panelHeight) {
            y = -size;
            x = (float) (Math.random() * panelWidth);
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, 210));
        g.fillOval((int) x, (int) y, size, (int) (size * 0.6));
        g.fillOval((int) (x + size * 0.3), (int) (y - size * 0.2), (int) (size * 0.6), (int) (size * 0.5));
        g.fillOval((int) (x + size * 0.55), (int) y, (int) (size * 0.5), (int) (size * 0.45));
    }
}
