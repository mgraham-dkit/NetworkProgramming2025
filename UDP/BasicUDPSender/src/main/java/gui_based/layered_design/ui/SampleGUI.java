package gui_based.layered_design.ui;

import gui_based.layered_design.network.UDPNetworkService;
import gui_based.layered_design.protocol_utils.AuthUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class SampleGUI {
    // Provide networking functionality
    private UDPNetworkService network;

    // GUI components
    private final HashMap<String, Container> guiContainers = new HashMap<>();
    // Main gui window
    private JFrame mainFrame;
    // Main Font setting
    private Font font = new Font("Arial", Font.PLAIN, 16);

    // Panel for initial view
    private JPanel initialView;
    // Display for initial options - labels, text fields and button
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;
    // Buttons to increase and decrease font size (on initial view)
    private JButton decreaseFontButton;
    private JButton increaseFontButton;

    // Panel for logged-in view
    private JPanel countCharView;
    private JButton countButton;
    private JLabel userTextLabel;
    private JTextField userTextField;
    private JLabel userCountLabel;
    private JTextField userCountField;


    // Use constructor to establish the components (parts) of the GUI
    public SampleGUI() {
        // Set the standard font settings for the application
        setStandardFonts();

        // Set up the main window
        configureMainWindow();

        // Set up the initial panel (the initial view on the system)
        // This takes in the username and password of the user
        configureInitialPanel();

        // Set up second panel
        configureCountView();
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

    private void configureMainWindow() {
        // Create the main frame - this is the main window
        mainFrame = new JFrame("Basic Sample GUI");
        mainFrame.setSize(400, 300);
        // Set what should happen when the X button is clicked on the window
        // This approach will dispose of the main window but not shut down the program
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the layout manager used for the main window
        mainFrame.setLayout(new CardLayout());

        // Add a listener to the overall window that reacts when window close action is requested
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                network.disconnect();
                System.out.println("Shutting down...");
                // Shut down the application fully
                System.exit(0);
            }
        });

        // Register the main window as a container in the system
        guiContainers.put("mainFrame", mainFrame);
    }

    // Set up initial panel (initial view)
    private void configureInitialPanel() {
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        initialView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("initialView", initialView);

        // Create text fields and associated labels to take in username and password
        // Username info
        usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(15);

        // Password info
        passwordLabel = new JLabel("Password: ");
        passwordField = new JTextField(15);

        // Create a button to log in user
        loginButton = new JButton("Log in");
        // Specify what the button should DO when clicked:
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        // Create a button to increase font
        increaseFontButton = new JButton("Increase Font");
        increaseFontButton.addActionListener(e -> {
            changeFontSize(1.0f);
        });

        // Create a button to decrease font
        decreaseFontButton = new JButton("Decrease Font");
        decreaseFontButton.addActionListener(e -> {
            changeFontSize(-1.0f);
        });

        // Add credential components to initial view panel in specific positions within the gridbag
        // Add username label and text field on first row (y = 0)
        initialView.add(usernameLabel, getGridBagConstraints(0, 0, 1));
        initialView.add(usernameField, getGridBagConstraints(1, 0, 1));
        // Add password label and text field on second row (y = 1)
        initialView.add(passwordLabel, getGridBagConstraints(0, 1, 1));
        initialView.add(passwordField, getGridBagConstraints(1, 1, 1));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        initialView.add(loginButton, getGridBagConstraints(0, 2, 2));

        // Add empty space on fourth row (y = 3) spanning two columns (width = 2)
        initialView.add(new JPanel(), getGridBagConstraints(0, 3, 2));

        // Add button on fifth row (y = 4) spanning two columns (width = 2)
        initialView.add(increaseFontButton, getGridBagConstraints(0, 4, 2));
        // Add button on sixth row (y = 5) spanning two columns (width = 2)
        initialView.add(decreaseFontButton, getGridBagConstraints(0, 5, 2));
    }

    private void configureCountView(){
        // Create and configure the config panel
        // This will provide a view to take in the user credentials
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        countCharView = new JPanel(new GridBagLayout());
        // Register this panel as a container in the system
        guiContainers.put("countCharView", countCharView);

        // Create text fields and associated labels to take in username and password
        // Username info
        userTextLabel = new JLabel("Enter your text: ");
        userTextField = new JTextField(15);

        userCountLabel = new JLabel("What to count: ");
        userCountField = new JTextField(15);
        // Create a button to log in user
        countButton = new JButton("Count occurrences");
        // Specify what the button should DO when clicked:
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //count();
                System.out.println("Counting!!");
            }
        });

        // Add credential components to count view panel in specific positions within the gridbag
        // Add username label and text field on first row (y = 0)
        countCharView.add(userTextLabel, getGridBagConstraints(0, 0, 1));
        countCharView.add(userTextField, getGridBagConstraints(1, 0, 1));
        // Add password label and text field on second row (y = 1)
        countCharView.add(userCountLabel, getGridBagConstraints(0, 1, 1));
        countCharView.add(userCountField, getGridBagConstraints(1, 1, 1));

        // Add button on third row (y = 2) spanning two columns (width = 2)
        countCharView.add(countButton, getGridBagConstraints(0, 2, 2));
    }

    private void showInitialView(){
        // Add config panel to the main window and make it visible
        mainFrame.add(initialView);
        mainFrame.setVisible(true);
    }

    private void showCountView(){
        // Add config panel to the main window and make it visible
        mainFrame.remove(0);
        mainFrame.add(countCharView);
        mainFrame.setVisible(true);
    }

    public void start() throws UnknownHostException, SocketException {
        network = new UDPNetworkService(AuthUtils.SERVER_HOST, AuthUtils.SERVER_PORT, 5656);
        network.connect();
        // Add the initial panel to the main window and display the interface
        showInitialView();
    }

    /*
     * All methods below this point provide application logic
     */
    private void loginUser(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Send a message to server asking to authenticate the user
            network.send(username + AuthUtils.DELIMITER + password);
            // Wait to receive a response to the authentication request
            String response = network.receive();

            // If the response matches the expected success message, treat user as authenticated
            if(response.equalsIgnoreCase(AuthUtils.SUCCESS)){
                JOptionPane.showMessageDialog(initialView, "You have successfully logged in!", "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                mainFrame.remove(initialView);
                showCountView();
                return;
            }

            JOptionPane.showMessageDialog(initialView, "Username/password incorrect. Please try again.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }catch(IOException e){
            JOptionPane.showMessageDialog(initialView, "Message could not be sent. Please try again later", "Mesage Sending Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void changeFontSize(float sizeChange){
        font = font.deriveFont(font.getSize()+sizeChange);
        setStandardFonts();
        updateContainers();
        mainFrame.pack();
    }

    private void setStandardFonts(){
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.buttonFont", font);
    }

    private void updateContainers() {
        for (Container c : guiContainers.values()) {
            for (Component component : c.getComponents()) {
                // Set the font in the component
                component.setFont(font);
            }
            // Revalidate and repaint the container
            c.revalidate();
            c.repaint();
        }
    }
    // GUI runner
    public static void main(String[] args) {
        // Create an instance of the GUI
        SampleGUI gui = new SampleGUI();
        // Start the GUI - this will trigger the application to be made visible
        try{
            gui.start();
        }catch(UnknownHostException e){
            System.out.println("Hostname could not be found. Please contact system administrator");
        }catch(SocketException e){
            System.out.println("Socket exception occurred. Please try again later.");
        }
    }
}
