package ads.project;

import java.sql.*;
import javax.swing.JOptionPane;

public class UtilityCRUD {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    // Add Room
    public static void addUtility(String appliance, String price) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO UTILITY (Appliance, Price) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, appliance);
            stmt.setBigDecimal(2, new java.math.BigDecimal(price));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Utility added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add utility: " + e.getMessage());
        }
    }

    // Update Room
    public static void updateUtility(String utilityNo, String appliance, String price) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE UTILITY SET Appliance = ?, Price = ? WHERE UtilityNo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, appliance);
            stmt.setBigDecimal(2, new java.math.BigDecimal(price));
            stmt.setInt(3, Integer.parseInt(utilityNo));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Utility updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Utility No not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update utility: " + e.getMessage());
        }
    }

    // Delete Room
    public static void deleteUtility(String utilityNo) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM UTILITY WHERE UtilityNo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(utilityNo));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Utility deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Utility No not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete utility: " + e.getMessage());
        }
    }
}
