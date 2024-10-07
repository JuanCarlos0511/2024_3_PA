package Parte1;

import javax.swing.*;
import java.awt.*;

public class Practica06_a extends JFrame {
    public Practica06_a() {
        
        setLayout(new FlowLayout());

        
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        
        panel1.add(new JLabel("Panel 1"));
        panel2.add(new JLabel("Panel 2"));
        panel3.add(new JLabel("Panel 3"));
        panel4.add(new JLabel("Panel 4"));

       
        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);

        
        setTitle("Practica06_a - FlowLayout");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Practica06_a();
    }
}
