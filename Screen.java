import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Screen extends JPanel {
    private static final String PHOTO_BASE = "https://www.nps.gov/common/uploads/structured_data/";

    private final MyHashMap<State, StateInfo> states = new MyHashMap<>();
    private final JTextField abbreviationField = new JTextField(4);
    private final JLabel stateInfo = new JLabel("Try AZ, CA, or NY to view state information.");
    private final JPanel pictures = new JPanel(new GridLayout(0, 2, 12, 12));

    public Screen() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        loadStarterStates();

        JButton view = new JButton("View State Information");
        view.addActionListener(event -> viewState());
        abbreviationField.addActionListener(event -> viewState());

        JButton delete = new JButton("Delete State");
        delete.addActionListener(event -> deleteState());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel abbreviationLabel = new JLabel("State abbreviation");
        abbreviationLabel.setLabelFor(abbreviationField);
        controls.add(abbreviationLabel);
        controls.add(abbreviationField);
        controls.add(view);
        controls.add(delete);

        JPanel header = new JPanel(new BorderLayout(0, 8));
        header.add(controls, BorderLayout.NORTH);
        header.add(stateInfo, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JPanel gallery = new JPanel(new BorderLayout());
        gallery.add(pictures, BorderLayout.NORTH);
        add(new JScrollPane(gallery), BorderLayout.CENTER);
        add(new JLabel("Starter data: 2020 U.S. Census. Photos: National Park Service."), BorderLayout.SOUTH);
    }

    private void loadStarterStates() {
        // Grand Canyon and Saguaro National Parks
        addStarterState("Arizona", "AZ", "Phoenix", 7151502,
                "6C7F2214-CE4D-E366-A283BE0F4E65F83B", "5CB8B2F6-01B7-9A50-73702A355E4136B8");
        // Yosemite and Joshua Tree National Parks
        addStarterState("California", "CA", "Sacramento", 39538223,
                "3C84CF74-1DD8-B71B-0B9C7FF83F7C68EB", "3C85EF02-1DD8-B71B-0B467F2B5F7483AC");
        // Statue of Liberty and Fire Island Lighthouse
        addStarterState("New York", "NY", "Albany", 20201249,
                "3C85443B-1DD8-B71B-0B4E0A1FFCCDEEC0", "36851BAB-E864-649F-D190A838BF95C5D5");
    }

    private void addStarterState(String name, String abbreviation, String capital,
            int population, String firstPhoto, String secondPhoto) {
        StateInfo info = new StateInfo(name, capital, population);
        info.getPictureUrls().add(photoUrl(firstPhoto));
        info.getPictureUrls().add(photoUrl(secondPhoto));
        states.put(new State(name, abbreviation), info);
    }

    private String photoUrl(String id) {
        return PHOTO_BASE + id + ".jpg?maxHeight=800&maxWidth=1200&quality=90";
    }

    private State readStateKey() {
        String abbreviation = abbreviationField.getText().trim().toUpperCase(Locale.ROOT);
        if (!abbreviation.matches("[A-Z]{2}")) {
            stateInfo.setText("Enter a two-letter state abbreviation.");
            return null;
        }
        return new State(abbreviation);
    }

    private void viewState() {
        clearPictures();
        State key = readStateKey();
        if (key == null) {
            return;
        }

        StateInfo info = states.get(key);
        if (info == null) {
            showMissingState(key);
            return;
        }

        stateInfo.setText(info.toString());
        DLList<String> urls = info.getPictureUrls();
        for (int i = 0; i < urls.size(); i++) {
            JLabel picture = new JLabel("Loading photo " + (i + 1) + "…", JLabel.CENTER);
            pictures.add(picture);
            loadPicture(urls.get(i), picture);
        }
        pictures.revalidate();
        pictures.repaint();
    }

    private void deleteState() {
        clearPictures();
        State key = readStateKey();
        if (key == null) {
            return;
        }

        StateInfo removed = states.remove(key);
        if (removed == null) {
            showMissingState(key);
        } else {
            stateInfo.setText("Deleted " + removed.getName() + " (" + key.getAbbreviation() + ").");
        }
    }

    private void clearPictures() {
        pictures.removeAll();
        pictures.revalidate();
        pictures.repaint();
    }

    private void showMissingState(State key) {
        stateInfo.setText("No state found for " + key.getAbbreviation() + ".");
    }

    private void loadPicture(String url, JLabel picture) {
        // Load on a background thread so the window stays responsive during downloads.
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws IOException {
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                try (InputStream input = connection.getInputStream()) {
                    BufferedImage image = ImageIO.read(input);
                    if (image == null) {
                        throw new IOException("Unsupported image format");
                    }
                    double scale = Math.min(350.0 / image.getWidth(), 260.0 / image.getHeight());
                    int width = Math.max(1, (int) (image.getWidth() * scale));
                    int height = Math.max(1, (int) (image.getHeight() * scale));
                    return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
                }
            }

            @Override
            protected void done() {
                try {
                    picture.setIcon(get());
                    picture.setText("");
                } catch (ExecutionException exception) {
                    picture.setText("Photo unavailable");
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }
}
