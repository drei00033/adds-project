package ads.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReservationTable extends JPanel {

    public ReservationTable() {
        setLayout(new BorderLayout());

        //Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Reservation Records", SwingConstants.CENTER);
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
            String checkIn = JOptionPane.showInputDialog("Enter Check-In Date (YYYY-MM-DD):");
            String checkOut = JOptionPane.showInputDialog("Enter Check-Out Date (YYYY-MM-DD):");
            String status = JOptionPane.showInputDialog("Enter Reservation Status:");
            String customerId = JOptionPane.showInputDialog("Enter Customer ID:");
            String packageCode = JOptionPane.showInputDialog("Enter Package Code:");
            String paymentId = JOptionPane.showInputDialog("Enter Payment ID:");
            ReservationCRUD.addReservation(checkIn, checkOut, status, customerId, packageCode, paymentId);
           
        });

        update.addActionListener(e -> {
            String reservationNo = JOptionPane.showInputDialog("Enter Reservation Number to Update:");
            String checkIn = JOptionPane.showInputDialog("Enter New Check-In Date (YYYY-MM-DD):");
            String checkOut = JOptionPane.showInputDialog("Enter New Check-Out Date (YYYY-MM-DD):");
            String status = JOptionPane.showInputDialog("Enter New Reservation Status:");
            String customerId = JOptionPane.showInputDialog("Enter New Customer ID:");
            String packageCode = JOptionPane.showInputDialog("Enter New Package Code:");
            String paymentId = JOptionPane.showInputDialog("Enter New Payment ID:");
            ReservationCRUD.updateReservation(reservationNo, checkIn, checkOut, status, customerId, packageCode, paymentId);
           
        });

        delete.addActionListener(e -> {
            String reservationNo = JOptionPane.showInputDialog("Enter Reservation Number to Delete:");
            ReservationCRUD.deleteReservation(reservationNo);
           
        });

        
        
        actionPanel.add(add);
        actionPanel.add(update);
        actionPanel.add(delete);
        
        for (JButton button : new JButton[]{add, update, delete}) {
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.setFocusPainted(false);

                actionPanel.add(button);
        }
        //Main panel 
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        //Table setup 
        String[] columnNames = {
            "Reservation ID", "Check-In", "Check-Out",
            "Reservation Status", "Customer ID", "Package Code", "Payment ID"
        };
        String[][] data = fetchReservationData();
        JTable reservationTable = new JTable(data, columnNames);
        
        reservationTable.setRowHeight(25);
        reservationTable.setFont(new Font("Arial", Font.PLAIN, 16));
        reservationTable.setBackground(new Color(220, 220, 220));
        reservationTable.setForeground(new Color(50, 50, 50));
        reservationTable.getTableHeader().setBackground(new Color(75, 83, 32));
        reservationTable.getTableHeader().setForeground(Color.WHITE);
        reservationTable.setSelectionBackground(new Color(150, 200, 150));
        reservationTable.setSelectionForeground(Color.BLACK);
        
        //Positioning
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    //Fetch data from the database
    private String[][] fetchReservationData() {
        String[][] data = new String[0][7];
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM RESERVATION");
            
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            
            data = new String[rowCount][7];
            int i = 0;
            
            while (rs.next()) {
                data[i][0] = String.valueOf(rs.getInt("ReservationNo"));
                data[i][1] = rs.getString("CheckInDate");
                data[i][2] = rs.getString("CheckOutDate");
                data[i][3] = rs.getString("ReservationStatus");
                data[i][4] = rs.getString("CustomerID");
                data[i][5] = rs.getString("PackageCode");
                data[i][6] = rs.getString("PaymentID");
                i++;
            }
            
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
