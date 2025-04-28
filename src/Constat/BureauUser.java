package Constat;

import Constat.Client.ClientReportServer;
import Constat.ClientRmi.Client.MainClient;


import javax.swing.*;
import java.rmi.Naming;
import java.sql.ResultSet;

public class BureauUser extends JFrame {
    private JDesktopPane desktop;
    private JMenuBar menuBar;
    private JMenu menuCars, menuAccidents;
    JMenu menuReports;
    JMenu menuChat;
    private JMenuItem addCar, updateCar, deleteCar, viewAccidents;
    JMenuItem addReport, openChat;
    private String userCin;
    private DriverrManager driverManager;

    public BureauUser(String userCin) {
        this.userCin = userCin;
        this.driverManager = new DriverrManager();

        // Set up the frame
        setTitle("Bureau Utilisateur");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create desktop pane
        desktop = new JDesktopPane();
        add(desktop);

        // Create menu bar
        menuBar = new JMenuBar();

        // Cars menu
        menuCars = new JMenu("Voitures");
        addCar = new JMenuItem("Ajouter une voiture");
        updateCar = new JMenuItem("Modifier une voiture");
        deleteCar = new JMenuItem("Supprimer une voiture");


        menuCars.add(addCar);
        menuCars.add(updateCar);
        menuCars.add(deleteCar);

        // Accidents menu
        menuAccidents = new JMenu("Accidents");
        viewAccidents = new JMenuItem("Voir mes accidents");

        menuAccidents.add(viewAccidents);


        //Report Menu
        menuReports = new JMenu("Rapports");
        addReport = new JMenuItem("Ajouter un rapport");
        menuReports.add(addReport);

        // Chat menu
        menuChat = new JMenu("Chat");
        openChat = new JMenuItem("Ouvrir le chat");
        menuChat.add(openChat);


        // Add menus to the menu bar
        menuBar.add(menuCars);
        menuBar.add(menuAccidents);
        menuBar.add(menuReports);
        menuBar.add(menuChat);


        setJMenuBar(menuBar);

        // Add action listeners
        addCar.addActionListener(e -> openGestionCarUser());
        updateCar.addActionListener(e -> openGestionCarUser());
        deleteCar.addActionListener(e -> openGestionCarUser());
        viewAccidents.addActionListener(e -> openGestionAccidents());
        addReport.addActionListener(e -> openReportInterface());
        openChat.addActionListener(e -> openChatInterface());
    }

    private void openGestionCarUser() {
        GestionCarUser gestionCarUser = new GestionCarUser(userCin);
        desktop.add(gestionCarUser);
        gestionCarUser.setVisible(true);
    }

    private void openGestionAccidents() {
        // Create and display a new internal frame for managing accidents
        GestionAccidentUser gestionAccidentUser = new GestionAccidentUser(userCin);
        desktop.add(gestionAccidentUser);
        gestionAccidentUser.setVisible(true);

    }
    private void openReportInterface() {
        // Create and display a new internal frame for the report interface

        ClientReportServer client = new ClientReportServer(userCin);
       // desktop.add(client);
        client.setVisible(true);
        /*
        GestionReportUser gestionReport = new GestionReportUser(userCin, true); // true for Client A
        gestionReport.setVisible(true);

         */
    }

    private void openChatInterface() {
        try {
            // Get driver info from database
            ResultSet driverInfo = driverManager.driverrExist(userCin);
            if (driverInfo != null && driverInfo.next()) {
                String firstName = driverInfo.getString("first_name");
                String lastName = driverInfo.getString("last_name");
                String pseudo = firstName + " " + lastName;

                // Create and show chat client with the driver's name as pseudo
                MainClient chatClient = new MainClient(pseudo);
                chatClient.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Driver information not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening chat: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BureauUser bureauUser = new BureauUser("12"); // Replace with actual CIN
            bureauUser.setVisible(true);
        });
    }
}