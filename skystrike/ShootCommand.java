package skystrike;

/**
 * ShootCommand
 * ------------
 * Tells the active PlayingState to fire the player's current
 * ShootStrategy. Bound to the space bar's key-down. PlayingState's
 * Player.tryShoot() already respects a cooldown, so key-repeat while
 * holding space doesn't cause unlimited fire rate.
 */
public class ShootCommand implements Command {

    private final PlayingState playingState;

    public ShootCommand(PlayingState playingState) {
        this.playingState = playingState;
    }

    @Override
    public void execute() {
        playingState.playerShoot();
    }
}