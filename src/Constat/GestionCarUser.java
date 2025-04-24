package Constat;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;

public class GestionCarUser extends JInternalFrame {
    // Same fields as GestionCar
    JLabel lb_make, lb_model, lb_license_plate, lb_insurance_company, lb_insurance_policy_number, lb_insurance_expiry_date;
    DatePicker dp_insurance_expiry_date = new DatePicker();
    JTextField tf_make, tf_model, tf_license_plate, tf_insurance_company, tf_insurance_policy_number;
    JButton btn_add;
    JPanel panel_nord;
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Editer");
    JMenuItem deleteItem = new JMenuItem("Supprimer");
    MyTableModelCarUser myTableModelCarUser;
    JTable table;
    private String currentUserCin;

    public GestionCarUser(String cin) {
        this.currentUserCin = cin;
        this.setTitle("Gestion Voiture - Utilisateur");
        this.setSize(1200, 1200);
        getContentPane().setBackground(new Color(230, 230, 250));

        // Same UI setup as GestionCar
        lb_make = new JLabel("Marque : ");
        lb_model = new JLabel("Modèle : ");
        lb_license_plate = new JLabel("Plaque d'immatriculation : ");
        lb_insurance_company = new JLabel("Société d'assurance : ");
        lb_insurance_policy_number = new JLabel("Numéro de police d'assurance : ");
        lb_insurance_expiry_date = new JLabel("Date d'expiration de l'assurance : ");

        tf_make = new JTextField(10);
        tf_model = new JTextField(10);
        tf_license_plate = new JTextField(10);
        tf_insurance_company = new JTextField(10);
        tf_insurance_policy_number = new JTextField(10);

        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        dateSettings.setAllowEmptyDates(true);
        dp_insurance_expiry_date = new DatePicker(dateSettings);
        dp_insurance_expiry_date.setDateToToday();

        btn_add = new JButton("Ajouter");

        panel_nord = new JPanel();
        panel_nord.setLayout(new GridLayout(4, 2, 5, 5));
        panel_nord.add(lb_make);
        panel_nord.add(tf_make);
        panel_nord.add(lb_model);
        panel_nord.add(tf_model);
        panel_nord.add(lb_license_plate);
        panel_nord.add(tf_license_plate);
        panel_nord.add(lb_insurance_company);
        panel_nord.add(tf_insurance_company);
        panel_nord.add(lb_insurance_policy_number);
        panel_nord.add(tf_insurance_policy_number);
        panel_nord.add(lb_insurance_expiry_date);
        panel_nord.add(dp_insurance_expiry_date);

        JPanel panel_bouton = new JPanel();
        panel_bouton.add(btn_add);

        JPanel panel_principal = new JPanel();
        panel_principal.setLayout(new BoxLayout(panel_principal, BoxLayout.Y_AXIS));
        panel_principal.add(panel_nord);
        panel_principal.add(panel_bouton);
        this.add(panel_principal, BorderLayout.NORTH);

        CarManager carManager = new CarManager();
        carManager.createTable();

        CarUserManager carUserManager = new CarUserManager();
        carUserManager.createTable();

        ResultSet rs = carUserManager.getUserCars(currentUserCin);
        myTableModelCarUser = new MyTableModelCarUser(rs, carUserManager, currentUserCin);
        table = new JTable(myTableModelCarUser);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        this.add(scrollPane, BorderLayout.CENTER);
        table.setFillsViewportHeight(true);

        btn_add.addActionListener(e -> {
            String make = tf_make.getText();
            String model = tf_model.getText();
            String license_plate = tf_license_plate.getText();
            String insurance_company = tf_insurance_company.getText();
            String insurance_policy_number = tf_insurance_policy_number.getText();
            LocalDate expiryDate = dp_insurance_expiry_date.getDate();

            if (make.isEmpty() || model.isEmpty() || license_plate.isEmpty() ||
                    insurance_company.isEmpty() || insurance_policy_number.isEmpty() || expiryDate == null) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs");
                return;
            }

            try {
                // First add to Car table
                int carId = carManager.carExists(make, model, license_plate);

                if (carId == -1) {
                    // Car doesn't exist, insert it
                    int result = carManager.insertCar(make, model, license_plate,
                            insurance_company, insurance_policy_number, java.sql.Date.valueOf(expiryDate));

                    if (result > 0) {
                        carId = carManager.carExists(make, model, license_plate);
                        // Now add to CarUser table
                        int userResult = carUserManager.insertCarUser(currentUserCin, carId);

                        if (userResult > 0) {
                            JOptionPane.showMessageDialog(this, "Voiture ajoutée avec succès");
                            myTableModelCarUser.loadData(); // Refresh the table
                        } else {
                            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la voiture à l'utilisateur");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la voiture");
                    }
                } else {
                    // Car exists, just add to CarUser table
                    int userResult = carUserManager.insertCarUser(currentUserCin, carId);
                    if (userResult > 0) {
                        JOptionPane.showMessageDialog(this, "Voiture associée avec succès");
                        myTableModelCarUser.loadData(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de l'association de la voiture");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(new EcouteurPopupMenuCarUser(this));
        deleteItem.addActionListener(new EcouteurPopupMenuCarUser(this));
    }

    public void editCarUser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String currentMake = table.getValueAt(row, 1).toString();
            String currentModel = table.getValueAt(row, 2).toString();
            String currentLicensePlate = table.getValueAt(row, 3).toString();
            String currentInsuranceCompany = table.getValueAt(row, 4).toString();
            String currentInsurancePolicyNumber = table.getValueAt(row, 5).toString();
            String currentInsuranceExpiryDate = table.getValueAt(row, 6).toString();

            // Create edit dialog (similar to GestionCar)
            JTextField makeField = new JTextField(currentMake);
            JTextField modelField = new JTextField(currentModel);
            JTextField licensePlateField = new JTextField(currentLicensePlate);
            JTextField insuranceCompanyField = new JTextField(currentInsuranceCompany);
            JTextField insurancePolicyNumberField = new JTextField(currentInsurancePolicyNumber);
            DatePicker insuranceExpiryDateField = new DatePicker();
            insuranceExpiryDateField.setDate(LocalDate.parse(currentInsuranceExpiryDate));

            JPanel panel = new JPanel(new GridLayout(6, 2));
            panel.add(new JLabel("Marque:"));
            panel.add(makeField);
            panel.add(new JLabel("Modèle:"));
            panel.add(modelField);
            panel.add(new JLabel("Plaque d'immatriculation:"));
            panel.add(licensePlateField);
            panel.add(new JLabel("Société d'assurance:"));
            panel.add(insuranceCompanyField);
            panel.add(new JLabel("Numéro de police d'assurance:"));
            panel.add(insurancePolicyNumberField);
            panel.add(new JLabel("Date d'expiration de l'assurance:"));
            panel.add(insuranceExpiryDateField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Modifier Voiture", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int carId = Integer.parseInt(table.getValueAt(row, 0).toString());
                    CarManager carManager = new CarManager();

                    int updateResult = carManager.updateCar(carId,
                            makeField.getText(),
                            modelField.getText(),
                            licensePlateField.getText(),
                            insuranceCompanyField.getText(),
                            insurancePolicyNumberField.getText(),
                            java.sql.Date.valueOf(insuranceExpiryDateField.getDate()));

                    if (updateResult > 0) {
                        JOptionPane.showMessageDialog(this, "Voiture modifiée avec succès");
                        myTableModelCarUser.loadData(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                }
            }
        }
    }

    public void deleteCarUser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer cette voiture?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int carId = Integer.parseInt(table.getValueAt(row, 0).toString());
                    CarUserManager carUserManager = new CarUserManager();

                    // First remove from CarUser table
                    int deleteResult = carUserManager.deleteCarUser(currentUserCin, carId);

                    if (deleteResult > 0) {
                        JOptionPane.showMessageDialog(this, "Voiture supprimée avec succès");
                        myTableModelCarUser.loadData(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                }
            }
        }
    }
}