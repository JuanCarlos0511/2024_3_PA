package Vista.Patrones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormularioEmergente extends JDialog {

    private final Map<String, JTextField> camposTexto = new LinkedHashMap<>();
    private JComboBox<String> categoriaComboBox;
    private JComboBox<String> nivelRelevanciaComboBox;
    private boolean confirmado = false; // Indica si el usuario confirmó el formulario
    private JPanel panelFormulario;

    // Constructor principal modificado
    public FormularioEmergente(Frame owner, String titulo, Map<String, String> etiquetas, String[] categorias) {
        super(owner, titulo, true); // Diálogo modal
        setSize(800, 300);
        setLocationRelativeTo(owner);

        panelFormulario = crearPanelFormulario(etiquetas, categorias); // Crear los campos del formulario
        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones(); // Crear el panel con los botones
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Crear el panel del formulario
    private JPanel crearPanelFormulario(Map<String, String> etiquetas, String[] categorias) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Definir el orden deseado
        String[] orden = {"nombre", "precioUnitario", "nivelRelevancia", "categoria"};
        int row = 0;
    
        for (String clave : orden) {
            String etiqueta = etiquetas.get(clave);
            if (etiqueta != null) {
                JLabel label = crearEtiqueta(etiqueta);
                gbc.gridx = 0;
                gbc.gridy = row;
                gbc.anchor = GridBagConstraints.WEST;
                panel.add(label, gbc);
    
                if (clave.equals("categoria") && categorias != null) {
                    categoriaComboBox = crearComboBox(categorias);
                    gbc.gridx = 1;
                    panel.add(categoriaComboBox, gbc);
                } else {
                    JTextField campoTexto = crearCampoTexto();
                    camposTexto.put(clave, campoTexto);
                    gbc.gridx = 1;
                    panel.add(campoTexto, gbc);
                }
                row++;
            }
        }
    
        return panel;
    }
    
    // Crear una etiqueta personalizada
    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    // Crear un campo de texto con estilos personalizados
    private JTextField crearCampoTexto() {
        JTextField campoTexto = new JTextField(15);
        campoTexto.setBackground(new Color(70, 70, 70));
        campoTexto.setForeground(Color.WHITE);
        campoTexto.setCaretColor(Color.WHITE); // Color del cursor
        campoTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        campoTexto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoTexto.setBackground(new Color(80, 80, 80));
                campoTexto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                campoTexto.setBackground(new Color(70, 70, 70));
                campoTexto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
        return campoTexto;
    }

    // Crear un combo box con estilos personalizados
    private JComboBox<String> crearComboBox(String[] opciones) {
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(150, 25));
        return comboBox;
    }

    // Crear el panel de botones (Aceptar y Cancelar)
    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(45, 45, 45));

        JButton botonOk = new JButton("Aceptar");
        botonOk.setBackground(new Color(60, 60, 60));
        botonOk.setForeground(Color.WHITE);
        botonOk.setFocusPainted(false);
        botonOk.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botonOk.addActionListener(this::confirmarFormulario);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBackground(new Color(60, 60, 60));
        botonCancelar.setForeground(Color.WHITE);
        botonCancelar.setFocusPainted(false);
        botonCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botonCancelar.addActionListener(e -> dispose());

        panelBotones.add(botonOk);
        panelBotones.add(botonCancelar);
        return panelBotones;
    }

    // Confirmar el formulario (validar campos)
    private void confirmarFormulario(ActionEvent e) {
        if (validarCampos()) {
            confirmado = true;
            dispose(); // Cerrar el formulario
        }
    }

    // Validar que los campos no estén vacíos
    private boolean validarCampos() {
        for (Map.Entry<String, JTextField> entrada : camposTexto.entrySet()) {
            if (entrada.getValue().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo '" + entrada.getKey() + "' no puede estar vacío.",
                        "Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    // Métodos de acceso y configuración
    public void setCampoTexto(String clave, String valor) {
        if ("categoria".equals(clave) && categoriaComboBox != null) {
            categoriaComboBox.setSelectedItem(valor);
        } else if ("nivelRelevancia".equals(clave) && nivelRelevanciaComboBox != null) {
            nivelRelevanciaComboBox.setSelectedItem(valor);
        } else if (camposTexto.containsKey(clave)) {
            JTextField campo = camposTexto.get(clave);
            if (campo != null) {
                campo.setText(valor);
            }
        }
    }

    public String getCampoTexto(String clave) {
        if ("categoria".equals(clave) && categoriaComboBox != null) {
            return (String) categoriaComboBox.getSelectedItem();
        } else if ("nivelRelevancia".equals(clave) && nivelRelevanciaComboBox != null) {
            return (String) nivelRelevanciaComboBox.getSelectedItem();
        } else if (camposTexto.containsKey(clave)) {
            JTextField campo = camposTexto.get(clave);
            return campo != null ? campo.getText() : null;
        }
        return null;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    // Crear y mostrar un formulario emergente
    public static FormularioEmergente crearFormulario(Frame owner, String titulo, Map<String, String> etiquetas, String[] categorias) {
        FormularioEmergente formulario = new FormularioEmergente(owner, titulo, etiquetas, categorias);
        formulario.setVisible(true);
        return formulario;
    }
}
