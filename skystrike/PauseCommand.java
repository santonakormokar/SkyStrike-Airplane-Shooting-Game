package skystrike;

public class PauseCommand implements Command {

    @Override
    public void execute() {
        GameManager.getInstance().togglePause();
    }
}
