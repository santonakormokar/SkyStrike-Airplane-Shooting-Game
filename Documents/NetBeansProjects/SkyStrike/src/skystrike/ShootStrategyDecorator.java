package skystrike;

import java.util.List;

/**
 * ShootStrategyDecorator (Decorator pattern)
 * --------------------------------------------
 * Wraps a ShootStrategy and, by default, just forwards to it. Concrete
 * decorators (RapidFireDecorator, DoubleDamageDecorator) override only
 * the one method they change. Because this still implements ShootStrategy
 * itself, decorators can be stacked: new RapidFireDecorator(new
 * DoubleDamageDecorator(base)) gives you both effects at once, and
 * Player never needs to know how many layers deep its strategy is.
 */
public abstract class ShootStrategyDecorator implements ShootStrategy {

    protected final ShootStrategy wrapped;

    protected ShootStrategyDecorator(ShootStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public List<Bullet> shoot(float originX, float originY, int originWidth) {
        return wrapped.shoot(originX, originY, originWidth);
    }

    @Override
    public int getCooldownFrames() {
        return wrapped.getCooldownFrames();
    }
}