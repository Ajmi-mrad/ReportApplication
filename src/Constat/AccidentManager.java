package Constat;

import java.sql.*;

import static Constat.Config.*;
import static Constat.Config.password;

public class AccidentManager {
    private static Connection connection = null;
    private static Statement statement = null;

    public AccidentManager() {
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

    public void createTable() {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Accident", null);
            if (tables.next()) {
                System.out.println("Table Accident already exists");
            } else {
                // create table Accident that have an id, date, time, location, weather_conditions, description
                String query = "CREATE TABLE Accident (id int primary key auto_increment, date DATE, time TIME, location VARCHAR(255), weather_conditions VARCHAR(255), description VARCHAR(255))";
                statement.executeUpdate(query);
                System.out.println("Table Accident created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertAccident(Date date, Time time, String location, String weatherConditions, String description) {
        String query = "INSERT INTO Accident (date, time, location, weather_conditions, description) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, date);
            preparedStatement.setTime(2, time);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, weatherConditions);
            preparedStatement.setString(5, description);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if insertion fails
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
    public ResultSet selectAllAccidents() {
        ResultSet rs = null;
        if (statement != null) {
            try {
                String query = "SELECT * FROM Accident";
                rs = statement.executeQuery(query);
                // get the result metadata

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
    public ResultSet selectAccidentById(int id) {
        ResultSet rs = null;
        if (statement != null) {
            try {
                String query = "SELECT * FROM Accident WHERE id = '" + id + "'";
                rs = statement.executeQuery(query);
                // get the result metadata

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
    public int deleteAccident(int id){
        String req = "DELETE FROM Accident WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, id);
            System.out.println("Executing delete query for CIN: " + id);
            int result = preparedStatement.executeUpdate();
            System.out.println("Deleted from DB rows: " + result);
            return result;
        } catch (SQLException e) {
            System.err.println("Error deleting accident with CIN " + id);
            e.printStackTrace();
            return 0;
        }
    }
    public int updateAccident(int id, Date date, Time time, String location, String weather_conditions, String description) {
        String req = "UPDATE Accident SET date = ?, time = ?, location = ?, weather_conditions = ?, description = ? WHERE id = ?" ;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setDate(1, date);
            preparedStatement.setTime(2, time);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, weather_conditions);
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, id);

            // Log the values being updated
            System.out.println("Updating Accident with id: " + id + " to date: " + date + ", time: " + time + ", location: " + location + ", weather_conditions: " + weather_conditions + ", description: " + description);

            int result = preparedStatement.executeUpdate();
            // Log the result of the update
            System.out.println("Update result: " + result);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    // check if the accident exisit or not
    public ResultSet accidentExist(int id) {
        ResultSet rs = null;
        if (statement != null) {
            try {
                String query = "SELECT * FROM Accident WHERE id = '" + id + "'";
                rs = statement.executeQuery(query);
                // get the result metadata

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

}