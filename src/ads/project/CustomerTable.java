package ads.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CustomerTable extends JPanel {

    public CustomerTable() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Customer Reservations", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Table setup
        String[] columnNames = {"Customer Name", "Email", "Contact No", "Reservation Period"};
        String[][] data = fetchCustomerData();
        JTable customerTable = new JTable(data, columnNames);
        customerTable.setRowHeight(25);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 16));
        customerTable.setBackground(new Color(220, 220, 220));
        customerTable.setForeground(new Color(50, 50, 50));
        customerTable.getTableHeader().setBackground(new Color(75, 83, 32));
        customerTable.getTableHeader().setForeground(Color.WHITE);
        customerTable.setSelectionBackground(new Color(150, 200, 150));
        customerTable.setSelectionForeground(Color.BLACK);

        // Positioning
        JScrollPane scrollPane = new JScrollPane(customerTable);
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Fetch data from the database
    private String[][] fetchCustomerData() {
        String[][] data = new String[0][4];
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM CustomerTable");

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][4];
            int i = 0;

            while (rs.next()) {
                data[i][0] = rs.getString("CustomerName");
                data[i][1] = rs.getString("Email");
                data[i][2] = rs.getString("ContactNo");
                data[i][3] = rs.getString("ReservationPeriod");
                i++;
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}

