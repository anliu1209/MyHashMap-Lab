import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Runner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("State HashMap – Partner 1");
            frame.setSize(820, 470);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Screen());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
