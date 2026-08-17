package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends Aircraft {

    private final int panelWidth, panelHeight;
    private boolean movingLeft, movingRight, movingUp, movingDown;

    private ShootStrategy shootStrategy = new SingleShotStrategy();
    private int shootCooldown = 0;
    private static final int SHOOT_COOLDOWN_FRAMES = 12;

    public Player(float x, float y, int panelWidth, int panelHeight) {
        super(x, y, 48, 48, 5f, 3);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }
    public void setShootStrategy(ShootStrategy strategy) { this.shootStrategy = strategy; }
    public ShootStrategy getShootStrategy() { return shootStrategy; }

    public java.util.List<Bullet> tryShoot() {
        if (shootCooldown > 0) {
            shootCooldown--;
            return java.util.Collections.emptyList();
        }
        shootCooldown = SHOOT_COOLDOWN_FRAMES;
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

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(230, 60, 60));
        g.fillRoundRect(Math.round(x), Math.round(y), width, height, 12, 12);

        g.setColor(Color.WHITE);
        int cx = Math.round(x + width / 2f);
        int nose = Math.round(y);
        int wingY = Math.round(y + height * 0.4f);
        g.fillPolygon(
                new int[]{cx, Math.round(x + width * 0.15f), Math.round(x + width * 0.85f)},
                new int[]{nose, wingY, wingY},
                3
        );
    }
}
