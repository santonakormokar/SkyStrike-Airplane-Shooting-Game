
package skystrike;

import javax.swing.*;
 
/**
 * Main
 * ----
 * Application entry point. Builds the window, drops in the GamePanel,
 * and starts the game loop. Kept intentionally thin — all game logic
 * lives in GamePanel and the manager/state/etc. classes, not here.
 */
public class SkyStrike {

   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager.getInstance().reset();
 
            JFrame frame = new JFrame("SkyStrike");
            GamePanel panel = new GamePanel();
 
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
 
            panel.requestFocusInWindow();
            panel.startGame();
        });
    }
    
}
