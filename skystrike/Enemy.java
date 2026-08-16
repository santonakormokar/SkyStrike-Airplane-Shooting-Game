package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

/**
 * Enemy (abstract)
 * ----------------
 * Common base for every enemy type. Still abstract here in Step 2 — the
 * concrete EasyEnemy / MediumEnemy / HardEnemy / BossEnemy subclasses are
 * built in Step 3 via the Factory Method pattern (EnemyFactory). This class
 * fixes what *all* enemies share: they drift downward by default, they're
 * worth coins and score, they render as a plane silhouette (nose pointing
 * down, since they're flying toward the player) colored per subclass, and
 * — as of this update — they periodically fire a bullet down at the player.
 */
public abstract class Enemy extends Aircraft {

    protected final int coinValue;
    protected final int scoreValue;
    private int shootTimer;

    protected Enemy(float x, float y, int width, int height, float speed,
                     int maxHealth, int coinValue, int scoreValue) {
        super(x, y, width, height, speed, maxHealth);
        this.coinValue = coinValue;
        this.scoreValue = scoreValue;
        // Stagger each enemy's first shot instead of every enemy firing in lockstep.
        this.shootTimer = (int) (Math.random() * getShootIntervalFrames());
    }

    /** Default enemy movement: straight down. Subclasses may override for their own pattern. */
    @Override
    protected void move() {
        y += speed;
    }

    /**
     * Call once per frame from PlayingState. Returns a bullet aimed down at
     * the player when this enemy's fire cooldown elapses, otherwise null.
     */
    public Bullet tryShoot() {
        shootTimer++;
        if (shootTimer >= getShootIntervalFrames()) {
            shootTimer = 0;
            float bulletX = x + width / 2f - 3;
            float bulletY = y + height;
            return new Bullet(bulletX, bulletY, 0f, 4.5f, 6, 14, 1, false);
        }
        return null;
    }

    /** Frames between shots. Subclasses (e.g. BossEnemy) can override to fire faster. */
    protected int getShootIntervalFrames() {
        return 150;
    }

    @Override
    public void draw(Graphics2D g) {
        Path2D.Float body = new Path2D.Float();
        float cx = x + width / 2f;

        // Nose-down plane silhouette: nose tip, swept-back wings, tail fins.
        body.moveTo(cx, y + height);
        body.lineTo(cx - width * 0.12f, y + height * 0.75f);
        body.lineTo(cx - width * 0.50f, y + height * 0.45f);
        body.lineTo(cx - width * 0.18f, y + height * 0.45f);
        body.lineTo(cx - width * 0.22f, y + height * 0.15f);
        body.lineTo(cx - width * 0.40f, y);
        body.lineTo(cx, y + height * 0.12f);
        body.lineTo(cx + width * 0.40f, y);
        body.lineTo(cx + width * 0.22f, y + height * 0.15f);
        body.lineTo(cx + width * 0.18f, y + height * 0.45f);
        body.lineTo(cx + width * 0.50f, y + height * 0.45f);
        body.lineTo(cx + width * 0.12f, y + height * 0.75f);
        body.closePath();

        g.setColor(bodyColor());
        g.fill(body);
        g.setColor(bodyColor().darker());
        g.draw(body);

        // Cockpit
        g.setColor(new Color(40, 40, 40, 160));
        int cockpitSize = Math.max(4, width / 6);
        g.fillOval(Math.round(cx - cockpitSize / 2f), Math.round(y + height * 0.55f), cockpitSize, cockpitSize);
    }

    /** Hook: each concrete enemy picks its own color without re-implementing draw(). */
    protected abstract Color bodyColor();

    public int getCoinValue() { return coinValue; }
    public int getScoreValue() { return scoreValue; }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }
}