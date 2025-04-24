package Menu;

import java.sql.*;

import static Menu.Config.*;

public class EtudiantManager {
    private static Connection connection = null;
    private static Statement statement = null;

    public EtudiantManager() {
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
            ResultSet tables = dbm.getTables(null, null, "Etudiant", null);
            if (tables.next()) {
                System.out.println("Table Etudiant already exists");
            } else {
                // create table Etudiant that have an id, first_name, last_name, moyenne
                String query = "CREATE TABLE Etudiant (cin int primary key , first_name VARCHAR(255), last_name VARCHAR(255), moyenne FLOAT)";
                statement.executeUpdate(query);
                System.out.println("Table Etudiant created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertEtudiant(int cin, String first_name, String last_name, float moyenne) {
        /*
        if (statement != null) {
            try {
                String query = "INSERT INTO Etudiant (cin,first_name, last_name, moyenne) VALUES ('" + cin + "', '" + first_name + "', '" + last_name + "', '" + moyenne + "')";
                statement.executeUpdate(query);
                System.out.println("Etudiant added successfully");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
         */
        String req = "INSERT INTO Etudiant (cin,first_name, last_name, moyenne) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, cin);
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            preparedStatement.setFloat(4, moyenne);
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
    public ResultSet selectAllEtudiants() {
        ResultSet rs = null;
        if (statement != null) {
            try {
                String query = "SELECT * FROM Etudiant";
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
    public int deleteEtudiant(int cin){
        String req = "DELETE FROM Etudiant WHERE cin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, cin);
            System.out.println("Executing delete query for CIN: " + cin);
            int result = preparedStatement.executeUpdate();
            System.out.println("Deleted from DB rows: " + result);
            return result;
        } catch (SQLException e) {
            System.err.println("Error deleting etudiant with CIN " + cin);
            e.printStackTrace();
            return 0;
        }
    }
    public int updateEtudiant(int cin, String firstName, String lastName, float moyenne) {
        String req = "UPDATE Etudiant SET first_name = ?, last_name = ?, moyenne = ? WHERE cin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setFloat(3, moyenne);
            preparedStatement.setInt(4, cin);

            // Log the values being updated
            System.out.println("Updating Etudiant with cin: " + cin + " to moyenne: " + moyenne);

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