package skystrike;

/**
 * MoveCommand
 * -----------
 * A Command that sets one of Player's directional flags on or off.
 * "active" is true on key-down and false on key-up, so holding an
 * arrow key keeps moving and releasing it stops — without Player ever
 * touching a KeyEvent.
 */
public class MoveCommand implements Command {

    public enum Direction { LEFT, RIGHT, UP, DOWN }

    private final Player player;
    private final Direction direction;
    private final boolean active;

    public MoveCommand(Player player, Direction direction, boolean active) {
        this.player = player;
        this.direction = direction;
        this.active = active;
    }

    @Override
    public void execute() {
        switch (direction) {
            case LEFT:  player.setMovingLeft(active);  break;
            case RIGHT: player.setMovingRight(active); break;
            case UP:    player.setMovingUp(active);    break;
            case DOWN:  player.setMovingDown(active);  break;
        }
    }
}