package Vista.Informes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Controlador.Controlador;

public class InformesInventarioPanel extends JPanel {

    private JTable inventarioTable;
    private JButton exportarInformeButton;
    private JComboBox<String> filtroCategoriaComboBox;
    private DefaultTableModel modeloTabla;

    public InformesInventarioPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(45, 45, 45));

        JLabel labelFiltro = new JLabel("Filtrar por Categoría:");
        labelFiltro.setForeground(Color.LIGHT_GRAY);
        topPanel.add(labelFiltro);

        // Categorías disponibles para el filtro
        String[] categorias = {"Todas", "Frutas", "Verduras", "Bebidas", "Libros", "Papelería", 
                                "Electrónica", "Dulces", "Carnes", "Higiene", "Ropa", "Calzado"};
        filtroCategoriaComboBox = new JComboBox<>(categorias);
        filtroCategoriaComboBox.setBackground(new Color(60, 60, 60));
        filtroCategoriaComboBox.setForeground(Color.WHITE);
        filtroCategoriaComboBox.addActionListener(e -> actualizarTabla()); // Listener para el filtro
        topPanel.add(filtroCategoriaComboBox);

        add(topPanel, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID de Producto", "Nombre", "Cantidad Disponible", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        inventarioTable = new JTable(modeloTabla);
        inventarioTable.setBackground(new Color(45, 45, 45));
        inventarioTable.setForeground(Color.WHITE);
        inventarioTable.getTableHeader().setBackground(new Color(60, 60, 60));
        inventarioTable.getTableHeader().setForeground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(inventarioTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        exportarInformeButton = crearBotonConEstilo("Exportar Informe");

        buttonPanel.add(exportarInformeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        actualizarTabla(); // Cargar datos iniciales
    }

    private JButton crearBotonConEstilo(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(60, 60, 60));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    public void agregarExportarInformeListener(ActionListener listener) {
        exportarInformeButton.addActionListener(listener);
    }

    public String getCategoriaSeleccionada() {
        return filtroCategoriaComboBox.getSelectedItem().toString();
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Actualizar la tabla según la categoría seleccionada
    private void actualizarTabla() {
        String categoriaSeleccionada = getCategoriaSeleccionada();

        // Limpiar la tabla actual
        modeloTabla.setRowCount(0);

        // Condición para filtrar por categoría (o mostrar todas)
        String condicion = categoriaSeleccionada.equals("Todas") ? null : "CATEGORIA = '" + categoriaSeleccionada + "'";

        // Consultar los datos desde el controlador
        ArrayList<String[]> datosInventario = Controlador.consultar("PRODUCTOS", null, 
                "IDPRODUCTO, NOMBRE, STOCK, CATEGORIA", condicion);

        // Agregar las filas al modelo de la tabla
        for (String[] fila : datosInventario) {
            modeloTabla.addRow(fila);
        }
    }
}
