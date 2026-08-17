package skystrike;

import java.util.ArrayList;
import java.util.List;

public class DoubleShotStrategy implements ShootStrategy {

    @Override
    public List<Bullet> shoot(float originX, float originY, int originWidth) {
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(new Bullet(originX + originWidth * 0.25f - 3, originY, 0f, -9f, 6, 14, 1, true));
        bullets.add(new Bullet(originX + originWidth * 0.75f - 3, originY, 0f, -9f, 6, 14, 1, true));
        return bullets;
    }
}
