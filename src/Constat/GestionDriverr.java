package Constat;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GestionDriverr extends JInternalFrame {
    JLabel lb_cin, lb_first_name, lb_last_name, lb_date_of_birth, lb_address, lb_phone_number, lb_license_number, lb_license_issue_date, lb_pass;
    JTextField tf_cin, tf_first_name, tf_last_name, tf_address, tf_phone_number, tf_license_number, tf_pass;
    DatePicker dp_date_of_birth, dp_license_issue_date;
    JButton btn_add;
    JPanel panel_nord;
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Editer");
    JMenuItem deleteItem = new JMenuItem("Supprimer");
    MyTableModelDriverr myTableModelDriverr;
    JTable table;

    public GestionDriverr() {
        this.setTitle("Gestion Conducteurs");
        this.setSize(1200, 1200);
        getContentPane().setBackground(new Color(230, 230, 250));

        lb_cin = new JLabel("CIN : ");
        lb_first_name = new JLabel("Prénom : ");
        lb_last_name = new JLabel("Nom : ");
        lb_date_of_birth = new JLabel("Date de naissance : ");
        lb_address = new JLabel("Adresse : ");
        lb_phone_number = new JLabel("Numéro de téléphone : ");
        lb_license_number = new JLabel("Numéro de permis : ");
        lb_license_issue_date = new JLabel("Date de délivrance du permis : ");
        lb_pass = new JLabel("Mot de passe : ");

        tf_cin = new JTextField(10);
        tf_first_name = new JTextField(10);
        tf_last_name = new JTextField(10);
        tf_address = new JTextField(10);
        tf_phone_number = new JTextField(10);
        tf_license_number = new JTextField(10);
        tf_pass = new JTextField(10);

        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        dateSettings.setAllowEmptyDates(true);


        DatePickerSettings dateSettingsLicense = new DatePickerSettings();
        dateSettingsLicense.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettingsLicense.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        dateSettingsLicense.setAllowEmptyDates(true);

        dp_date_of_birth = new DatePicker(dateSettings);
        dp_license_issue_date = new DatePicker(dateSettingsLicense);

        dp_date_of_birth.setText("Sélectionner la date de naissance");
        dp_date_of_birth.setDateToToday();

        dp_license_issue_date.setText("Sélectionner la license issue date");
        dp_license_issue_date.setDateToToday();


        btn_add = new JButton("Ajouter");

        panel_nord = new JPanel();
        panel_nord.setLayout(new GridLayout(5, 2, 5, 5));
        panel_nord.add(lb_cin);
        panel_nord.add(tf_cin);
        panel_nord.add(lb_first_name);
        panel_nord.add(tf_first_name);
        panel_nord.add(lb_last_name);
        panel_nord.add(tf_last_name);
        panel_nord.add(lb_date_of_birth);
        panel_nord.add(dp_date_of_birth);
        panel_nord.add(lb_address);
        panel_nord.add(tf_address);
        panel_nord.add(lb_phone_number);
        panel_nord.add(tf_phone_number);
        panel_nord.add(lb_license_number);
        panel_nord.add(tf_license_number);
        panel_nord.add(lb_license_issue_date);
        panel_nord.add(dp_license_issue_date);
        panel_nord.add(lb_pass);
        panel_nord.add(tf_pass);

        JPanel panel_bouton = new JPanel();
        panel_bouton.add(btn_add);

        JPanel panel_principal = new JPanel();
        panel_principal.setLayout(new BoxLayout(panel_principal, BoxLayout.Y_AXIS));
        panel_principal.add(panel_nord);
        panel_principal.add(panel_bouton);
        this.add(panel_principal, BorderLayout.NORTH);

        DriverrManager driverrManager = new DriverrManager();
        driverrManager.createTableDriver();
        ResultSet rs = driverrManager.selectAllDriverss();

        myTableModelDriverr = new MyTableModelDriverr(rs, driverrManager);
        table = new JTable(myTableModelDriverr);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        this.add(scrollPane, BorderLayout.CENTER);
        table.setFillsViewportHeight(true);

        btn_add.addActionListener(e -> {
            String cin = tf_cin.getText();
            String firstName = tf_first_name.getText();
            String lastName = tf_last_name.getText();
            String address = tf_address.getText();
            String phoneNumber = tf_phone_number.getText();
            String licenseNumber = tf_license_number.getText();
            String pass = tf_pass.getText();
            String dateOfBirth = dp_date_of_birth.getText();
            String licenseIssueDate = dp_license_issue_date.getText();

            if (cin.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || licenseNumber.isEmpty() || pass.isEmpty() || dateOfBirth.isEmpty() || licenseIssueDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Entrer tous les champs");
                return;
            }

            try {
                // Validate date format
                Date.valueOf(dateOfBirth); // Throws IllegalArgumentException if invalid
                Date.valueOf(licenseIssueDate); // Throws IllegalArgumentException if invalid

                int result = myTableModelDriverr.ajoutDriver(cin, firstName, lastName, dateOfBirth, address, phoneNumber, licenseNumber, licenseIssueDate, pass);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Conducteur ajouté avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du conducteur");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez le format yyyy-MM-dd.");
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(new EcouteurPopupMenuDriverr(this));
        deleteItem.addActionListener(new EcouteurPopupMenuDriverr(this));
    }

    public void editDriver() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String cin = table.getValueAt(row, 0).toString();
            String currentFirstName = table.getValueAt(row, 1).toString();
            String currentLastName = table.getValueAt(row, 2).toString();
            String currentDateOfBirth = table.getValueAt(row, 3).toString();
            String currentAddress = table.getValueAt(row, 4).toString();
            String currentPhoneNumber = table.getValueAt(row, 5).toString();
            String currentLicenseNumber = table.getValueAt(row, 6).toString();
            String currentLicenseIssueDate = table.getValueAt(row, 7).toString();

            JTextField firstNameField = new JTextField(currentFirstName);
            JTextField lastNameField = new JTextField(currentLastName);
            JTextField addressField = new JTextField(currentAddress);
            JTextField phoneNumberField = new JTextField(currentPhoneNumber);
            JTextField licenseNumberField = new JTextField(currentLicenseNumber);
            DatePicker dateOfBirthField = new DatePicker();
            DatePicker licenseIssueDateField = new DatePicker();
            dateOfBirthField.setDate(LocalDate.parse(currentDateOfBirth));
            licenseIssueDateField.setDate(LocalDate.parse(currentLicenseIssueDate));
            // Set initial values for DatePicker fields
            //try {

            //} catch (DateTimeParseException e) {
              //  System.err.println("Invalid date format for Date of Birth: " + currentDateOfBirth);
            //}
/*
            try {

            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format for License Issue Date: " + currentLicenseIssueDate);
            }

 */

            JPanel panel = new JPanel(new GridLayout(6, 2));
            panel.add(new JLabel("Prénom:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Nom:"));
            panel.add(lastNameField);
            panel.add(new JLabel("Adresse:"));
            panel.add(addressField);
            panel.add(new JLabel("Numéro de téléphone:"));
            panel.add(phoneNumberField);
            panel.add(new JLabel("Numéro de permis:"));
            panel.add(licenseNumberField);
            panel.add(new JLabel("Date de naissance:"));
            panel.add(dateOfBirthField);
            panel.add(new JLabel("Date de délivrance du permis:"));
            panel.add(licenseIssueDateField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Modifier Conducteur", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String address = addressField.getText();
                String phoneNumber = phoneNumberField.getText();
                String licenseNumber = licenseNumberField.getText();
                String dateOfBirth = dateOfBirthField.getDate().toString();
                String licenseIssueDate = licenseIssueDateField.getDate().toString();



                int updateResult = myTableModelDriverr.modifierDriver(cin, firstName, lastName, dateOfBirth, address, phoneNumber, licenseNumber, licenseIssueDate);
                if (updateResult > 0) {
                    JOptionPane.showMessageDialog(this, "Conducteur modifié avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
                }
            }else if(result == JOptionPane.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(this, "Modification annulée");
            }
        }
    }
    public void deleteDriver() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String cin = table.getValueAt(row, 0).toString();
            int result = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer ce conducteur ?", "Supprimer Conducteur", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int deleteResult = myTableModelDriverr.suppDriver(cin);
                if (deleteResult > 0) {
                    JOptionPane.showMessageDialog(this, "Conducteur supprimé avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");
                }
            }
        }
    }
}