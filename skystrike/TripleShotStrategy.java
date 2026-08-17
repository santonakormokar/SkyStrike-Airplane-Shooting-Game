package skystrike;

import java.util.ArrayList;
import java.util.List;

public class TripleShotStrategy implements ShootStrategy {

    @Override
    public List<Bullet> shoot(float originX, float originY, int originWidth) {
        List<Bullet> bullets = new ArrayList<>();
        float centerX = originX + originWidth / 2f - 3;
        bullets.add(new Bullet(centerX, originY, 0f, -9f, 6, 14, 1, true));
        bullets.add(new Bullet(centerX, originY, -2.5f, -8.5f, 6, 14, 1, true));
        bullets.add(new Bullet(centerX, originY, 2.5f, -8.5f, 6, 14, 1, true));
        return bullets;
    }
}
