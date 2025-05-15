package ads.project;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AdminDashboard extends JFrame {
    
    public AdminDashboard () {
            JFrame frame = new JFrame("Admin Dashboard - Luz Ville Resort");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            
            // Sidebar
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(new Color(107, 142, 35));
            sidebar.setPreferredSize(new Dimension(250, frame.getHeight()));

            // sidebar buttons
            String[] buttonNames = {"Customers", "Reservations", "Payments", "Room Order", "Utility Order", "Package", "Room", "Utility", "Summary"};
            
           ;

            for (String btnText : buttonNames) {
                JButton button = new JButton(btnText);
                button.setMaximumSize(new Dimension(200, 50));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setBackground(new Color(75, 83, 32));
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createLineBorder(new Color(50, 60, 80), 2));
                
                button.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        button.setBackground(new Color(100, 120, 50));
                    }
                    public void mouseExited(MouseEvent e) {
                        button.setBackground(new Color(75, 83, 32));
                    }
                });

                sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
                sidebar.add(button);
            }

            
            // Main panel - actionpabel and CardPanel
            JPanel mainPanel = new JPanel(new BorderLayout());
            //ActionPanel
            JPanel actionPanel = new JPanel();
            actionPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 10));

            JButton addButton = new JButton("Add");
            JButton updateButton = new JButton("Update");
            JButton deleteButton = new JButton("Delete");

            //button styling
            for (JButton button : new JButton[]{addButton, updateButton, deleteButton}) {
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.setFocusPainted(false);

                actionPanel.add(button);
            }
            //CardPanel
            JPanel cardPanel = new JPanel(new CardLayout());

            // Initialize panels
            CustomerTable customerPanel = new CustomerTable();
            ReservationTable reservationPanel = new ReservationTable();
            PaymentTable paymentsPanel = new PaymentTable();
            RoomOrderTable roomOrderPanel = new RoomOrderTable();
            UtilityOrderTable utilityOrderPanel = new UtilityOrderTable();
            PackageTable packagePanel = new PackageTable();
            RoomTable roomPanel = new RoomTable();
            UtilityTable utilityPanel = new UtilityTable();
            ReservationMonthFilterTable reportsPanel = new ReservationMonthFilterTable();
           
            
            // Add panels to cardPanel
            cardPanel.add(customerPanel, "Customers");
            cardPanel.add(reservationPanel, "Reservations");
            cardPanel.add(paymentsPanel, "Payments");
            cardPanel.add(roomOrderPanel, "Room Order");
            cardPanel.add(utilityOrderPanel, "Utility Order");
            
            cardPanel.add(packagePanel, "Package");
            cardPanel.add(roomPanel, "Room");
            cardPanel.add(utilityPanel, "Utility");
            cardPanel.add(reportsPanel, "Summary");
            

            // ActionListeners 
            for (Component comp : sidebar.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    btn.addActionListener(e -> {
                        CardLayout cl = (CardLayout) (cardPanel.getLayout());
                        cl.show(cardPanel, btn.getText());
                    });
                }
            }

            // Add components 
            mainPanel.add(cardPanel, BorderLayout.CENTER);
            frame.add(sidebar, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);
            frame.setVisible(true);    
    }
}


