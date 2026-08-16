package skystrike;

/**
 * PlayerHealthAdapter
 * --------------------
 * Aircraft (and therefore Player) has its own private health field to
 * satisfy Damageable — but the HUD (Observer, Step 7) and GameOverState
 * only ever look at GameManager's health, not Player's. If collision
 * code called player.takeDamage() directly, the player's own health
 * would drop while GameManager's (and therefore the HUD hearts) stayed
 * frozen. This adapter makes Damageable calls go where the rest of the
 * game actually looks: GameManager. ShieldDecorator wraps *this*
 * instead of wrapping Player directly.
 */
public class PlayerHealthAdapter implements Damageable {

    @Override
    public void takeDamage(int amount) {
        GameManager.getInstance().damagePlayer(amount);
    }

    @Override
    public boolean isDestroyed() {
        return GameManager.getInstance().isPlayerDead();
    }
}