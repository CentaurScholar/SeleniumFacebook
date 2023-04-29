package org.example;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GUI {
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
                e.printStackTrace();
            }
        }

        int choice = JOptionPane.showOptionDialog(null, "Read login credentials from:", "Login credentials", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"File", "Enter here", "Cancel"}, "File");
        if (choice == 0) {
            // Read login credentials from file
            try {
                File file = new File("login.txt");
                String[] lines = Files.readAllLines(file.toPath()).toArray(new String[0]);
                email = lines[0];
                password = lines[1];
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (choice == 1) {
            // Read login credentials from Swing
            email = JOptionPane.showInputDialog(null, "Email:");
            password = JOptionPane.showInputDialog(null, "Password:");
        } else {
            // Cancel was clicked
            System.exit(0);
        }

        int saveChoice = JOptionPane.showConfirmDialog(null, "Do you want to save login credentials?", "Login credentials", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (saveChoice == JOptionPane.YES_OPTION) {
            try {
                Files.write(loginFile.toPath(), (email + "\n" + password).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new String[]{email, password};
    }
}
