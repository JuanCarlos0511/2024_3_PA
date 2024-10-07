package Parte1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica05 extends JFrame implements ActionListener {

    private JDesktopPane Escritorio;
    private JMenuBar BarraMenu;
    private JMenu MOperacion, MConfiguracion, MSalir;
    private JMenuItem SMInsumos, SMCategorias, SMSalida;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public Practica05() {
        setTitle("Ventana Principal Practica05");
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Escritorio = new JDesktopPane();

        panelPrincipal = new JPanel();
        cardLayout = new CardLayout();
        panelPrincipal.setLayout(cardLayout);

        BarraMenu = new JMenuBar();
        setJMenuBar(BarraMenu);

        MOperacion = new JMenu("Operación");
        MConfiguracion = new JMenu("Configuración");
        MSalir = new JMenu("Salir");

        BarraMenu.add(MOperacion);
        BarraMenu.add(MConfiguracion);
        BarraMenu.add(MSalir);

        SMInsumos = new JMenuItem("Insumos");
        MOperacion.add(SMInsumos);

        SMCategorias = new JMenuItem("Categorías");
        MConfiguracion.add(SMCategorias);

        SMSalida = new JMenuItem("Salir");
        MSalir.add(SMSalida);

        SMCategorias.addActionListener(this);
        SMInsumos.addActionListener(this);
        SMSalida.addActionListener(this);

        add(panelPrincipal, BorderLayout.CENTER);

        panelPrincipal.add(Escritorio, "Escritorio");
        cardLayout.show(panelPrincipal, "Escritorio");

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == SMSalida) {
            dispose(); 
        } else if (e.getSource() == SMCategorias) {
            JOptionPane.showMessageDialog(this, "Llamando a Categorías");
        } else if (e.getSource() == SMInsumos) {
            Practica03_b hijo = new Practica03_b(); 
            Escritorio.add(hijo);  
            hijo.setVisible(true);  
        }
    }

    public static void main(String[] args) {
        new Practica05();
    }
}
