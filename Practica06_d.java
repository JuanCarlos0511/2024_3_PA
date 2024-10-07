package Parte1;

import javax.swing.*;
import java.awt.*;

public class Practica06_d extends JFrame {
    public Practica06_d() {
        
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(layout);

        
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        
        panel1.add(new JLabel("Panel 1"));
        panel2.add(new JLabel("Panel 2"));
        panel3.add(new JLabel("Panel 3"));
        panel4.add(new JLabel("Panel 4"));

        
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(panel1, gbc);

        gbc.gridy = 1;
        add(panel2, gbc);

        gbc.gridy = 2;
        gbc.gridheight = 3;
        add(panel3, gbc);

        gbc.gridy = 5;
        gbc.gridheight = 1;
        add(panel4, gbc);

        
        setTitle("Practica06_d - GridBagLayout");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Practica06_d();
    }
}
