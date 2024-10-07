package Parte1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsumosFrame extends JInternalFrame implements ActionListener {
    private JButton btnAgregar, btnEliminar, btnModificar;
    private JList<String> listaInsumos;
    private DefaultListModel<String> modeloInsumos;

    public InsumosFrame() {
        setTitle("Gestión de Insumos");
        setLayout(new FlowLayout());

        modeloInsumos = new DefaultListModel<>();
        listaInsumos = new JList<>(modeloInsumos);
        JScrollPane scrollPane = new JScrollPane(listaInsumos);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        add(scrollPane);
        btnAgregar = new JButton("Agregar Insumo");
        btnEliminar = new JButton("Eliminar Insumo");
        btnModificar = new JButton("Modificar Insumo");
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
            String nuevoInsumo = mostrarDialogo("Agregar Insumo");
            if (nuevoInsumo != null) {
                modeloInsumos.addElement(nuevoInsumo);
            }
        } else if (e.getSource() == btnModificar) {
            String insumoSeleccionado = listaInsumos.getSelectedValue();
            if (insumoSeleccionado != null) {
                String insumoModificado = mostrarDialogo("Modificar Insumo", insumoSeleccionado);
                if (insumoModificado != null) {
                    modeloInsumos.setElementAt(insumoModificado, listaInsumos.getSelectedIndex());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un insumo para modificar.");
            }
        } else if (e.getSource() == btnEliminar) {
            String insumoSeleccionado = listaInsumos.getSelectedValue();
            if (insumoSeleccionado != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el insumo?");
                if (confirmacion == JOptionPane.YES_OPTION) {
                    modeloInsumos.removeElement(insumoSeleccionado);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un insumo para eliminar.");
            }
        }
    }

    private String mostrarDialogo(String titulo) {
        return mostrarDialogo(titulo, "");
    }

    private String mostrarDialogo(String titulo, String valorInicial) {
        return JOptionPane.showInputDialog(this, "Ingrese el insumo:", titulo, JOptionPane.PLAIN_MESSAGE, null, null, valorInicial).toString();
    }
}
