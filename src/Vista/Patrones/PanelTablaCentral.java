package Vista.Patrones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelTablaCentral extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JPanel panelBotones;
    private Map<String, JButton> botones;
    private JPanel panelSuperior;
    private JComboBox<String> filtroComboBox;
    private JTextField campoBusqueda;

    public PanelTablaCentral() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición de celdas
            }
        };

        tabla = new JTable(modeloTabla);
        configurarEstiloTabla();

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(new Color(30, 30, 30));
        botones = new HashMap<>();
        add(panelBotones, BorderLayout.SOUTH);

        panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSuperior.setBackground(new Color(45, 45, 45));
        add(panelSuperior, BorderLayout.NORTH);
    }

    private void configurarEstiloTabla() {
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.setBackground(new Color(45, 45, 45));
        tabla.setForeground(Color.WHITE);
        tabla.getTableHeader().setBackground(new Color(60, 60, 60));
        tabla.getTableHeader().setForeground(Color.LIGHT_GRAY);

        // Deshabilitar la posibilidad de mover las columnas
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    public JLabel crearLabelConEstilo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    public JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campo.setBackground(new Color(80, 80, 80));
                campo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                campo.setBackground(new Color(70, 70, 70));
                campo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
        return campo;
    }

    public JButton crearBotonConEstilo(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(60, 60, 60));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    public void setModeloTabla(DefaultTableModel modelo) {
        this.modeloTabla = modelo;
        modeloTabla.setRowCount(0); // Limpiar filas antes de configurar el modelo
        tabla.setModel(modelo);

        // Asegurar que las celdas no sean editables y columnas no movibles
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    public void agregarCampoBusqueda(String placeholder) {
        JLabel label = crearLabelConEstilo(placeholder);
        campoBusqueda = crearCampoTexto();
        campoBusqueda.setColumns(20); // Define el ancho del campo de texto

        panelSuperior.add(label);
        panelSuperior.add(campoBusqueda);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JTextField getCampoBusqueda() {
        return campoBusqueda;
    }

    public JTable getTable() {
        return tabla;
    }

    public JPanel getPanelBotones() {
        return panelBotones;
    }
}
