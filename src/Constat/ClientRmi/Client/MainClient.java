package Constat.ClientRmi.Client;

import Constat.ClientRmi.Chat.ChatRemote;
import Constat.ClientRmi.Chat.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class MainClient extends JFrame {
    private ChatRemote chatService;
    private JTextArea chatArea;
    private JTextField inputField;
    private String pseudo;

    public MainClient(String pseudo) {
        this.pseudo = pseudo;
        initializeClient();
        createGUI();
        setTitle("Chat - " + pseudo);
    }

    private void initializeClient() {
        try {
            String url = "rmi://localhost:9003/chatapp";
            chatService = (ChatRemote) Naming.lookup(url);
            System.out.println("Connected to chat server as: " + pseudo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void createGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Chat display area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Message receiver thread
        new Thread(() -> {
            while (true) {
                try {
                    ArrayList<Message> messages = chatService.getListMsg();
                    chatArea.setText(""); // Clear before updating
                    for (Message msg : messages) {
                        chatArea.append(msg + "\n");
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    chatArea.append("\nError retrieving messages: " + e.getMessage());
                    break;
                }
            }
        }).start();

        // Send message action
        ActionListener sendAction = e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                try {
                    chatService.addMsg(new Message(text, pseudo));
                    inputField.setText("");
                } catch (RemoteException ex) {
                    chatArea.append("\nError sending message: " + ex.getMessage());
                }
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // This is just for testing without BureauUser
            MainClient client = new MainClient("Test User");
            client.setVisible(true);
        });
    }
}