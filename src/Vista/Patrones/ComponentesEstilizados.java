package Vista.Patrones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ComponentesEstilizados extends JPanel {

    // Componentes compartidos
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JPanel panelBotones;
    private JPanel panelSuperior;
    private JTextField campoBusqueda;

    public ComponentesEstilizados() {
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
        add(panelBotones, BorderLayout.SOUTH);

        panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSuperior.setBackground(new Color(45, 45, 45));
        add(panelSuperior, BorderLayout.NORTH);
    }

    public void configurarEstiloTabla() {
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.setBackground(new Color(45, 45, 45));
        tabla.setForeground(Color.WHITE);
        tabla.getTableHeader().setBackground(new Color(60, 60, 60));
        tabla.getTableHeader().setForeground(Color.LIGHT_GRAY);

        // Deshabilitar la posibilidad de mover las columnas
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    // Métodos para crear componentes reutilizables
    public static JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    public static JTextField crearCampoTexto() {
        JTextField campo = new JTextField(15);
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campo.setBackground(new Color(80, 80, 80));
                campo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                campo.setBackground(new Color(60, 60, 60));
                campo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
        return campo;
    }

    public static JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(60, 60, 60));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    public static JComboBox<String> crearComboBox(String[] opciones) {
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(150, 25));
        return comboBox;
    }

    public void agregarCampoBusqueda(String placeholder) {
        JLabel label = crearEtiqueta(placeholder);
        campoBusqueda = crearCampoTexto();
        campoBusqueda.setColumns(20); // Define el ancho del campo de texto

        panelSuperior.add(label);
        panelSuperior.add(campoBusqueda);
    }

    // Métodos para manejar la tabla
    public void setModeloTabla(DefaultTableModel modelo) {
        this.modeloTabla = modelo;
        modeloTabla.setRowCount(0); // Limpiar filas antes de configurar el modelo
        tabla.setModel(modelo);

        // Asegurar que las celdas no sean editables y columnas no movibles
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JTable getTable() {
        return tabla;
    }

    public JPanel getPanelBotones() {
        return panelBotones;
    }

    public static JTextArea crearAreaTexto() {
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setBackground(new Color(60, 60, 60));
        areaTexto.setForeground(Color.WHITE);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        areaTexto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return areaTexto;
    }
    
    public static JScrollPane crearScrollPane(JTextArea areaTexto) {
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return scrollPane;
    }
    

    // Crear y mostrar un formulario emergente
    public static void crearFormularioEmergente(Frame owner, String titulo, Map<String, String> etiquetas, String[] categorias) {
        JDialog dialog = new JDialog(owner, titulo, true);
        dialog.setSize(800, 300);
        dialog.setLocationRelativeTo(owner);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(45, 45, 45));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (Map.Entry<String, String> entry : etiquetas.entrySet()) {
            JLabel label = crearEtiqueta(entry.getValue());
            JTextField campo = crearCampoTexto();

            gbc.gridx = 0;
            gbc.gridy++;
            panelFormulario.add(label, gbc);

            gbc.gridx = 1;
            panelFormulario.add(campo, gbc);
        }

        if (categorias != null) {
            JLabel label = crearEtiqueta("Categoría:");
            JComboBox<String> comboBox = crearComboBox(categorias);

            gbc.gridx = 0;
            gbc.gridy++;
            panelFormulario.add(label, gbc);

            gbc.gridx = 1;
            panelFormulario.add(comboBox, gbc);
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton botonAceptar = crearBoton("Aceptar");
        JButton botonCancelar = crearBoton("Cancelar");

        botonAceptar.addActionListener(e -> dialog.dispose());
        botonCancelar.addActionListener(e -> dialog.dispose());

        panelBotones.add(botonAceptar);
        panelBotones.add(botonCancelar);

        dialog.add(panelFormulario, BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        dialog.setVisible(true);
        
    }

    public static void estilizarComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        comboBox.setFocusable(false);
    }
}
