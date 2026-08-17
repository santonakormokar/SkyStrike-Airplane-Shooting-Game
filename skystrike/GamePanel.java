package skystrike;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GamePanel
 * ---------
 * The rendering + update surface for SkyStrike. As of Step 6, GamePanel
 * itself no longer knows what "the game" is doing — it just owns the
 * always-on background (sky + clouds), runs the fixed-timestep loop, and
 * forwards update()/draw() to whichever GameState is current. Switching
 * screens (menu -> playing -> pause -> game over) is entirely the State
 * pattern's job now; GamePanel has no if/else for "which screen am I on".
 */
public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 720;
    private static final int TARGET_FPS = 60;

    private Thread gameThread;
    private volatile boolean running = false;

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