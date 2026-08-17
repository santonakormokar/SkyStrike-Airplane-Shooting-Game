package skystrike;

import java.util.ArrayList;
import java.util.List;
public class LaserShotStrategy implements ShootStrategy {

    @Override
    public List<Bullet> shoot(float originX, float originY, int originWidth) {
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(new Bullet(originX + originWidth / 2f - 5, originY, 0f, -14f, 10, 26, 3, true));
        return bullets;
    }
}
