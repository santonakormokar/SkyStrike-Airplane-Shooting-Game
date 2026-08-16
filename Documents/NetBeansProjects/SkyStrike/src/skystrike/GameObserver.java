package skystrike;

/**
 * GameObserver (Observer pattern)
 * ---------------------------------
 * Anything that wants to react whenever GameManager's stats change
 * (health, score, coins, difficulty) implements this and registers
 * itself with GameManager.addObserver(). GameManager doesn't know or
 * care what the observer does with the update — draw a HUD, log it,
 * play a sound — it just calls onStatsChanged() on everyone registered.
 */
public interface GameObserver {
    void onStatsChanged(int health, int maxHealth, int score, int coins, GameManager.Difficulty difficulty);
}