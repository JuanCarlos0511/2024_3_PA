package Vista.Usuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegistroUsuariosPanel extends JPanel {

    private JTextField nombreField;
    private JTextField correoField;
    private JComboBox<String> rolComboBox;
    private JTextField telefonoField;
    private JPasswordField passwordField;
    private JButton registrarButton;

    public RegistroUsuariosPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(45, 45, 45));

        formPanel.add(crearLabel("Nombre:"));
        nombreField = crearCampoTexto();
        formPanel.add(nombreField);

        formPanel.add(crearLabel("Correo Electrónico:"));
        correoField = crearCampoTexto();
        formPanel.add(correoField);

        formPanel.add(crearLabel("Rol:"));
        rolComboBox = new JComboBox<>(new String[]{"Administrador", "Vendedor", "Almacén"});
        rolComboBox.setBackground(new Color(60, 60, 60));
        rolComboBox.setForeground(Color.WHITE);
        formPanel.add(rolComboBox);

        formPanel.add(crearLabel("Teléfono:"));
        telefonoField = crearCampoTexto();
        formPanel.add(telefonoField);

        formPanel.add(crearLabel("Contraseña:"));
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(60, 60, 60));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(passwordField);

        registrarButton = crearBotonConEstilo("Registrar Usuario");
        formPanel.add(new JLabel());
        formPanel.add(registrarButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.LIGHT_GRAY);
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
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

    public void agregarRegistrarListener(ActionListener listener) {
        registrarButton.addActionListener(listener);
    }

    public String getNombre() {
        return nombreField.getText();
    }

    public String getCorreo() {
        return correoField.getText();
    }

    public String getRol() {
        return (String) rolComboBox.getSelectedItem();
    }

    public String getTelefono() {
        return telefonoField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void limpiarCampos() {
        nombreField.setText("");
        correoField.setText("");
        telefonoField.setText("");
        passwordField.setText("");
        rolComboBox.setSelectedIndex(0);
    }
}
