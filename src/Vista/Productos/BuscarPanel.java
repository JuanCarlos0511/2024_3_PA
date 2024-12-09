package Vista.Productos;

import Controlador.Controlador;
import Vista.Patrones.PanelTablaCentral;
import Vista.Patrones.Interfaces.InterfaceActualizar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BuscarPanel extends JPanel implements InterfaceActualizar {

    private PanelTablaCentral panelTablaCentral;
    private DefaultTableModel modeloTabla;

    public BuscarPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        panelTablaCentral = new PanelTablaCentral();

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Categoria", "Precio Unitario", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición de las celdas
            }
        };

        panelTablaCentral.setModeloTabla(modeloTabla);
        panelTablaCentral.agregarCampoBusqueda("Buscar Producto:");
        JTextField campoBusqueda = panelTablaCentral.getCampoBusqueda();

        // Agregar listener al campo de búsqueda
        campoBusqueda.addActionListener(e -> buscarProductos(campoBusqueda.getText().trim()));

        add(panelTablaCentral, BorderLayout.CENTER);
        actualizar(); // Cargar todos los productos al iniciar
    }

    
    private void buscarProductos(String busqueda) {
        modeloTabla.setRowCount(0); // Limpiar tabla

        String condicion = "NOMBRE LIKE '%" + busqueda + "%'";
        ArrayList<String[]> productos = Controlador.consultar("PRODUCTOS", null, null, condicion);

        for (String[] producto : productos) {
            modeloTabla.addRow(new Object[]{
                producto[0], // IDProducto
                producto[1], // Nombre
                producto[2], // Categoría
                String.format("$%.2f", Double.parseDouble(producto[3])), // PrecioUnitario
                producto[4] != null ? producto[4] : "Agotado" // Stock (manejar NULL como "Agotado")
            });
        }

        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron productos.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public JTextField getCampoBusqueda() {
        return panelTablaCentral.getCampoBusqueda();
    }

    @Override
    public void actualizar() {
        modeloTabla.setRowCount(0); // Limpiar tabla

        ArrayList<String[]> productos = Controlador.consultar("PRODUCTOS", null, null, null);

        for (String[] producto : productos) {
            modeloTabla.addRow(new Object[]{
                producto[0], // IDProducto
                producto[1], // Nombre
                producto[2], // Categoría
                String.format("$%.2f", Double.parseDouble(producto[3])), // PrecioUnitario
                producto[4] != null ? producto[4] : "Agotado" // Stock (manejar NULL como "Agotado")
            });
        }
    }

}
