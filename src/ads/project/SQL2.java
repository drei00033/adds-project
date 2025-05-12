package ads.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL2 {
    public static void main(String[] args) {
        String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=campus;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";
        String user = "LAPTOP-979NQF0O\\Renzen"; // Change to your username
        String password = ""; // Change to your password

        try {
            // Approach 1: Automatic Driver Loading (JDBC 4.0+)
            System.out.println("Attempting automatic driver loading...");
            Connection conn1 = DriverManager.getConnection(dbURL, user, password);
            System.out.println("Connected using automatic driver loading!\n");

            // Approach 2: Explicit Driver Loading (Legacy JDBC 3.0)
            System.out.println("Attempting manual driver loading...");
            Class.forName("com.mysql.cj.jdbc.Driver"); // Manually loading driver
            Connection conn2 = DriverManager.getConnection(dbURL, user, password);
            System.out.println("Connected using manual driver loading!");
            // Closing connections
            conn1.close();
            conn2.close();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found! " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failed! " + e.getMessage());
        }
    }
}

