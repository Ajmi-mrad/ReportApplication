package Constat;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;

public class GestionAccidentUser extends JInternalFrame {
    JLabel lb_date, lb_time, lb_location, lb_weather_conditions, lb_description;
    DatePicker dp_date;
    JTextField tf_time, tf_location, tf_weather_conditions, tf_description;
    JButton btn_add;
    JPanel panel_nord;
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Editer");
    JMenuItem deleteItem = new JMenuItem("Supprimer");
    MyTableModelAccidentUser myTableModelAccidentUser;
    JTable table;
    private String currentUserCin;
    AccidentUserManager accidentUserManager;
    AccidentManager accidentManager;

    public GestionAccidentUser(String cin) {
        this.currentUserCin = cin;
        this.setTitle("Gestion Accidents - Utilisateur");
        this.setSize(1200, 1200);
        getContentPane().setBackground(new Color(230, 230, 250));

        // Accident-specific fields
        lb_date = new JLabel("Date : ");
        lb_time = new JLabel("Heure : ");
        lb_location = new JLabel("Lieu : ");
        lb_weather_conditions = new JLabel("Conditions météorologiques : ");
        lb_description = new JLabel("Description : ");

        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dp_date = new DatePicker(dateSettings);

        tf_time = new JTextField(10);
        tf_location = new JTextField(10);
        tf_weather_conditions = new JTextField(10);
        tf_description = new JTextField(10);

        btn_add = new JButton("Ajouter");

        // Layout for input fields
        panel_nord = new JPanel();
        panel_nord.setLayout(new GridLayout(5, 2, 5, 5));
        panel_nord.add(lb_date);
        panel_nord.add(dp_date);
        panel_nord.add(lb_time);
        panel_nord.add(tf_time);
        panel_nord.add(lb_location);
        panel_nord.add(tf_location);
        panel_nord.add(lb_weather_conditions);
        panel_nord.add(tf_weather_conditions);
        panel_nord.add(lb_description);
        panel_nord.add(tf_description);

        JPanel panel_bouton = new JPanel();
        panel_bouton.add(btn_add);

        JPanel panel_principal = new JPanel();
        panel_principal.setLayout(new BoxLayout(panel_principal, BoxLayout.Y_AXIS));
        panel_principal.add(panel_nord);
        panel_principal.add(panel_bouton);
        this.add(panel_principal, BorderLayout.NORTH);

        // Initialize table model and table
        accidentUserManager = new AccidentUserManager();
        accidentUserManager.createTable();

        accidentManager = new AccidentManager();
        accidentManager.createTable();

        ResultSet rs = accidentUserManager.getUserAccidents(currentUserCin);
        myTableModelAccidentUser = new MyTableModelAccidentUser(rs, accidentUserManager, currentUserCin);
        //myTableModelAccidentUser.loadData();
        table = new JTable(myTableModelAccidentUser);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        this.add(scrollPane, BorderLayout.CENTER);
        table.setFillsViewportHeight(true);

        // Add button action
        btn_add.addActionListener(e -> {
            String date = dp_date.getText();
            String time = tf_time.getText();
            String location = tf_location.getText();
            String weatherConditions = tf_weather_conditions.getText();
            String description = tf_description.getText();

            if (date.isEmpty() || time.isEmpty() || location.isEmpty() || weatherConditions.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs");
                return;
            }

            Time sqlTime = Time.valueOf(time);
            int idAccident = accidentManager.insertAccident(java.sql.Date.valueOf(date), sqlTime, location, weatherConditions, description);

            if (idAccident > 0) {
                int resultUser = accidentUserManager.insertAccidentUser(currentUserCin, idAccident);
                if (resultUser > 0) {
                    JOptionPane.showMessageDialog(this, "Accident ajouté avec succès");
                    myTableModelAccidentUser.loadData(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'association de l'accident à l'utilisateur");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'accident");
            }
        });
        // Popup menu actions
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(e -> editAccidentUser());
        deleteItem.addActionListener(e -> deleteAccidentUser());
    }

    public void editAccidentUser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int accidentId = Integer.parseInt(table.getValueAt(row, 0).toString());
            String currentDate = table.getValueAt(row, 1).toString();
            String currentTime = table.getValueAt(row, 2).toString();
            String currentLocation = table.getValueAt(row, 3).toString();
            String currentWeatherConditions = table.getValueAt(row, 4).toString();
            String currentDescription = table.getValueAt(row, 5).toString();

            DatePicker dateField = new DatePicker();
            dateField.setDate(LocalDate.parse(currentDate));
            JTextField timeField = new JTextField(currentTime);
            JTextField locationField = new JTextField(currentLocation);
            JTextField weatherConditionsField = new JTextField(currentWeatherConditions);
            JTextField descriptionField = new JTextField(currentDescription);

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Date:"));
            panel.add(dateField);
            panel.add(new JLabel("Heure:"));
            panel.add(timeField);
            panel.add(new JLabel("Lieu:"));
            panel.add(locationField);
            panel.add(new JLabel("Conditions météorologiques:"));
            panel.add(weatherConditionsField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Modifier Accident", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String date = dateField.getDate().toString();
                String time = timeField.getText();
                String location = locationField.getText();
                String weatherConditions = weatherConditionsField.getText();
                String description = descriptionField.getText();

                int updateResult = accidentManager.updateAccident(accidentId, java.sql.Date.valueOf(date), Time.valueOf(time), location, weatherConditions, description);
                if (updateResult > 0) {
                    JOptionPane.showMessageDialog(this, "Accident modifié avec succès");
                    myTableModelAccidentUser.loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
                }
            }
        }
    }

    public void deleteAccidentUser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer cet accident?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int accidentId = Integer.parseInt(table.getValueAt(row, 0).toString());
                    // First delete from child table
                    int deleteResult = myTableModelAccidentUser.deleteAccidentUser(currentUserCin, accidentId);
                    if (deleteResult > 0) {
                        // Then delete from parent table
                        int deleteAccident = accidentManager.deleteAccident(accidentId);
                        if (deleteAccident > 0) {
                            myTableModelAccidentUser.loadData();
                            JOptionPane.showMessageDialog(this, "Accident supprimé avec succès");
                        }
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