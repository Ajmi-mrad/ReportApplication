package Constat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.SystemColor.desktop;

public class ConnexionInterface extends JFrame {
    JTextField tfCin;
    JLabel lbCin,lbPassword;
    JPasswordField tfPassword;
    JButton btnConnexion;
    JButton btnCreerCompte;
    JPanel panel;
    JDesktopPane desktop;
    public ConnexionInterface() {
        setTitle("Connexion");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        lbCin = new JLabel("CIN:");
        lbPassword = new JLabel("Mot de passe:");
        tfCin = new JTextField(10);
        tfPassword = new JPasswordField(10);

        btnConnexion = new JButton("Connexion");
        btnCreerCompte = new JButton("Créer un compte");

        // Layout setup
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.add(lbCin);
        panel.add(tfCin);
        panel.add(lbPassword);
        panel.add(tfPassword);
        panel.add(btnConnexion);
        panel.add(btnCreerCompte);
        desktop = new JDesktopPane();

        JPanel panel_button = new JPanel();
        panel_button.add(btnConnexion);
        panel_button.add(btnCreerCompte);

        JPanel panel_principal = new JPanel();
        panel_principal.setLayout(new BoxLayout(panel_principal, BoxLayout.Y_AXIS));
        panel_principal.add(panel);
        panel_principal.add(panel_button);
        this.add(panel_principal, BorderLayout.NORTH);

        // Button actions
        btnConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cin = tfCin.getText();
                String password = tfPassword.getText();

                DriverrManager driverrManager = new DriverrManager();
                // Add logic to check if the user exists in the database

                if (cin.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                // For example, you can use a method like driverrManager.checkUser(cin, password);
                // If the user exists, navigate to the next screen
                // For example, you can use a method like new NextScreen().setVisible(true);
                if (driverrManager.checkDriver(cin,password) == true) {
                    // Navigate to the next screen
                    JOptionPane.showMessageDialog(null, "Connexion réussie!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    // new NextScreen().setVisible(true);
                    Bureau b = new Bureau();
                    b.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "CIN ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCreerCompte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current ConnexionInterface
                dispose();

                // Create a new JFrame to display GestionUser
                JFrame frame = new JFrame("Gestion Conducteurs");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 800);

                // Add GestionUser to the new frame
                GestionUser gestionUser = new GestionUser(cin -> {
                    // After account creation
                    frame.dispose(); // Close the current frame
                    BureauUser bureauUser = new BureauUser(cin); // Open BureauUser with the CIN
                    bureauUser.setVisible(true);
                });

                frame.add(gestionUser);
                gestionUser.setVisible(true);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConnexionInterface connexionInterface = new ConnexionInterface();
            connexionInterface.setVisible(true);
        });
    }
}