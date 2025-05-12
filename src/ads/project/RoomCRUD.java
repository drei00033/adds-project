package ads.project;

import java.sql.*;
import javax.swing.JOptionPane;

public class RoomCRUD {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    // Add Room
    public static void addRoom(String roomNo, String roomType, String rentPrice) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO ROOM (RoomNo, RoomType, RentPrice) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, roomNo);
            stmt.setString(2, roomType);
            stmt.setBigDecimal(3, new java.math.BigDecimal(rentPrice));

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
    public static void updateRoom(String roomId, String roomNo, String roomType, String rentPrice) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE ROOM SET RoomNo = ?, RoomType = ?, RentPrice = ? WHERE RoomID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, roomNo);
            stmt.setString(2, roomType);
            stmt.setBigDecimal(3, new java.math.BigDecimal(rentPrice));
            stmt.setInt(4, Integer.parseInt(roomId));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Room updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Room ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update room: " + e.getMessage());
        }
    }

    // Delete Room
    public static void deleteRoom(String roomId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM ROOM WHERE RoomID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(roomId));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Room deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Room ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete room: " + e.getMessage());
        }
    }
}
