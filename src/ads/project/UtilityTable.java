package ads.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UtilityTable extends JPanel {

    private JTable utilityTable;
    private DefaultTableModel tableModel;
    private final String[] columnNames = { "Utility No", "Appliance", "Price" };

    public UtilityTable() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Utility Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Action Panel and Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        
        // Add Button Action
        add.addActionListener(e -> {
            String appliance = JOptionPane.showInputDialog("Enter Appliance:");
            String price = JOptionPane.showInputDialog("Enter Price:");
            UtilityCRUD.addUtility(appliance, price);
        });

        // Update Button Action
        update.addActionListener(e -> {
            String utilityNo = JOptionPane.showInputDialog("Enter Utility No to Update:");
            String appliance = JOptionPane.showInputDialog("Enter New Appliance:");
            String price = JOptionPane.showInputDialog("Enter New Price:");
            UtilityCRUD.updateUtility(utilityNo, appliance, price);
        });

        // Delete Button Action
        delete.addActionListener(e -> {
            String utilityNo = JOptionPane.showInputDialog("Enter Utility No to Delete:");
            UtilityCRUD.deleteUtility(utilityNo);
        });

        // Refresh Button Action
        refresh.addActionListener(e -> refreshTable());

        // Add and style buttons in one loop
        JButton[] buttons = { add, update, delete, refresh };
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setFocusPainted(false);
            actionPanel.add(button);
        }
        add(actionPanel, BorderLayout.SOUTH);

        // Main Panel: Table Setup
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Initialize table with data from the database
        String[][] data = fetchUtilityData();
        tableModel = new DefaultTableModel(data, columnNames);
        utilityTable = new JTable(tableModel);
        
        utilityTable.setRowHeight(25);
        utilityTable.setFont(new Font("Arial", Font.PLAIN, 16));
        utilityTable.setBackground(new Color(220, 220, 220));
        utilityTable.setForeground(new Color(50, 50, 50));
        utilityTable.getTableHeader().setBackground(new Color(75, 83, 32));
        utilityTable.getTableHeader().setForeground(Color.WHITE);
        utilityTable.setSelectionBackground(new Color(150, 200, 150));
        utilityTable.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(utilityTable);

        // Position the scroll pane in the grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Refresh the table data by re-fetching the database information.
    private void refreshTable() {
        String[][] data = fetchUtilityData();
        tableModel.setDataVector(data, columnNames);
    }

    // Fetch updated utility data from the database.
    private String[][] fetchUtilityData() {
        String[][] data = new String[0][3];

        String url = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";
        String query = "SELECT * FROM UTILITY";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(query)) {

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][3];
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString("UtilityNo");
                data[i][1] = rs.getString("Appliance");
                data[i][2] = rs.getString("Price");
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }
}
