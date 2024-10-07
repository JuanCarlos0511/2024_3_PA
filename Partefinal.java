package Parte1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Partefinal extends JFrame implements ActionListener {
    private JDesktopPane desktopPane;
    private JMenuBar menuBar;
    private JMenu menuCategoria, menuInsumos, menuObras;

    public Partefinal() {
        setTitle("Gestión de Categorías, Insumos y Obras");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        desktopPane = new JDesktopPane();
        add(desktopPane, BorderLayout.CENTER);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuCategoria = new JMenu("Categorías");
        menuBar.add(menuCategoria);
        JMenuItem itemCategoria = new JMenuItem("Abrir Categorías");
        itemCategoria.addActionListener(this);
        menuCategoria.add(itemCategoria);

        menuInsumos = new JMenu("Insumos");
        menuBar.add(menuInsumos);
        JMenuItem itemInsumos = new JMenuItem("Abrir Insumos");
        itemInsumos.addActionListener(this);
        menuInsumos.add(itemInsumos);

        menuObras = new JMenu("Obras");
        menuBar.add(menuObras);
        JMenuItem itemObras = new JMenuItem("Abrir Obras");
        itemObras.addActionListener(this);
        menuObras.add(itemObras);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Abrir Categorías")) {
            abrirInternalFrame(new CategoriasFrame());
        } else if (command.equals("Abrir Insumos")) {
            abrirInternalFrame(new InsumosFrame());
        } else if (command.equals("Abrir Obras")) {
            abrirInternalFrame(new ObrasFrame());
        }
    }

    private void abrirInternalFrame(JInternalFrame internalFrame) {
        internalFrame.setSize(400, 300);
        internalFrame.setVisible(true);
        desktopPane.add(internalFrame);
    }

    public static void main(String[] args) {
        new Partefinal();
    }
}
