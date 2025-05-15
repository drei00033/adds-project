package ads.project;

import java.sql.*;
import javax.swing.JOptionPane;

public class PackageCRUD {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    // Add Package
    public static void addPackage(String code, String type, String price) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "INSERT INTO PACKAGE (PackageCode, Package, PackagePrice) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            stmt.setString(2, type);
            stmt.setBigDecimal(3, new java.math.BigDecimal(price));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Package added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add package: " + e.getMessage());
        }
    }

    // Update Package
    public static void updatePackage(String code, String newType, String newPrice) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "UPDATE PACKAGE SET Package = ?, PackagePrice = ? WHERE PackageCode = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newType);
            stmt.setBigDecimal(2, new java.math.BigDecimal(newPrice));
            stmt.setString(3, code);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Package updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Package code not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update package: " + e.getMessage());
        }
    }

    // Delete Package
    public static void deletePackage(String code) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "DELETE FROM PACKAGE WHERE PackageCode = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Package deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Package code not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete package: " + e.getMessage());
        }
    }
}
