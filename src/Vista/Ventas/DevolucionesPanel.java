package Vista.Ventas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Controlador.Controlador;
import Vista.Patrones.PanelTablaCentral;
import Vista.Patrones.Interfaces.InterfaceActualizar;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DevolucionesPanel extends JPanel implements InterfaceActualizar {

    private PanelTablaCentral panelTablaCentral;
    private JTextField campoBusqueda;
    private JButton botonDevolver;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    public DevolucionesPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        panelTablaCentral = new PanelTablaCentral();

        // Panel superior con campo de búsqueda y botón de devolución
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(30, 30, 30));

        campoBusqueda = panelTablaCentral.crearCampoTexto();
        topPanel.add(panelTablaCentral.crearLabelConEstilo("Buscar por ID de Venta:"), BorderLayout.WEST);
        topPanel.add(campoBusqueda, BorderLayout.CENTER);

        botonDevolver = panelTablaCentral.crearBotonConEstilo("Devolver");
        botonDevolver.addActionListener(e -> mostrarFormularioDevolucion());
        topPanel.add(botonDevolver, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Configurar el modelo de tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre del Cliente", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        panelTablaCentral.setModeloTabla(modeloTabla);

        // Configurar el TableRowSorter para la búsqueda
        sorter = new TableRowSorter<>(modeloTabla);
        panelTablaCentral.getTable().setRowSorter(sorter);

        // Listener para el campo de búsqueda
        campoBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarTabla();
            }
        });

        // Agregar el PanelTablaCentral al panel principal
        add(panelTablaCentral, BorderLayout.CENTER);

        actualizar();
    }

    // Método para filtrar la tabla según el texto ingresado en el campo de búsqueda
    private void filtrarTabla() {
        String texto = campoBusqueda.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // Búsqueda insensible a mayúsculas/minúsculas
        }
    }

    // Método para mostrar el formulario de devolución
    private void mostrarFormularioDevolucion() {
        int filaSeleccionada = panelTablaCentral.getTable().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta.");
            return;
        }

        // Obtener el ID de la venta seleccionada
        String idVenta = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        // Consultar los detalles de los productos de la venta seleccionada
        ArrayList<String[]> detallesVenta = Controlador.consultar("DetalleVenta", null, "IDPRODUCTO, CANTIDAD, PRECIOunitario", "IDVENTA = '" + idVenta + "'");

        // Crear el modelo de tabla para los detalles de los productos
        DefaultTableModel modeloDetalle = new DefaultTableModel(new String[]{"Producto", "Cantidad", "Precio", "Total"}, 0);
        for (String[] detalle : detallesVenta) {
            modeloDetalle.addRow(detalle);
        }



        // Crear la tabla de detalles
        JTable tablaDetalles = new JTable(modeloDetalle);

        // Crear un panel con la tabla de detalles
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);

        // Botón de devolución
        JButton botonDevolverProducto = new JButton("Devolver Producto");
        botonDevolverProducto.addActionListener(e -> devolverProducto(idVenta, tablaDetalles));

        // Crear el cuadro de diálogo para mostrar los detalles de la venta
        JDialog dialog = new JDialog((Frame) null, "Detalles de Devolución", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(botonDevolverProducto, BorderLayout.SOUTH);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // Método para devolver el producto, actualizando el stock
    private void devolverProducto(String idVenta, JTable tablaDetalles) {
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto para devolver.");
            return;
        }

        // Obtener los datos del producto seleccionado
        String idProducto = (String) tablaDetalles.getValueAt(filaSeleccionada, 0);
        int cantidadDevuelta = Integer.parseInt((String) tablaDetalles.getValueAt(filaSeleccionada, 1));
        
        // Consultar el stock actual del producto
        ArrayList<String[]> resultadoConsulta = Controlador.consultar("Productos", null, "STOCK", "IDPRODUCTO = '" + idProducto + "'");
        int stockActual = Integer.parseInt(resultadoConsulta.get(0)[0]);

        // Calcular el nuevo stock después de la devolución
        int nuevoStock = stockActual + cantidadDevuelta;
        // Actualizar el stock del producto en la base de datos (suponiendo que tienes el método para esto)
        Controlador.modificar("Productos", "IDPRODUCTO", idProducto, "=", "STOCK", String.valueOf(nuevoStock));
        Controlador.eliminarDB("DETALLEVENTA", "IDVENTA", "" + idVenta + "' AND IDPRODUCTO = '" + idProducto + "");
        // Eliminar el producto de la tabla de detalles de la venta
        DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
        modelo.removeRow(filaSeleccionada);

        // Consultar nuevamente todos los detalles de la venta para calcular el nuevo total
        ArrayList<String[]> detallesVentaActualizados = Controlador.consultar("DetalleVenta", null, "CANTIDAD, PRECIOunitario", "IDVENTA = '" + idVenta + "'");
        
        double nuevoTotal = 0.0;
        for (String[] detalle : detallesVentaActualizados) {
            int cantidad = Integer.parseInt(detalle[0]);
            double precioUnitario = Double.parseDouble(detalle[1]);
            nuevoTotal += cantidad * precioUnitario;
        }

        // Actualizar el total de la venta en la base de datos
        Controlador.modificar("Ventas", "IDVENTA", idVenta, "=", "TOTAL", String.valueOf(nuevoTotal));
        // Opcional: Actualizar la tabla principal
        actualizar();

        JOptionPane.showMessageDialog(this, "Producto devuelto correctamente.");
    }

    // Método de acceso para el campo de búsqueda y el modelo de la tabla
    public String getTextoBusqueda() {
        return campoBusqueda.getText();
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    @Override
    public void actualizar() {
        // Limpiar la tabla existente
        modeloTabla.setRowCount(0);

        // Obtener los datos actualizados (esto es solo un ejemplo, deberías obtener los datos reales)
        ArrayList<String[]> datosActualizados = Controlador.consultar("Ventas", null, "IDVENTA, CLIENTE, TOTAL", null);

        // Agregar los datos actualizados al modelo de la tabla
        for (String[] fila : datosActualizados) {
            modeloTabla.addRow(fila);
        }
    }
}
