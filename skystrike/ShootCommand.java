package skystrike;

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
