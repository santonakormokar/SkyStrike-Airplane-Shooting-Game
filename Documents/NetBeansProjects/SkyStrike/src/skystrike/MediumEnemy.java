package skystrike;

import java.awt.Color;

/** Mid-tier enemy: 2 hits to kill, moderate speed, worth 20 coins. */
public class MediumEnemy extends Enemy {

    public MediumEnemy(float x, float y) {
        super(x, y, 42, 42, 2.0f, 2, 20, 20);
    }

    @Override
    protected Color bodyColor() {
        return new Color(240, 170, 40);
    }
}