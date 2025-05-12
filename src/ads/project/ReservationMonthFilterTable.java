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
        JLabel headerLabel = new JLabel("Reservation Records (By Month)", SwingConstants.CENTER);
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

        // Auto filter on month change
        monthComboBox.addActionListener(e -> {
            String selected = (String) monthComboBox.getSelectedItem();
            if (selected != null) {
                String month = selected.substring(0, 2); // "03"
                currentMonthText = selected.substring(5); // "March"
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

        // Table setup
        String[] columnNames = {
            "Reservation ID", "Check-In", "Check-Out",
            "Reservation Status", "Customer ID", "Package Code", "Payment ID"
        };
        reservationTable = new JTable(fetchReservationDataByMonth("01"), columnNames);
        reservationTable.setRowHeight(25);
        reservationTable.setFont(new Font("Arial", Font.PLAIN, 16));
        reservationTable.setBackground(new Color(220, 220, 220));
        reservationTable.setForeground(new Color(50, 50, 50));
        reservationTable.getTableHeader().setBackground(new Color(75, 83, 32));
        reservationTable.getTableHeader().setForeground(Color.WHITE);
        reservationTable.setSelectionBackground(new Color(150, 200, 150));
        reservationTable.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(reservationTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void refreshTable(String month) {
        String[][] data = fetchReservationDataByMonth(month);
        reservationTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{
            "Reservation ID", "Check-In", "Check-Out", "Reservation Status", "Customer ID", "Package Code", "Payment ID"
        }));
    }

    private String[][] fetchReservationDataByMonth(String month) {
        Vector<String[]> rows = new Vector<>();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;"
        )) {
            String sql = "SELECT * FROM RESERVATION WHERE MONTH(CheckInDate) = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(month));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("ReservationNo"),
                    rs.getString("CheckInDate"),
                    rs.getString("CheckOutDate"),
                    rs.getString("ReservationStatus"),
                    rs.getString("CustomerID"),
                    rs.getString("PackageCode"),
                    rs.getString("PaymentID")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String[][] data = new String[rows.size()][7];
        return rows.toArray(data);
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

    // Getter for printing externally (if needed)
    public JTable getReservationTable() {
        return reservationTable;
    }
}
