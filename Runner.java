import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Runner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("State HashMap – Evan & Angela");
            frame.setSize(850, 500);
            frame.setMinimumSize(new Dimension(750, 390));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Screen());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
