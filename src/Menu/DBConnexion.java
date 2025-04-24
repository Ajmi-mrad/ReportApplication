package Menu;

import java.sql.*;

public class DBConnexion {

    public static void main(String[] args) {
        // import the connection and all the methode from EtudiantManager
        EtudiantManager etudiantManager = new EtudiantManager();
        // create a new table
        etudiantManager.createTable();
        // insert some data
        etudiantManager.insertEtudiant(125, "ajmi", "mrad", 17.5f);
        etudiantManager.insertEtudiant(126, "ahmed", "mrad", 17.5f);
        // select all the data
        //etudiantManager.selectAllEtudiants();
        ResultSet rs = etudiantManager.selectAllEtudiants();


    }
}