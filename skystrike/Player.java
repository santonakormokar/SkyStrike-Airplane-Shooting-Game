package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Player
 * ------
 * The player-controlled aircraft. Implements Aircraft's move() step with
 * simple directional flags rather than reading the keyboard directly —
 * that keeps Player decoupled from input handling. In Step 5 the Command
 * pattern will call these same setMoving...() setters from key bindings,
 * so nothing here will need to change.
 */
public class Player extends Aircraft {

    private final int panelWidth, panelHeight;
    private boolean movingLeft, movingRight, movingUp, movingDown;

    private ShootStrategy shootStrategy = new SingleShotStrategy();
    private int shootCooldown = 0;

    public Player(float x, float y, int panelWidth, int panelHeight) {
        super(x, y, 48, 48, 5f, 3);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    /** Swaps the current firing pattern (e.g. from a future power-up) without Player needing to change. */
    public void setShootStrategy(ShootStrategy strategy) { this.shootStrategy = strategy; }
    public ShootStrategy getShootStrategy() { return shootStrategy; }

    /** Returns newly fired bullets if off cooldown, otherwise an empty list. Safe to call as often as input arrives. */
    public java.util.List<Bullet> tryShoot() {
        if (shootCooldown > 0) {
            return java.util.Collections.emptyList();
        }
        shootCooldown = shootStrategy.getCooldownFrames();
        return shootStrategy.shoot(x, y, width);
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

    /**
     * Ticks the shoot cooldown down once per real game frame (this runs every
     * frame regardless of whether the player is shooting, since it's called
     * from Aircraft's update() template method via move() -> onAfterMove()).
     * Previously the cooldown only decremented inside tryShoot(), so it only
     * ticked down when you tried to shoot again — meaning waiting longer
     * between shots didn't actually help. This fixes that.
     */
    @Override
    protected void onAfterMove() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        java.awt.geom.Path2D.Float body = new java.awt.geom.Path2D.Float();
        float cx = x + width / 2f;

        // Nose-up plane silhouette: nose tip, swept wings, tail fins — mirror of Enemy's shape.
        body.moveTo(cx, y);
        body.lineTo(cx - width * 0.12f, y + height * 0.25f);
        body.lineTo(cx - width * 0.50f, y + height * 0.55f);
        body.lineTo(cx - width * 0.18f, y + height * 0.55f);
        body.lineTo(cx - width * 0.22f, y + height * 0.85f);
        body.lineTo(cx - width * 0.40f, y + height);
        body.lineTo(cx, y + height * 0.88f);
        body.lineTo(cx + width * 0.40f, y + height);
        body.lineTo(cx + width * 0.22f, y + height * 0.85f);
        body.lineTo(cx + width * 0.18f, y + height * 0.55f);
        body.lineTo(cx + width * 0.50f, y + height * 0.55f);
        body.lineTo(cx + width * 0.12f, y + height * 0.25f);
        body.closePath();

        Color bodyColor = new Color(220, 55, 55);
        g.setColor(bodyColor);
        g.fill(body);
        g.setColor(bodyColor.darker());
        g.draw(body);

        // Cockpit
        g.setColor(new Color(180, 225, 255, 210));
        int cockpitSize = Math.max(5, width / 6);
        g.fillOval(Math.round(cx - cockpitSize / 2f), Math.round(y + height * 0.28f), cockpitSize, cockpitSize);
    }
}