package Menu;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.time.zone.ZoneRulesProvider.refresh;

public class MyTableModel extends AbstractTableModel {
    private ResultSetMetaData rsmd;
    private List<Object[]> data = new ArrayList<>();
    private EtudiantManager etudiantManager;

    public MyTableModel(ResultSet rs, EtudiantManager etudiantManager) {
        this.etudiantManager = etudiantManager;
        try {
            rsmd = rs.getMetaData();
            while (rs.next()) {
                Object[] row = new Object[getColumnCount()];
                for (int i = 0; i < getColumnCount(); i++) {
                    row[i] = rs.getObject(i + 1);
                }
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        try {
            return rsmd.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    // get column name
    @Override
    public String getColumnName(int column) {
        try {
            return rsmd.getColumnName(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (getColumnName(columnIndex).equalsIgnoreCase("cin")) {
            return false;
        }
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = aValue;
    }

    // function for search if the student exist in the table
    public boolean etudiantExist(String cin) {
        for (Object[] row : data) {
            if (row[0].toString().equals(cin)) {
                return true;
            }
        }
        return false;
    }

    // add a new student
    public int ajoutEtudiant(String nom, String prenom, String cin, String moyenne) {
        int a = etudiantManager.insertEtudiant(Integer.parseInt(cin), nom, prenom, Float.parseFloat(moyenne));
        if (a > 0 && !etudiantExist(cin)) {
            // add to data
            data.add(new Object[]{cin, nom, prenom, moyenne});
            fireTableDataChanged();
        }
        return 0;
    }

    // delete a student
    public int suppEudiant(String cin) {
        // check if exist in our local data
        if (etudiantExist(cin)) {
            // delete from database first
            int a = etudiantManager.deleteEtudiant(Integer.parseInt(cin));
            if (a > 0) {
                // remove from data if database deletion was successful
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i)[0].toString().equals(cin)) {
                        data.remove(i);
                        fireTableRowsDeleted(i, i);  // Notify table that rows were deleted
                        System.out.println("Etudiant supprimé de la liste: " + cin);
                        return a;  // Return the number of rows affected
                    }
                }
            } else {
                System.out.println("Failed to delete from database");
            }
        } else {
            System.out.println("Etudiant with CIN " + cin + " not found in local data");
        }
        return 0;
    }
    public int modifierEtudiant(String cin, String nom, String prenom, String moyenne) {
        int a = etudiantManager.updateEtudiant(Integer.parseInt(cin), nom, prenom, Float.parseFloat(moyenne));
        if (a > 0) {
            // update data
            for (Object[] row : data) {
                if (row[0].toString().equals(cin)) {
                    row[1] = nom;
                    row[2] = prenom;
                    row[3] = moyenne;
                    break;
                }
            }
            fireTableDataChanged();
        }
        //System.out.println("a: " + a);
        return a;
    }
}