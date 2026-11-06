import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Screen extends JPanel {
    private static final String PHOTO_BASE = "https://www.nps.gov/common/uploads/structured_data/";

    private final MyHashMap<State, StateInfo> states = new MyHashMap<>();
    private final JTextField abbreviationField = new JTextField(4);
    private final JTextField urlField = new JTextField(20);
    private final JTextField nameField = new JTextField(10);
    private final JTextField capitalField = new JTextField(10);
    private final JTextField populationField = new JTextField(8);
    private final JLabel stateInfo = new JLabel("Try AZ, CA, or NY to view state information.");
    private final CardLayout views = new CardLayout();
    private final JPanel results = new JPanel(views);
    private final JTextArea stateList = new JTextArea();
    private final JPanel pictures = new JPanel(new GridLayout(0, 2, 12, 12));
    private final DLList<SwingWorker<ImageIcon, Void>> photoLoads = new DLList<>();

    public Screen() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        loadStarterStates();

        JButton view = new JButton("View State Information");
        view.addActionListener(event -> viewState());
        abbreviationField.addActionListener(event -> viewState());

        JButton delete = new JButton("Delete State");
        delete.addActionListener(event -> deleteState());

        JButton viewAll = new JButton("View All States");
        viewAll.addActionListener(event -> viewAllStates());

        JButton addPic = new JButton("Add Picture");
        addPic.addActionListener(event -> addPicture());

        JButton addState = new JButton("Add State");
        addState.addActionListener(event -> addState());

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addField(row1, "State abbreviation (all actions)", abbreviationField);
        row1.add(view);
        row1.add(delete);
        row1.add(viewAll);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addField(row2, "State name", nameField);
        addField(row2, "Capital", capitalField);
        addField(row2, "Population", populationField);
        row2.add(addState);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addField(row3, "Picture URL", urlField);
        row3.add(addPic);

        JPanel controls = new JPanel(new GridLayout(3, 1));
        controls.add(row1);
        controls.add(row2);
        controls.add(row3);

        JPanel header = new JPanel(new BorderLayout(0, 8));
        header.add(controls, BorderLayout.NORTH);
        header.add(stateInfo, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JPanel gallery = new JPanel(new BorderLayout());
        gallery.add(pictures, BorderLayout.NORTH);
        results.add(new JScrollPane(gallery), "photos");

        stateList.setEditable(false);
        stateList.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        results.add(new JScrollPane(stateList), "states");
        add(results, BorderLayout.CENTER);
        add(new JLabel("Starter populations from 2020 U.S. Census. Starter photos from National Park Service."),
                BorderLayout.SOUTH);
    }

    private void addField(JPanel row, String title, JTextField field) {
        JLabel label = new JLabel(title);
        label.setLabelFor(field);
        row.add(label);
        row.add(field);
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
        try {
            return new State(abbreviationField.getText());
        } catch (IllegalArgumentException exception) {
            stateInfo.setText("Enter a two-letter state abbreviation.");
            return null;
        }
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

        String details = info.toString();
        if (info.getPictureUrls().size() == 0) {
            details += " | No photos yet";
        }
        showState(info, details);
    }

    private void showState(StateInfo info, String message) {
        stateInfo.setText(message);
        views.show(results, "photos");

        DLList<String> urls = info.getPictureUrls();
        for (int i = 0; i < urls.size(); i+=1) {
            JLabel picture = new JLabel("Loading photo " + (i + 1) + "…", JLabel.CENTER);
            picture.setPreferredSize(new Dimension(350, 260));
            picture.setToolTipText(urls.get(i));
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
        while (photoLoads.size() > 0) {
            photoLoads.remove(photoLoads.size() - 1).cancel(true);
        }
        pictures.removeAll();
        views.show(results, "photos");
        pictures.revalidate();
        pictures.repaint();
    }

    private void showMissingState(State key) {
        stateInfo.setText("No state found for " + key.getAbbreviation() + ".");
    }

    private void loadPicture(String url, JLabel picture) {
        // Load on a background thread so the window stays responsive during download
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws IOException {
                URLConnection connection = URI.create(url).toURL().openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                try (InputStream input = connection.getInputStream()) {
                    BufferedImage image = ImageIO.read(input);
                    if (image == null) {
                        throw new IOException("Unsupported image format");
                    }
                    double scale = Math.min(1.0, Math.min(350.0 / image.getWidth(), 260.0 / image.getHeight()));
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
                } catch (CancellationException exception) {
                    // Another action has already cleared the photo
                } catch (ExecutionException exception) {
                    picture.setText("Photo unavailable");
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        photoLoads.add(worker);
        worker.execute();
    }

    private void viewAllStates() {
        clearPictures();
        DLList<State> keys = states.keySet().toDLList();
        StringBuilder listing = new StringBuilder();
        for (int i = 0; i < keys.size(); i+=1) {
            listing.append(keys.get(i)).append('\n');
        }

        stateList.setText(listing.toString());
        stateList.setCaretPosition(0);
        views.show(results, "states");
        stateInfo.setText(keys.size() == 0 ? "No states currently exist." : "All states (" + keys.size() + ")");
    }

    private void addPicture() {
        State key = readStateKey();
        if (key == null) {
            return;
        }

        StateInfo info = states.get(key);
        if (info == null) {
            showMissingState(key);
            return;
        }

        String url = urlField.getText().trim();
        if (!isPictureUrl(url)) {
            stateInfo.setText("Enter a complete http or https picture URL.");
            return;
        }

        info.getPictureUrls().add(url);
        urlField.setText("");
        clearPictures();
        showState(info, "Added picture to " + key.getAbbreviation() + ". " + info);
    }

    private boolean isPictureUrl(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost() != null
                    && ("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme()));
        } catch (URISyntaxException exception) {
            return false;
        }
    }

    private void addState() {
        State key = readStateKey();
        if (key == null) {
            return;
        }

        String name = nameField.getText().trim();
        String capital = capitalField.getText().trim();
        if (name.isEmpty() || capital.isEmpty()) {
            stateInfo.setText("Enter a state name and capital.");
            return;
        }

        int population;
        try {
            population = Integer.parseInt(populationField.getText().trim());
        } catch (NumberFormatException exception) {
            stateInfo.setText("Enter a nonnegative whole number for population.");
            return;
        }
        if (population < 0) {
            stateInfo.setText("Enter a nonnegative whole number for population.");
            return;
        }

        State newState = new State(name, key.getAbbreviation());
        StateInfo newInfo = new StateInfo(name, capital, population);

        // Replace both the key shown in the list and the value used for state details
        boolean replaced = states.remove(newState) != null;
        states.put(newState, newInfo);

        nameField.setText("");
        capitalField.setText("");
        populationField.setText("");
        clearPictures();
        showState(newInfo, (replaced ? "Replaced " : "Added ") + newState + ".");
    }
}
