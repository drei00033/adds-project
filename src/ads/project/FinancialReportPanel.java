package ads.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.*;
import java.util.Vector;

public class FinancialReportPanel extends JPanel {

    private JTable reportTable;
    private JComboBox<String> monthComboBox;
    private String currentMonthText = "All Months";

    // Database credentials
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "your_username";    // <-- Change this
    private static final String DB_PASSWORD = "your_password"; // <-- Change this

    public FinancialReportPanel() {
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Financial Report", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        add(headerLabel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        JLabel filterLabel = new JLabel("Select Month:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 16));

        monthComboBox = new JComboBox<>(new String[]{
                "All Months", "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        });
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        monthComboBox.addActionListener(e -> {
            String selected = (String) monthComboBox.getSelectedItem();
            currentMonthText = selected;
            if ("All Months".equals(selected)) {
                refreshTable(null);
            } else {
                refreshTable(selected);
            }
        });

        JButton printButton = new JButton("Print Report");
        printButton.setFont(new Font("Arial", Font.BOLD, 16));
        printButton.setFocusPainted(false);
        printButton.addActionListener(e -> printReport());

        filterPanel.add(filterLabel);
        filterPanel.add(monthComboBox);
        filterPanel.add(printButton);

        add(filterPanel, BorderLayout.SOUTH);

        // Table columns exactly matching SQL view columns
        String[] columns = {"Month", "Year", "Total Customers", "Total Income", "Utility Rentals", "Room Rentals"};

        // Initialize JTable with empty data
        reportTable = new JTable(new DefaultTableModel(new Object[][]{}, columns));
        styleTable(reportTable);

        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // Load initial data for all months
        refreshTable(null);
    }

    private void refreshTable(String monthName) {
        Vector<Vector<Object>> rows = new Vector<>();
        try {
            // Load driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT MonthName, Year, TotalCustomers, TotalIncome, UtilityRentals, RoomRentals FROM FinancialReportView";
                if (monthName != null) {
                    sql += " WHERE MonthName = ?";
                }
                sql += " ORDER BY Year, MonthName";

                PreparedStatement stmt = conn.prepareStatement(sql);
                if (monthName != null) {
                    stmt.setString(1, monthName);
                }
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("MonthName"));
                    row.add(rs.getInt("Year"));
                    row.add(rs.getInt("TotalCustomers"));
                    row.add(rs.getBigDecimal("TotalIncome"));
                    row.add(rs.getInt("UtilityRentals"));
                    row.add(rs.getInt("RoomRentals"));
                    rows.add(row);
                }
            }

            if (rows.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No data found for the selected month.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "SQL Server JDBC Driver not found.", "Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error:\n" + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Update table model
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Month");
        columnNames.add("Year");
        columnNames.add("Total Customers");
        columnNames.add("Total Income");
        columnNames.add("Utility Rentals");
        columnNames.add("Room Rentals");

        DefaultTableModel model = new DefaultTableModel(rows, columnNames) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable.setModel(model);
        styleTable(reportTable);
    }

    private void printReport() {
        try {
            boolean printed = reportTable.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("Luz Ville Resort - Financial Report for " + currentMonthText),
                    new java.text.MessageFormat("Page - {0}")
            );
            if (printed) {
                JOptionPane.showMessageDialog(this, "Report printed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Print was cancelled.");
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Failed to print report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setBackground(new Color(220, 220, 220));
        table.setForeground(new Color(50, 50, 50));
        table.getTableHeader().setBackground(new Color(75, 83, 32));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(150, 200, 150));
        table.setSelectionForeground(Color.BLACK);
    }

    // For quick testing standalone
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Financial Report Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);

            FinancialReportPanel panel = new FinancialReportPanel();
            frame.add(panel);

            frame.setVisible(true);
        });
    }
}
