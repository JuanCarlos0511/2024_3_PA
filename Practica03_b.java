package Parte1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica03_b extends JInternalFrame implements ActionListener {

    // Componentes
    private JTextField tfID, tfInsumo;
    private JComboBox<String> comboCategoria;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEliminar, btnSalir;
    private JLabel lblImagen;

    public Practica03_b() {
        // Configuración de la ventana interna
        setTitle("Administración de Productos");
        setBounds(10, 10, 600, 400);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setLayout(null);

        // Configurar componentes
        // Etiquetas y campos de texto
        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(10, 10, 100, 25);
        getContentPane().add(lblID);

        tfID = new JTextField();
        tfID.setBounds(120, 10, 150, 25);
        getContentPane().add(tfID);

        JLabel lblInsumo = new JLabel("Insumo:");
        lblInsumo.setBounds(10, 40, 100, 25);
        getContentPane().add(lblInsumo);

        tfInsumo = new JTextField();
        tfInsumo.setBounds(120, 40, 150, 25);
        getContentPane().add(tfInsumo);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(10, 70, 100, 25);
        getContentPane().add(lblCategoria);

        comboCategoria = new JComboBox<>(new String[]{"Materiales", "Mano de Obra", "Maquinaria y Equipo"});
        comboCategoria.setBounds(120, 70, 150, 25);
        getContentPane().add(comboCategoria);

        // Botones
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(10, 110, 100, 25);
        btnAgregar.addActionListener(this);
        getContentPane().add(btnAgregar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(120, 110, 100, 25);
        btnEliminar.addActionListener(this);
        getContentPane().add(btnEliminar);

        btnSalir = new JButton("Salir");
        btnSalir.setBounds(230, 110, 100, 25);
        btnSalir.addActionListener(this);
        getContentPane().add(btnSalir);

        // Tabla de productos
        String[] columnas = {"ID", "Insumo", "Categoría"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBounds(10, 150, 350, 150);
        getContentPane().add(scrollTabla);

        // Imagen de productos
        lblImagen = new JLabel();
        lblImagen.setBounds(370, 150, 200, 150);
        lblImagen.setIcon(new ImageIcon("Imagenes/no-disponible.png"));  // Imagen por defecto
        getContentPane().add(lblImagen);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar) {
            agregarProducto();
        } else if (e.getSource() == btnEliminar) {
            eliminarProducto();
        } else if (e.getSource() == btnSalir) {
            dispose();  // Cerrar la ventana interna
        }
    }

    // Método para agregar productos a la tabla
    private void agregarProducto() {
        String id = tfID.getText();
        String insumo = tfInsumo.getText();
        String categoria = comboCategoria.getSelectedItem().toString();

        if (!id.isEmpty() && !insumo.isEmpty()) {
            modeloTabla.addRow(new Object[]{id, insumo, categoria});
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar productos de la tabla
    private void eliminarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada != -1) {
            modeloTabla.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar los campos de texto
    private void limpiarCampos() {
        tfID.setText("");
        tfInsumo.setText("");
        comboCategoria.setSelectedIndex(0);
    }
}
