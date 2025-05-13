package ads.project;

import java.sql.*;
import javax.swing.JOptionPane;

public class RoomCRUD {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    // Add Room
    public static void addRoom(String roomType, String rentPrice) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO ROOM (RoomType, RentPrice) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, roomType);
            stmt.setBigDecimal(2, new java.math.BigDecimal(rentPrice));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Room added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add room: " + e.getMessage());
        }
    }

    // Update Room
    public static void updateRoom(String roomNo, String roomType, String rentPrice) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE ROOM SET RoomType = ?, RentPrice = ? WHERE RoomNo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, roomType);
            stmt.setBigDecimal(2, new java.math.BigDecimal(rentPrice));
            stmt.setInt(3, Integer.parseInt(roomNo));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Room updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Room No not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update room: " + e.getMessage());
        }
    }

    // Delete Room
    public static void deleteRoom(String roomNo) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM ROOM WHERE RoomNo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(roomNo));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Room deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Room No not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete room: " + e.getMessage());
        }
    }
}
