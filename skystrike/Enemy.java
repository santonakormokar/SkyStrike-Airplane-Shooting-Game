package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Enemy extends Aircraft {

    protected final int coinValue;
    protected final int scoreValue;

    protected Enemy(float x, float y, int width, int height, float speed,
                     int maxHealth, int coinValue, int scoreValue) {
        super(x, y, width, height, speed, maxHealth);
        this.coinValue = coinValue;
        this.scoreValue = scoreValue;
    }

    @Override
    protected void move() {
        y += speed;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(bodyColor());
        g.fillRoundRect(Math.round(x), Math.round(y), width, height, 10, 10);
    }

    protected abstract Color bodyColor();

    public int getCoinValue() { return coinValue; }
    public int getScoreValue() { return scoreValue; }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }
}
