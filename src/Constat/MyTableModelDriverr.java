package Constat;

import javax.swing.table.AbstractTableModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyTableModelDriverr extends AbstractTableModel {
    private ResultSetMetaData rsmd;
    private List<Object[]> data = new ArrayList<>();
    private DriverrManager driverrManager;

    public MyTableModelDriverr(ResultSet rs, DriverrManager driverrManager) {
        this.driverrManager = driverrManager;
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
        return !getColumnName(columnIndex).equalsIgnoreCase("cin");
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = aValue;
    }

    public int driverExist(String cin) {
        for (Object[] row : data) {
            if (row[0].toString().equals(cin)) {
                return 1; // Driver exists
            }
        }
        return -1; // Driver does not exist
    }

    public int ajoutDriver(String cin, String firstName, String lastName, String dateOfBirth, String address, String phoneNumber, String licenseNumber, String licenseIssueDate, String pass) {
        try {
            // Validate dateOfBirth
            if (dateOfBirth == null || dateOfBirth.isEmpty()) {
                System.err.println("Invalid date of birth");
                return 0;
            }
            Date dob = Date.valueOf(dateOfBirth); // Ensure the format is yyyy-MM-dd

            // Validate licenseIssueDate
            if (licenseIssueDate == null || licenseIssueDate.isEmpty()) {
                System.err.println("Invalid license issue date");
                return 0;
            }
            Date lid = Date.valueOf(licenseIssueDate); // Ensure the format is yyyy-MM-dd
            // Check if driver already exists
            if (driverrManager.driverExist(cin) != null) {
                System.out.println("Driver already exists");
                return 0;
            }
            // Insert driver into the database
            int result = driverrManager.insertDriver(cin, firstName, lastName, dob, address, phoneNumber, licenseNumber, lid, pass);
            if (result > 0) {
                data.add(new Object[]{cin, firstName, lastName, dateOfBirth, address, phoneNumber, licenseNumber, licenseIssueDate, pass});
                fireTableDataChanged();
                System.out.println("Driver added successfully: " + firstName + " " + lastName);
                return result;
            } else {
                System.out.println("Failed to add driver to database");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int suppDriver(String cin) {
        if (driverrManager.driverExist(cin) != null) {
            int result = driverrManager.deleteDriver(cin);
            if (result > 0) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i)[0].toString().equals(cin)) {
                        data.remove(i);
                        fireTableRowsDeleted(i, i);
                        System.out.println("Driver deleted successfully: " + cin);
                        return result;
                    }
                }
            } else {
                System.out.println("Failed to delete driver from database");
            }
        } else {
            System.out.println("Driver does not exist");
        }
        return 0;
    }
    public int modifierDriver(String cin, String firstName, String lastName, String dateOfBirth, String address, String phoneNumber, String licenseNumber, String licenseIssueDate) {
        try{
            Date formattedDateOfBirth = Date.valueOf(dateOfBirth); // Ensure the format is yyyy-MM-dd
            Date formattedLicenseIssueDate = Date.valueOf(licenseIssueDate); // Ensure the format is yyyy-MM-dd
            int result = driverrManager.updateDriver(cin, firstName, lastName, formattedDateOfBirth, address, phoneNumber, licenseNumber, formattedLicenseIssueDate);
            if(result > 0) {
                for (Object[] row : data) {
                    if (row[0].toString().equals(cin)) {
                        row[1] = firstName;
                        row[2] = lastName;
                        row[3] = dateOfBirth;
                        row[4] = address;
                        row[5] = phoneNumber;
                        row[6] = licenseNumber;
                        row[7] = licenseIssueDate;
                        break;
                    }
                }
                fireTableDataChanged();
                System.out.println("Driver updated successfully: " + firstName + " " + lastName);
                return result;
            } else {
                System.out.println("Failed to update driver in database");
            }
        }catch(IllegalArgumentException e){
            System.err.println("Error parsing date: " + e.getMessage());
        }
        return 0;
    }
}