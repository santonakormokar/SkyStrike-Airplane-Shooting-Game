package skystrike;

import java.util.ArrayList;
import java.util.List;

public class SingleShotStrategy implements ShootStrategy {

    @Override
    public List<Bullet> shoot(float originX, float originY, int originWidth) {
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(new Bullet(originX + originWidth / 2f - 3, originY, 0f, -9f, 6, 14, 1, true));
        return bullets;
    }
}
