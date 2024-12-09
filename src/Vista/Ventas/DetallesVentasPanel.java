package Vista.Ventas;

import Vista.Patrones.ComponentesEstilizados;
import Vista.Patrones.Interfaces.InterfaceActualizar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import Controlador.Controlador;

public class DetallesVentasPanel extends JPanel implements InterfaceActualizar{

    private JLabel labelCliente;
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private ComponentesEstilizados componentesEstilizados;

    public DetallesVentasPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));
        componentesEstilizados = new ComponentesEstilizados();  // Instanciamos ComponentesEstilizados

        initComponents();
        cargarDatosVentas(); // Llenar la tabla al inicializar el panel

        // Agregar un listener para mostrar el recibo al seleccionar una fila
        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaVentas.getSelectedRow() != -1) {
                mostrarDetallesVentaSeleccionada();
            }
        });
    }

    private void initComponents() {
        // Usamos los métodos de ComponentesEstilizados para crear la tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Fecha", "Cliente", "Monto Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaVentas = componentesEstilizados.getTable();
        tablaVentas.setModel(modeloTabla);
        tablaVentas.setPreferredScrollableViewportSize(new Dimension(400, 150));

        // Establecer estilos de la tabla
        componentesEstilizados.configurarEstiloTabla();

        JScrollPane tableScrollPane = new JScrollPane(tablaVentas);
        tableScrollPane.setPreferredSize(new Dimension(400, 200));
        tableScrollPane.getViewport().setBackground(new Color(30, 30, 30));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.setBackground(new Color(30, 30, 30));
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);

        labelCliente = ComponentesEstilizados.crearEtiqueta("Seleccione una fila para ver el nombre del cliente");
        labelCliente.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(new Color(45, 45, 45));
        rightPanel.add(labelCliente, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void cargarDatosVentas() {
        // Consultar las ventas desde el controlador
        ArrayList<String[]> datosVentas = Controlador.consultar("Ventas", null, null, null);

        // Limpiar la tabla antes de agregar nuevos datos
        modeloTabla.setRowCount(0);

        // Agregar las filas a la tabla
        for (String[] fila : datosVentas) {
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDetallesVentaSeleccionada() {
        int selectedRow = tablaVentas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para ver los detalles.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener el ID de la venta seleccionada
        String idVenta = modeloTabla.getValueAt(selectedRow, 0).toString();

        // Consultar los detalles de la venta utilizando el ID de la venta
        ArrayList<String[]> detalleVentas = Controlador.consultar(
                "detalleVenta", 
                "PRODUCTOS", 
                "PRODUCTOS.NOMBRE, detalleVenta.cantidad, detalleVenta.subtotal", 
                "detalleVenta.idVenta = '" + idVenta + "' AND PRODUCTOS.IDPRODUCTO = detalleVenta.IDPRODUCTO"
        );

        // Construir el recibo de los detalles de la venta
        StringBuilder recibo = new StringBuilder();
        recibo.append("\n");
        recibo.append("*************** DETALLES DE LA VENTA ***************\n");
        recibo.append("                 ID Venta: ").append(idVenta).append("\n");
        recibo.append("----------------------------------------------------\n");
        recibo.append(String.format("%-30s\t%-15s\t%-15s\n", "Producto", "Cantidad", "Subtotal"));
        recibo.append("----------------------------------------------------\n");

        for (String[] detalle : detalleVentas) {
            recibo.append(String.format("%-30s\t%-15s\t%-15s\n", detalle[0], detalle[1], "$" + detalle[2]));
        }

        recibo.append("----------------------------------------------------\n");
        recibo.append("¡Gracias por su compra! Revise cuidadosamente sus datos.\n");
        recibo.append("Si tiene alguna duda, por favor comuníquese con atención al cliente.\n");
        recibo.append("****************************************************\n");

        // Mostrar el recibo en el labelCliente
        labelCliente.setText("<html>" + recibo.toString().replace("\n", "<br>") + "</html>");
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public String obtenerIdVentaSeleccionada() {
        int selectedRow = tablaVentas.getSelectedRow();
        return selectedRow != -1 ? modeloTabla.getValueAt(selectedRow, 0).toString() : null;
    }

    @Override

    public void actualizar() {
        cargarDatosVentas();
    }
}
