package Vista.Productos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Controlador.Controlador;
import Vista.Patrones.FormularioEmergente;
import Vista.Patrones.PanelTablaCentral;
import Vista.Patrones.Interfaces.InterfaceActualizar;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModificarProductoPanel extends JPanel implements InterfaceActualizar {

    private PanelTablaCentral panelTablaCentral;
    private JButton agregarProductoButton;
    private JButton eliminarProductoButton;
    private JButton modificarProductoButton;
    private DefaultTableModel modeloTabla;

    public ModificarProductoPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        panelTablaCentral = new PanelTablaCentral();
        initUI();
        actualizar();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(30, 30, 30));

        agregarProductoButton = panelTablaCentral.crearBotonConEstilo("Agregar Producto");
        eliminarProductoButton = panelTablaCentral.crearBotonConEstilo("Eliminar Producto");
        modificarProductoButton = panelTablaCentral.crearBotonConEstilo("Modificar Producto");

        agregarProductoButton.addActionListener(e -> mostrarFormularioAgregarProducto());
        eliminarProductoButton.addActionListener(e -> eliminarProducto());
        modificarProductoButton.addActionListener(e -> mostrarFormularioEditarProducto());

        topPanel.add(agregarProductoButton);
        topPanel.add(eliminarProductoButton);
        topPanel.add(modificarProductoButton);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio Unitario", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        panelTablaCentral.setModeloTabla(modeloTabla);
        add(topPanel, BorderLayout.NORTH);
        add(panelTablaCentral, BorderLayout.CENTER);
    }

    private void mostrarFormularioAgregarProducto() {
        Map<String, String> etiquetas = Map.of(
            "nombre", "Nombre:",
            "precioUnitario", "Precio Unitario:",
            "categoria", "Categoría:",
            "nivelRelevancia", "Nivel de Relevancia:" // Nuevo campo
        );
    
        String[] categorias = {"Frutas", "Verduras", "Bebidas", "Libros", "Papelería", 
                               "Electrónica", "Dulces", "Carnes", "Higiene", "Ropa", "Calzado"};
        
        // Niveles de relevancia posibles
        
        FormularioEmergente formulario = FormularioEmergente.crearFormulario(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Agregar Producto",
                etiquetas,
                categorias // Categorías para el JComboBox
        );
    
        if (!formulario.isConfirmado()) return;
    
        String precioUnitarioTexto = formulario.getCampoTexto("precioUnitario");
        String nombre = formulario.getCampoTexto("nombre");
        String categoria = formulario.getCampoTexto("categoria");
        String nivelRelevancia = formulario.getCampoTexto("nivelRelevancia"); // Obtener nivel de relevancia
    
        if (esNombreProductoDuplicado(nombre)) {
            JOptionPane.showMessageDialog(this, "El producto con este nombre ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            double precioUnitario = Double.parseDouble(precioUnitarioTexto);
            String productoId = generarIdProducto(categoria);
            String[] nuevoProducto = {productoId, nombre, categoria, String.valueOf(precioUnitario), "0", "1969-12-31", nivelRelevancia}; // Incluir nivel de relevancia
            Controlador.agregarProducto(nuevoProducto);
            JOptionPane.showMessageDialog(this, "Producto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizar();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void mostrarFormularioEditarProducto() {
        int selectedRow = panelTablaCentral.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un producto para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String productoId = (String) modeloTabla.getValueAt(selectedRow, 0);
    
        Map<String, String> etiquetas = Map.of(
            "precioUnitario", "Nuevo Precio Unitario:"
        );
    
        // Crear el formulario emergente
        FormularioEmergente formulario = FormularioEmergente.crearFormulario(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Editar Producto",
                etiquetas,
                null
        );
    
    
        // Verificar si se confirmó el formulario
        if (!formulario.isConfirmado()) return;
    
        // Capturar el nuevo precio ingresado
        String nuevoPrecioTexto = formulario.getCampoTexto("precioUnitario");
    
        try {
            // Validar que el nuevo precio sea un número válido
            double nuevoPrecio = Double.parseDouble(nuevoPrecioTexto);
            System.out.println("Nuevo Precio: " + nuevoPrecioTexto);
    
            // Actualizar en la base de datos
            boolean resultadoPrecio = Controlador.modificar(
                "PRODUCTOS", "IDPRODUCTO", productoId, "=", "PRECIOUNITARIO", String.valueOf(nuevoPrecio)
            );
    
            if (resultadoPrecio) {
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizar(); // Refrescar la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio inválido. Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void eliminarProducto() {
        int selectedRow = panelTablaCentral.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un producto para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productoId = (String) modeloTabla.getValueAt(selectedRow, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            Controlador.eliminarDB("PRODUCTOS", "IDPRODUCTO", productoId);
            actualizar();
        }
    }

    private boolean esNombreProductoDuplicado(String nombre) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombreExistente = (String) modeloTabla.getValueAt(i, 1);
            if (nombreExistente.equalsIgnoreCase(nombre)) return true;
        }
        return false;
    }

    private String generarIdProducto(String categoria) {
        String siglas = obtenerSiglasCategoria(categoria);
        int cantidadProductos = contarProductosEnCategoria(siglas);
        return siglas + "-" + String.format("%03d", cantidadProductos + 1);
    }

    private String obtenerSiglasCategoria(String categoria) {
        return switch (categoria.toUpperCase()) {
            case "FRUTAS" -> "FRU";
            case "VERDURAS" -> "VER";
            case "BEBIDAS" -> "BEB";
            case "LIBROS" -> "LIB";
            case "PAPELERIA" -> "PAP";
            case "ELECTRONICA" -> "ELE";
            case "DULCES" -> "DUL";
            case "CARNES" -> "CRN";
            case "HIGIENE" -> "HIG";
            case "ROPA" -> "ROP";
            case "CALZADO" -> "CAL";
            default -> "UNK";
        };
    }

    private int contarProductosEnCategoria(String siglasCategoria) {
        int count = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String id = (String) modeloTabla.getValueAt(i, 0);
            if (id.startsWith(siglasCategoria)) count++;
        }
        return count;
    }

    @Override
    public void actualizar() {
        modeloTabla.setRowCount(0);
        ArrayList<String[]> productos = Controlador.consultar("PRODUCTOS", null, null, null);
        for (String[] producto : productos) {
            modeloTabla.addRow(new Object[]{
                producto[0],
                producto[1],
                String.format("$%.2f", Double.parseDouble(producto[3])),
                producto[2]
            });
        }
    }
}
