package ads.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RoomTable extends JPanel {

    public RoomTable() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Room Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        //Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        
        add.addActionListener(e -> {
            String roomNo = JOptionPane.showInputDialog("Enter Room Number:");
            String roomType = JOptionPane.showInputDialog("Enter Room Type:");
            String rentPrice = JOptionPane.showInputDialog("Enter Rent Price:");
            RoomCRUD.addRoom(roomNo, roomType, rentPrice);
          
        });

        update.addActionListener(e -> {
            String roomId = JOptionPane.showInputDialog("Enter Room ID to Update:");
            String roomNo = JOptionPane.showInputDialog("Enter New Room Number:");
            String roomType = JOptionPane.showInputDialog("Enter New Room Type:");
            String rentPrice = JOptionPane.showInputDialog("Enter New Rent Price:");
            RoomCRUD.updateRoom(roomId, roomNo, roomType, rentPrice);
          
        });

        delete.addActionListener(e -> {
            String roomId = JOptionPane.showInputDialog("Enter Room ID to Delete:");
            RoomCRUD.deleteRoom(roomId);
           
        });

        
        actionPanel.add(add);
        actionPanel.add(update);
        actionPanel.add(delete);
        
        for (JButton button : new JButton[]{add, update, delete}) {
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.setFocusPainted(false);

                actionPanel.add(button);
        }
        //Main Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        //Table setup 
        String[] columnNames = { "Room ID", "Room No", "Room Type", "Rent Price" };
        String[][] data = fetchRoomData();
        JTable roomTable = new JTable(data, columnNames);
        
        
        roomTable.setRowHeight(25);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 16));
        roomTable.setBackground(new Color(220, 220, 220));
        roomTable.setForeground(new Color(50, 50, 50));
        roomTable.getTableHeader().setBackground(new Color(75, 83, 32));
        roomTable.getTableHeader().setForeground(Color.WHITE);
        roomTable.setSelectionBackground(new Color(150, 200, 150));
        roomTable.setSelectionForeground(Color.BLACK);
        
        
        JScrollPane scrollPane = new JScrollPane(roomTable);

        //Position 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    //Fetch data from the database
    private String[][] fetchRoomData() {
        String[][] data = new String[0][4];

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;"
            );
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM ROOM");

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][4];
            int i = 0;

            while (rs.next()) {
                data[i][0] = String.valueOf(rs.getInt("RoomID"));
                data[i][1] = rs.getString("RoomNo");
                data[i][2] = rs.getString("RoomType");
                data[i][3] = rs.getString("RentPrice");
                i++;
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }
}
