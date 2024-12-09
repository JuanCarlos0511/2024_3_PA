package Vista.Ventas;

import Vista.Patrones.PanelTablaCentral;
import Vista.Patrones.Interfaces.InterfaceActualizar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NuevaVentaPanel extends JPanel {

    private PanelTablaCentral panelTablaCentral;
    private JLabel labelMontoActual;
    private JLabel labelIVA;
    private JLabel labelTotal;
    private JButton botonCatalogo;
    private JButton botonPagar;
    private JButton botonAgregarProducto;
    private JComboBox<String> comboMetodoPago;
    private JTextField campoNumeroTarjeta;
    private JTextField campoFechaExpiracion;
    private JTextField campoCVV;

    public NuevaVentaPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        panelTablaCentral = new PanelTablaCentral();
        configurarPanelTabla();
        add(panelTablaCentral, BorderLayout.CENTER);

        add(crearPanelDerecho(), BorderLayout.EAST);
    }
    private void configurarPanelTabla() {
        // Configurar columnas para la tabla de productos
        String[] columnNames = {"ID", "Nombre", "Precio Unitario", "Cantidad", "Subtotal"};
        panelTablaCentral.setModeloTabla(new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo permitir editar la columna de cantidad
            }
        });
    
        // Listener para detectar cambios en las celdas
        panelTablaCentral.getModeloTabla().addTableModelListener(e -> {
            int fila = e.getFirstRow();
            int columna = e.getColumn();
            
            // Verificar que el cambio ocurrió en la columna "Cantidad"
            if (columna == 3) {
                DefaultTableModel model = (DefaultTableModel) e.getSource();
                Object cantidadObj = model.getValueAt(fila, columna);
                Object precioObj = model.getValueAt(fila, 2);
    
                try {
                    int cantidad = Integer.parseInt(cantidadObj.toString());
                    double precio = Double.parseDouble(precioObj.toString());
    
                    // Calcular el nuevo subtotal y actualizar la celda correspondiente
                    double subtotal = cantidad * precio;
                    model.setValueAt(String.format("%.2f", subtotal), fila, 4);
    
                    actualizarTotales();
                    // Actualizar los totales generales
                } catch (NumberFormatException ex) {
                    System.err.println("Error al convertir los valores de cantidad o precio: " + ex.getMessage());
                }
            }
        });
    
        // Botón para agregar productos al carrito
        botonAgregarProducto = panelTablaCentral.crearBotonConEstilo("Agregar Producto");
        botonAgregarProducto.addActionListener(e -> {
            VentanaAgregarProducto ventana = new VentanaAgregarProducto((Frame) SwingUtilities.getWindowAncestor(this));
            ventana.mostrarVentana();
        });
        panelTablaCentral.getPanelBotones().add(botonAgregarProducto);
    }
    

    private void actualizarTotales() {
        DefaultTableModel model = panelTablaCentral.getModeloTabla();
        double montoActual = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object valorCelda = model.getValueAt(i, 4); // Columna de subtotal
            if (valorCelda instanceof Double) {
                montoActual += (Double) valorCelda;
            } else if (valorCelda instanceof String) {
                try {
                    montoActual += Double.parseDouble((String) valorCelda);
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir el valor de la celda a Double: " + valorCelda);
                }
            } else {
                System.err.println("Tipo de dato inesperado en la celda: " + valorCelda);
            }
        }

        double iva = montoActual * 0.16; // Suponiendo un IVA del 16%
        double total = montoActual + iva;

        actualizarTotales(montoActual, iva, total);
    }

    private JPanel crearPanelDerecho() {
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setPreferredSize(new Dimension(300, 700));
        panelDerecho.setBackground(new Color(30, 30, 30));

        JPanel panelTotales = new JPanel();
        panelTotales.setLayout(new BoxLayout(panelTotales, BoxLayout.Y_AXIS));
        panelTotales.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelTotales.setBackground(new Color(30, 30, 30));

        labelMontoActual = panelTablaCentral.crearLabelConEstilo("Monto Actual: $0.00");
        labelIVA = panelTablaCentral.crearLabelConEstilo("IVA: $0.00");
        labelTotal = panelTablaCentral.crearLabelConEstilo("Total: $0.00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 20));

        panelTotales.add(labelMontoActual);
        panelTotales.add(Box.createVerticalStrut(10));
        panelTotales.add(labelIVA);
        panelTotales.add(Box.createVerticalStrut(10));
        panelTotales.add(labelTotal);

        botonPagar = panelTablaCentral.crearBotonConEstilo("Pagar");
        panelTotales.add(Box.createVerticalStrut(20));
        panelTotales.add(botonPagar);

        
        

        botonPagar.addActionListener(e -> {
            VentanaPago ventanaPago = new VentanaPago((Frame) SwingUtilities.getWindowAncestor(this), calcularMontoTotal(), obtenerProductosDeLaTabla());
            ventanaPago.setVisible(true);
        });
        

        panelDerecho.add(panelTotales, BorderLayout.SOUTH);

        return panelDerecho;
    }

    public ArrayList<String[]> obtenerProductosDeLaTabla() {
        ArrayList<String[]> productos = new ArrayList<>();
        DefaultTableModel model = panelTablaCentral.getModeloTabla();
        for (int i = 0; i < model.getRowCount(); i++) {
            String[] producto = new String[4];
            producto[0] = model.getValueAt(i, 0).toString(); // ID
            producto[1] = model.getValueAt(i, 3).toString(); // Cantidad
            producto[2] = model.getValueAt(i, 2).toString(); // Precio Unitario
            producto[3] = model.getValueAt(i, 4).toString(); // Subtotal
            productos.add(producto);
        }
        return productos;
    }


    private String calcularMontoTotal() {
        DefaultTableModel model = panelTablaCentral.getModeloTabla();
        double montoTotal = 0.0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            Object valorCelda = model.getValueAt(i, 4); // Columna de subtotal
    
            // Verificar el tipo de dato antes de la conversión
            if (valorCelda instanceof Double) {
                montoTotal += (Double) valorCelda;
            } else if (valorCelda instanceof String) {
                try {
                    montoTotal += Double.parseDouble((String) valorCelda);
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir el valor de la celda a Double: " + valorCelda);
                }
            } else {
                System.err.println("Tipo de dato inesperado en la celda: " + valorCelda);
            }
        }
    
        return String.format("%.2f", montoTotal); // Devolver con formato de dos decimales
    }
    

    
    public DefaultTableModel getTableModel() {
        return panelTablaCentral.getModeloTabla();
    }

    public void actualizarTotales(double montoActual, double iva, double total) {
        labelMontoActual.setText("Monto Actual: $" + String.format("%.2f", montoActual));
        labelIVA.setText("IVA: $" + String.format("%.2f", iva));
        labelTotal.setText("Total: $" + String.format("%.2f", total));
    }

    public JButton getBotonPagar() {
        return botonPagar;
    }

    
}
