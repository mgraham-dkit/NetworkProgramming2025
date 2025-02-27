package gui_based.layered_design.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.*;
import gui_based.layered_design.network.NetworkService;

import static javax.swing.JOptionPane.showMessageDialog;

public class ClearClientInterface {
    // Networking layer
    private NetworkService connection;

    private String screenName;

    // GUI components
    // Main gui window
    private JFrame mainFrame;

    // Panel for config view
    private JPanel configPanel;
    // Display for config options - labels, text fields and button
    private JLabel ipLabel;
    private JLabel portLabel;
    private JLabel screenNameLabel;
    private JTextField ipEntry;
    private JTextField portEntry;
    private JTextField screenNameEntry;
    private JButton configureButton;

    // Panel for messaging view
    private JPanel messagingPanel;

    // Display for message exchange
    // Text area with scroll pane to allow for many messages to be displayed
    private JTextArea messageExchangeTextArea;
    private JScrollPane messageExchangeScrollPane;

    // Display for message entry and transmission
    // Panel to group text field and button together
    private JPanel textFieldPanel;
    private JTextField messageEntryTextField;
    private JButton sendButton;

    // Use constructor to establish the components (parts) of the GUI
    public ClearClientInterface() throws UnknownHostException {
        connection = new NetworkService();
        // Set up the main window
        configureMainWindow();

        // Set up the config panel (the initial view on the system)
        // This takes in the connection details for the receiver (i.e. where to send the messages)
        configureConfigPanel();

        // Set up the messaging panel (second view on the system)
        // This allows for the sending and display of received messages
        configureMessagingArea();

    }

    private void configureMainWindow() {
        // Create the main frame - this is the main window
        mainFrame = new JFrame("Message Sending Application");
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
                // If we have a socket created, close it
                connection.disconnect();
                System.out.println("Shutting down...");
                // Shut down the application fully
                System.exit(0);
            }
        });
    }

    // Set up config panel (initial view)
    private void configureConfigPanel() {
        // Create and configure the config panel
        // This will provide a view to take in the connection details of the receiver program
        // Use a GridBag layout so we have a grid to work with, but there's some flexibility (button can span columns)
        configPanel = new JPanel(new GridBagLayout());

        // Create text fields and associated labels to take in IP and port info of receiver
        // IP Address info
        ipLabel = new JLabel("Destination IP/Hostname: ");
        ipEntry = new JTextField(15);

        // Port info
        portLabel = new JLabel("Destination port number: ");
        portEntry = new JTextField(5);

        // Screen name info
        screenNameLabel = new JLabel("Screen name: ");
        screenNameEntry = new JTextField(20);

        // Create a button to save supplied connection information
        configureButton = new JButton("Configure");

        // Action Listener to set up socket when the configure button is pressed
        configureButton.addActionListener(e -> {
            // Call method to set up the socket
            setRecipientDetails();
        });

        // Add config components to config panel in specific positions within the gridbag
        // Add IP label and text field on first row (y = 0)
        configPanel.add(ipLabel, getGridBagConstraints(0, 0, 1));
        configPanel.add(ipEntry, getGridBagConstraints(1, 0, 1));
        // Add port label and text field on second row (y = 1)
        configPanel.add(portLabel, getGridBagConstraints(0, 1, 1));
        configPanel.add(portEntry, getGridBagConstraints(1, 1, 1));
        // Add screen name label and text field on third row (y = 2)
        configPanel.add(screenNameLabel, getGridBagConstraints(0, 2, 1));
        configPanel.add(screenNameEntry, getGridBagConstraints(1, 2, 1));
        // Add button on fourth row (y = 3) spanning two columns (width = 2)
        configPanel.add(configureButton, getGridBagConstraints(0, 3, 2));
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

    private void clearConfigTextFields() {
        ipEntry.setText("");
        portEntry.setText("");
        screenNameEntry.setText("");
    }

    private void showConfigPanel(){
        // Hide the messaging panel and remove it from the main window (if it's there)
        messagingPanel.setVisible(false);
        mainFrame.remove(messagingPanel);

        // Add config panel to the main window and make it visible
        mainFrame.add(configPanel);
        mainFrame.setVisible(true);
    }

    // Set up messaging panel
    private void configureMessagingArea() {
        messagingPanel = new JPanel();
        configureMessageDisplayArea();
        configureMessageEntryArea();
    }

    private void configureMessageEntryArea() {
        // Create a textfield to take in messages
        messageEntryTextField = new JTextField(25);
        // Add a listener to react if the user presses enter
        messageEntryTextField.addActionListener(e -> sendMessage());

        // Create a button to send messages
        sendButton = new JButton("Send");
        // Action Listener to send the message when the button is clicked
        sendButton.addActionListener(e -> sendMessage());

        // Create a panel to hold the button and text field beside each other
        textFieldPanel = new JPanel();

        // Add button and text field to panel
        textFieldPanel.add(messageEntryTextField, BorderLayout.WEST);
        textFieldPanel.add(sendButton, BorderLayout.EAST);

        // Add the text entry panel to the messaging panel
        messagingPanel.add(textFieldPanel, BorderLayout.SOUTH);
    }

    private void configureMessageDisplayArea() {
        // Create a text area inside a scroll pane
        messageExchangeTextArea = new JTextArea(10, 30);
        // Make it read-only, i.e. only for display use
        messageExchangeTextArea.setEditable(false);
        // Make it wrap text longer than the width of the area
        messageExchangeTextArea.setLineWrap(true);
        // Wrap words, don't break words in the middle
        messageExchangeTextArea.setWrapStyleWord(true);
        // Create a scrollbar so we can see the scrollback information
        messageExchangeScrollPane = new JScrollPane(messageExchangeTextArea);
        // Add the scrollbar to the messaging panel
        messagingPanel.add(messageExchangeScrollPane, BorderLayout.CENTER);
    }

    // Method to hide the configuration panel and show the main messaging panel
    private void showMessagingPanel(){
        // Hide the configuration panel and remove it from the main window
        configPanel.setVisible(false);
        mainFrame.remove(configPanel);

        // Add the messaging panel to the main frame and make it visible
        mainFrame.add(messagingPanel);
        messagingPanel.setVisible(true);
    }

    // NETWORKING CODE
    // Establish the socket through which the program will send and receive messages
    private void setUpNetworkConnection(){
        try {
            connection.connect();
        }catch(NumberFormatException e){
            showMessageDialog(configPanel, "Port provided must be a number.");
        }catch(IllegalArgumentException e){
            showMessageDialog(configPanel, "Port provided must be a number between 1 and 65535 (inclusive).");
        }catch(UnknownHostException e){
            showMessageDialog(configPanel, "Sending host could not be found. Please try again!");
        }catch(BindException e){
            showMessageDialog(configPanel, "Socket could not be bound to port " + connection.getSenderPort() + ". Please choose " +
                    "another " +
                    "port or clear that port on your system." +
                    ".");
        }catch(SocketException e){
            showMessageDialog(configPanel, "Socket could not be connected at this time. Please try again later.");
        }
    }

    // Configure the destination for messages
    private void setRecipientDetails(){
        try {
            // Get entered receiver hostname from gui and convert it to an InetAddress
            String host = ipEntry.getText();

            // Get entered receiver port number and convert it to an int
            String portText = portEntry.getText();
            int port = Integer.parseInt(portText);

            // Set messaging information in service layer
            connection.setRecipient(host, port);

            // Store the screen name entered by the user
            screenName = screenNameEntry.getText();
            showMessagingPanel();
            clearConfigTextFields();
        }catch(UnknownHostException e){
            showMessageDialog(configPanel, "Recipient host could not be found. Please enter a new destination host!");
            // Remove the bad host data from the text box
            ipEntry.setText("");
        }catch(NumberFormatException e){
            // This will cover both where the value entered is not a number AND where the number is too big/small
            // because NumberFormatException is a subclass of IllegalArgumentException
            showMessageDialog(configPanel, "Port provided must be a number between 1 and 65535 (inclusive).");
            // Remove the bad port data from the text box
            portEntry.setText("");
        }
    }

    // Send a message to the established destination/recipient
    private void sendMessage(){
        try {
            // Take in the message from the text field
            String enteredText = messageEntryTextField.getText();

            // Attempt to send the message
            connection.send(enteredText);
            // Display the entered message in the text area
            // (append it so that the scrollback contains the message exchange history)
            messageExchangeTextArea.append(screenName + "> " + enteredText + "\n");
            // Reset the text field
            messageEntryTextField.setText("");
        }catch(IOException e){
            // If the packet can't be sent or the receive action fails, show an alert
            showMessageDialog(configPanel, "Message could not be sent at this time.");
        }
        try{
            // Receive the response and display it
            String response = connection.receive();
            messageExchangeTextArea.append(connection.getReceiverAddress() + "> " + response + "\n");
        }catch(SocketTimeoutException e){
            showMessageDialog(configPanel, "No message received within socket timeout window. Try again later.");
        }catch(IOException e){
            // If the receive action fails, show an alert
            showMessageDialog(configPanel, "Message could not be received at this time.");
        }
    }

    public void start(){
        // Set up socket for communication
        this.setUpNetworkConnection();
        // Show the appropriate view (config panel to start)
        this.showConfigPanel();
    }
    // GUI runner
    public static void main(String[] args) {
        try {
            ClearClientInterface gui = new ClearClientInterface();
            gui.start();
        }catch(UnknownHostException e){
            System.out.println("Cannot connect to localhost at this time. Program terminating...");
        }
    }
}

