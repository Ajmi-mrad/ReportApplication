package Constat;

import java.sql.*;

public class CarUserManager {
    private static Connection connection = null;
    private static Statement statement = null;

    public CarUserManager() {
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
            String query = "CREATE TABLE IF NOT EXISTS CarUser (" +
                    "cin VARCHAR(255), " +
                    "car_id INT, " +
                    "PRIMARY KEY (cin, car_id), " +
                    "FOREIGN KEY (cin) REFERENCES Driverr(cin), " +
                    "FOREIGN KEY (car_id) REFERENCES Car(id))";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertCarUser(String cin, int carId) {
        String query = "INSERT INTO CarUser (cin, car_id) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cin);
            preparedStatement.setInt(2, carId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteCarUser(String cin, int carId) {
        String query = "DELETE FROM CarUser WHERE cin = ? AND car_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cin);
            preparedStatement.setInt(2, carId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public ResultSet getUserCars(String cin) {
        try {
            String query = "SELECT c.* FROM Car c JOIN CarUser cu ON c.id = cu.car_id WHERE cu.cin = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, cin);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet selectAllCarUsers(String cin) {
        String query = "SELECT * FROM CarUser WHERE cin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cin);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}