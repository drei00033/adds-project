package ads.project;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.*;
import java.util.Vector;

public class ReservationMonthFilterTable extends JPanel {

    private JTable reservationTable;
    private JComboBox<String> monthComboBox;
    private String currentMonthText = "January";

    public ReservationMonthFilterTable() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Summary", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        JLabel filterLabel = new JLabel("Select Month:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 16));

        monthComboBox = new JComboBox<>(new String[]{
            "01 - January", "02 - February", "03 - March", "04 - April",
            "05 - May", "06 - June", "07 - July", "08 - August",
            "09 - September", "10 - October", "11 - November", "12 - December"
        });
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        monthComboBox.addActionListener(e -> {
            String selected = (String) monthComboBox.getSelectedItem();
            if (selected != null) {
                String month = selected.substring(0, 2);
                currentMonthText = selected.substring(5);
                refreshTable(month);
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

        // Reservation Summary Table
        String[] summaryCols = {"Month", "Year", "Total Customers", "Total Reservation Fees", "Total Amount Paid", "Total Income"};
        reservationTable = new JTable(fetchReservationDataByMonth("01"), summaryCols);
        styleTable(reservationTable);
        add(new JScrollPane(reservationTable), BorderLayout.CENTER);
    }

    private void refreshTable(String month) {
        String[][] summaryData = fetchReservationDataByMonth(month);
        reservationTable.setModel(new javax.swing.table.DefaultTableModel(summaryData, new String[]{
            "Month", "Year", "Total Customers", "Total Reservation Fees",
            "Total Amount Paid", "Total Income"
        }));
    }

    private String[][] fetchReservationDataByMonth(String month) {
        Vector<String[]> rows = new Vector<>();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;")) {

            String sql = "SELECT * FROM MonthlyReservationReport WHERE Month = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(month));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("Month"),
                    rs.getString("Year"),
                    rs.getString("TotalCustomers"),
                    rs.getString("TotalReservationFees"),
                    rs.getString("TotalAmountPaid"),
                    rs.getString("TotalIncome")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rows.toArray(new String[0][]);
    }

    private void printReport() {
        try {
            boolean printed = reservationTable.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("Luz Ville Resort - Reservation Report for " + currentMonthText),
                    new java.text.MessageFormat("Page - {0}")
            );

            if (printed) {
                JOptionPane.showMessageDialog(this, "Report printed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Print was cancelled.");
            }
        } catch (PrinterException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to print report: " + e.getMessage());
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

    public JTable getReservationTable() {
        return reservationTable;
    }
}

