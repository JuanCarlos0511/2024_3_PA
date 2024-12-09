package Vista.Suministrar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Vista.Patrones.ComponentesEstilizados;
import Vista.Patrones.Interfaces.InterfaceActualizar;

import java.awt.*;
import java.util.ArrayList;

public class ComprarPanel extends JPanel implements InterfaceActualizar {

    private ComponentesEstilizados panelTablaCentral;
    private JTextField proveedorField;
    private JTextField cantidadField;
    private JButton realizarPedidoButton;
    private DefaultTableModel modeloTabla;

    public ComprarPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        panelTablaCentral = new ComponentesEstilizados();
        initComponents();

        add(panelTablaCentral, BorderLayout.CENTER);
        actualizar();
    }

    private void initComponents() {
        // Crear el formulario con campos de texto
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(45, 45, 45));

        // Campos para proveedor y cantidad
        formPanel.add(ComponentesEstilizados.crearEtiqueta("Proveedor:"));
        proveedorField = ComponentesEstilizados.crearCampoTexto();
        formPanel.add(proveedorField);

        formPanel.add(ComponentesEstilizados.crearEtiqueta("Cantidad:"));
        cantidadField = ComponentesEstilizados.crearCampoTexto();
        formPanel.add(cantidadField);

        // Botón para realizar pedido
        realizarPedidoButton = ComponentesEstilizados.crearBoton("Realizar Pedido");
        realizarPedidoButton.addActionListener(e -> realizarPedido());

        formPanel.add(new JLabel()); // Espacio vacío
        formPanel.add(realizarPedidoButton);

        add(formPanel, BorderLayout.NORTH);

        // Configurar tabla para productos
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Producto", "Precio Unitario", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        panelTablaCentral.setModeloTabla(modeloTabla);
    }

    private void realizarPedido() {
        String proveedor = proveedorField.getText().trim();
        String cantidadTexto = cantidadField.getText().trim();

        // Validación de campos
        if (proveedor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo 'Proveedor' no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El campo 'Cantidad' debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar selección en la tabla
        int filaSeleccionada = panelTablaCentral.getTable().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 0); // ID del producto
        String nombreProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 1); // Nombre del producto

        // Actualización de la cantidad en la base de datos
        ArrayList<String[]> cantidadProductos = Controlador.consultar("PRODUCTOS", null, "STOCK", "IDPRODUCTO = '" + idProducto + "'");
        int cantidadActual = Integer.parseInt(cantidadProductos.get(0)[0]);
        Controlador.modificar("PRODUCTOS", "IDPRODUCTO", idProducto, "=", "STOCK", String.valueOf(cantidadActual + cantidad));

        JOptionPane.showMessageDialog(this, "Pedido realizado con éxito:\n" +
                "Producto: " + nombreProducto + "\n" +
                "Proveedor: " + proveedor + "\n" +
                "Cantidad: " + cantidad, "Éxito", JOptionPane.INFORMATION_MESSAGE);

        // Generar y registrar el pedido en la base de datos
        String idCompra = generarIdCompra();
        String fechaHoraActual = obtenerFechaHoraActual();
        Controlador.agregar("COMPRAS", new String[]{idCompra, fechaHoraActual, proveedor, idProducto, "EMP006", String.valueOf(cantidad)});
        String fechaHoy = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        Controlador.modificar("PRODUCTOS", "IDPRODUCTO", idProducto, "=", "ULTIMAIMPORTACION", fechaHoy);

    }

    private String generarIdCompra() {
        java.util.Date fechaActual = new java.util.Date();
        java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        return "CMP_" + formatoFecha.format(fechaActual);
    }

    private String obtenerFechaHoraActual() {
        java.util.Date fechaActual = new java.util.Date();
        java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatoFecha.format(fechaActual);
    }

    @Override
    public void actualizar() {
        modeloTabla.setRowCount(0); // Limpia todas las filas del modelo
        ArrayList<String[]> productos = Controlador.consultar("Productos", null, "IDPRODUCTO, NOMBRE, PRECIOUNITARIO, STOCK", null);

        for (String[] producto : productos) {
            modeloTabla.addRow(producto); // Añade los datos
        }
        System.out.println("Tabla actualizada con " + productos.size() + " productos.");
    }
}
