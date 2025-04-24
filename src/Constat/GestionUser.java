package Constat;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class GestionUser extends JInternalFrame {
    private JTextField tfCin, tfFirstName, tfLastName, tfAddress, tfPhoneNumber, tfLicenseNumber, tfPassword, tfConfirmPassword;
    private DatePicker dpDateOfBirth, dpLicenseIssueDate;
    private JButton btnUpdate, btnDelete,btnAddAccount;
    private OnAccountCreatedListener accountCreatedListener;

    public GestionUser(OnAccountCreatedListener listener) {
        this.setTitle("Gestion de Mon Compte");
        this.accountCreatedListener = listener;
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());

        // Create form components
        JLabel lbCin = new JLabel("CIN:");
        tfCin = new JTextField();

        JLabel lbFirstName = new JLabel("Prénom:");
        tfFirstName = new JTextField();

        JLabel lbLastName = new JLabel("Nom:");
        tfLastName = new JTextField();

        JLabel lbDateOfBirth = new JLabel("Date de naissance:");
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dpDateOfBirth = new DatePicker(dateSettings);

        JLabel lbAddress = new JLabel("Adresse:");
        tfAddress = new JTextField();

        JLabel lbPhoneNumber = new JLabel("Numéro de téléphone:");
        tfPhoneNumber = new JTextField();

        JLabel lbLicenseNumber = new JLabel("Numéro de permis:");
        tfLicenseNumber = new JTextField();

        JLabel lbLicenseIssueDate = new JLabel("Date de délivrance du permis:");
        DatePickerSettings licenseDateSettings = new DatePickerSettings();
        licenseDateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dpLicenseIssueDate = new DatePicker(licenseDateSettings);

        JLabel lbPassword = new JLabel("Mot de passe:");
        tfPassword = new JPasswordField();

        JLabel lbConfirmPassword = new JLabel("Confirmer le mot de passe:");
        tfConfirmPassword = new JPasswordField();

        btnUpdate = new JButton("Mettre à jour");
        btnDelete = new JButton("Supprimer le compte");
        btnAddAccount = new JButton("Créer un compte");

        // Layout setup
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        formPanel.add(lbCin);
        formPanel.add(tfCin);
        formPanel.add(lbFirstName);
        formPanel.add(tfFirstName);
        formPanel.add(lbLastName);
        formPanel.add(tfLastName);
        formPanel.add(lbDateOfBirth);
        formPanel.add(dpDateOfBirth);
        formPanel.add(lbAddress);
        formPanel.add(tfAddress);
        formPanel.add(lbPhoneNumber);
        formPanel.add(tfPhoneNumber);
        formPanel.add(lbLicenseNumber);
        formPanel.add(tfLicenseNumber);
        formPanel.add(lbLicenseIssueDate);
        formPanel.add(dpLicenseIssueDate);
        formPanel.add(lbPassword);
        formPanel.add(tfPassword);
        formPanel.add(lbConfirmPassword);
        formPanel.add(tfConfirmPassword);

        JPanel buttonPanel = new JPanel();
        //buttonPanel.add(btnUpdate);
        //buttonPanel.add(btnDelete);
        buttonPanel.add(btnAddAccount);

        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);


        btnAddAccount.addActionListener(e -> {
            String cin = tfCin.getText();
            String firstName = tfFirstName.getText();
            String lastName = tfLastName.getText();
            String address = tfAddress.getText();
            String phoneNumber = tfPhoneNumber.getText();
            String licenseNumber = tfLicenseNumber.getText();
            String password = tfPassword.getText();
            String confirmPassword = tfConfirmPassword.getText();
            String dateOfBirth = dpDateOfBirth.getText();
            String licenseIssueDate = dpLicenseIssueDate.getText();

            if (cin.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || licenseNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dateOfBirth.isEmpty() || licenseIssueDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.");
                return;
            }

            try {
                Date dob = Date.valueOf(dateOfBirth);
                Date lid = Date.valueOf(licenseIssueDate);

                DriverrManager driverrManager = new DriverrManager();
                int result = driverrManager.insertDriver(cin, firstName, lastName, dob, address, phoneNumber, licenseNumber, lid, password);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Compte créé avec succès.");
                    if (accountCreatedListener != null) {
                        accountCreatedListener.onAccountCreated(cin);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte.");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez le format yyyy-MM-dd.");
            }
        });

        // Button actions
        btnUpdate.addActionListener(e -> {
            String cin = tfCin.getText();
            String firstName = tfFirstName.getText();
            String lastName = tfLastName.getText();
            String address = tfAddress.getText();
            String phoneNumber = tfPhoneNumber.getText();
            String licenseNumber = tfLicenseNumber.getText();
            String password = tfPassword.getText();
            String confirmPassword = tfConfirmPassword.getText();
            String dateOfBirth = dpDateOfBirth.getText();
            String licenseIssueDate = dpLicenseIssueDate.getText();

            if (cin.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || licenseNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dateOfBirth.isEmpty() || licenseIssueDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.");
                return;
            }

            try {
                Date dob = Date.valueOf(dateOfBirth);
                Date lid = Date.valueOf(licenseIssueDate);

                DriverrManager driverrManager = new DriverrManager();
                int result = driverrManager.updateDriver(cin, firstName, lastName, dob, address, phoneNumber, licenseNumber, lid);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Compte mis à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du compte.");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez le format yyyy-MM-dd.");
            }
        });

        btnDelete.addActionListener(e -> {
            String cin = tfCin.getText();
            if (cin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer le CIN.");
                return;
            }

            int result = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer votre compte ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                DriverrManager driverrManager = new DriverrManager();
                if (driverrManager.deleteDriver(cin) > 0) {
                    JOptionPane.showMessageDialog(this, "Compte supprimé avec succès.");
                    System.exit(0); // Close the application
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du compte.");
                }
            }
        });
    }
    public interface OnAccountCreatedListener {
        void onAccountCreated(String cin);
    }
}