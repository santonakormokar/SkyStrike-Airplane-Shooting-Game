package skystrike;

import java.util.ArrayList;
import java.util.List;

/** Power-up: doubles the damage of every bullet produced by whatever ShootStrategy it wraps. */
public class DoubleDamageDecorator extends ShootStrategyDecorator {

    public DoubleDamageDecorator(ShootStrategy wrapped) {
        super(wrapped);
    }

    @Override
    public List<Bullet> shoot(float originX, float originY, int originWidth) {
        List<Bullet> original = wrapped.shoot(originX, originY, originWidth);
        List<Bullet> doubled = new ArrayList<>();
        for (Bullet b : original) {
            doubled.add(b.withDamage(b.getDamage() * 2));
        }
        return doubled;
    }
}