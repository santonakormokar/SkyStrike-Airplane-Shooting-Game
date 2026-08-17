package skystrike;

import java.awt.Color;
public class BossEnemy extends Enemy {

    private final int panelWidth;
    private int direction = 1;
    private static final float HOVER_Y = 60f;

    public BossEnemy(float x, float y, int panelWidth) {
        super(x, y, 100, 100, 1.0f, 20, 200, 500);
        this.panelWidth = panelWidth;
    }

    @Override
    protected void move() {
        if (y < HOVER_Y) {
            y += speed;
            return;
        }
        x += direction * speed * 2f;
        if (x <= 0 || x + width >= panelWidth) {
            direction *= -1;
        }
    }

    @Override
    protected Color bodyColor() {
        return new Color(120, 30, 150);
    }
}
