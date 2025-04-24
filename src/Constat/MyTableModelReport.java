package Constat;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyTableModelReport extends AbstractTableModel {
    private ResultSetMetaData rsmd;
    private List<Object[]> data = new ArrayList<>();
    private ReportManager reportManager;

    public MyTableModelReport(ResultSet rs, ReportManager reportManager) {
        this.reportManager = reportManager;
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
            return rsmd.getColumnCount() ; // Exclude 'id' and 'created_at'
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < data.size() &&
                columnIndex >= 0 && columnIndex < getColumnCount()) {
            return data.get(rowIndex)[columnIndex]; // Return exact column
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        try {
            return rsmd.getColumnName(column + 1); // Use actual column names
        } catch (SQLException e) {
            e.printStackTrace();
            return "Column " + column;
        }
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Prevent editing directly in the table
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = aValue;
    }

    public int ajoutReport(int accidentId, int vehicleAId, int vehicleBId, String driverACin, String driverBCin, String descriptionA, String descriptionB, String damagesA, String damagesB) {
        // check if the report exist by using reportExist from reportManager
        if (reportManager.reportExists(accidentId, vehicleAId, vehicleBId, driverACin, driverBCin) != -1) {
            System.out.println("Report already exists");
            return 0;
        }else{
            int result = reportManager.insertReport(accidentId, vehicleAId, vehicleBId, driverACin, driverBCin, descriptionA, descriptionB, damagesA, damagesB);
            if (result>0){
                data.add(new Object[]{accidentId, vehicleAId, vehicleBId, driverACin, driverBCin, descriptionA, descriptionB, damagesA, damagesB});
                fireTableDataChanged();
                return result;
            } else {
                System.out.println("Failed to add to database");
            }
        }
        return 0;
    }

    public int deleteReport(int id) {
        int result = reportManager.deleteReport(id);
        if (result > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (Integer.parseInt(data.get(i)[0].toString()) == id) {
                    data.remove(i);
                    fireTableRowsDeleted(i, i);
                    return result;
                }
            }
        }
        return 0;
    }

    public int updateReport(int id, int accidentId, int vehicleAId, int vehicleBId, String driverACin, String driverBCin, String descriptionA, String descriptionB, String damagesA, String damagesB) {
        try {
            int result = reportManager.updateReport(id, accidentId, vehicleAId, vehicleBId, driverACin, driverBCin, descriptionA, descriptionB, damagesA, damagesB);
            if (result > 0) {
                for (Object[] row : data) {
                    if (Integer.parseInt(row[0].toString()) == id) {
                        row[1] = accidentId;
                        row[2] = vehicleAId;
                        row[3] = vehicleBId;
                        row[4] = driverACin;
                        row[5] = driverBCin;
                        row[6] = descriptionA;
                        row[7] = descriptionB;
                        row[8] = damagesA;
                        row[9] = damagesB;
                        break;
                    }
                }
                fireTableDataChanged();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}