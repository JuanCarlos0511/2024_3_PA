package Vista.Informes;

import Vista.Patrones.ComponentesEstilizados;
import Controlador.Controlador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InformesUsuariosPanel extends JPanel {

    private JTable usuariosTable;
    private JButton exportarInformeButton;
    private JComboBox<String> periodoComboBox;  // Cambiar campo de texto por JComboBox
    private DefaultTableModel modeloTabla;
    private ComponentesEstilizados componentes;

    public InformesUsuariosPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        // Inicializar la clase ComponentesEstilizados
        componentes = new ComponentesEstilizados();

        // Panel superior: Campo para seleccionar el periodo
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(45, 45, 45));

        JLabel labelPeriodo = ComponentesEstilizados.crearEtiqueta("Seleccionar Rango de Fechas:");
        periodoComboBox = ComponentesEstilizados.crearComboBox(new String[]{"Hoy", "Última Semana", "Último Mes", "Histórico"});
        periodoComboBox.addActionListener(e -> actualizarTabla()); // Actualiza la tabla cuando cambie el valor

        topPanel.add(labelPeriodo);

        // ComboBox para seleccionar periodo
        topPanel.add(periodoComboBox);

        add(topPanel, BorderLayout.NORTH);

        // Configurar tabla de usuarios
        modeloTabla = new DefaultTableModel(new String[]{"ID de Usuario", "Nombre", "Rol", "Estatus", "Inicio Contrato", "Fin Contrato"}, 0) {
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

        componentes.configurarEstiloTabla();  // Usamos el método de la clase ComponentesEstilizados para estilizar la tabla

        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));

        // Botón de exportar informe
        exportarInformeButton = ComponentesEstilizados.crearBoton("Exportar Informe");
        buttonPanel.add(exportarInformeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actualizar la tabla al iniciar
        actualizarTabla();  // Inicializamos con el periodo seleccionado por defecto
    }

    // Método para actualizar la tabla
    private void actualizarTabla() {
        // Obtener el periodo seleccionado
        String periodoSeleccionado = getPeriodoSeleccionado();
        String filtroFecha = generarFiltroFecha(periodoSeleccionado);

        // Si "Histórico" es seleccionado, no se pasa filtro de fecha
        String filtroFechaSQL = filtroFecha != null ? "Empleados.FinCONTRATO >= '" + filtroFecha + "'" : null;

        // Consultar datos de usuarios desde el controlador con el filtro de fecha
        ArrayList<String[]> datos = Controlador.consultar(
                "Empleados",
                null,
                "IDEmpleado, Nombre, Rol, Estatus, InicioCONTRATO, FinCONTRATO",
                filtroFechaSQL // Se pasa el filtro generado o null para histórico
        );

        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);

        // Rellenar la tabla con los datos obtenidos
        for (String[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    // Genera el filtro de fecha según el periodo seleccionado
    private String generarFiltroFecha(String periodo) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendario = Calendar.getInstance();

        switch (periodo) {
            case "Hoy":
                return formatoFecha.format(new Date());  // Fecha de hoy
            case "Última Semana":
                calendario.add(Calendar.DAY_OF_YEAR, -7);  // Retroceder 7 días
                return formatoFecha.format(calendario.getTime());
            case "Último Mes":
                calendario.add(Calendar.MONTH, -1);  // Retroceder 1 mes
                return formatoFecha.format(calendario.getTime());
            case "Histórico":
                return null;  // No aplica filtro de fecha
            default:
                return formatoFecha.format(new Date());  // Por defecto, hoy
        }
    }


    public String getPeriodoSeleccionado() {
        return periodoComboBox.getSelectedItem().toString();
    }



    public void agregarExportarInformeListener(ActionListener listener) {
        exportarInformeButton.addActionListener(listener);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
