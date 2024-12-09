package Vista.Informes;

import Vista.Patrones.ComponentesEstilizados;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Controlador.Controlador;

public class InformesVentasPanel extends JPanel {

    private JTable ventasTable;
    private JComboBox<String> rangoFechasComboBox;
    private DefaultTableModel modeloTabla;
    private ComponentesEstilizados componentes;

    public InformesVentasPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        // Inicializar la clase ComponentesEstilizados
        componentes = new ComponentesEstilizados();

        // Panel superior: Selección de rango de fechas
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(45, 45, 45));

        JLabel labelRangoFechas = ComponentesEstilizados.crearEtiqueta("Seleccionar Rango de Fechas:");
        topPanel.add(labelRangoFechas);

        // ComboBox para seleccionar rango de fechas
        rangoFechasComboBox = ComponentesEstilizados.crearComboBox(new String[]{"Hoy", "Última Semana", "Último Mes", "Histórico"});
        rangoFechasComboBox.addActionListener(e -> actualizarTabla()); // Actualiza la tabla cuando cambie el valor
        topPanel.add(rangoFechasComboBox);

        add(topPanel, BorderLayout.NORTH);

        // Configurar tabla de ventas
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Fecha", "Cliente", "Monto Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ventasTable = new JTable(modeloTabla);
        ventasTable.setBackground(new Color(45, 45, 45));
        ventasTable.setForeground(Color.WHITE);
        ventasTable.getTableHeader().setBackground(new Color(60, 60, 60));
        ventasTable.getTableHeader().setForeground(Color.LIGHT_GRAY);

        componentes.configurarEstiloTabla();  // Usamos el método de la clase ComponentesEstilizados para estilizar la tabla

        JScrollPane scrollPane = new JScrollPane(ventasTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        // Botón para exportar reporte
        JButton botonExportarReporte = ComponentesEstilizados.crearBoton("Exportar Reporte");
        botonExportarReporte.addActionListener(e -> exportarReportePDF());
        // Panel inferior (si es necesario para agregar más botones)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(botonExportarReporte);
        buttonPanel.setBackground(new Color(30, 30, 30));
        add(buttonPanel, BorderLayout.SOUTH);

        

        // Actualizar la tabla al iniciar
        actualizarTabla();
    }

    private void exportarReportePDF() {
        try {
            // Ruta al archivo .jrxml
            String rutaJrxml = "src/Vista/Informes/ReporteVentas.jrxml";
    
            // Compilar el archivo .jrxml en un archivo .jasper
            String rutaJasper = "src/Vista/Informes/ReporteVentas.jasper";
            JasperCompileManager.compileReportToFile(rutaJrxml, rutaJasper);
    
            // Cargar el reporte compilado (.jasper)
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaJasper);
    
            // Obtener datos de la tabla
            ArrayList<Map<String, String>> datos = obtenerDatosParaReporte();
    
            // Crear el JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            // Imprimir cada valor del dataSource
            for (Map<String, String> fila : datos) {
                for (Map.Entry<String, String> entrada : fila.entrySet()) {
                    System.out.println("Columna: " + entrada.getKey() + ", Valor: " + entrada.getValue());
                }
            }
            // Parámetros para el reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("titulo", "Reporte de Ventas");
            parametros.put("REPORT_DATE", new Date());
            
            // Rellenar el reporte
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, dataSource);
    
            // Exportar a PDF
            String archivoSalida = "Reporte_Ventas_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            JasperExportManager.exportReportToPdfFile(print, archivoSalida);
    
            JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente a " + archivoSalida, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private ArrayList<Map<String, String>> obtenerDatosParaReporte() {
        ArrayList<Map<String, String>> listaDatos = new ArrayList<>();
    
        DefaultTableModel modelo = (DefaultTableModel) ventasTable.getModel();
        int columnas = modelo.getColumnCount();
        int filas = modelo.getRowCount();
    
        // Iterar sobre las filas y columnas de la tabla
        for (int i = 0; i < filas; i++) {
            Map<String, String> fila = new HashMap<>();
            for (int j = 0; j < columnas; j++) {
                String columna = modelo.getColumnName(j);
                String valor = String.valueOf(modelo.getValueAt(i, j));
                fila.put(columna, valor);
                System.out.println("Columna: " + columna + ", Valor: " + valor);
            }
            listaDatos.add(fila);
        }
    
        return listaDatos;
    }
    

    private void actualizarTabla() {
        // Obtener el rango de fechas seleccionado
        String periodo = getRangoFechasSeleccionado();
        String filtroFecha = generarFiltroFecha(periodo);

        // Si "Histórico" es seleccionado, no se pasa filtro de fecha
        String filtroFechaSQL = filtroFecha != null ? "VENTAS.FECHA >= '" + filtroFecha + "'" : null;

        // Consultar datos de ventas desde el controlador
        ArrayList<String[]> datos = Controlador.consultar(
                "VENTAS",
                null,
                null,
                filtroFechaSQL // Se pasa el filtro generado o null para histórico
        );

        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);

        // Rellenar la tabla con los datos obtenidos
        for (String[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

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
            case "Histórico":
                return null;
            default:
                return formatoFecha.format(new Date());
        }
    }

    public String getRangoFechasSeleccionado() {
        return rangoFechasComboBox.getSelectedItem().toString();
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
