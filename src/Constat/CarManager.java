package Constat;
import java.sql.*;

import static Constat.Config.*;


public class CarManager {
    private static Connection connection = null;
    private static Statement statement = null;

    public CarManager() {
        try {
            // load and register the driver
            Class.forName(driverName);
            System.out.println("Connexion établie");
            // create a connection
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int carExists(String make, String model, String license_plate) {
        String req = "SELECT id FROM Car WHERE make = ? AND model = ? AND license_plate = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, make);
            preparedStatement.setString(2, model);
            preparedStatement.setString(3, license_plate);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1; // Car not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void createTable() {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Car", null);
            if (tables.next()) {
                System.out.println("Table Car already exists");
            } else {
                // create table Car that have an id, make, model, license_plate, insurance_company, insurance_policy_number, insurance_expiry_date
                String query = "CREATE TABLE Car (id int primary key auto_increment, make VARCHAR(255), model VARCHAR(255), license_plate VARCHAR(255), insurance_company VARCHAR(255), insurance_policy_number VARCHAR(255), insurance_expiry_date DATE)";
                statement.executeUpdate(query);
                System.out.println("Table Car created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertCar(String make, String model, String license_plate, String insurance_company, String insurance_policy_number, Date insurance_expiry_date) {
        String req = "INSERT INTO Car (make, model, license_plate, insurance_company, insurance_policy_number, insurance_expiry_date) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            // dont forget the id that generated automatically
            preparedStatement.setString(1, make);
            preparedStatement.setString(2, model);
            preparedStatement.setString(3, license_plate);
            preparedStatement.setString(4, insurance_company);
            preparedStatement.setString(5, insurance_policy_number);
            preparedStatement.setDate(6, insurance_expiry_date);
            return preparedStatement.executeUpdate();
            //System.out.println("Etudiant added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }



    /*
        public void selectAllEtudiants() {
            if (statement != null) {
                try {
                    String query = "SELECT * FROM Etudiant";
                    ResultSet rs = statement.executeQuery(query);
                    // get the result metadata
                    ResultSetMetaData rsmd = rs.getMetaData();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

     */
    // funciton that return juste the resultSet
    public ResultSet selectAllCars() {
        ResultSet rs = null;
        if (statement != null) {
            try {
                String query = "SELECT * FROM Car";
                rs = statement.executeQuery(query);
                // get the result metadata

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
    public ResultSet carExist(int id) {
        ResultSet rs = null;
        if (statement != null) {
            try {
                String query = "SELECT * FROM Car WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
    // affiche le resultSet
    public void afficheResultSet(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int deleteCar(int id){
        String req = "DELETE FROM Car WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, id);
            System.out.println("Executing delete query for CIN: " + id);
            int result = preparedStatement.executeUpdate();
            System.out.println("Deleted from DB rows: " + result);
            return result;
        } catch (SQLException e) {
            System.err.println("Error deleting etudiant with CIN " + id);
            e.printStackTrace();
            return 0;
        }
    }

    public int updateCar(int id,String make, String model, String license_plate, String insurance_company, String insurance_policy_number, Date insurance_expiry_date) {
        String req = "UPDATE Car SET make = ?, model = ?, license_plate = ?, insurance_company = ?, insurance_policy_number = ?, insurance_expiry_date = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, make);
            preparedStatement.setString(2, model);
            preparedStatement.setString(3, license_plate);
            preparedStatement.setString(4, insurance_company);
            preparedStatement.setString(5, insurance_policy_number);
            preparedStatement.setDate(6, insurance_expiry_date);
            preparedStatement.setInt(7, id);


            // Log the values being updated
            System.out.println("Updating Car with id: " + id + " to make: " + make + ", model: " + model);

            int result = preparedStatement.executeUpdate();
            // Log the result of the update
            System.out.println("Update result: " + result);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
