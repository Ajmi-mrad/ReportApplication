package Constat;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyTableModelAccident extends AbstractTableModel {
    private ResultSetMetaData rsmd;
    private List<Object[]> data = new ArrayList<>();
    private AccidentManager accidentManager;

    public MyTableModelAccident(ResultSet rs, AccidentManager accidentManager) {
        this.accidentManager = accidentManager;
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
        return !getColumnName(columnIndex).equalsIgnoreCase("id");
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = aValue;
    }
    public int ajoutAccident(String date, String time, String location, String weatherConditions, String description) {
        try {
            // Validate time format
            if (!isValidTimeFormat(time)) {
                throw new IllegalArgumentException("Invalid time format. Expected format: HH:mm:ss");
            }

            // Convert to java.sql.Time
            Time sqlTime = Time.valueOf(time);

            // Insert accident into the database
            
            int result = accidentManager.insertAccident(java.sql.Date.valueOf(date), sqlTime, location, weatherConditions, description);
            if (result > 0) {
                ResultSet rs = accidentManager.selectAllAccidents();
                if (rs != null && rs.next()) { // Move cursor to the first row
                    int id = rs.getInt("id"); // Access the "id" column
                    System.out.println("Accident added successfully with ID: " + id);
                    data.add(new Object[]{id, date, time, location, weatherConditions, description});
                    fireTableDataChanged();
                    return result;
                } else {
                    System.err.println("No data found in ResultSet.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Helper method to validate time format
    private boolean isValidTimeFormat(String time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setLenient(false);
        try {
            timeFormat.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public int suppAccident(int id) {
        int result = accidentManager.deleteAccident(id);
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

    public int modifierAccident(int id, String date, String time, String location, String weatherConditions, String description) {
        try {
            int result = accidentManager.updateAccident(id, java.sql.Date.valueOf(date), java.sql.Time.valueOf(time), location, weatherConditions, description);
            if (result > 0) {
                for (Object[] row : data) {
                    if (Integer.parseInt(row[0].toString()) == id) {
                        row[1] = date;
                        row[2] = time;
                        row[3] = location;
                        row[4] = weatherConditions;
                        row[5] = description;
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