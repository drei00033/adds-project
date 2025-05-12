package ads.project;

import java.sql.*;
import javax.swing.JOptionPane;

public class CustomerCRUD {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    // Add Customer
    public static void addCustomer(String lastName, String firstName, String middleName, String contactNo, String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "INSERT INTO CUSTOMER (LastName, FirstName, MiddleName, ContactNo, Email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            stmt.setString(4, contactNo);
            stmt.setString(5, email);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Customer added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add customer: " + e.getMessage());
        }
    }

    // Update Customer
    public static void updateCustomer(String customerId, String lastName, String firstName, String middleName, String contactNo, String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "UPDATE CUSTOMER SET LastName = ?, FirstName = ?, MiddleName = ?, ContactNo = ?, Email = ? WHERE CustomerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            stmt.setString(4, contactNo);
            stmt.setString(5, email);
            stmt.setInt(6, Integer.parseInt(customerId));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Customer updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Customer ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update customer: " + e.getMessage());
        }
    }

    // Delete Customer
    public static void deleteCustomer(String customerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "DELETE FROM CUSTOMER WHERE CustomerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(customerId));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Customer deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Customer ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete customer: " + e.getMessage());
        }
    }
}
