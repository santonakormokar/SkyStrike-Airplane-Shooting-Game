package skystrike;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GamePanel
 * ---------
 * The rendering + update surface for SkyStrike. Owns the always-on
 * background (sky + clouds) and forwards update()/draw() to whichever
 * GameState is current.
 *
 * The game loop uses a javax.swing.Timer instead of a separate Thread.
 * Earlier versions ran update() on a background thread while Swing
 * painted on the Event Dispatch Thread (EDT) — once Step 9 started
 * adding/removing several list items per frame (bullets, enemies,
 * explosions), that became a real ConcurrentModificationException risk:
 * the EDT could be mid-iteration in draw() while the other thread mutated
 * the same list. A Swing Timer fires its callback ON the EDT, so update()
 * and paintComponent() now always run on the same thread, one at a time —
 * this entire bug class is gone by construction, not by careful locking.
 */
public class GamePanel extends JPanel {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 720;
    private static final int TARGET_FPS = 60;

    private Timer gameTimer;

    private final List<Cloud> clouds = new ArrayList<>();
    private float skyScroll = 0f;

    private GameState currentState;

    /** Shared clickable region for the pause/resume icon, top-right corner. */
    public static final Rectangle PAUSE_BUTTON_BOUNDS = new Rectangle(WIDTH - 44, 10, 34, 34);

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setBackground(Color.BLACK);
        spawnClouds();
        addKeyListener(new KeyInputHandler(this));
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                currentState.handleMouseClick(e.getX(), e.getY());
            }
        });
        currentState = new MenuState(this);
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

    /** Switches the active screen/state. Called by states themselves (e.g. MenuState on ENTER). */
    public void setState(GameState newState) {
        currentState = newState;
        currentState.onEnter();
    }

    public GameState getCurrentState() {
        return currentState;
    }

    /** Starts the game loop. Safe to call once. */
    public void startGame() {
        if (gameTimer == null) {
            gameTimer = new Timer(1000 / TARGET_FPS, e -> {
                update();
                repaint();
            });
            gameTimer.start();
        }
    }

    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    /** Background always animates, regardless of which state is active; the state handles its own logic. */
    private void update() {
        skyScroll += 1.5f;
        if (skyScroll >= HEIGHT) {
            skyScroll = 0f;
        }
        for (Cloud c : clouds) {
            c.update(HEIGHT, WIDTH);
        }
        currentState.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawScrollingSky(g2);
        for (Cloud c : clouds) {
            c.draw(g2);
        }
        currentState.draw(g2);
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