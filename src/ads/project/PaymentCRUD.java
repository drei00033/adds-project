package ads.project;

import java.sql.*;
import javax.swing.JOptionPane;

public class PaymentCRUD {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    // Add Payment
    public static void addPayment(String customerId, String amount, String method, String date, String fee) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "INSERT INTO PAYMENT (CustomerID, Amount, PaymentMethod, PaymentDate, ReservationFee) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerId);
            stmt.setBigDecimal(2, new java.math.BigDecimal(amount));
            stmt.setString(3, method);
            stmt.setString(4, date); // Assumes valid format: "YYYY-MM-DD"
            stmt.setBigDecimal(5, new java.math.BigDecimal(fee));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Payment added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add payment: " + e.getMessage());
        }
    }

    // Update Payment
    public static void updatePayment(String paymentId, String customerId, String amount, String method, String date, String fee) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "UPDATE PAYMENT SET CustomerID = ?, Amount = ?, PaymentMethod = ?, PaymentDate = ?, ReservationFee = ? WHERE PaymentID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerId);
            stmt.setBigDecimal(2, new java.math.BigDecimal(amount));
            stmt.setString(3, method);
            stmt.setString(4, date);
            stmt.setBigDecimal(5, new java.math.BigDecimal(fee));
            stmt.setInt(6, Integer.parseInt(paymentId));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Payment updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Payment ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update payment: " + e.getMessage());
        }
    }

    // Delete Payment
    public static void deletePayment(String paymentId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "DELETE FROM PAYMENT WHERE PaymentID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(paymentId));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Payment deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Payment ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete payment: " + e.getMessage());
        }
    }
}
