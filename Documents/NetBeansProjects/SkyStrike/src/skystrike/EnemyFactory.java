package skystrike;

import java.util.Random;

/**
 * EnemyFactory (Factory Method)
 * ------------------------------
 * Centralizes "how to build an enemy" in one place. Callers (GamePanel's
 * spawn logic) ask for an EnemyType and get back a ready-to-use Enemy —
 * they never call `new EasyEnemy(...)` etc. directly, so adding a new
 * enemy type later only means adding a case here, not hunting through
 * spawn code scattered across the game.
 */
public class EnemyFactory {

    public enum EnemyType { EASY, MEDIUM, HARD, BOSS }

    private static final Random RANDOM = new Random();

    private EnemyFactory() { } // pure static factory — never instantiated

    public static Enemy createEnemy(EnemyType type, int panelWidth) {
        switch (type) {
            case EASY: {
                int w = 36;
                float x = RANDOM.nextInt(Math.max(1, panelWidth - w));
                return new EasyEnemy(x, -w);
            }
            case MEDIUM: {
                int w = 42;
                float x = RANDOM.nextInt(Math.max(1, panelWidth - w));
                return new MediumEnemy(x, -w);
            }
            case HARD: {
                int w = 48;
                float x = RANDOM.nextInt(Math.max(1, panelWidth - w));
                return new HardEnemy(x, -w);
            }
            case BOSS: {
                int w = 100;
                float x = (panelWidth - w) / 2f;
                return new BossEnemy(x, -w, panelWidth);
            }
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    /** Convenience: picks a random regular (non-boss) type, weighted toward easier enemies. */
    public static EnemyType randomRegularType() {
        int roll = RANDOM.nextInt(100);
        if (roll < 55) return EnemyType.EASY;
        if (roll < 85) return EnemyType.MEDIUM;
        return EnemyType.HARD;
    }
} 