package skystrike;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Aircraft {

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

    public final void update() {
        move();
        onAfterMove();
    }
    protected abstract void move();
    protected void onAfterMove() { }
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
