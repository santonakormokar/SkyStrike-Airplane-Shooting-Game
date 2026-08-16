package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Bullet
 * ------
 * A simple projectile: position + constant velocity (dx, dy), a damage
 * value, and a flag for who fired it (so player bullets don't hurt the
 * player and enemy bullets don't hurt enemies once collision is wired up
 * in a later step). The various ShootStrategy implementations decide how
 * many of these to create and at what angles/speeds — Bullet itself has
 * no idea which strategy made it.
 */
public class Bullet {

    private float x, y;
    private final float dx, dy;
    private final int width, height;
    private final int damage;
    private final boolean fromPlayer;

    public Bullet(float x, float y, float dx, float dy, int width, int height, int damage, boolean fromPlayer) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.fromPlayer = fromPlayer;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D g) {
        g.setColor(fromPlayer ? new Color(255, 230, 60) : new Color(255, 70, 70));
        g.fillRoundRect(Math.round(x), Math.round(y), width, height, 4, 4);
    }

    public boolean isOffScreen(int panelWidth, int panelHeight) {
        return y + height < 0 || y > panelHeight || x + width < 0 || x > panelWidth;
    }

    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), width, height);
    }

    public int getDamage() { return damage; }
    public boolean isFromPlayer() { return fromPlayer; }

    /** Returns a copy of this bullet with a different damage value. Used by DoubleDamageDecorator. */
    public Bullet withDamage(int newDamage) {
        return new Bullet(x, y, dx, dy, width, height, newDamage, fromPlayer);
    }
}