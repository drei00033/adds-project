package ads.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RoomTable extends JPanel {
    
    private JTable roomTable;
    private DefaultTableModel tableModel;
    // Make column names available for refreshing the table.
    private final String[] columnNames = {"Room No", "Room Type", "Rent Price"};

    public RoomTable() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Room Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");

        // Action for Add button
        add.addActionListener(e -> {
            String roomType = JOptionPane.showInputDialog("Enter Room Type:");
            String rentPrice = JOptionPane.showInputDialog("Enter Rent Price:");
            RoomCRUD.addRoom(roomType, rentPrice);
        });

        // Action for Update button
        update.addActionListener(e -> {
            String roomNo = JOptionPane.showInputDialog("Enter Room No. to Update:");
            String roomType = JOptionPane.showInputDialog("Enter New Room Type:");
            String rentPrice = JOptionPane.showInputDialog("Enter New Rent Price:");
            RoomCRUD.updateRoom(roomNo, roomType, rentPrice);
        });

        // Action for Delete button
        delete.addActionListener(e -> {
            String roomNo = JOptionPane.showInputDialog("Enter Room No to Delete:");
            RoomCRUD.deleteRoom(roomNo);
        });

        // Action for Refresh button: this re-fetches the table data from the database.
        refresh.addActionListener(e -> refreshTable());

        // Adding buttons to action panel
        for (JButton button : new JButton[]{add, update, delete, refresh}) {
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

        // Initialize table with data from the database.
        String[][] data = fetchRoomData();
        tableModel = new DefaultTableModel(data, columnNames);
        roomTable = new JTable(tableModel);
        
        roomTable.setRowHeight(25);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 16));
        roomTable.setBackground(new Color(220, 220, 220));
        roomTable.setForeground(new Color(50, 50, 50));
        roomTable.getTableHeader().setBackground(new Color(75, 83, 32));
        roomTable.getTableHeader().setForeground(Color.WHITE);
        roomTable.setSelectionBackground(new Color(150, 200, 150));
        roomTable.setSelectionForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        
        // Position table in the grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Method to refresh the table data.
    private void refreshTable() {
        String[][] data = fetchRoomData();
        // Update the table model with new data.
        tableModel.setDataVector(data, columnNames);
    }

    // Fetch updated data from the database.
    private String[][] fetchRoomData() {
        String[][] data = new String[0][3];

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;"
             );
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT * FROM ROOM")) {

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][3];
            int i = 0;

            while (rs.next()) {
                data[i][0] = String.valueOf(rs.getInt("RoomNo"));
                data[i][1] = rs.getString("RoomType");
                data[i][2] = rs.getString("RentPrice");
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }
}
