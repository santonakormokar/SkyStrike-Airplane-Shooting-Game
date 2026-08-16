package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;

/** A short expanding, fading burst drawn wherever an enemy is destroyed. */
public class Explosion {

    private final float x, y;
    private int age = 0;
    private static final int LIFETIME_FRAMES = 20;

    public Explosion(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        age++;
    }

    public boolean isFinished() {
        return age >= LIFETIME_FRAMES;
    }

    public void draw(Graphics2D g) {
        float progress = age / (float) LIFETIME_FRAMES;
        int radius = (int) (10 + progress * 30);
        int alpha = Math.max(0, (int) (255 * (1 - progress)));

        g.setColor(new Color(255, 150, 30, alpha));
        g.fillOval((int) (x - radius), (int) (y - radius), radius * 2, radius * 2);

        int inner = radius / 2;
        g.setColor(new Color(255, 230, 140, alpha));
        g.fillOval((int) (x - inner), (int) (y - inner), inner * 2, inner * 2);
    }
}