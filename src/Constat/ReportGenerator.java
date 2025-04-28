package Constat;

import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ReportGenerator {
    private final AccidentManager accidentManager;
    private final CarManager carManager;
    private final DriverrManager driverManager;

    public ReportGenerator() {
        this.accidentManager = new AccidentManager();
        this.carManager = new CarManager();
        this.driverManager = new DriverrManager();
    }

    public void generateReport(int accidentId, int vehicleIdA, int vehicleIdB,
                               String cinA, String cinB) {
        try {
            // Get all data first before building the report
            AccidentData accidentData = getAccidentData(accidentId);
            CarData carAData = getCarData(vehicleIdA);
            CarData carBData = getCarData(vehicleIdB);
            DriverData driverAData = getDriverData(cinA);
            DriverData driverBData = getDriverData(cinB);

            // Validate all data exists
            if (accidentData == null || carAData == null || carBData == null ||
                    driverAData == null || driverBData == null) {
                JOptionPane.showMessageDialog(null, "Could not retrieve all required data",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create the report content
            String reportContent = buildReportContent(accidentData, carAData, carBData,
                    driverAData, driverBData);

            // Display the report
            displayReport(reportContent);

            // Option to save as file
            if (JOptionPane.showConfirmDialog(null, "Do you want to save this report as a file?",
                    "Save Report", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                saveReportToFile(reportContent, accidentId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private AccidentData getAccidentData(int accidentId) throws SQLException {
        try (ResultSet rs = accidentManager.selectAccidentById(accidentId)) {
            if (rs.next()) {
                return new AccidentData(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("location"),
                        rs.getString("weather_conditions"),
                        rs.getString("description")
                );
            }
        }
        return null;
    }

    private CarData getCarData(int vehicleId) throws SQLException {
        try (ResultSet rs = carManager.carExist(vehicleId)) {
            if (rs.next()) {
                return new CarData(
                        rs.getInt("id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        rs.getString("insurance_company"),
                        rs.getString("insurance_policy_number"),
                        rs.getDate("insurance_expiry_date")
                );
            }
        }
        return null;
    }

    private DriverData getDriverData(String cin) throws SQLException {
        try (ResultSet rs = driverManager.driverrExist(cin)) {
            if (rs.next()) {
                return new DriverData(
                        rs.getString("cin"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("license_number"),
                        rs.getString("phone_number")
                );
            }
        }
        return null;
    }

    private String buildReportContent(AccidentData accident, CarData carA, CarData carB,
                                      DriverData driverA, DriverData driverB) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("           ACCIDENT REPORT\n");
        sb.append("========================================\n\n");

        // Accident details
        sb.append("ACCIDENT DETAILS:\n");
        sb.append("ID: ").append(accident.id()).append("\n");
        sb.append("Date: ").append(dateFormat.format(accident.date())).append("\n");
        sb.append("Time: ").append(timeFormat.format(accident.time())).append("\n");
        sb.append("Location: ").append(accident.location()).append("\n");
        sb.append("Weather Conditions: ").append(accident.weatherConditions()).append("\n");
        sb.append("Description: ").append(accident.description()).append("\n\n");

        // Driver A details
        sb.append("DRIVER A DETAILS:\n");
        sb.append("Name: ").append(driverA.firstName()).append(" ")
                .append(driverA.lastName()).append("\n");
        sb.append("License Number: ").append(driverA.licenseNumber()).append("\n");
        sb.append("Phone: ").append(driverA.phoneNumber()).append("\n\n");

        // Vehicle A details
        sb.append("VEHICLE A DETAILS:\n");
        sb.append("Make: ").append(carA.make()).append("\n");
        sb.append("Model: ").append(carA.model()).append("\n");
        sb.append("License Plate: ").append(carA.licensePlate()).append("\n");
        sb.append("Insurance: ").append(carA.insuranceCompany()).append(" (")
                .append(carA.insurancePolicyNumber()).append(")\n\n");

        // Driver B details
        sb.append("DRIVER B DETAILS:\n");
        sb.append("Name: ").append(driverB.firstName()).append(" ")
                .append(driverB.lastName()).append("\n");
        sb.append("License Number: ").append(driverB.licenseNumber()).append("\n");
        sb.append("Phone: ").append(driverB.phoneNumber()).append("\n\n");

        // Vehicle B details
        sb.append("VEHICLE B DETAILS:\n");
        sb.append("Make: ").append(carB.make()).append("\n");
        sb.append("Model: ").append(carB.model()).append("\n");
        sb.append("License Plate: ").append(carB.licensePlate()).append("\n");
        sb.append("Insurance: ").append(carB.insuranceCompany()).append(" (")
                .append(carB.insurancePolicyNumber()).append(")\n\n");

        sb.append("========================================\n");
        sb.append("END OF REPORT\n");
        sb.append("========================================\n");

        return sb.toString();
    }

    private void displayReport(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        JOptionPane.showMessageDialog(null, scrollPane, "Accident Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveReportToFile(String content, int accidentId) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("AccidentReport_" + accidentId + ".txt"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.write(content);
                JOptionPane.showMessageDialog(null, "Report saved successfully to:\n" +
                        file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Record classes to hold data
    private record AccidentData(int id, Date date, Time time, String location,
                                String weatherConditions, String description) {}

    private record CarData(int id, String make, String model, String licensePlate,
                           String insuranceCompany, String insurancePolicyNumber,
                           Date insuranceExpiryDate) {}

    private record DriverData(String cin, String firstName, String lastName,
                              String licenseNumber, String phoneNumber) {}
}