package skystrike;

/**
 * PauseCommand
 * ------------
 * Toggles GameManager's paused flag. Bound to the P key. KeyInputHandler
 * guards against key-repeat so holding P doesn't rapidly flip pause on
 * and off — this Command only ever runs once per actual press.
 */
public class PauseCommand implements Command {

    @Override
    public void execute() {
        GameManager.getInstance().togglePause();
    }
}