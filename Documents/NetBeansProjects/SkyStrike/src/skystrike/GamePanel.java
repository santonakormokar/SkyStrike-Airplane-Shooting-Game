package skystrike;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GamePanel
 * ---------
 * The rendering + update surface for SkyStrike. Right now it only
 * owns the scrolling sky background and cloud layer and runs the core game
 * loop. Later steps will add the player, enemies, bullets, HUD, and states
 * on top of this same loop without changing how the loop itself works.
 */
public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 720;
    private static final int TARGET_FPS = 60;

    private Thread gameThread;
    private volatile boolean running = false;

    private final List<Cloud> clouds = new ArrayList<>();
    private float skyScroll = 0f;

    private final Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private int spawnTimer = 0;
    private static final int SPAWN_INTERVAL = 90; // frames between enemy spawns

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setBackground(Color.BLACK);
        spawnClouds();
        player = new Player(WIDTH / 2f - 24, HEIGHT - 100, WIDTH, HEIGHT);
    }

    private void spawnClouds() {
        for (int i = 0; i < 8; i++) {
            float x = (float) (Math.random() * WIDTH);
            float y = (float) (Math.random() * HEIGHT);
            int size = 40 + (int) (Math.random() * 50);
            float speed = 0.6f + (float) (Math.random() * 1.2f);
            clouds.add(new Cloud(x, y, size, speed));
        }
    }

    /** Starts the background game loop thread. Safe to call once. */
    public void startGame() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this, "SkyStrike-GameLoop");
            gameThread.start();
        }
    }

    public void stopGame() {
        running = false;
    }

    @Override
    public void run() {
        final double nsPerFrame = 1_000_000_000.0 / TARGET_FPS;
        long lastTime = System.nanoTime();
        double accumulator = 0;

        while (running) {
            long now = System.nanoTime();
            accumulator += (now - lastTime) / nsPerFrame;
            lastTime = now;

            while (accumulator >= 1) {
                update();
                accumulator--;
            }
            repaint();

            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /** Per-frame update: currently just the background scroll and clouds. */
    private void update() {
        skyScroll += 1.5f;
        if (skyScroll >= HEIGHT) {
            skyScroll = 0f;
        }
        for (Cloud c : clouds) {
            c.update(HEIGHT, WIDTH);
        }
        player.update();

        spawnTimer++;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            enemies.add(EnemyFactory.createEnemy(EnemyFactory.randomRegularType(), WIDTH));
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.update();
            if (e.isOffScreen(HEIGHT) || e.isDestroyed()) {
                it.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawScrollingSky(g2);
        for (Cloud c : clouds) {
            c.draw(g2);
        }
        for (Enemy e : enemies) {
            e.draw(g2);
        }
        player.draw(g2);
    }

    /** Draws two stacked sky-gradient tiles and scrolls them to create a seamless vertical loop. */
    private void drawScrollingSky(Graphics2D g2) {
        GradientPaint sky = new GradientPaint(
                0, 0, new Color(70, 140, 220),
                0, HEIGHT, new Color(160, 210, 245));

        g2.setPaint(sky);
        g2.fillRect(0, (int) skyScroll - HEIGHT, WIDTH, HEIGHT);
        g2.fillRect(0, (int) skyScroll, WIDTH, HEIGHT);
    }
}