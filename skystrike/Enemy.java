package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Enemy (abstract)
 * ----------------
 * Common base for every enemy type. Still abstract here — the
 * concrete EasyEnemy / MediumEnemy / HardEnemy / BossEnemy subclasses are
 * built in the Factory Method pattern (EnemyFactory). This class
 * only fixes what *all* enemies share: they drift downward by default,
 * they're worth coins and score, and they render as a colored body whose
 * exact color each subclass decides via bodyColor().
 */
public abstract class Enemy extends Aircraft {

    protected final int coinValue;
    protected final int scoreValue;

    protected Enemy(float x, float y, int width, int height, float speed,
                     int maxHealth, int coinValue, int scoreValue) {
        super(x, y, width, height, speed, maxHealth);
        this.coinValue = coinValue;
        this.scoreValue = scoreValue;
    }

    /** Default enemy movement: straight down. Subclasses may override for their own pattern. */
    @Override
    protected void move() {
        y += speed;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(bodyColor());
        g.fillRoundRect(Math.round(x), Math.round(y), width, height, 10, 10);
    }

    /** Hook: each concrete enemy picks its own color without re-implementing draw(). */
    protected abstract Color bodyColor();

    public int getCoinValue() { return coinValue; }
    public int getScoreValue() { return scoreValue; }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }
}