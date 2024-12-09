package Vista.Ventas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Vista.Patrones.PanelTablaCentral;
import Controlador.Controlador;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VentanaAgregarProducto extends JDialog {

    private final PanelTablaCentral panelTablaCentral;
    private final JTextField campoCantidad;
    private final JLabel labelCategoria;
    private final JLabel labelUltimaImportacion;
    private final JLabel labelCantidadStock;
    private final JButton botonAgregar;

    public VentanaAgregarProducto(Frame owner) {
        super(owner, "Agregar Producto", true);
        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(30, 30, 30));

        // Panel Superior
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 20, 20));
        panelSuperior.setBackground(new Color(30, 30, 30));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField campoID = crearCampoTexto();
        JTextField campoNombre = crearCampoTexto();

        panelSuperior.add(crearCampoConEtiqueta("ID:", campoID));
        panelSuperior.add(crearCampoConEtiqueta("Nombre:", campoNombre));
        add(panelSuperior, BorderLayout.NORTH);

        // Panel Central (Tabla)
        panelTablaCentral = new PanelTablaCentral();
        String[] columnas = {"ID", "Nombre", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición de celdas
            }
        };
        panelTablaCentral.setModeloTabla(modelo);
        cargarProductosEnTabla();
        add(panelTablaCentral, BorderLayout.CENTER);

        // Panel Derecho (Detalles)
        JPanel panelDerecho = new JPanel(new GridLayout(3, 2, 10, 10));
        panelDerecho.setBackground(new Color(30, 30, 30));
        panelDerecho.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Detalles",
                0, 0, new Font("Arial", Font.BOLD, 16), Color.LIGHT_GRAY));

        labelCategoria = panelTablaCentral.crearLabelConEstilo("");
        labelUltimaImportacion = panelTablaCentral.crearLabelConEstilo("");
        labelCantidadStock = panelTablaCentral.crearLabelConEstilo("");

        panelDerecho.add(panelTablaCentral.crearLabelConEstilo("Categoría:"));
        panelDerecho.add(labelCategoria);
        panelDerecho.add(panelTablaCentral.crearLabelConEstilo("Última Importación:"));
        panelDerecho.add(labelUltimaImportacion);
        panelDerecho.add(panelTablaCentral.crearLabelConEstilo("Cantidad Stock:"));
        panelDerecho.add(labelCantidadStock);

        add(panelDerecho, BorderLayout.EAST);

        // Panel Inferior (Cantidad y botón agregar)
        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 20, 20));
        panelInferior.setBackground(new Color(30, 30, 30));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campoCantidad = crearCampoTexto();
        panelInferior.add(crearCampoConEtiqueta("Cantidad:", campoCantidad));

        botonAgregar = crearBotonConEstilo("Agregar");
        botonAgregar.setBackground(Color.RED);
        botonAgregar.addActionListener(e -> agregarProductoSeleccionado());
        panelInferior.add(botonAgregar);

        add(panelInferior, BorderLayout.SOUTH);

        // Evento para mostrar detalles al seleccionar un producto
        panelTablaCentral.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = panelTablaCentral.getTable().getSelectedRow();
                if (selectedRow != -1) {
                    mostrarDetallesProducto(selectedRow);
                }
            }
        });
    }

    private void cargarProductosEnTabla() {
        ArrayList<String[]> productos = Controlador.consultar("Productos", null, null, null);
        DefaultTableModel modelo = panelTablaCentral.getModeloTabla();
        for (String[] producto : productos) {
            modelo.addRow(new Object[]{producto[0], producto[1], producto[3]});
        }
    }

    private void mostrarDetallesProducto(int selectedRow) {
        DefaultTableModel modelo = panelTablaCentral.getModeloTabla();
        String idProducto = (String) modelo.getValueAt(selectedRow, 0); // Aquí debes cargar la categoría
        ArrayList<String[]> parametros = Controlador.consultar("PRODUCTOS", null, "ULTIMAIMPORTACION, STOCK, CATEGORIA", "IDPRODUCTO = '" + idProducto + "'");
        
        String ultimaImportacion = "";
        String cantidadStock = "";
        String categoria = "";
        for (String[] strings : parametros) {
            ultimaImportacion = strings[0];
            cantidadStock = strings[1];
            categoria = strings[2];
        }
        labelCategoria.setText(categoria);
        labelUltimaImportacion.setText(ultimaImportacion);
        labelCantidadStock.setText(cantidadStock);
    }

    private void agregarProductoSeleccionado() {
        int selectedRow = panelTablaCentral.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto antes de agregarlo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String idProducto = (String) panelTablaCentral.getModeloTabla().getValueAt(selectedRow, 0); // Columna 0: ID
        String nombreProducto = (String) panelTablaCentral.getModeloTabla().getValueAt(selectedRow, 1); // Columna 1: Nombre
        String precioTexto = panelTablaCentral.getModeloTabla().getValueAt(selectedRow, 2).toString(); // Columna 2: Precio
    
        System.out.println("ID: " + idProducto);
        System.out.println("Nombre: " + nombreProducto);
        System.out.println("Precio: " + precioTexto);

        double precioProducto;
        try {
            precioProducto = Double.parseDouble(precioTexto); // Validar conversión
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio del producto no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
    
        String cantidadTexto = campoCantidad.getText();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
    
    
        double subtotal = precioProducto * cantidad;
    
        // Crear el arreglo con los datos del producto
        String[] productoSeleccionado = {
            idProducto,
            nombreProducto,
            String.format("%.2f", precioProducto),
            String.valueOf(cantidad),
            String.format("%.2f", subtotal)
        };
    
        // Enviar al controlador
        Controlador.agregarProductoACarrito(productoSeleccionado);
    
        JOptionPane.showMessageDialog(this, "Producto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // Cerrar ventana
    }
    
    

    private JPanel crearCampoConEtiqueta(String etiqueta, JTextField campo) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 30));
        JLabel label = new JLabel(etiqueta);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, BorderLayout.WEST);
        panel.add(campo, BorderLayout.CENTER);
        return panel;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campo;
    }

    private JButton crearBotonConEstilo(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(60, 60, 60));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    public void mostrarVentana() {
        setVisible(true);
    }
}
