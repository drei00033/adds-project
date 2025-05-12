package ads.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PaymentTable extends JPanel {

    public PaymentTable() {
        setLayout(new BorderLayout());

        //Header Panel 
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Payment Records", SwingConstants.CENTER);
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
            String customerId = JOptionPane.showInputDialog("Enter Customer ID:");
            String amount = JOptionPane.showInputDialog("Enter Amount:");
            String method = JOptionPane.showInputDialog("Enter Payment Method:");
            String date = JOptionPane.showInputDialog("Enter Payment Date (YYYY-MM-DD):");
            String fee = JOptionPane.showInputDialog("Enter Reservation Fee:");
            PaymentCRUD.addPayment(customerId, amount, method, date, fee);
         
        });

        update.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Payment ID to Update:");
            String customerId = JOptionPane.showInputDialog("Enter New Customer ID:");
            String amount = JOptionPane.showInputDialog("Enter New Amount:");
            String method = JOptionPane.showInputDialog("Enter New Payment Method:");
            String date = JOptionPane.showInputDialog("Enter New Payment Date (YYYY-MM-DD):");
            String fee = JOptionPane.showInputDialog("Enter New Reservation Fee:");
            PaymentCRUD.updatePayment(id, customerId, amount, method, date, fee);
      
        });

        delete.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Payment ID to Delete:");
            PaymentCRUD.deletePayment(id);
          
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
        String[] columnNames = { "Payment ID", "Customer ID", "Amount", "Method", "Date" };
        String[][] data = fetchPaymentData();
        JTable paymentTable = new JTable(data, columnNames);
        paymentTable.setRowHeight(25);
        paymentTable.setFont(new Font("Arial", Font.PLAIN, 16));
        paymentTable.setBackground(new Color(220, 220, 220));
        paymentTable.setForeground(new Color(50, 50, 50));
        paymentTable.getTableHeader().setBackground(new Color(75, 83, 32));
        paymentTable.getTableHeader().setForeground(Color.WHITE);
        paymentTable.setSelectionBackground(new Color(150, 200, 150));
        paymentTable.setSelectionForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        //Position
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    // Fetch data from the database
    private String[][] fetchPaymentData() {
        String[][] data = new String[0][5];

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM PAYMENT");

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][5];
            int i = 0;
            while (rs.next()) {
                data[i][0] = String.valueOf(rs.getInt("PaymentID"));
                data[i][1] = rs.getString("PaymentMethod");
                data[i][2] = rs.getString("PaymentDate");
                data[i][3] = rs.getString("ReservationFee");
                data[i][4] = rs.getString("Amount");
                i++;
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }
}
