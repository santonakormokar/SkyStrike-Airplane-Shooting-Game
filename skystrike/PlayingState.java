package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayingState implements GameState {

    private final GamePanel gamePanel;
    private final Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private int spawnTimer = 0;
    private static final int SPAWN_INTERVAL = 90;

    public PlayingState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        GameManager.getInstance().reset();
        player = new Player(GamePanel.WIDTH / 2f - 24, GamePanel.HEIGHT - 100, GamePanel.WIDTH, GamePanel.HEIGHT);
    }

    @Override
    public void onEnter() { }

    /** Called by ShootCommand. */
    public void playerShoot() {
        bullets.addAll(player.tryShoot());
    }

    @Override
    public void update() {
        player.update();

        spawnTimer++;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            enemies.add(EnemyFactory.createEnemy(EnemyFactory.randomRegularType(), GamePanel.WIDTH));
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.update();
            if (e.isOffScreen(GamePanel.HEIGHT) || e.isDestroyed()) {
                it.remove();
            }
        }

        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();
            b.update();
            if (b.isOffScreen(GamePanel.WIDTH, GamePanel.HEIGHT)) {
                bit.remove();
            }
        }
        if (GameManager.getInstance().isPlayerDead()) {
            gamePanel.setState(new GameOverState(gamePanel));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        for (Enemy e : enemies) {
            e.draw(g);
        }
        for (Bullet b : bullets) {
            b.draw(g);
        }
        player.draw(g);
        drawPauseButton(g);
    }

    /** Draws a small pause icon (two bars) in the top-right corner. Click it to pause. */
    private void drawPauseButton(Graphics2D g) {
        Rectangle b = GamePanel.PAUSE_BUTTON_BOUNDS;
        g.setColor(new Color(0, 0, 0, 130));
        g.fillRoundRect(b.x, b.y, b.width, b.height, 8, 8);

        g.setColor(Color.WHITE);
        int barWidth = 5;
        int barHeight = 16;
        int gap = 6;
        int startX = b.x + (b.width - (2 * barWidth + gap)) / 2;
        int barY = b.y + (b.height - barHeight) / 2;
        g.fillRect(startX, barY, barWidth, barHeight);
        g.fillRect(startX + barWidth + gap, barY, barWidth, barHeight);
    }

    @Override
    public void handleKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:  new MoveCommand(player, MoveCommand.Direction.LEFT, true).execute();  break;
            case KeyEvent.VK_RIGHT: new MoveCommand(player, MoveCommand.Direction.RIGHT, true).execute(); break;
            case KeyEvent.VK_UP:    new MoveCommand(player, MoveCommand.Direction.UP, true).execute();    break;
            case KeyEvent.VK_DOWN:  new MoveCommand(player, MoveCommand.Direction.DOWN, true).execute();  break;
            case KeyEvent.VK_SPACE: new ShootCommand(this).execute(); break;
            default:
                break;
        }
    }

    @Override
    public void handleKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:  new MoveCommand(player, MoveCommand.Direction.LEFT, false).execute();  break;
            case KeyEvent.VK_RIGHT: new MoveCommand(player, MoveCommand.Direction.RIGHT, false).execute(); break;
            case KeyEvent.VK_UP:    new MoveCommand(player, MoveCommand.Direction.UP, false).execute();    break;
            case KeyEvent.VK_DOWN:  new MoveCommand(player, MoveCommand.Direction.DOWN, false).execute();  break;
            default:
                break;
        }
    }

    @Override
    public void handleMouseClick(int x, int y) {
        if (GamePanel.PAUSE_BUTTON_BOUNDS.contains(x, y)) {
            gamePanel.setState(new PauseState(gamePanel, this));
        }
    }
}
