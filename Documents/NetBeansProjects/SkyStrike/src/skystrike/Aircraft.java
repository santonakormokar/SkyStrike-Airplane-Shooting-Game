package skystrike;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Aircraft (Template Method)
 * ---------------------------
 * Defines the fixed skeleton every flying entity follows each frame:
 * update() always does "move, then run any after-move hook" — Player and
 * Enemy cannot skip or reorder that, they can only plug in *how* they move
 * by overriding move(). This is the Template Method pattern: the algorithm's
 * shape lives here once; the varying step lives in the subclasses.
 */
public abstract class Aircraft implements Damageable {

    protected float x, y;
    protected final int width, height;
    protected float speed;
    protected int health, maxHealth;

    protected Aircraft(float x, float y, int width, int height, float speed, int maxHealth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    /**
     * Template method: the invariant per-frame algorithm. It is final so
     * subclasses cannot change the sequence — only the steps marked
     * as hooks below.
     */
    public final void update() {
        move();
        onAfterMove();
    }

    /** Required step: how this aircraft decides to move this frame. */
    protected abstract void move();

    /** Optional hook: extra per-frame behavior after moving (default: nothing). */
    protected void onAfterMove() { }

    /** Each aircraft type renders itself differently. */
    public abstract void draw(Graphics2D g);

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), width, height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
}