import javax.swing.JFrame;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("State HashMap – Evan & Angela");
        frame.setSize(1280, 720); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the Screen panel with absolute positioning
        Screen screen = new Screen();
        screen.setBounds(0, 0, 1280, 720);
        frame.add(screen);
        
        frame.setVisible(true);
    }
}