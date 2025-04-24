package Constat;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyTableModelAccidentUser extends AbstractTableModel {
    private List<Object[]> data = new ArrayList<>();
    private ResultSetMetaData rsmd;
    private AccidentUserManager accidentUserManager;
    private String cin;

    public MyTableModelAccidentUser(ResultSet rs, AccidentUserManager accidentUserManager, String cin) {
        this.accidentUserManager = accidentUserManager;
        this.cin = cin;
        loadData();
    }

    public void loadData() {
        data.clear();
        try {
            ResultSet rs = accidentUserManager.getUserAccidents(cin);
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
            fireTableDataChanged();
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



    public int deleteAccidentUser(String cin, int accidentId) {
        int result = accidentUserManager.deleteAccidentUser(cin, accidentId);
        if (result > 0) {
            loadData();
        }
        return result;
    }
}