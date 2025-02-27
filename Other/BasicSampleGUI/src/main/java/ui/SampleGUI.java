package ui;

import javax.swing.*;
import java.awt.*;

public class SampleGUI {
    // GUI components
    // Main gui window
    private JFrame mainFrame;

    // Panel for initial view
    private JPanel initialView;
    // Display for initial options - labels, text fields and button
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;

    // Use constructor to establish the components (parts) of the GUI
    public SampleGUI() {
        // Set up the main window
        configureMainWindow();

        // Set up the initial panel (the initial view on the system)
        // This takes in the username and password of the user
        configureInitialPanel();
    }

    private void configureMainWindow() {
        // Create the main frame - this is the main window
        mainFrame = new JFrame("Basic Sample GUI");
        mainFrame.setSize(400, 300);
        // Set what should happen when the X button is clicked on the window
        // This approach will dispose of the main window but not shut down the program
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the layout manager used for the main window
        mainFrame.setLayout(new CardLayout());
    }

    // Set up initial panel (initial view)
    private void configureInitialPanel() {
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        initialView = new JPanel(new GridBagLayout());

        // Create text fields and associated labels to take in username and password
        // Username info
        usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(15);

        // Password info
        passwordLabel = new JLabel("Password: ");
        passwordField = new JTextField(15);

        // Create a button to log in user
        loginButton = new JButton("Log in");


        // Add credential components to initial view panel in specific positions within the gridbag
        // Add username label and text field on first row (y = 0)
        initialView.add(usernameLabel, getGridBagConstraints(0, 0, 1));
        initialView.add(usernameField, getGridBagConstraints(1, 0, 1));
        // Add password label and text field on second row (y = 1)
        initialView.add(passwordLabel, getGridBagConstraints(0, 1, 1));
        initialView.add(passwordField, getGridBagConstraints(1, 1, 1));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        initialView.add(loginButton, getGridBagConstraints(0, 2, 2));
    }

    private static GridBagConstraints getGridBagConstraints(int col, int row, int width) {
        // Create a constraints object to manage component placement within a frame/panel
        GridBagConstraints gbc = new GridBagConstraints();
        // Set it to fill horizontally (component will expand to fill width)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Add padding around the component (Pad by 5 on all sides)
        gbc.insets = new Insets(5, 5, 5, 5);

        // Set the row position to the supplied value
        gbc.gridx = col;
        // Set the column position to the supplied value
        gbc.gridy = row;
        // Set the component's width to the supplied value (in columns)
        gbc.gridwidth = width;
        return gbc;
    }

    private void showInitialView(){
        // Add config panel to the main window and make it visible
        mainFrame.add(initialView);
        mainFrame.setVisible(true);
    }

    public void start(){
        // Add the initial panel to the main window and display the interface
        showInitialView();
    }

    // GUI runner
    public static void main(String[] args) {
        SampleGUI gui = new SampleGUI();
        gui.start();
    }
}
