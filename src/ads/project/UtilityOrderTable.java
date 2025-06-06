package ads.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UtilityOrderTable extends JPanel {

    public UtilityOrderTable() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Utility Order Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel 
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Table setup
        String[] columnNames = { "Utility ID", "Utility No", "ReservationNo" };
        String[][] data = fetchUtilityOrderData();
        JTable utilityTable = new JTable(data, columnNames);
        
        utilityTable.setRowHeight(25);
        utilityTable.setFont(new Font("Arial", Font.PLAIN, 16));
        utilityTable.setBackground(new Color(220, 220, 220));
        utilityTable.setForeground(new Color(50, 50, 50));
        utilityTable.getTableHeader().setBackground(new Color(75, 83, 32));
        utilityTable.getTableHeader().setForeground(Color.WHITE);
        utilityTable.setSelectionBackground(new Color(150, 200, 150));
        utilityTable.setSelectionForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(utilityTable);

        // Positioning 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Fetch data from the database
    private String[][] fetchUtilityOrderData() {
        String[][] data = new String[0][3];

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;"
            );
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM UTILITY_ORDER");

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][3];
            int i = 0;
            while (rs.next()) {
                data[i][0] = String.valueOf(rs.getInt("UtilityID"));
                data[i][1] = rs.getString("UtilityNo");
                data[i][2] = rs.getString("ReservationNo");
                i++;
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}

