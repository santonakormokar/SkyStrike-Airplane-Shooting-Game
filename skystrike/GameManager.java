package skystrike;

/**
 * GameManager (Singleton)
 * -----------------------
 * Single, globally accessible source of truth for game-wide state:
 * score, coins, health, and the currently selected difficulty.
 * Any class in the project reaches it via GameManager.getInstance()
 * instead of passing state objects around by hand.
 */
public class GameManager {

    private static GameManager instance;

    public enum Difficulty { EASY, MEDIUM, HARD }

    private int score;
    private int coins;
    private int health;
    private final int maxHealth = 3;
    private Difficulty difficulty;
    private boolean paused;

    // Private constructor: only getInstance() may create the one instance.
    private GameManager() {
        reset();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /** Resets all run state back to defaults for a new game. */
    public void reset() {
        score = 0;
        coins = 0;
        health = maxHealth;
        difficulty = Difficulty.EASY;
        paused = false;
    }

    public void addScore(int amount) { score += amount; }
    public int getScore() { return score; }

    public void addCoins(int amount) { coins += amount; }
    public int getCoins() { return coins; }

    public void damagePlayer(int amount) {
        health = Math.max(0, health - amount);
    }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isPlayerDead() { return health <= 0; }

    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public Difficulty getDifficulty() { return difficulty; }

    public void togglePause() { paused = !paused; }
    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }
}