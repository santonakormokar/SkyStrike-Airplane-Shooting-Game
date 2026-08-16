package skystrike;

/**
 * Command (Command pattern)
 * ---------------------------
 * Wraps a single request ("start moving left", "fire", "toggle pause")
 * as an object with one execute() method. KeyInputHandler turns raw
 * KeyEvents into these objects instead of calling Player/GamePanel
 * methods directly — so swapping input schemes (WASD, a gamepad, a
 * replay file) only means producing different Commands, nothing that
 * receives a command needs to change.
 */
public interface Command {
    void execute();
}