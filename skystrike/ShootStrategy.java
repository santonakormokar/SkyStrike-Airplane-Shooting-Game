package skystrike;

import java.util.List;

/**
 * ShootStrategy (Strategy)
 * -------------------------
 * Defines *how firing works* as a swappable behavior, separate from Player
 * itself. Player just holds a ShootStrategy and calls shoot() on it — it
 * doesn't know or care whether that produces one bullet or five. Swapping
 * SingleShotStrategy for TripleShotStrategy at runtime (e.g. from a future
 * power-up) changes the firing pattern without touching Player at all.
 */
public interface ShootStrategy {

    /**
     * @param originX     left edge of the shooter (player or enemy)
     * @param originY     top edge of the shooter
     * @param originWidth width of the shooter, used to center bullets
     * @return the bullets fired this call
     */
    List<Bullet> shoot(float originX, float originY, int originWidth);
}