package ads.project;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerTable extends JPanel {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;

    public CustomerTable() {
        setLayout(new BorderLayout());

        // Top Panel (vertical layout)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Header Label
        JLabel headerLabel = new JLabel("Customer Resecords");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0)); // spacing
        topPanel.add(headerLabel);

        // Search Panel (Top-Left)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search: ");
        searchField = new JTextField(20);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        topPanel.add(searchPanel);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Customer Name", "Email", "Contact No", "Reservation Period"};
        String[][] data = fetchCustomerData();
        tableModel = new DefaultTableModel(data, columnNames);
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(20);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 16));
        customerTable.setBackground(new Color(220, 220, 220));
        customerTable.setForeground(new Color(50, 50, 50));
        customerTable.getTableHeader().setBackground(new Color(75, 83, 32));
        customerTable.getTableHeader().setForeground(Color.WHITE);
        customerTable.setSelectionBackground(new Color(150, 200, 150));
        customerTable.setSelectionForeground(Color.BLACK);

        // Enable row sorting and filtering
        rowSorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(rowSorter);

        // Search field listener
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable(searchField.getText());
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable(searchField.getText());
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable(searchField.getText());
            }

            private void filterTable(String text) {
                if (text.trim().isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0)); // Filter by customer name
                }
            }
        });

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button (Bottom-Right)
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTable());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        String[][] newData = fetchCustomerData();
        tableModel.setDataVector(newData, new String[]{"Customer Name", "Email", "Contact No", "Reservation Period"});
        rowSorter.setModel(tableModel); // Reset sorter to new data
    }

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

