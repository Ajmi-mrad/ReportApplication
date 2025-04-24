package Constat;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;

public class GestionAccident extends JInternalFrame {
    JLabel lb_date, lb_time, lb_location, lb_weather_conditions, lb_description;
    DatePicker dp_date;
    JTextField tf_time, tf_location, tf_weather_conditions, tf_description;
    JButton btn_add;
    JPanel panel_nord;
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Editer");
    JMenuItem deleteItem = new JMenuItem("Supprimer");
    MyTableModelAccident myTableModelAccident;
    JTable table;

    public GestionAccident() {
        this.setTitle("Gestion Accidents");
        this.setSize(1200, 1200);
        getContentPane().setBackground(new Color(230, 230, 250));

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

        AccidentManager accidentManager = new AccidentManager();
        accidentManager.createTable();
        ResultSet rs = accidentManager.selectAllAccidents();

        myTableModelAccident = new MyTableModelAccident(rs, accidentManager);
        table = new JTable(myTableModelAccident);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        this.add(scrollPane, BorderLayout.CENTER);
        table.setFillsViewportHeight(true);

        btn_add.addActionListener(e -> {
            String date = dp_date.getText();
            String time = tf_time.getText();
            String location = tf_location.getText();
            String weatherConditions = tf_weather_conditions.getText();
            String description = tf_description.getText();

            if (date.isEmpty() || time.isEmpty() || location.isEmpty() || weatherConditions.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Entrer tous les champs");
                return;
            }

            int result = myTableModelAccident.ajoutAccident(date, time, location, weatherConditions, description);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Accident ajouté avec succès");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'accident");
            }
        });

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(new EcouteurPopupMenuAccident(this));
        deleteItem.addActionListener(new EcouteurPopupMenuAccident(this));
    }

    public void editAccident() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
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

                int updateResult = myTableModelAccident.modifierAccident(id, date, time, location, weatherConditions, description);
                if (updateResult > 0) {
                    JOptionPane.showMessageDialog(this, "Accident modifié avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
                }
            }
        }
    }

    public void deleteAccident() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            int result = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer cet accident ?", "Supprimer Accident", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int deleteResult = myTableModelAccident.suppAccident(id);
                if (deleteResult > 0) {
                    JOptionPane.showMessageDialog(this, "Accident supprimé avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");
                }
            }
        }
    }
}