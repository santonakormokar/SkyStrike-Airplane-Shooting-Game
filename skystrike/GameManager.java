package skystrike;

import java.util.ArrayList;
import java.util.List;

/**
 * GameManager (Singleton + Observer subject)
 * --------------------------------------------
 * Single, globally accessible source of truth for game-wide state:
 * score, coins, health, and the currently selected difficulty.
 *
 * As of Step 7, it's also the Subject half of the Observer pattern: any
 * GameObserver (like HUD) can register with addObserver() and gets
 * called automatically whenever score/coins/health/difficulty change.
 * GameManager doesn't import or know about HUD at all — it just holds
 * a list of GameObserver, so any future observer (an achievements
 * tracker, a debug logger) can plug in the same way.
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

    private final List<GameObserver> observers = new ArrayList<>();

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

    /** Resets all run state back to defaults for a new game, and drops any observers from a previous run. */
    public void reset() {
        score = 0;
        coins = 0;
        health = maxHealth;
        difficulty = Difficulty.EASY;
        paused = false;
        observers.clear();
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
        notifyObservers(); // sync the new observer with current values immediately
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (GameObserver o : new ArrayList<>(observers)) {
            o.onStatsChanged(health, maxHealth, score, coins, difficulty);
        }
    }

    public void addScore(int amount) {
        score += amount;
        notifyObservers();
    }
    public int getScore() { return score; }

    public void addCoins(int amount) {
        coins += amount;
        notifyObservers();
    }
    public int getCoins() { return coins; }

    public void damagePlayer(int amount) {
        health = Math.max(0, health - amount);
        notifyObservers();
    }

    /** Heals up to maxHealth. Used by PlayingState's passive regeneration (1 heart back after ~35s undamaged). */
    public void healPlayer(int amount) {
        health = Math.min(maxHealth, health + amount);
        notifyObservers();
    }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isPlayerDead() { return health <= 0; }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        notifyObservers();
    }
    public Difficulty getDifficulty() { return difficulty; }

    public void togglePause() { paused = !paused; }
    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }
}