/*
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Screen Class
public class Screen extends JPanel {
    private JTextArea textArea;
    private JTextField inputField;
    private MyArrayList<Song> playlist;

    public Screen() {
        this.setLayout(null);
        
        // Initialize the playlist
        playlist = new MyArrayList<Song>();
        playlist.add(new Song("Here Comes the Sun", "The Beatles", "Abbey Road"));
        playlist.add(new Song("Let It Be", "The Beatles", "Let It Be"));
        playlist.add(new Song("Come Together", "The Beatles", "Abbey Road"));
        playlist.add(new Song("Yesterday", "The Beatles", "Help!"));
        playlist.add(new Song("Twist and Shout", "The Beatles", "Please Please Me"));

        // Text Area setup
        textArea = new JTextArea();
        textArea.setBounds(10, 10, 400, 250);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.ITALIC, 14));
        this.add(textArea);

        // Input field setup
        JLabel inputLabel = new JLabel("Input:");
        inputLabel.setBounds(10, 270, 50, 30);
        this.add(inputLabel);
        inputField = new JTextField();
        inputField.setBounds(50, 270, 360, 30);
        this.add(inputField);

        // Instruction label
        JLabel instructionLabel = new JLabel("Add Format: Name, Artist, Album, Location | Remove Format: Name, Artist, Album");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        instructionLabel.setBounds(10, 300, 400, 20);
        this.add(instructionLabel);

        //-- Buttons
        // Randomize Playlist
        JButton btnRandomize = new JButton("Randomize");
        btnRandomize.setBounds(430, 10, 150, 30);
        btnRandomize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shufflePlaylist();
                updateDisplay();
            }
        });
        this.add(btnRandomize);

        // Add Song Button
        JButton btnAdd = new JButton("Add Song");
        btnAdd.setBounds(430, 50, 150, 30);
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSong();
            }
        });
        this.add(btnAdd);

        // Remove by Index Button
        JButton btnRemoveIndex = new JButton("Remove by Location");
        btnRemoveIndex.setBounds(430, 90, 150, 30);
        btnRemoveIndex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeByIndex();
            }
        });
        this.add(btnRemoveIndex);

        // Remove by Object Button
        JButton btnRemoveObj = new JButton("Remove by Info");
        btnRemoveObj.setBounds(430, 130, 150, 30);
        btnRemoveObj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeByObject();
            }
        });
        this.add(btnRemoveObj);

        // Sort Buttons
        JButton btnSortName = new JButton("Sort by Name");
        btnSortName.setBounds(600, 10, 150, 30);
        btnSortName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortPlaylist(0); // 0 corresponds to Name
                updateDisplay();
            }
        });
        this.add(btnSortName);

        JButton btnSortArtist = new JButton("Sort by Artist");
        btnSortArtist.setBounds(600, 50, 150, 30);
        btnSortArtist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortPlaylist(1); // 1 corresponds to Artist
                updateDisplay();
            }
        });
        this.add(btnSortArtist);

        JButton btnSortAlbum = new JButton("Sort by Album");
        btnSortAlbum.setBounds(600, 90, 150, 30);
        btnSortAlbum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortPlaylist(2); // 2 corresponds to Album
                updateDisplay();
            }
        });
        this.add(btnSortAlbum);

        this.setFocusable(true);
        updateDisplay();
    }

    // Sorting & Shuffling functions
    // Shuffle
    private void shufflePlaylist() {
        for (int i = 0; i < playlist.size(); i++) {
            int randomIdx = (int) (Math.random() * playlist.size());
            Song temp = playlist.get(i);
            playlist.remove(i);
            playlist.add(randomIdx, temp);
        }
    }

    // Bubble Sort
    private void sortPlaylist(int sortBy) {
        for (int i = 0; i < playlist.size() - 1; i+=1) {
            for (int j = 0; j < playlist.size() - i - 1; j+=1) {
                Song s1 = playlist.get(j);
                Song s2 = playlist.get(j + 1);
                boolean shouldSwap = false;
                
                // Compare Strings based on mode
                // > 0 if s1 is later in alphabet than s2
                if (sortBy == 0) {
                    if (s1.getName().compareToIgnoreCase(s2.getName()) > 0) shouldSwap = true;
                } else if (sortBy == 1) {
                    if (s1.getArtist().compareToIgnoreCase(s2.getArtist()) > 0) shouldSwap = true;
                } else if (sortBy == 2) {
                    if (s1.getAlbum().compareToIgnoreCase(s2.getAlbum()) > 0) shouldSwap = true;
                }
                
                // Perform song swap
                if (shouldSwap) {
                    Song temp = playlist.get(j);
                    playlist.remove(j);
                    playlist.add(j + 1, temp);
                }
            }
        }
    }

    // Refreshes JTextArea
    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playlist.size(); i++) {
            Song s = playlist.get(i);
            sb.append((i + 1)).append(". ").append(s.getName()).append(", ").append(s.getArtist()).append(", ").append(s.getAlbum()).append("\n");
        }
        textArea.setText(sb.toString());
    }

    // Adds a Song
    private void addSong() {
        String input = inputField.getText();
        String[] parts = input.split(",");
        if (parts.length == 4) {
            try {
                String name = parts[0].trim();
                String artist = parts[1].trim();
                String album = parts[2].trim();
                // Subtract 1 for 1-indexed list
                int location = Integer.parseInt(parts[3].trim()) - 1; 
                
                Song newSong = new Song(name, artist, album);
                
                // Keep location within valid bounds
                if (location < 0){
                  location = 0;
                }
                if (location > playlist.size()){
                  location = playlist.size();
                }
                
                playlist.add(location, newSong);
                updateDisplay();
                inputField.setText(""); // Clear input
            } catch (NumberFormatException ex) {
                System.out.println("Invalid location number.");
            }
        }
    }

    // Removes a song by index
    private void removeByIndex() {
        try {
            // Subtract 1 for 1-indexed list
            int location = Integer.parseInt(inputField.getText().trim()) - 1;
            if (location >= 0 && location < playlist.size()) {
                playlist.remove(location);
                updateDisplay();
                inputField.setText("");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid number to remove by location.");
        }
    }

    // Removing a song by property
    private void removeByObject() {
        String input = inputField.getText();
        String[] parts = input.split(",");
        if (parts.length == 3) {
            String name = parts[0].trim();
            String artist = parts[1].trim();
            String album = parts[2].trim();
            
            Song target = new Song(name, artist, album);
            playlist.remove(target);
            
            updateDisplay();
            inputField.setText("");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 400);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(240, 30, 230));
        g.fillRect(0, 0, 800, 400);  
    }
}

*/