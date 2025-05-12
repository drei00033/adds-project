package ads.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PackageTable extends JPanel {

    public PackageTable() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Package Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        //Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        
       JButton add = new JButton("Add");
       JButton update = new JButton("Update");
       JButton delete = new JButton("Delete");

        // Action listeners for CRUD
        add.addActionListener(e -> {
            String code = JOptionPane.showInputDialog("Enter Package Code:");
            String type = JOptionPane.showInputDialog("Enter Package Type:");
            String price = JOptionPane.showInputDialog("Enter Package Price:");
            PackageCRUD.addPackage(code, type, price);
        });

        update.addActionListener(e -> {
            String code = JOptionPane.showInputDialog("Enter Package Code to Update:");
            String newType = JOptionPane.showInputDialog("Enter New Package Type:");
            String newPrice = JOptionPane.showInputDialog("Enter New Package Price:");
            PackageCRUD.updatePackage(code, newType, newPrice);
        });

        delete.addActionListener(e -> {
            String code = JOptionPane.showInputDialog("Enter Package Code to Delete:");
            PackageCRUD.deletePackage(code);
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
        String[] columnNames = { "Package Code", "Package", "Package Price" };
        String[][] data = fetchPackageData();
        JTable packageTable = new JTable(data, columnNames);
        
        packageTable.setRowHeight(25);
        packageTable.setFont(new Font("Arial", Font.PLAIN, 16));
        packageTable.setBackground(new Color(220, 220, 220));
        packageTable.setForeground(new Color(50, 50, 50));
        packageTable.getTableHeader().setBackground(new Color(75, 83, 32));
        packageTable.getTableHeader().setForeground(Color.WHITE);
        packageTable.setSelectionBackground(new Color(150, 200, 150));
        packageTable.setSelectionForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(packageTable);

        //Positioning 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);
        
        add(actionPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    //Fetch data fromm database
    private String[][] fetchPackageData() {
        String[][] data = new String[0][3];

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;"
            );
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM PACKAGE");

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][3];
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString("PackageCode");
                data[i][1] = rs.getString("Package");
                data[i][2] = rs.getString("PackagePrice");
                i++;
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }
}
