package skystrike;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PlayingState
 * ------------
 * The actual gameplay. Owns the player, enemy list, bullet list, and
 * (as of Step 9) collision detection: bullets hitting enemies award
 * score/coins and spawn an explosion; enemies touching the player deal
 * damage through playerDamageHandler (so a Shield, if active, can
 * absorb it) and are destroyed on contact.
 */
public class PlayingState implements GameState {

    private final GamePanel gamePanel;
    private final Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final List<Heart> hearts = new ArrayList<>();
    private int spawnTimer = 0;
    private int coinSpawnTimer = 0;
    private int heartSpawnTimer = 0;
    private static final int COIN_SPAWN_INTERVAL = 100;
    private static final int HEART_SPAWN_INTERVAL = 500;
    private static final int BOSS_SCORE_THRESHOLD = 500;
    private boolean bossSpawned = false;
    private final HUD hud = new HUD();

    /**
     * Level progression: starts at EASY, and automatically advances to
     * MEDIUM then HARD as the player's score crosses fixed thresholds.
     * Advancing a level both spawns enemies more often (shorter interval)
     * and makes each newly spawned enemy faster (multiplySpeed) — existing
     * enemies already on screen keep their current speed, only new ones
     * spawn harder, so the ramp-up feels gradual rather than jarring.
     */
    private GameManager.Difficulty currentLevel = GameManager.Difficulty.EASY;
    private static final int MEDIUM_SCORE_THRESHOLD = 100;
    private static final int HARD_SCORE_THRESHOLD = 300;

    private final Damageable playerHealthAdapter = new PlayerHealthAdapter();

    /**
     * What collision code actually calls takeDamage() on. Normally this is
     * just playerHealthAdapter (which forwards to GameManager); activateShield()
     * swaps it for a ShieldDecorator wrapping that same adapter, so hits get
     * absorbed without collision code needing to know a shield is active.
     */
    private Damageable playerDamageHandler;

    public PlayingState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        GameManager.getInstance().reset();
        player = new Player(GamePanel.WIDTH / 2f - 24, GamePanel.HEIGHT - 100, GamePanel.WIDTH, GamePanel.HEIGHT);
        playerDamageHandler = playerHealthAdapter;
        GameManager.getInstance().addObserver(hud);
    }

    /**
     * Applies the Decorator pattern from Step 8. These three methods aren't
     * called by anything right now (no power-up pickups exist in the world),
     * but they're kept here, fully working, since Shield/RapidFire/DoubleDamage
     * are required patterns for the project — call them directly if you want
     * to demonstrate or test the effect.
     */
    public void activateShield(int hits) {
        playerDamageHandler = new ShieldDecorator(playerHealthAdapter, hits);
    }

    public void activateRapidFire() {
        player.setShootStrategy(new RapidFireDecorator(player.getShootStrategy()));
    }

    public void activateDoubleDamage() {
        player.setShootStrategy(new DoubleDamageDecorator(player.getShootStrategy()));
    }

    /** Routes all player damage through here (both enemy-ramming and enemy-bullet hits use this). */
    private void damagePlayer(int amount, float atX, float atY) {
        playerDamageHandler.takeDamage(amount);
        SoundManager.getInstance().play("explosion");
        explosions.add(new Explosion(atX, atY));
    }

    /** Checks score against thresholds and advances the level (never regresses) when crossed. */
    private void updateLevel() {
        int score = GameManager.getInstance().getScore();
        GameManager.Difficulty target;
        if (score >= HARD_SCORE_THRESHOLD) {
            target = GameManager.Difficulty.HARD;
        } else if (score >= MEDIUM_SCORE_THRESHOLD) {
            target = GameManager.Difficulty.MEDIUM;
        } else {
            target = GameManager.Difficulty.EASY;
        }
        if (target != currentLevel) {
            currentLevel = target;
            GameManager.getInstance().setDifficulty(currentLevel); // notifies HUD via Observer
        }
    }

    private int currentSpawnInterval() {
        switch (currentLevel) {
            case HARD:   return 45;
            case MEDIUM: return 65;
            default:     return 90;
        }
    }

    private float currentEnemySpeedMultiplier() {
        switch (currentLevel) {
            case HARD:   return 1.6f;
            case MEDIUM: return 1.3f;
            default:     return 1.0f;
        }
    }

    @Override
    public void onEnter() { }

    /** Called by ShootCommand. */
    public void playerShoot() {
        List<Bullet> newBullets = player.tryShoot();
        if (!newBullets.isEmpty()) {
            SoundManager.getInstance().play("shoot");
        }
        bullets.addAll(newBullets);
    }

    @Override
    public void update() {
        player.update();
        updateLevel();

        spawnTimer++;
        if (!bossSpawned && spawnTimer >= currentSpawnInterval()) {
            spawnTimer = 0;
            Enemy enemy = EnemyFactory.createEnemy(EnemyFactory.randomRegularType(), GamePanel.WIDTH);
            enemy.multiplySpeed(currentEnemySpeedMultiplier());
            enemies.add(enemy);
        }

        if (!bossSpawned && GameManager.getInstance().getScore() >= BOSS_SCORE_THRESHOLD) {
            bossSpawned = true;
            enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, GamePanel.WIDTH));
        }

        coinSpawnTimer++;
        if (coinSpawnTimer >= COIN_SPAWN_INTERVAL) {
            coinSpawnTimer = 0;
            float x = (float) (Math.random() * (GamePanel.WIDTH - 20));
            coins.add(new Coin(x, -20, 2.5f));
        }

        heartSpawnTimer++;
        if (heartSpawnTimer >= HEART_SPAWN_INTERVAL) {
            heartSpawnTimer = 0;
            if (GameManager.getInstance().getHealth() < GameManager.getInstance().getMaxHealth()) {
                float x = (float) (Math.random() * (GamePanel.WIDTH - 20));
                hearts.add(new Heart(x, -20, 2.3f));
            }
        }

        for (Enemy e : enemies) {
            e.update();
            Bullet enemyBullet = e.tryShoot();
            if (enemyBullet != null) {
                bullets.add(enemyBullet);
            }
        }
        for (Bullet b : bullets) {
            b.update();
        }
        for (Explosion ex : explosions) {
            ex.update();
        }
        for (Coin c : coins) {
            c.update();
        }
        for (Heart h : hearts) {
            h.update();
        }

        handleCollisions();

        enemies.removeIf(e -> e.isOffScreen(GamePanel.HEIGHT) || e.isDestroyed());
        bullets.removeIf(b -> b.isOffScreen(GamePanel.WIDTH, GamePanel.HEIGHT));
        explosions.removeIf(Explosion::isFinished);
        coins.removeIf(c -> c.isOffScreen(GamePanel.HEIGHT));
        hearts.removeIf(h -> h.isOffScreen(GamePanel.HEIGHT));

        if (GameManager.getInstance().isPlayerDead()) {
            SoundManager.getInstance().play("gameover");
            gamePanel.setState(new GameOverState(gamePanel));
        }
    }

    /**
     * Player bullets vs enemies (score/explosion, boss death -> victory),
     * enemy bullets vs player (damage), enemies ramming the player (damage),
     * and player vs coins (collect).
     */
    private void handleCollisions() {
        for (Bullet b : bullets) {
            if (!b.isFromPlayer() || b.isConsumed()) {
                continue;
            }
            for (Enemy e : enemies) {
                if (e.isDestroyed()) {
                    continue;
                }
                if (b.getBounds().intersects(e.getBounds())) {
                    e.takeDamage(b.getDamage());
                    b.consume();
                    if (e.isDestroyed()) {
                        onEnemyDestroyed(e);
                    }
                    break;
                }
            }
        }

        for (Bullet b : bullets) {
            if (b.isFromPlayer() || b.isConsumed()) {
                continue;
            }
            if (b.getBounds().intersects(player.getBounds())) {
                damagePlayer(b.getDamage(), player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f);
                b.consume();
            }
        }
        bullets.removeIf(Bullet::isConsumed);

        for (Enemy e : enemies) {
            if (!e.isDestroyed() && e.getBounds().intersects(player.getBounds())) {
                damagePlayer(1, e.getX() + e.getWidth() / 2f, e.getY() + e.getHeight() / 2f);
                e.takeDamage(e.getMaxHealth()); // enemy is destroyed by the collision too
            }
        }

        for (Coin c : coins) {
            if (c.getBounds().intersects(player.getBounds())) {
                GameManager.getInstance().addCoins(c.getValue());
                SoundManager.getInstance().play("coin");
                c.collect();
            }
        }
        coins.removeIf(Coin::isCollected);

        for (Heart h : hearts) {
            if (h.getBounds().intersects(player.getBounds())) {
                GameManager.getInstance().healPlayer(1);
                SoundManager.getInstance().play("heal");
                h.collect();
            }
        }
        hearts.removeIf(Heart::isCollected);
    }

    private void onEnemyDestroyed(Enemy e) {
        GameManager.getInstance().addScore(e.getScoreValue());
        SoundManager.getInstance().play("explosion");
        explosions.add(new Explosion(e.getX() + e.getWidth() / 2f, e.getY() + e.getHeight() / 2f));
        if (e instanceof BossEnemy) {
            SoundManager.getInstance().play("victory");
            gamePanel.setState(new VictoryState(gamePanel));
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
        for (Explosion ex : explosions) {
            ex.draw(g);
        }
        for (Coin c : coins) {
            c.draw(g);
        }
        for (Heart h : hearts) {
            h.draw(g);
        }
        player.draw(g);
        hud.draw(g);
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