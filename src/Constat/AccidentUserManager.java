package Constat;

import java.sql.*;

public class AccidentUserManager {
    private static Connection connection = null;
    private static Statement statement = null;

    public AccidentUserManager() {
        try {
            Class.forName(Config.driverName);
            connection = DriverManager.getConnection(Config.url, Config.user, Config.password);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            // delete cascade
            String query = "CREATE TABLE IF NOT EXISTS AccidentUser (" +
                    "cin VARCHAR(255), " +
                    "accident_id INT, " +
                    "PRIMARY KEY (cin, accident_id), " +
                    "FOREIGN KEY (cin) REFERENCES Driverr(cin), " +
                    "FOREIGN KEY (accident_id) REFERENCES Accident(id))"
                    //" ON DELETE CASCADE"
                    ;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertAccidentUser(String cin, int accidentId) {
        String query = "INSERT INTO AccidentUser (cin, accident_id) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cin);
            preparedStatement.setInt(2, accidentId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int deleteAccidentUser(String cin, int accidentId) {
        String query = "DELETE FROM AccidentUser WHERE cin = ? AND accident_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cin);
            preparedStatement.setInt(2, accidentId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ResultSet getUserAccidents(String cin) {
        try {
            String query = "SELECT a.* FROM Accident a JOIN AccidentUser au ON a.id = au.accident_id WHERE au.cin = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, cin);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // check if the accidentuser exists
    public boolean accidentUserExists(String cin, int accidentId) {
        String query = "SELECT * FROM AccidentUser WHERE cin = ? AND accident_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cin);
            preparedStatement.setInt(2, accidentId);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}