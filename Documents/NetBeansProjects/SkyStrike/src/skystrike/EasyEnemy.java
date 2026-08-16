package skystrike;

import java.awt.Color;

/** Weakest enemy: 1 hit to kill, fastest descent, worth 10 coins. */
public class EasyEnemy extends Enemy {

    public EasyEnemy(float x, float y) {
        super(x, y, 36, 36, 2.6f, 1, 10, 10);
    }

    @Override
    protected Color bodyColor() {
        return new Color(70, 200, 90);
    }
}