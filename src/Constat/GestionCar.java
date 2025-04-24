package Constat;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import Constat.MyTableModelCar;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;

public class GestionCar extends JInternalFrame {
    // this is my fields String make, String model, String license_plate, String insurance_company, String insurance_policy_number, Date insurance_expiry_date
    JLabel lb_make, lb_model, lb_license_plate, lb_insurance_company, lb_insurance_policy_number, lb_insurance_expiry_date;
    // the insurance_expiry_date is a date so I will use JDatePicker for it
    DatePicker dp_insurance_expiry_date = new DatePicker();
    JTextField tf_make, tf_model, tf_license_plate, tf_insurance_company, tf_insurance_policy_number;

    JButton btn_add;
    JPanel panel_nord;
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Editer");
    JMenuItem deleteItem = new JMenuItem("Supprimer");
    MyTableModelCar myTableModelCar;
    JTable table;

    private void validateTextField(JTextField textField, String placeholder) {
        if (textField.getText().isEmpty() || textField.getText().equals(placeholder)) {
            textField.setBackground(Color.RED);
        } else {
            textField.setBackground(Color.WHITE);
        }
    }

    public GestionCar() {
        this.setTitle("Gestion Voiture");
        this.setSize(1200, 1200);

        getContentPane().setBackground(new Color(230, 230, 250));

        lb_make = new JLabel("Marque : ");
        lb_model = new JLabel("Modèle : ");
        lb_license_plate = new JLabel("Plaque d'immatriculation : ");
        lb_insurance_company = new JLabel("Société d'assurance : ");
        lb_insurance_policy_number = new JLabel("Numéro de police d'assurance : ");
        lb_insurance_expiry_date = new JLabel("Date d'expiration de l'assurance : ");

        tf_make = new JTextField(10);
        tf_make.setText("Entrer la marque");
        tf_model = new JTextField(10);
        tf_model.setText("Entrer le modèle");
        tf_license_plate = new JTextField(10);
        tf_license_plate.setText("Entrer la plaque d'immatriculation");
        tf_insurance_company = new JTextField(10);
        tf_insurance_company.setText("Entrer la société d'assurance");
        tf_insurance_policy_number = new JTextField(10);
        tf_insurance_policy_number.setText("Entrer le numéro de police d'assurance");
        DatePickerSettings dateSettings = new DatePickerSettings();
        // Set the locale to French
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        // Set the date to today
        dateSettings.setAllowEmptyDates(true);
        // Set the date range limits to null
        //dateSettings.setDateRangeLimits(null, null);
        dp_insurance_expiry_date = new DatePicker(dateSettings);
        dp_insurance_expiry_date.setText("Sélectionner la date d'expiration de l'assurance");
        dp_insurance_expiry_date.setDateToToday();

/*
        tf_nom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_nom.getText().equals("Entrer votre nom")) {
                    tf_nom.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_nom, "Entrer votre nom");
            }
        });

        tf_prenom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_prenom.getText().equals("Entrer votre prenom")) {
                    tf_prenom.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_prenom, "Entrer votre prenom");
            }
        });

        tf_cin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_cin.getText().equals("Entrer votre cin")) {
                    tf_cin.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_cin, "Entrer votre cin");
            }
        });

        tf_moyenne.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_moyenne.getText().equals("Entrer votre moyenne")) {
                    tf_moyenne.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_moyenne, "Entrer votre moyenne");
            }
        });

 */
        // appel to EcouteurFocudEtudiant
        /*
        tf_nom.addFocusListener(new EcouteurFocusEtudiant(this));
        tf_prenom.addFocusListener(new EcouteurFocusEtudiant(this));
        tf_cin.addFocusListener(new EcouteurFocusEtudiant(this));
        tf_moyenne.addFocusListener(new EcouteurFocusEtudiant(this));
*/
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
        ResultSet rs = carManager.selectAllCars();

        myTableModelCar = new MyTableModelCar(rs, carManager);
        table = new JTable(myTableModelCar);
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
            String insurance_expiry_date = dp_insurance_expiry_date.getText();

            // make a few condition of insertion
            if (make.isEmpty() || model.isEmpty() || license_plate.isEmpty() || insurance_company.isEmpty() || insurance_policy_number.isEmpty() || insurance_expiry_date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Entrer tous les champs");
                return;
            }
            if (make.equals("Entrer la marque") || model.equals("Entrer le modèle") || license_plate.equals("Entrer la plaque d'immatriculation") || insurance_company.equals("Entrer la société d'assurance") || insurance_policy_number.equals("Entrer le numéro de police d'assurance")) {
                JOptionPane.showMessageDialog(this, "Entrer tous les champs");
                return;
            }
            try {
                // check if the car exist in the table
                int carExist = myTableModelCar.carExist(make, model, license_plate);
                if (carExist > 0) {
                    JOptionPane.showMessageDialog(this, "La voiture existe déjà");
                    return;
                }
                // check if the insurance_expiry_date is a valid date
                if (insurance_expiry_date.equals("Sélectionner la date d'expiration de l'assurance")) {
                    JOptionPane.showMessageDialog(this, "Sélectionner la date d'expiration de l'assurance");
                    return;
                }
                // insert the car in the table
                int result = myTableModelCar.ajoutCar(make, model, license_plate, insurance_company, insurance_policy_number, insurance_expiry_date);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Voiture ajoutée avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la voiture");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            };
        });

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(new EcouteurPopupMenuCar(this));
        deleteItem.addActionListener(new EcouteurPopupMenuCar(this));
    }

    public void editCar() {
        int row = table.getSelectedRow();
        if (row != -1) {
            // get the current values of the selected row

            String currentMake = table.getValueAt(row, 1).toString();
            String currentModel = table.getValueAt(row, 2).toString();
            String currentLicensePlate = table.getValueAt(row, 3).toString();
            String currentInsuranceCompany = table.getValueAt(row, 4).toString();
            String currentInsurancePolicyNumber = table.getValueAt(row, 5).toString();
            String currentInsuranceExpiryDate = table.getValueAt(row, 6).toString();

            // create a new panel with text fields for each attribute
            JTextField makeField = new JTextField(currentMake);
            JTextField modelField = new JTextField(currentModel);
            JTextField licensePlateField = new JTextField(currentLicensePlate);
            JTextField insuranceCompanyField = new JTextField(currentInsuranceCompany);
            JTextField insurancePolicyNumberField = new JTextField(currentInsurancePolicyNumber);
            DatePicker insuranceExpiryDateField = new DatePicker();
            insuranceExpiryDateField.setDate(LocalDate.parse(currentInsuranceExpiryDate));


            JPanel panel = new JPanel(new GridLayout(3, 2));
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
                String make = makeField.getText();
                String model = modelField.getText();
                String licensePlate = licensePlateField.getText();
                String insuranceCompany = insuranceCompanyField.getText();
                String insurancePolicyNumber = insurancePolicyNumberField.getText();
                String insuranceExpiryDate = insuranceExpiryDateField.getDate().toString();


                // get the id of th car
                int carId = myTableModelCar.carExist(currentMake, currentModel, currentLicensePlate);

                int updateResult = myTableModelCar.modifierCar(carId, make, model, licensePlate, insuranceCompany, insurancePolicyNumber, insuranceExpiryDate);
                if (updateResult > 0) {
                    JOptionPane.showMessageDialog(this, "Voiture modifié avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(this, "Aucune modification");
            }
        }
    }

    public void deleteCar() {
        int row = table.getSelectedRow();
        if (row != -1) {
            /*
            int carId = myTableModelcar.carExist(make, model, licensePlate);
            System.out.println("Trying to delete CIN: " + carId);

             */
            // get the current values of the selected row
            String make = table.getValueAt(row, 1).toString();
            String model = table.getValueAt(row, 2).toString();
            String licensePlate = table.getValueAt(row, 3).toString();
            JPanel panelConfirmation = new JPanel(new GridLayout(3, 2));
            panelConfirmation.add(new JLabel("Êtes-vous sûr de vouloir supprimer cette voiture ?"));
            int result = JOptionPane.showConfirmDialog(null, panelConfirmation,
                    "Supprimer étudiant", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int deleteResult = myTableModelCar.suppCar(make, model, licensePlate);
                if (deleteResult > 0) {
                    JOptionPane.showMessageDialog(this, "Voiture supprimé avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'étudiant");
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(this, "Aucune suppression");
            }
        }
    }
}
