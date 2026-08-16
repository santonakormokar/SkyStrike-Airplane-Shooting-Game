package skystrike;

/**
 * Damageable
 * ----------
 * Anything that can take damage and be destroyed. Aircraft already has
 * matching takeDamage()/isDestroyed() methods, so it implements this for
 * free — the point of pulling this out as an interface is so ShieldDecorator
 * (Step 8) can wrap a Damageable and intercept damage before it reaches
 * the real target, without needing to extend Aircraft or Player directly.
 */
public interface Damageable {
    void takeDamage(int amount);
    boolean isDestroyed();
}