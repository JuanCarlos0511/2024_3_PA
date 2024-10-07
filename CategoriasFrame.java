package Parte1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoriasFrame extends JInternalFrame implements ActionListener {
    private JButton btnAgregar, btnEliminar, btnModificar;
    private JList<String> listaCategorias;
    private DefaultListModel<String> modeloCategorias;

    public CategoriasFrame() {
        setTitle("Gestión de Categorías");
        setLayout(new FlowLayout());
        modeloCategorias = new DefaultListModel<>();
        listaCategorias = new JList<>(modeloCategorias);
        JScrollPane scrollPane = new JScrollPane(listaCategorias);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        add(scrollPane);
        btnAgregar = new JButton("Agregar Categoría");
        btnEliminar = new JButton("Eliminar Categoría");
        btnModificar = new JButton("Modificar Categoría");
        btnAgregar.addActionListener(this);
        btnEliminar.addActionListener(this);
        btnModificar.addActionListener(this);
        add(btnAgregar);
        add(btnEliminar);
        add(btnModificar);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar) {
            String nuevaCategoria = mostrarDialogo("Agregar Categoría");
            if (nuevaCategoria != null) {
                modeloCategorias.addElement(nuevaCategoria);
            }
        } else if (e.getSource() == btnModificar) {
            String categoriaSeleccionada = listaCategorias.getSelectedValue();
            if (categoriaSeleccionada != null) {
                String categoriaModificada = mostrarDialogo("Modificar Categoría", categoriaSeleccionada);
                if (categoriaModificada != null) {
                    modeloCategorias.setElementAt(categoriaModificada, listaCategorias.getSelectedIndex());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una categoría para modificar.");
            }
        } else if (e.getSource() == btnEliminar) {
            String categoriaSeleccionada = listaCategorias.getSelectedValue();
            if (categoriaSeleccionada != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la categoría?");
                if (confirmacion == JOptionPane.YES_OPTION) {
                    modeloCategorias.removeElement(categoriaSeleccionada);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una categoría para eliminar.");
            }
        }
    }

    private String mostrarDialogo(String titulo) {
        return mostrarDialogo(titulo, "");
    }

    private String mostrarDialogo(String titulo, String valorInicial) {
        return JOptionPane.showInputDialog(this, "Ingrese la categoría:", titulo, JOptionPane.PLAIN_MESSAGE, null, null, valorInicial).toString();
    }
}
