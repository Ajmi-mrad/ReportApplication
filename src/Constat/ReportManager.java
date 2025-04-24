package Constat;

import java.sql.*;

import static Constat.Config.*;
import static Constat.Config.password;

public class ReportManager {
    private static Connection connection = null;
    private static Statement statement = null;

    public ReportManager() {
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
            ResultSet tables = dbm.getTables(null, null, "Report", null);
            if (tables.next()) {
                System.out.println("Table Report already exists");
            } else {
                // create table Report that have an id, accident_id, vehicle_a_id, vehicle_b_id, driver_a_id, driver_b_id, description_a, description_b, damages_a, damages_b
                // created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                // accident_id, vehicle_a_id, vehicle_b_id, driver_a_id, driver_b_id are foreign keys that reference the id of the Accident, Car and Driverr tables respectively
                String query = "CREATE TABLE Report (id int primary key auto_increment, accident_id int, vehicle_a_id int, vehicle_b_id int, driver_a_id varchar(255), driver_b_id varchar(255), description_a VARCHAR(255), description_b VARCHAR(255), damages_a VARCHAR(255), damages_b VARCHAR(255), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (accident_id) REFERENCES Accident(id), FOREIGN KEY (vehicle_a_id) REFERENCES Car(id), FOREIGN KEY (vehicle_b_id) REFERENCES Car(id), FOREIGN KEY (driver_a_id) REFERENCES Driverr(cin), FOREIGN KEY (driver_b_id) REFERENCES Driverr(cin))";
                statement.executeUpdate(query);
                System.out.println("Table Car created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertReport(int accident_id, int vehicle_a_id, int vehicle_b_id, String driver_a_id, String driver_b_id, String description_a, String description_b, String damages_a, String damages_b) {
        String req = "INSERT INTO Report (accident_id, vehicle_a_id, vehicle_b_id, driver_a_id, driver_b_id, description_a, description_b, damages_a, damages_b) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            // dont forget the id that generated automatically
            preparedStatement.setInt(1, accident_id);
            preparedStatement.setInt(2, vehicle_a_id);
            preparedStatement.setInt(3, vehicle_b_id);
            preparedStatement.setString(4, driver_a_id);
            preparedStatement.setString(5, driver_b_id);
            preparedStatement.setString(6, description_a);
            preparedStatement.setString(7, description_b);
            preparedStatement.setString(8, damages_a);
            preparedStatement.setString(9, damages_b);


            return preparedStatement.executeUpdate();
            //System.out.println("Etudiant added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int reportExists(int accident_id, int vehicle_a_id, int vehicle_b_id, String driver_a_id, String driver_b_id) {
        String req = "SELECT id FROM Report WHERE accident_id = ? AND vehicle_a_id = ? AND vehicle_b_id = ? AND driver_a_id = ? AND driver_b_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, accident_id);
            preparedStatement.setInt(2, vehicle_a_id);
            preparedStatement.setInt(3, vehicle_b_id);
            preparedStatement.setString(4, driver_a_id);
            preparedStatement.setString(5, driver_b_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1; // Report not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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
    // function that return just the resultSet with the nameFamily of driverr and the description of the accidentManager and the license plate of the car by the id using the function selectCarById from CarManager and ...

    public ResultSet selectAllReports() {
        ResultSet rs = null;
        if (statement != null) {
            try {
                // lets make inner join query to get the license_plate from table car and the firstName from Driverr and the description from Accident
                /*
                String query = "SELECT Report.id, Report.accident_id, Report.vehicle_a_id," +
                        " Report.vehicle_b_id, Report.driver_a_id, Report.driver_b_id, " +
                        "Report.description_a, Report.description_b, Report.damages_a," +
                        " Report.damages_b," +
                        " Car.license_plate as license_plate_a, Car2.license_plate as license_plate_b," +
                        " Driverr.first_name as first_name_a, Driverr2.first_name as first_name_b," +
                        " Accident.description as accident_description FROM Report" +
                        " INNER JOIN Car ON Report.vehicle_a_id = Car.id " +
                        "INNER JOIN Car AS Car2 ON Report.vehicle_b_id = Car2.id " +
                        "INNER JOIN Driverr ON Report.driver_a_id = Driverr.cin " +
                        "INNER JOIN Driverr AS Driverr2 ON Report.driver_b_id = Driverr2.cin " +
                        "INNER JOIN Accident ON Report.accident_id = Accident.id";
                 */
                String query = "select * from Report";
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

    public int deleteReport(int id){
        String req = "DELETE FROM Report WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, id);
            System.out.println("Executing delete query for CIN: " + id);
            int result = preparedStatement.executeUpdate();
            System.out.println("Deleted from DB rows: " + result);
            return result;
        } catch (SQLException e) {
            System.err.println("Error deleting Report with CIN " + id);
            e.printStackTrace();
            return 0;
        }
    }
    public int updateReport(int id, int accident_id, int vehicle_a_id, int vehicle_b_id, String driver_a_id, String driver_b_id, String description_a, String description_b, String damages_a, String damages_b) {
        String req = "UPDATE Report SET accident_id = ?, vehicle_a_id = ?, vehicle_b_id = ?, driver_a_id = ?, driver_b_id = ?, description_a = ?, description_b = ?, damages_a = ?, damages_b = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, accident_id);
            preparedStatement.setInt(2, vehicle_a_id);
            preparedStatement.setInt(3, vehicle_b_id);
            preparedStatement.setString(4, driver_a_id);
            preparedStatement.setString(5, driver_b_id);
            preparedStatement.setString(6, description_a);
            preparedStatement.setString(7, description_b);
            preparedStatement.setString(8, damages_a);
            preparedStatement.setString(9, damages_b);
            preparedStatement.setInt(10, id); // Set the 10th parameter (id)

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
