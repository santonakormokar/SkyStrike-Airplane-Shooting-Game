package skystrike;

import java.awt.Color;

public class HardEnemy extends Enemy {

    public HardEnemy(float x, float y) {
        super(x, y, 48, 48, 1.5f, 3, 30, 30);
    }

    @Override
    protected Color bodyColor() {
        return new Color(150, 40, 40);
    }
}
