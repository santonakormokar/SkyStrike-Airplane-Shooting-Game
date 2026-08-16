package skystrike;

/** Power-up: halves the cooldown of whatever ShootStrategy it wraps, up to a minimum. */
public class RapidFireDecorator extends ShootStrategyDecorator {

    public RapidFireDecorator(ShootStrategy wrapped) {
        super(wrapped);
    }

    @Override
    public int getCooldownFrames() {
        return Math.max(2, wrapped.getCooldownFrames() / 2);
    }
}