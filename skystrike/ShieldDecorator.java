package skystrike;

/**
 * ShieldDecorator (Decorator pattern)
 * --------------------------------------
 * Wraps a Damageable (normally the player) and absorbs a fixed number of
 * hits before any damage reaches the real target. This decorates a
 * different interface than RapidFire/DoubleDamage (Damageable instead of
 * ShootStrategy), showing the same pattern applies wherever you want to
 * add behavior to an object without touching that object's own class.
 */
public class ShieldDecorator implements Damageable {

    private final Damageable wrapped;
    private int remainingHits;

    public ShieldDecorator(Damageable wrapped, int hitsToAbsorb) {
        this.wrapped = wrapped;
        this.remainingHits = hitsToAbsorb;
    }

    @Override
    public void takeDamage(int amount) {
        if (remainingHits > 0) {
            remainingHits--;
            return; // absorbed — nothing reaches the wrapped target
        }
        wrapped.takeDamage(amount);
    }

    @Override
    public boolean isDestroyed() {
        return wrapped.isDestroyed();
    }

    public boolean isActive() { return remainingHits > 0; }
    public int getRemainingHits() { return remainingHits; }
}