package Constat.Client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientReportServer extends JFrame {
    private JTextField tf_accidentId, tf_vehicleId, tf_description, tf_damages;
    private JButton btn_send, btn_confirm;
    private PrintWriter out;
    private BufferedReader in;
    private String cin;
    private JTextArea ta_updates;

    public ClientReportServer(String cin) {
        this.cin = cin;
        initializeUI();
        connectSocket("localhost", 9000);
    }

    private void initializeUI() {
        setTitle("Client Report Interface - CIN: " + cin);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        tf_accidentId = new JTextField();
        tf_vehicleId = new JTextField();
        tf_description = new JTextField();
        tf_damages = new JTextField();

        btn_send = new JButton("Send Update");
        btn_confirm = new JButton("Confirm Report");

        inputPanel.add(new JLabel("Accident ID:"));
        inputPanel.add(tf_accidentId);
        inputPanel.add(new JLabel("Vehicle ID:"));
        inputPanel.add(tf_vehicleId);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(tf_description);
        inputPanel.add(new JLabel("Damages:"));
        inputPanel.add(tf_damages);
        inputPanel.add(btn_send);
        inputPanel.add(btn_confirm);

        ta_updates = new JTextArea();
        ta_updates.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ta_updates);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btn_send.addActionListener(e -> sendUpdate());
        btn_confirm.addActionListener(e -> confirmReport());
    }

    private void connectSocket(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send client CIN to server
            out.println("CIN:" + cin);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith("UPDATE:")) {
                            // Display updates in the text area
                            ta_updates.append(message.substring(7) + "\n");
                        } else {
                            JOptionPane.showMessageDialog(this, message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage());
        }
    }

    private void sendUpdate() {
        String accidentId = tf_accidentId.getText();
        String vehicleId = tf_vehicleId.getText();
        String description = tf_description.getText();
        String damages = tf_damages.getText();

        if (accidentId.isEmpty() || vehicleId.isEmpty() || description.isEmpty() || damages.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!");
            return;
        }

        out.println("ACCIDENT_ID:" + accidentId);
        out.println("UPDATE:VEHICLE_ID:" + vehicleId + ",DESCRIPTION:" + description + ",DAMAGES:" + damages);
    }

    private void confirmReport() {
        String accidentId = tf_accidentId.getText();
        if (accidentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Accident ID must be filled!");
            return;
        }

        // Debugging log
        System.out.println("Sending CONFIRM for Accident ID: " + accidentId);

        out.println("CONFIRM:" + accidentId);
    }
}