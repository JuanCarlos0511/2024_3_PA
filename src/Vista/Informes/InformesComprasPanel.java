package Vista.Informes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Controlador.Controlador;

public class InformesComprasPanel extends JPanel {

    private JTable comprasTable;
    private JButton exportarInformeButton;
    private JComboBox<String> rangoFechasComboBox;
    private DefaultTableModel modeloTabla;

    public InformesComprasPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        // Panel superior con comboBox
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(45, 45, 45));

        JLabel labelRangoFechas = new JLabel("Seleccionar Rango de Fechas:");
        labelRangoFechas.setForeground(Color.LIGHT_GRAY);
        topPanel.add(labelRangoFechas);

        rangoFechasComboBox = new JComboBox<>(new String[]{"Hoy", "Última Semana", "Último Mes", "Personalizado"});
        rangoFechasComboBox.setBackground(new Color(60, 60, 60));
        rangoFechasComboBox.setForeground(Color.WHITE);
        topPanel.add(rangoFechasComboBox);

        add(topPanel, BorderLayout.NORTH);

        // Configurar la tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Fecha", "Proveedor", "Producto", "ID_Empleado", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        comprasTable = new JTable(modeloTabla);
        comprasTable.setBackground(new Color(45, 45, 45));
        comprasTable.setForeground(Color.WHITE);
        comprasTable.getTableHeader().setBackground(new Color(60, 60, 60));
        comprasTable.getTableHeader().setForeground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(comprasTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botón
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        exportarInformeButton = crearBotonConEstilo("Exportar Informe");

        buttonPanel.add(exportarInformeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar funcionalidad al comboBox
        rangoFechasComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTabla();
            }
        });

        // Establecer el valor predeterminado del comboBox a "Último Mes"
        rangoFechasComboBox.setSelectedItem("Último Mes");

        // Llamar a actualizarTabla para mostrar los datos iniciales
        actualizarTabla();
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

    public void agregarExportarInformeListener(ActionListener listener) {
        exportarInformeButton.addActionListener(listener);
    }

    public String getRangoFechasSeleccionado() {
        return rangoFechasComboBox.getSelectedItem().toString();
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Método para actualizar la tabla
    private void actualizarTabla() {
        String periodo = getRangoFechasSeleccionado();
        String filtroFecha = generarFiltroFecha(periodo);
    
        // Consultar datos desde la tabla compras
        ArrayList<String[]> datos = Controlador.consultar(
            "COMPRAS",
            "PRODUCTOS",
            "COMPRAS.IDCompra, COMPRAS.FECHA, COMPRAS.PROVEEDOR, PRODUCTOS.NOMBRE, COMPRAS.IDEMPLEADO, COMPRAS.CANTIDAD",
            "Fecha >= '" + filtroFecha + "' AND PRODUCTOS.IDPRODUCTO = COMPRAS.IDPRODUCTO"
        );
    
        // Limpiar y llenar la tabla con nuevos datos
        modeloTabla.setRowCount(0);
        for (String[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
    
    // Método para generar el filtro de fecha
    private String generarFiltroFecha(String periodo) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendario = Calendar.getInstance();

        switch (periodo) {
            case "Hoy":
                return formatoFecha.format(new Date());

            case "Última Semana":
                calendario.add(Calendar.DAY_OF_YEAR, -7);
                return formatoFecha.format(calendario.getTime());

            case "Último Mes":
                calendario.add(Calendar.MONTH, -1);
                return formatoFecha.format(calendario.getTime());

            case "Personalizado":
                // Abrir un diálogo para seleccionar fechas
                JOptionPane.showMessageDialog(this, "La funcionalidad de rango personalizado aún no está implementada.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return formatoFecha.format(new Date());

            default:
                return formatoFecha.format(new Date());
        }
    }
}
