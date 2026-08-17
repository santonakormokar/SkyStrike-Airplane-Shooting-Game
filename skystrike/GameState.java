package skystrike;

import java.awt.Graphics2D;

public interface GameState {

    void onEnter();
    void update();

    void draw(Graphics2D g);

    void handleKeyPressed(int keyCode);

    void handleKeyReleased(int keyCode);

    default void handleMouseClick(int x, int y) { }
}
