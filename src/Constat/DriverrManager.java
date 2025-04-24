package Constat;

import java.sql.*;

import static Constat.Config.*;
import static Constat.Config.password;

public class DriverrManager {
    private static Connection connection = null;
    private static Statement statement = null;

    public DriverrManager() {
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

    public void createTableDriver() {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Driverr", null);
            if (tables.next()) {
                System.out.println("Table Driver already exists");
            } else {
                // create table car that have a cin as pk, first_name, last_name, date_of_birth, address, phone_number, license_number, license_issue_date,password
                String query = "CREATE TABLE Driverr (cin VARCHAR(255) primary key , first_name VARCHAR(255), last_name VARCHAR(255), date_of_birth DATE, address VARCHAR(255), phone_number VARCHAR(255), license_number VARCHAR(255), license_issue_date DATE, pass VARCHAR(255))";
                statement.executeUpdate(query);
                System.out.println("Table Driver created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int insertDriver(String cin,String first_name, String last_name, Date date_of_birth, String address, String phone_number, String license_number, Date license_issue_date,String pass) {
        String req = "INSERT INTO Driverr (cin,first_name, last_name, date_of_birth, address, phone_number, license_number, license_issue_date,pass) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            // dont forget the id that generated automatically
            preparedStatement.setString(1, cin);
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            preparedStatement.setDate(4, date_of_birth);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, phone_number);
            preparedStatement.setString(7, license_number);
            preparedStatement.setDate(8, license_issue_date);
            preparedStatement.setString(9, pass);

            return preparedStatement.executeUpdate();
            //System.out.println("Etudiant added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // function that return the resultSet
    public ResultSet driverrExist(String cin) {
        ResultSet rs = null;
        if (statement != null) {
            try {
                String query = "SELECT * FROM Driverr WHERE cin = '" + cin + "'";
                rs = statement.executeQuery(query);
                // get the result metadata

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
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
    public ResultSet selectAllDriverss() {
        ResultSet rs = null;
        if (statement != null){
            try {
                String query = "SELECT * FROM Driverr";
                rs = statement.executeQuery(query);
                // get the result metadata
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
    public boolean checkDriver(String cin, String pass) {
        String req = "SELECT * FROM Driverr WHERE cin = ? AND pass = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, cin);
            preparedStatement.setString(2, pass);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next(); // returns true if a record is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // affiche le resultSet

    public int updateDriver(String cin,String first_name, String last_name, Date date_of_birth, String address, String phone_number, String license_number, Date license_issue_date) {
        String req = "UPDATE Driverr SET first_name = ?, last_name = ?, date_of_birth = ?, address = ?, phone_number = ?, license_number = ?, license_issue_date = ? WHERE cin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setDate(3, date_of_birth);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, phone_number);
            preparedStatement.setString(6, license_number);
            preparedStatement.setDate(7, license_issue_date);
            preparedStatement.setString(8, cin);

            // Log the values being updated
            System.out.println("Updating Driver with cin: " + cin + " to first_name: " + first_name + ", last_name: " + last_name);

            int result = preparedStatement.executeUpdate();
            // Log the result of the update
            System.out.println("Update result: " + result);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    // delete driver
    public int deleteDriver(String cin) {
        String req = "DELETE FROM Driverr WHERE cin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, cin);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    // check if the driver exist
    public String driverExist(String cin) {
        String req = "SELECT * FROM Driverr WHERE cin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, cin);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("cin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
