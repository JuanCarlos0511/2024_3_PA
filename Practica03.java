package Parte1;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Practica03 extends JFrame implements ActionListener {

    DefaultTableModel modeloInsumos;
    private JTextField tfID, tfInsumo;
    private JButton btnAgregar, btnEliminar, btnSalir;
    private JComboBox<String> comboCategoria;
    private JTable tablaProductos;
    private JLabel lblImagen;

    public Practica03() {
        setTitle("Administración de Productos");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(10, 10, 100, 25);
        add(lblID);
        tfID = new JTextField();
        tfID.setBounds(120, 10, 150, 25);
        add(tfID);
        JLabel lblInsumo = new JLabel("Insumo:");
        lblInsumo.setBounds(10, 40, 100, 25);
        add(lblInsumo);
        tfInsumo = new JTextField();
        tfInsumo.setBounds(120, 40, 150, 25);
        add(tfInsumo);
        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(10, 70, 100, 25);
        add(lblCategoria);
        comboCategoria = new JComboBox<>(new String[]{"Materiales", "Mano de Obra", "Maquinaria y Equipo"});
        comboCategoria.setBounds(120, 70, 150, 25);
        add(comboCategoria);
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(10, 110, 100, 25);
        add(btnAgregar);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(120, 110, 100, 25);
        add(btnEliminar);
        btnSalir = new JButton("Salir");
        btnSalir.setBounds(230, 110, 100, 25);
        add(btnSalir);
        String[] columnas = {"ID", "Insumo", "Categoría"};
        modeloInsumos = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloInsumos);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBounds(10, 150, 450, 200);  // Ajuste de posición y tamaño
        add(scrollPane);
        lblImagen = new JLabel();
        lblImagen.setBounds(470, 150, 200, 200);  // Ajuste de posición de la imagen
        lblImagen.setIcon(new ImageIcon("Imagenes/no-disponible.png"));  // Imagen por defecto si no se encuentra la imagen del insumo
        add(lblImagen);
        btnAgregar.addActionListener(this);
        btnEliminar.addActionListener(this);
        btnSalir.addActionListener(this);
        tablaProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarImagen();
                }
            }
        });
        modeloInsumos.addRow(new Object[]{"001", "Cemento", "Materiales"});
        modeloInsumos.addRow(new Object[]{"002", "Albañil", "Mano de Obra"});
        modeloInsumos.addRow(new Object[]{"003", "Arena", "Materiales"});
        modeloInsumos.addRow(new Object[]{"004", "Renovadora", "Maquinaria y Equipo"});
        modeloInsumos.addRow(new Object[]{"005", "Computadora", "Maquinaria y Equipo"});

        setVisible(true);
    }

    private void actualizarImagen() {
        int fila = tablaProductos.getSelectedRow();
        if (fila != -1) {
            String id = tablaProductos.getValueAt(fila, 0).toString(); 
            String rutaImagen = "Imagenes/" + id + ".jpeg";  
            File archivo = new File(rutaImagen);
            if (archivo.exists()) {
                lblImagen.setIcon(new ImageIcon(new ImageIcon(rutaImagen).getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), java.awt.Image.SCALE_DEFAULT)));
            } else {
                lblImagen.setIcon(new ImageIcon("Imagenes/no-disponible.png"));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar) {
            String id = tfID.getText();
            String insumo = tfInsumo.getText();
            String categoria = (String) comboCategoria.getSelectedItem();
            modeloInsumos.addRow(new Object[]{id, insumo, categoria});
        } else if (e.getSource() == btnEliminar) {
            int fila = tablaProductos.getSelectedRow();
            if (fila != -1) {
                modeloInsumos.removeRow(fila);
            }
        } else if (e.getSource() == btnSalir) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Practica03();
    }
}
