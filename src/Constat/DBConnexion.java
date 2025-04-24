package Constat;

import Constat.CarManager;

import java.sql.ResultSet;

public class DBConnexion {
    public static void main(String[] args) {
        // import the connection and all the methode from CarManager
        CarManager carManager = new CarManager();

        // create a new table
        carManager.createTable();
        // insert some data
/*
        carManager.insertCar("Toyota", "Corolla", "1234ABC", "AXA", "123456789", java.sql.Date.valueOf("2024-12-31"));
        carManager.insertCar("Honda", "Civic", "5678DEF", "Allianz", "987654321", java.sql.Date.valueOf("2025-06-30"));
        carManager.insertCar("Ford", "Focus", "9101GHI", "Generali", "456789123", java.sql.Date.valueOf("2026-03-31"));
        carManager.insertCar("Chevrolet", "Malibu", "1122JKL", "Zurich", "321654987", java.sql.Date.valueOf("2027-09-30"));


 */
        //carManager.deleteAllCarsWithDependencies();
        // select all the data
        //etudiantManager.selectAllEtudiants();
        ResultSet rs = carManager.selectAllCars();
        try {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Make: " + rs.getString("make"));
                System.out.println("Model: " + rs.getString("model"));
                System.out.println("License Plate: " + rs.getString("license_plate"));
                System.out.println("Insurance Company: " + rs.getString("insurance_company"));
                System.out.println("Insurance Policy Number: " + rs.getString("insurance_policy_number"));
                System.out.println("Insurance Expiry Date: " + rs.getDate("insurance_expiry_date"));
                System.out.println("-----------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // import the connection and all the methode from DriverrManager
        DriverrManager driverrManager = new DriverrManager();
        // create a new table
        driverrManager.createTableDriver();
        // insert some data
        driverrManager.insertDriver("123456", "John", "Doe", java.sql.Date.valueOf("1990-01-01"), "123 Main St", "555-1234", "ABC123", java.sql.Date.valueOf("2025-12-31"), "password");
        driverrManager.insertDriver("654321", "Jane", "Smith", java.sql.Date.valueOf("1992-02-02"), "456 Elm St", "555-5678", "XYZ789", java.sql.Date.valueOf("2026-06-30"), "password");

        // import the connection and all the methode from AccidentManager

        AccidentManager accidentManager = new AccidentManager();
        // create a new table
        accidentManager.createTable();
        // insert some data
        accidentManager.insertAccident(java.sql.Date.valueOf("2023-10-01"), java.sql.Time.valueOf("12:00:00"), "123 Main St", "Sunny", "Minor fender bender");
        accidentManager.insertAccident(java.sql.Date.valueOf("2023-10-02"), java.sql.Time.valueOf("14:00:00"), "456 Elm St", "Rainy", "Major collision");
        // select all the data
        //etudiantManager.selectAllEtudiants();
        ResultSet rs2 = accidentManager.selectAllAccidents();
        try {
            while (rs2.next()) {
                System.out.println("ID: " + rs2.getInt("id"));
                System.out.println("Date: " + rs2.getDate("date"));
                System.out.println("Time: " + rs2.getTime("time"));
                System.out.println("Location: " + rs2.getString("location"));
                System.out.println("Weather Conditions: " + rs2.getString("weather_conditions"));
                System.out.println("Description: " + rs2.getString("description"));
                System.out.println("-----------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //import the connection and all the methode from ReportManager
        ReportManager reportManager = new ReportManager();
        // create a new table
        reportManager.createTable();
        // insert some data
        reportManager.insertReport(1, 1, 1, "123456", "654321", "zazaeaze", "Minor damage to vehicle A", "Minor damage to vehicle B", "Scratches on bumper");
        reportManager.insertReport(2, 2, 2, "654321", "123456", "ezaaz", "Major damage to vehicle A", "Minor damage to vehicle B", "Total loss");

        // select all the data
        //etudiantManager.selectAllEtudiants();
        // delete all from the table

        ResultSet rs3 = reportManager.selectAllReports();
        try {
            while (rs3.next()) {
                // dont forgot the name of the driverr and the license plate of the car and the description of the accident
                System.out.println("ID: " + rs3.getInt("id"));
                System.out.println("Accident ID: " + rs3.getInt("accident_id"));
                System.out.println("Vehicle A ID: " + rs3.getInt("vehicle_a_id"));
                System.out.println("Vehicle B ID: " + rs3.getInt("vehicle_b_id"));
                System.out.println("Driver A ID: " + rs3.getString("driver_a_id"));
                System.out.println("Driver B ID: " + rs3.getString("driver_b_id"));
                System.out.println("Description A: " + rs3.getString("description_a"));
                System.out.println("Description B: " + rs3.getString("description_b"));
                System.out.println("Damages A: " + rs3.getString("damages_a"));
                System.out.println("Damages B: " + rs3.getString("damages_b"));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
