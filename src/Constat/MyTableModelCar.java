package Constat;

import Constat.CarManager;

import javax.swing.table.AbstractTableModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyTableModelCar extends AbstractTableModel {
    private ResultSetMetaData rsmd;
    private List<Object[]> data = new ArrayList<>();
    private CarManager carManager;

    public MyTableModelCar(ResultSet rs, CarManager carManager) {
        this.carManager = carManager;
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

    // function for search if the car exist in the table but the id is auto increment so i wanna search by the make and model and license plate
    public int carExist(String make, String model, String licensePlate) {
        for (Object[] row : data) {
            if (row[1].toString().equals(make) && row[2].toString().equals(model) && row[3].toString().equals(licensePlate)) {
                // return id
                return Integer.parseInt(row[0].toString());
            }
        }
        return -1;
    }

    // add a new car
    public int ajoutCar(String make, String model, String licensePlate, String insuranceCompany, String insurancePolicyNumber, String insuranceExpiryDate) {
        // check if exist in data base
        int carId = carManager.carExists(make, model, licensePlate);
        if (carId!= -1) {
            System.out.println("Car already exists");
            return 0;
        } else {
            // add to database first
            int a = carManager.insertCar(make, model, licensePlate, insuranceCompany, insurancePolicyNumber, Date.valueOf(insuranceExpiryDate));
            int id = carManager.carExists(make, model, licensePlate); // get the id of the car that was just added
            // check if the car was added to the database
            if (a > 0){
                // add to data if database insertion was successful
                data.add(new Object[]{id,make, model, licensePlate, insuranceCompany, insurancePolicyNumber, insuranceExpiryDate});
                fireTableDataChanged(); // Notify table that a new row was added
                System.out.println("Car added to the list: " + make + " " + model + " " + licensePlate);
                return a;  // Return the number of rows affected
            } else {
                System.out.println("Failed to add to database");
            }
        }
        return 0;
    }

    // delete a car
    public int suppCar(String make, String model, String licensePlate) {
        // check if exist in our local data
        int carId = carManager.carExists(make, model, licensePlate);
        if (carId!= -1) {
            // delete from database first
            int a = carManager.deleteCar(carId);
            if (a > 0) {
                // remove from data if database deletion was successful
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i)[1].toString().equals(make) && data.get(i)[2].toString().equals(model) && data.get(i)[3].toString().equals(licensePlate)) {
                        data.remove(i);
                        fireTableRowsDeleted(i, i);  // Notify table that rows were deleted
                        System.out.println("Car deleted from the list: " + make + " " + model + " " + licensePlate);
                        return a;  // Return the number of rows affected
                    }
                }
            } else {
                System.out.println("Failed to delete from database");
            }
        } else {
            System.out.println("Car does not exist in the list");
        }

        return 0;
    }
    public int modifierCar(int carId, String make, String model, String licensePlate, String insuranceCompany, String insurancePolicyNumber, String insuranceExpiryDate) {
        try {
            // Validate and format the date
            Date formattedDate = Date.valueOf(insuranceExpiryDate); // Ensure the date is in yyyy-MM-dd format

            // Call the updateCar method
            int result = carManager.updateCar(carId, make, model, licensePlate, insuranceCompany, insurancePolicyNumber, formattedDate);

            if (result > 0) {
                // Update the local data
                for (Object[] row : data) {
                    if (Integer.parseInt(row[0].toString()) == carId) {
                        row[1] = make;
                        row[2] = model;
                        row[3] = licensePlate;
                        row[4] = insuranceCompany;
                        row[5] = insurancePolicyNumber;
                        row[6] = insuranceExpiryDate;
                        break;
                    }
                }
                fireTableDataChanged();
            }
            return result;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid date format. Please use yyyy-MM-dd.");
            e.printStackTrace();
            return 0;
        }
    }
}
