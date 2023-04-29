package org.example;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GUI {
    private static final Logger LOGGER = Logger.getLogger(GUI.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("GUI.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error setting up log file", e);
        }
    }

    public String[] getLogin() {
        String email = "";
        String password = "";

        File loginFile = new File("login.txt");
        if (loginFile.exists()) {
            try {
                String[] lines = Files.readAllLines(loginFile.toPath()).toArray(new String[0]);
                email = lines[0];
                password = lines[1];
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to use saved login credentials?", "Login credentials", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    return new String[]{email, password};
                }
            } catch (IOException e) {
                LOGGER.warning("Failed to read login credentials from file");
                e.printStackTrace();
            }
        }

        int choice = JOptionPane.showOptionDialog(null, "Read login credentials from:", "Login credentials", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"File", "Enter here", "Cancel"}, "File");
        if (choice == 0) {
            // Read login credentials from file
            String[] credentials = readCredentialsFromFile();
            if (credentials != null) {
                email = credentials[0];
                password = credentials[1];
            }
        } else if (choice == 1) {
            // Read login credentials from Swing
            LOGGER.info("Reading login credentials from Swing input");
            email = JOptionPane.showInputDialog(null, "Email:");
            password = JOptionPane.showInputDialog(null, "Password:");
        } else {
            // Cancel was clicked
            LOGGER.info("Login canceled by user");
            System.exit(0);
        }

        int saveChoice = JOptionPane.showConfirmDialog(null, "Do you want to save login credentials?", "Login credentials", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (saveChoice == JOptionPane.YES_OPTION) {
            try {
                LOGGER.info("Saving login credentials to file");
                Files.write(loginFile.toPath(), (email + "\n" + password).getBytes());
            } catch (IOException e) {
                LOGGER.warning("Failed to save login credentials to file");
                e.printStackTrace();
            }
        }

        return new String[]{email, password};
    }

    private String[] readCredentialsFromFile() {
        try {
            LOGGER.info("Reading login credentials from file");
            File file = new File("login.txt");
            String[] lines = Files.readAllLines(file.toPath()).toArray(new String[0]);
            return new String[]{lines[0], lines[1]};
        } catch (IOException e) {
            LOGGER.warning("Failed to read login credentials from file");
            e.printStackTrace();
            return null;
        }
    }
}
