package ads.project;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReservationTable extends JPanel {

    private DefaultTableModel tableModel;
    private JTable reservationTable;

    public ReservationTable() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Reservation Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Column names including Confirmation
        String[] columnNames = {
            "Reservation ID", "Check-In", "Check-Out",
            "Reservation Status", "Customer ID", "Package Code", "Payment ID", "Confirmation"
        };

        // Fetch data from DB
        String[][] data = fetchReservationData();

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Confirmation column is editable for the button
                return column == 7;
            }
        };

        reservationTable = new JTable(tableModel);
        reservationTable.setRowHeight(25);
        reservationTable.setFont(new Font("Arial", Font.PLAIN, 16));
        reservationTable.setBackground(new Color(220, 220, 220));
        reservationTable.setForeground(new Color(50, 50, 50));
        reservationTable.getTableHeader().setBackground(new Color(75, 83, 32));
        reservationTable.getTableHeader().setForeground(Color.WHITE);
        reservationTable.setSelectionBackground(new Color(150, 200, 150));
        reservationTable.setSelectionForeground(Color.BLACK);

        // Set custom renderer and editor for Confirmation column
        reservationTable.getColumn("Confirmation").setCellRenderer(new ConfirmationRenderer());
        reservationTable.getColumn("Confirmation").setCellEditor(new ConfirmationEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(reservationTable);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    private String[][] fetchReservationData() {
        String[][] data = new String[0][8]; // 8 columns now

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM RESERVATION");

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][8];
            int i = 0;

            while (rs.next()) {
                String status = rs.getString("ReservationStatus");
                data[i][0] = String.valueOf(rs.getInt("ReservationNo"));
                data[i][1] = rs.getString("CheckInDate");
                data[i][2] = rs.getString("CheckOutDate");
                data[i][3] = status;
                data[i][4] = rs.getString("CustomerID");
                data[i][5] = rs.getString("PackageCode");
                data[i][6] = rs.getString("PaymentID");
                // Set Confirmation column depending on status
                if ("Pending".equalsIgnoreCase(status)) {
                    data[i][7] = "Check"; // Will display button
                } else {
                    data[i][7] = "Already Paid"; // Text only
                }
                i++;
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    // Renderer to display button or label depending on cell value
    class ConfirmationRenderer extends JButton implements TableCellRenderer {
        private final JLabel label = new JLabel();

        public ConfirmationRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if ("Check".equals(value)) {
                setText("✓");
                setBackground(new Color(150, 200, 150));
                setForeground(Color.BLACK);
                return this;
            } else {
                label.setText(value != null ? value.toString() : "");
                label.setOpaque(true);
                label.setBackground(new Color(200, 230, 200));
                label.setForeground(Color.BLACK);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        }
    }

    // Editor with button click handling
    class ConfirmationEditor extends DefaultCellEditor {
        private JButton button;
        private String reservationId;
        private boolean isPushed;

        public ConfirmationEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(150, 200, 150));
            button.setForeground(Color.BLACK);
            button.setText("✓");

            button.addActionListener(e -> {
                if (isPushed) {
                    // Update DB status to Paid
                    updateReservationStatusToPaid(reservationId);

                    // Update model only for this row - no full refresh
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (tableModel.getValueAt(i, 0).toString().equals(reservationId)) {
                            tableModel.setValueAt("Paid", i, 3);            // Update status
                            tableModel.setValueAt("Already Paid", i, 7);    // Update confirmation text
                            break;
                        }
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            isPushed = true;
            reservationId = table.getValueAt(row, 0).toString();
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return "Already Paid";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    // Update reservation status in the database to 'Paid'
    private void updateReservationStatusToPaid(String reservationId) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=SYSTEM;encrypt=true;trustServerCertificate=true;integratedSecurity=true;");
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE RESERVATION SET ReservationStatus = 'Paid' WHERE ReservationNo = ?");
            ps.setInt(1, Integer.parseInt(reservationId));
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
