package ads.project;

import javax.swing.*;
import java.awt.*;

public class gridbaglayout {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GridBagLayout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton button1 = new JButton("Button 1");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        pane.add(button1, c);

        JButton button2 = new JButton("Button 2");
        c.gridx = 1;
        c.gridy = 0;
        pane.add(button2, c);

        JButton button3 = new JButton("Button 3");
        c.gridx = 3;
        c.gridy = 0;
        pane.add(button3, c);
        
        JButton button32 = new JButton("Button 32");
        c.gridx = 4;
        c.gridy = 0;
        pane.add(button32, c);

        JButton button4 = new JButton("Long-Named Button 4");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(button4, c);

        JButton button5 = new JButton("5");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0; // reset to default
        c.weighty = 1.0; // request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; // bottom of space
        c.insets = new Insets(10, 0, 0, 0); // top padding
        c.gridx = 1; // aligned with button 2
        c.gridwidth = 2; // 2 columns wide
        c.gridy = 2; // third row
        pane.add(button5, c);

        frame.add(pane);
        frame.setVisible(true);
    }
}