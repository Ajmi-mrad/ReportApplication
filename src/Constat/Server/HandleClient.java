package Constat.Server;

import Constat.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleClient extends Thread {
    // Client connection properties
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Client identification
    private String cin;
    private String currentAccidentId;
    private Map<String, String> currentClientData = new HashMap<>();

    // Shared accident tracking
    private static final Map<String, List<HandleClient>> accidentClients = new HashMap<>();

    public HandleClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            initializeStreams();
            handleClientCommunication();
        } catch (IOException e) {
            System.out.println("Client disconnected: " + cin);
        } finally {
            cleanupResources();
        }

    }
    private void initializeStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void handleClientCommunication() throws IOException {
        String firstMessage = in.readLine();
        if (firstMessage != null && firstMessage.startsWith("CIN:")) {
            cin = firstMessage.substring(4);
            System.out.println("Client connected with CIN: " + cin);
        }

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received message from client " + cin + ": " + inputLine);
            processClientMessage(inputLine);
        }
    }
    private void processClientMessage(String message) {
        if (message.startsWith("ACCIDENT_ID:")) {
            handleAccidentId(message.substring(12));
        } else if (message.startsWith("UPDATE:")) {
            handleUpdate(message.substring(7), currentAccidentId);
        } else if (message.startsWith("CONFIRM:")) {
            handleConfirmation(message.substring(8));
        }
    }

    private void handleAccidentId(String accidentId) {
        currentAccidentId = accidentId;

        // Ensure the list exists for the accidentId
        List<HandleClient> clients = accidentClients.computeIfAbsent(accidentId, k -> new ArrayList<>());

        // Check if the current client is already in the list
        if (!clients.contains(this)) {
            clients.add(this);
        } else {
            System.out.println("Client " + cin + " is already working on accident: " + accidentId);
        }
        System.out.println("Client " + cin + " working on accident: " + accidentId);
    }

    private void handleUpdate(String updateMessage, String accidentId) {
        parseClientData(updateMessage);
        broadcastToAccidentClients(updateMessage, accidentId);
    }

    private void parseClientData(String updateMessage) {
        String[] parts = updateMessage.split(",");
        // map parts and display it
        for (String part : parts) {
            System.out.println("Part: " + part);
        }
        currentClientData.put("vehicleId", parts[0].split(":")[1].trim());
        currentClientData.put("description", parts[1].split(":")[1].trim());
        currentClientData.put("damages", parts[2].split(":")[1].trim());
    }

    private void handleConfirmation(String accidentId) {
        List<HandleClient> clients = accidentClients.get(accidentId);
        if (clients != null && clients.size() == 2) {
            System.out.println("Both clients confirmed the report for Accident ID: " + accidentId);

            HandleClient clientA = clients.get(0);
            HandleClient clientB = clients.get(1);

            // Create a thread for report generation and database insertion
            Thread reportThread = new Thread(() -> {
                try {
                    /*
                    // frst generate the report
                    ReportGenerator reportGenerator = new ReportGenerator();
                    reportGenerator.generateReport(
                            Integer.parseInt(accidentId),
                            Integer.parseInt(clientA.currentClientData.get("vehicleId")),
                            Integer.parseInt(clientB.currentClientData.get("vehicleId")),
                            clientA.cin,
                            clientB.cin
                    );
                    System.out.println("Report generated successfully for Accident ID: " + accidentId);

                     */

                    // then insert into database
                    ReportManager reportManager = new ReportManager();
                    int result = reportManager.insertReport(
                            Integer.parseInt(accidentId),
                            Integer.parseInt(clientA.currentClientData.get("vehicleId")),
                            Integer.parseInt(clientB.currentClientData.get("vehicleId")),
                            clientA.cin,
                            clientB.cin,
                            clientA.currentClientData.get("description"),
                            clientB.currentClientData.get("description"),
                            clientA.currentClientData.get("damages"),
                            clientB.currentClientData.get("damages")
                    );

                    if (result > 0) {
                        System.out.println("Report successfully added to database for Accident ID: " + accidentId);
                        // Notify clients that report is complete
                        clients.forEach(client -> client.out.println("REPORT_COMPLETE:" + accidentId));
                    } else {
                        System.err.println("Failed to add report to database for Accident ID: " + accidentId);
                        clients.forEach(client -> client.out.println("REPORT_FAILED:" + accidentId));
                    }
                } catch (Exception e) {
                    System.err.println("Error in report processing:");
                    e.printStackTrace();
                    clients.forEach(client -> client.out.println("REPORT_ERROR:" + accidentId));
                } finally {
                    accidentClients.remove(accidentId);
                }
            });

            reportThread.start();
        } else {
            System.out.println("Not enough clients confirmed for Accident ID: " + accidentId);
            if (clients != null) {
                clients.forEach(client -> client.out.println("CONFIRMATION_FAILED:Need both parties to confirm"));
            }
        }
    }
    private void broadcastToAccidentClients(String message, String accidentId) {
        // Notify other clients about the update
        System.out.println("Broadcasting update to other clients for Accident ID: " + accidentId);
        // get all clients with same accidentId
        List<HandleClient> clients = accidentClients.get(accidentId);
        // Both ClientA and ClientB see changes immediately without reloading manually.
        if (clients != null) {
            clients.stream()
                    // exclude the current client
                    .filter(client -> client != this)
                    // send Update message to other clients
                    .forEach(client -> client.out.println("UPDATE:" + message));
        }
    }
    private void generateAndShowReport(String accidentId, HandleClient clientA, HandleClient clientB) {
        try {
            ReportGenerator reportGenerator = new ReportGenerator();
            reportGenerator.generateReport(
                    Integer.parseInt(accidentId),
                    Integer.parseInt(clientA.currentClientData.get("vehicleId")),
                    Integer.parseInt(clientB.currentClientData.get("vehicleId")),
                    clientA.cin,
                    clientB.cin
            );
        } catch (NumberFormatException e) {
            System.err.println("Error parsing IDs for report generation");
            e.printStackTrace();
        }
    }
    private void cleanupResources() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (currentAccidentId != null && accidentClients.containsKey(currentAccidentId)) {
                accidentClients.get(currentAccidentId).remove(this);
            }
        } catch (IOException e) {
            System.err.println("Error cleaning up resources:");
            e.printStackTrace();
        }
    }

}