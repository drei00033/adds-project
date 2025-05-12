package ads.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CustomerTable extends JPanel {

    public CustomerTable() {
        setLayout(new BorderLayout());

        //Header Panel 
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Customer Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        //Action Panel 
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        
       
        
        // CRUD Actions
        add.addActionListener(e -> {
            String lastName = JOptionPane.showInputDialog("Enter Last Name:");
            String firstName = JOptionPane.showInputDialog("Enter First Name:");
            String middleName = JOptionPane.showInputDialog("Enter Middle Name:");
            String contact = JOptionPane.showInputDialog("Enter Contact No:");
            String email = JOptionPane.showInputDialog("Enter Email:");
            CustomerCRUD.addCustomer(lastName, firstName, middleName, contact, email);

        });

        update.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Customer ID to Update:");
            String lastName = JOptionPane.showInputDialog("Enter New Last Name:");
            String firstName = JOptionPane.showInputDialog("Enter New First Name:");
            String middleName = JOptionPane.showInputDialog("Enter New Middle Name:");
            String contact = JOptionPane.showInputDialog("Enter New Contact No:");
            String email = JOptionPane.showInputDialog("Enter New Email:");
            CustomerCRUD.updateCustomer(id, lastName, firstName, middleName, contact, email);

        });

        delete.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Customer ID to Delete:");
            CustomerCRUD.deleteCustomer(id);

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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        //Table setup
        String[] columnNames = {"Customer ID", "Last Name", "First Name", "Middle Name", "Contact No", "Email"};
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

        //Positioning
        JScrollPane scrollPane = new JScrollPane(customerTable);
        contentPanel.add(scrollPane, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    //Fetch data from the database
    private String[][] fetchCustomerData() {
        String[][] data = new String[0][6];
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM CUSTOMER");

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][6];
            int i = 0;

            while (rs.next()) {
                data[i][0] = rs.getString("CustomerID");   
                data[i][1] = rs.getString("LastName");
                data[i][2] = rs.getString("FirstName");
                data[i][3] = rs.getString("MiddleName");
                data[i][4] = rs.getString("ContactNo");
                data[i][5] = rs.getString("Email");
                i++;
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
