package Vista.Usuarios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ListaUsuariosPanel extends JPanel {

    private JTable usuariosTable;
    private JButton editarUsuarioButton;
    private JButton eliminarUsuarioButton;
    private JComboBox<String> filtroRolComboBox;
    private JTextField buscarUsuarioField;
    private DefaultTableModel modeloTabla;

    public ListaUsuariosPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(45, 45, 45));

        JLabel buscarLabel = new JLabel("Buscar Usuario:");
        buscarLabel.setForeground(Color.LIGHT_GRAY);
        topPanel.add(buscarLabel);

        buscarUsuarioField = crearCampoTexto();
        topPanel.add(buscarUsuarioField);

        JLabel rolLabel = new JLabel("Filtrar por Rol:");
        rolLabel.setForeground(Color.LIGHT_GRAY);
        topPanel.add(rolLabel);

        filtroRolComboBox = new JComboBox<>(new String[]{"Todos", "Administrador", "Vendedor", "Almacén"});
        filtroRolComboBox.setBackground(new Color(60, 60, 60));
        filtroRolComboBox.setForeground(Color.WHITE);
        topPanel.add(filtroRolComboBox);

        add(topPanel, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID de Usuario", "Nombre", "Rol", "Fecha de Registro", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        usuariosTable = new JTable(modeloTabla);
        usuariosTable.setBackground(new Color(45, 45, 45));
        usuariosTable.setForeground(Color.WHITE);
        usuariosTable.getTableHeader().setBackground(new Color(60, 60, 60));
        usuariosTable.getTableHeader().setForeground(Color.LIGHT_GRAY);
        usuariosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        editarUsuarioButton = crearBotonConEstilo("Editar");
        eliminarUsuarioButton = crearBotonConEstilo("Eliminar");

        buttonPanel.add(editarUsuarioButton);
        buttonPanel.add(eliminarUsuarioButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField(15);
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return campo;
    }

    private JButton crearBotonConEstilo(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(60, 60, 60));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    public void agregarEditarUsuarioListener(ActionListener listener) {
        editarUsuarioButton.addActionListener(listener);
    }

    public void agregarEliminarUsuarioListener(ActionListener listener) {
        eliminarUsuarioButton.addActionListener(listener);
    }

    public String getTextoBusquedaUsuario() {
        return buscarUsuarioField.getText();
    }

    public String getRolSeleccionado() {
        return filtroRolComboBox.getSelectedItem().toString();
    }

    public int getUsuarioSeleccionado() {
        return usuariosTable.getSelectedRow();
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
