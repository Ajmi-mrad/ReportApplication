package Constat;

import javax.swing.*;
import java.awt.*;

public class BureauUser extends JFrame {
    private JDesktopPane desktop;
    private JMenuBar menuBar;
    private JMenu menuCars, menuAccidents;
    private JMenuItem addCar, updateCar, deleteCar, viewAccidents;

    private String userCin; // CIN of the logged-in user

    public BureauUser(String userCin) {
        this.userCin = userCin;

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

        // Add menus to the menu bar
        menuBar.add(menuCars);
        menuBar.add(menuAccidents);

        setJMenuBar(menuBar);

        // Add action listeners
        addCar.addActionListener(e -> openGestionCarUser());
        updateCar.addActionListener(e -> openGestionCarUser());
        deleteCar.addActionListener(e -> openGestionCarUser());
        viewAccidents.addActionListener(e -> openGestionAccidents());
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BureauUser bureauUser = new BureauUser("12"); // Replace with actual CIN
            bureauUser.setVisible(true);
        });
    }
}