package ads.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Project extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Project() {
        setTitle("Admin Login - Luz Ville Resort");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Header Panel 
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Welcome to Luz Ville Resort!", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headerLabel.setForeground(new Color(75, 83, 32));
        headerPanel.add(headerLabel);

        //Main Panel 
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        //Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField = new JTextField(15);

        //password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField = new JPasswordField(15);

        //loginbutton
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(75, 83, 32)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateAdmin();
            }
        });
        
        // Positioning Comp
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        contentPanel.add(loginButton, gbc);
                     
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void authenticateAdmin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=reservation;encrypt=true;trustServerCertificate=true;integratedSecurity=true;");
            String query = "SELECT * FROM ADMIN_DETAILS WHERE adminUserName = ? AND adminPassword = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + username);
                dispose(); 
                SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.");
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Project().setVisible(true));
    }
}
