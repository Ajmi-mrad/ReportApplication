package Constat;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyTableModelCarUser extends AbstractTableModel {
    private List<Object[]> data = new ArrayList<>();
    private ResultSetMetaData rsmd;
    private CarUserManager carUserManager;
    private String cin;

    public MyTableModelCarUser(ResultSet rs, CarUserManager carUserManager, String cin) {
        this.carUserManager = carUserManager;
        this.cin = cin;
        loadData();
    }

    public void loadData() {
        data.clear(); // Clear existing data
        try {
            ResultSet rs = carUserManager.getUserCars(cin); // Fetch fresh data
            if (rs != null) {
                rsmd = rs.getMetaData();
                while (rs.next()) {
                    Object[] row = new Object[rsmd.getColumnCount()];
                    for (int i = 0; i < row.length; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    data.add(row);
                }
            }
            fireTableDataChanged(); // Notify the table of data changes
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
            return rsmd != null ? rsmd.getColumnCount() : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        try {
            return rsmd != null ? rsmd.getColumnName(column + 1) : "Column " + column;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Column " + column;
        }
    }

    public int addCarUser(String cin, int carId) {
        int result = carUserManager.insertCarUser(cin, carId);
        if (result > 0) {
            loadData(); // Reload all data instead of just adding one row
        }
        return result;
    }

    public int deleteCarUser(String cin, int carId) {
        int result = carUserManager.deleteCarUser(cin, carId);
        if (result > 0) {
            loadData(); // Reload all data instead of trying to remove specific row
        }
        return result;
    }
}