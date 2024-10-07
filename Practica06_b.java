package Parte1;

import javax.swing.*;
import java.awt.*;

public class Practica06_b extends JFrame {
    public Practica06_b() {

        setLayout(new BorderLayout());

       
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panelNorte = new JPanel();

        
        panel1.add(new JLabel("Panel 1"));
        panel2.add(new JLabel("Panel 2"));
        panel3.add(new JLabel("Panel 3"));
        panel4.add(new JLabel("Panel 4"));

        
        panelNorte.add(panel1);
        panelNorte.add(panel2);
        add(panelNorte, BorderLayout.NORTH);
        add(panel3, BorderLayout.CENTER);
        add(panel4, BorderLayout.SOUTH);

        
        setTitle("Practica06_b - BorderLayout");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Practica06_b();
    }
}
