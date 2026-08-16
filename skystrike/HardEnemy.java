package skystrike;

import java.awt.Color;

/** Toughest regular enemy: 3 hits to kill, slowest of the regulars, worth 30 coins. */
public class HardEnemy extends Enemy {

    public HardEnemy(float x, float y) {
        super(x, y, 48, 48, 1.5f, 3, 30, 30);
    }

    @Override
    protected Color bodyColor() {
        return new Color(150, 40, 40);
    }
}