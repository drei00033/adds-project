package ads.project;

import java.sql.Connection;
import java.sql.*;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLConnection {

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=reservation;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";
            Connection connection = DriverManager.getConnection(dbURL);
            Statement statement = connection.createStatement();
            String query = "Select firstname + ' ' + lastname from customer";
            ResultSet res = statement.executeQuery(query);
            
            while (res.next()){
                System.out.println(res.getString(1));
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
