package skystrike;

import java.util.List;

public interface ShootStrategy {
    List<Bullet> shoot(float originX, float originY, int originWidth);
}
