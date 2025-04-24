package Constat;

import javax.swing.*;
import java.awt.*;
import java.sql.Driver;
import java.sql.ResultSet;

public class GestionReport extends JInternalFrame {
    private JTextField tf_accidentId, tf_vehicleAId, tf_vehicleBId, tf_driverACin, tf_driverBCin, tf_descriptionA, tf_descriptionB, tf_damagesA, tf_damagesB;
    private JButton btn_add;
    private MyTableModelReport myTableModelReport;
    private JTable table;
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem editItem = new JMenuItem("Editer");
    private JMenuItem deleteItem = new JMenuItem("Supprimer");

    public GestionReport() {
        this.setTitle("Gestion des Rapports");
        this.setSize(1200, 800);
        this.setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        tf_accidentId = new JTextField();
        tf_vehicleAId = new JTextField();
        tf_vehicleBId = new JTextField();
        tf_driverACin = new JTextField();
        tf_driverBCin = new JTextField();
        tf_descriptionA = new JTextField();
        tf_descriptionB = new JTextField();
        tf_damagesA = new JTextField();
        tf_damagesB = new JTextField();
        btn_add = new JButton("Ajouter");

        formPanel.add(new JLabel("Accident ID:"));
        formPanel.add(tf_accidentId);
        formPanel.add(new JLabel("Vehicle A ID:"));
        formPanel.add(tf_vehicleAId);
        formPanel.add(new JLabel("Vehicle B ID:"));
        formPanel.add(tf_vehicleBId);
        formPanel.add(new JLabel("Driver A CIN:"));
        formPanel.add(tf_driverACin);
        formPanel.add(new JLabel("Driver B CIN:"));
        formPanel.add(tf_driverBCin);
        formPanel.add(new JLabel("Description A:"));
        formPanel.add(tf_descriptionA);
        formPanel.add(new JLabel("Description B:"));
        formPanel.add(tf_descriptionB);
        formPanel.add(new JLabel("Damages A:"));
        formPanel.add(tf_damagesA);
        formPanel.add(new JLabel("Damages B:"));
        formPanel.add(tf_damagesB);
        formPanel.add(new JLabel());
        formPanel.add(btn_add);

        this.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        ReportManager reportManager = new ReportManager();
        CarManager carManager = new CarManager();
        DriverrManager driverrManager = new DriverrManager();
        AccidentManager accidentManager = new AccidentManager();
        reportManager.createTable();
        ResultSet rs = reportManager.selectAllReports();
        myTableModelReport = new MyTableModelReport(rs, reportManager);
        table = new JTable(myTableModelReport);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        // Popup Menu
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(e -> editReport());
        deleteItem.addActionListener(e -> deleteReport());

        // Add Button Action
        btn_add.addActionListener(e -> {
            try {
                int accidentId = Integer.parseInt(tf_accidentId.getText());
                int vehicleAId = Integer.parseInt(tf_vehicleAId.getText());
                int vehicleBId = Integer.parseInt(tf_vehicleBId.getText());
                String driverACin = tf_driverACin.getText();
                String driverBCin = tf_driverBCin.getText();
                String descriptionA = tf_descriptionA.getText();
                String descriptionB = tf_descriptionB.getText();
                String damagesA = tf_damagesA.getText();
                String damagesB = tf_damagesB.getText();

                if (driverACin.isEmpty() || driverBCin.isEmpty() || descriptionA.isEmpty() || descriptionB.isEmpty() || damagesA.isEmpty() || damagesB.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                    return;
                }
                /*
                // check if accidentId exist or no also the vehicleAId, vehidcleBId, driverACin and driverBCin from the database using their manager
                if (accidentManager.accidentExist(accidentId) == null) {
                    JOptionPane.showMessageDialog(this, "L'accident n'existe pas.");
                    return;
                }
                if (carManager.carExist(vehicleAId) == null) {
                    JOptionPane.showMessageDialog(this, "Le véhicule A n'existe pas.");
                    return;
                }
                if (carManager.carExist(vehicleBId) == null) {
                    JOptionPane.showMessageDialog(this, "Le véhicule B n'existe pas.");
                    return;
                }
                if (driverrManager.driverrExist(driverACin) == null) {
                    JOptionPane.showMessageDialog(this, "Le conducteur A n'existe pas.");
                    return;
                }
                if (driverrManager.driverrExist(driverBCin) == null) {
                    JOptionPane.showMessageDialog(this, "Le conducteur B n'existe pas.");
                    return;
                }
                 */
                if (reportManager.reportExists(accidentId, vehicleAId, vehicleBId, driverACin, driverBCin) != -1) {
                    JOptionPane.showMessageDialog(this, "Le rapport existe déjà.");
                    return;
                }

                int result = myTableModelReport.ajoutReport(accidentId, vehicleAId, vehicleBId, driverACin, driverBCin, descriptionA, descriptionB, damagesA, damagesB);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Rapport ajouté avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du rapport.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides pour les IDs.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });
    }

    public void editReport() {
        int row = table.getSelectedRow();
        if (row != -1) {
            try {
                // Safely retrieve values from the table
                Object idObj = table.getValueAt(row, 0); // Column 0: ID
                Object accidentIdObj = table.getValueAt(row, 1); // Column 1: Accident ID
                Object vehicleAIdObj = table.getValueAt(row, 2); // Column 2: Vehicle A ID
                Object vehicleBIdObj = table.getValueAt(row, 3); // Column 3: Vehicle B ID
                Object driverACinObj = table.getValueAt(row, 4); // Column 4: Driver A CIN
                Object driverBCinObj = table.getValueAt(row, 5); // Column 5: Driver B CIN
                Object descriptionAObj = table.getValueAt(row, 6); // Column 6: Description A
                Object descriptionBObj = table.getValueAt(row, 7); // Column 7: Description B
                Object damagesAObj = table.getValueAt(row, 8); // Column 8: Damages A
                Object damagesBObj = table.getValueAt(row, 9); // Column 9: Damages B

                // Handle null values
                int id = idObj != null ? Integer.parseInt(idObj.toString()) : 0;
                int accidentId = accidentIdObj != null ? Integer.parseInt(accidentIdObj.toString()) : 0;
                int vehicleAId = vehicleAIdObj != null ? Integer.parseInt(vehicleAIdObj.toString()) : 0;
                int vehicleBId = vehicleBIdObj != null ? Integer.parseInt(vehicleBIdObj.toString()) : 0;
                String driverACin = driverACinObj != null ? driverACinObj.toString() : "";
                String driverBCin = driverBCinObj != null ? driverBCinObj.toString() : "";
                String descriptionA = descriptionAObj != null ? descriptionAObj.toString() : "";
                String descriptionB = descriptionBObj != null ? descriptionBObj.toString() : "";
                String damagesA = damagesAObj != null ? damagesAObj.toString() : "";
                String damagesB = damagesBObj != null ? damagesBObj.toString() : "";

                // Create input fields for editing
                JTextField accidentIdField = new JTextField(String.valueOf(accidentId));
                JTextField vehicleAIdField = new JTextField(String.valueOf(vehicleAId));
                JTextField vehicleBIdField = new JTextField(String.valueOf(vehicleBId));
                JTextField driverACinField = new JTextField(driverACin);
                JTextField driverBCinField = new JTextField(driverBCin);
                JTextField descriptionAField = new JTextField(descriptionA);
                JTextField descriptionBField = new JTextField(descriptionB);
                JTextField damagesAField = new JTextField(damagesA);
                JTextField damagesBField = new JTextField(damagesB);

                JPanel panel = new JPanel(new GridLayout(9, 2));
                panel.add(new JLabel("Accident ID:"));
                panel.add(accidentIdField);
                panel.add(new JLabel("Vehicle A ID:"));
                panel.add(vehicleAIdField);
                panel.add(new JLabel("Vehicle B ID:"));
                panel.add(vehicleBIdField);
                panel.add(new JLabel("Driver A CIN:"));
                panel.add(driverACinField);
                panel.add(new JLabel("Driver B CIN:"));
                panel.add(driverBCinField);
                panel.add(new JLabel("Description A:"));
                panel.add(descriptionAField);
                panel.add(new JLabel("Description B:"));
                panel.add(descriptionBField);
                panel.add(new JLabel("Damages A:"));
                panel.add(damagesAField);
                panel.add(new JLabel("Damages B:"));
                panel.add(damagesBField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Modifier Rapport", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int updatedAccidentId = Integer.parseInt(accidentIdField.getText());
                    int updatedVehicleAId = Integer.parseInt(vehicleAIdField.getText());
                    int updatedVehicleBId = Integer.parseInt(vehicleBIdField.getText());
                    String updatedDriverACin = driverACinField.getText();
                    String updatedDriverBCin = driverBCinField.getText();
                    String updatedDescriptionA = descriptionAField.getText();
                    String updatedDescriptionB = descriptionBField.getText();
                    String updatedDamagesA = damagesAField.getText();
                    String updatedDamagesB = damagesBField.getText();

                    int updateResult = myTableModelReport.updateReport(
                            id, updatedAccidentId, updatedVehicleAId, updatedVehicleBId,
                            updatedDriverACin, updatedDriverBCin,
                            updatedDescriptionA, updatedDescriptionB,
                            updatedDamagesA, updatedDamagesB
                    );

                    if (updateResult > 0) {
                        JOptionPane.showMessageDialog(this, "Rapport modifié avec succès.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides pour les IDs.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        }
    }
    public void deleteReport() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            int result = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer ce rapport ?", "Supprimer Rapport", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int deleteResult = myTableModelReport.deleteReport(id);
                if (deleteResult > 0) {
                    JOptionPane.showMessageDialog(this, "Rapport supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                }
            }
        }
    }
}