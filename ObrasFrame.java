package Parte1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObrasFrame extends JInternalFrame implements ActionListener {
    private JButton btnAgregar, btnEliminar, btnModificar;
    private JList<String> listaObras;
    private DefaultListModel<String> modeloObras;

    public ObrasFrame() {
        setTitle("Gestión de Obras");
        setLayout(new FlowLayout());

        modeloObras = new DefaultListModel<>();
        listaObras = new JList<>(modeloObras);
        JScrollPane scrollPane = new JScrollPane(listaObras);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        add(scrollPane);
        btnAgregar = new JButton("Agregar Obra");
        btnEliminar = new JButton("Eliminar Obra");
        btnModificar = new JButton("Modificar Obra");
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
            String nuevaObra = mostrarDialogo("Agregar Obra");
            if (nuevaObra != null) {
                modeloObras.addElement(nuevaObra);
            }
        } else if (e.getSource() == btnModificar) {
            String obraSeleccionada = listaObras.getSelectedValue();
            if (obraSeleccionada != null) {
                String obraModificada = mostrarDialogo("Modificar Obra", obraSeleccionada);
                if (obraModificada != null) {
                    modeloObras.setElementAt(obraModificada, listaObras.getSelectedIndex());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una obra para modificar.");
            }
        } else if (e.getSource() == btnEliminar) {
            String obraSeleccionada = listaObras.getSelectedValue();
            if (obraSeleccionada != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la obra?");
                if (confirmacion == JOptionPane.YES_OPTION) {
                    modeloObras.removeElement(obraSeleccionada);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una obra para eliminar.");
            }
        }
    }

    private String mostrarDialogo(String titulo) {
        return mostrarDialogo(titulo, "");
    }

    private String mostrarDialogo(String titulo, String valorInicial) {
        return JOptionPane.showInputDialog(this, "Ingrese la obra:", titulo, JOptionPane.PLAIN_MESSAGE, null, null, valorInicial).toString();
    }
}
