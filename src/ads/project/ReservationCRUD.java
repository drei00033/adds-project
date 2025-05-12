package ads.project;

import java.sql.*;
import javax.swing.JOptionPane;

public class ReservationCRUD {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    // Add Reservation
    public static void addReservation(String checkIn, String checkOut, String status, String customerId, String packageCode, String paymentId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "INSERT INTO RESERVATION (CheckInDate, CheckOutDate, ReservationStatus, CustomerID, PackageCode, PaymentID) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, checkIn);      // Format: YYYY-MM-DD
            stmt.setString(2, checkOut);     // Format: YYYY-MM-DD
            stmt.setString(3, status);
            stmt.setString(4, customerId);
            stmt.setString(5, packageCode);
            stmt.setInt(6, Integer.parseInt(paymentId));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Reservation added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add reservation: " + e.getMessage());
        }
    }

    // Update Reservation
    public static void updateReservation(String reservationNo, String checkIn, String checkOut, String status, String customerId, String packageCode, String paymentId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "UPDATE RESERVATION SET CheckInDate = ?, CheckOutDate = ?, ReservationStatus = ?, CustomerID = ?, PackageCode = ?, PaymentID = ? WHERE ReservationNo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, checkIn);
            stmt.setString(2, checkOut);
            stmt.setString(3, status);
            stmt.setString(4, customerId);
            stmt.setString(5, packageCode);
            stmt.setInt(6, Integer.parseInt(paymentId));
            stmt.setInt(7, Integer.parseInt(reservationNo));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Reservation updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Reservation number not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update reservation: " + e.getMessage());
        }
    }

    // Delete Reservation
    public static void deleteReservation(String reservationNo) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "DELETE FROM RESERVATION WHERE ReservationNo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(reservationNo));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Reservation deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Reservation number not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete reservation: " + e.getMessage());
        }
    }
}
