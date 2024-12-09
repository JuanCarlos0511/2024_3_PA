package Vista.Inventario;

import Vista.Patrones.ComponentesEstilizados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Controlador.Controlador;

public class ExportarInventarioPanel extends JPanel {

    private ComponentesEstilizados componentesEstilizados;
    private JButton exportarCSVButton;
    private JButton exportarXMLButton;
    private JButton exportarJSONButton;

    public ExportarInventarioPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        // Instanciar ComponentesEstilizados para gestionar la tabla y botones
        componentesEstilizados = new ComponentesEstilizados();

        // Configurar la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID de Producto", "Nombre", "Cantidad Disponible", "Precio Unitario"}, 0
        );

        componentesEstilizados.setModeloTabla(modeloTabla);
        // Panel superior para botones de exportación
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(45, 45, 45));

        exportarCSVButton = ComponentesEstilizados.crearBoton("Exportar como CSV");
        exportarXMLButton = ComponentesEstilizados.crearBoton("Exportar como XML");
        exportarJSONButton = ComponentesEstilizados.crearBoton("Exportar como JSON");

        topPanel.add(exportarCSVButton);
        topPanel.add(exportarXMLButton);
        topPanel.add(exportarJSONButton);

        add(topPanel, BorderLayout.NORTH);

        // Agregar la tabla al panel principal
        add(componentesEstilizados, BorderLayout.CENTER);

        // Llamar a actualizar para cargar los datos iniciales
        actualizar();

        // Agregar funcionalidad a los botones
        exportarCSVButton.addActionListener(e -> exportarDatos("csv"));
        exportarXMLButton.addActionListener(e -> exportarDatos("xml"));
        exportarJSONButton.addActionListener(e -> exportarDatos("json"));
    }

    public DefaultTableModel getModeloTabla() {
        return componentesEstilizados.getModeloTabla();
    }

    // Método para actualizar la tabla con datos desde la base de datos
    public void actualizar() {
        DefaultTableModel modeloTabla = componentesEstilizados.getModeloTabla();
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos

        // Consultar datos desde la base de datos
        ArrayList<String[]> datosInventario = Controlador.consultar(
            "Productos",
            null,
            "IDProducto, Nombre, Stock, PrecioUnitario",
            null
        );

        // Agregar los datos obtenidos al modelo de la tabla
        for (String[] fila : datosInventario) {
            modeloTabla.addRow(fila);
        }

        System.out.println("Tabla actualizada con " + datosInventario.size() + " registros.");
    }

    // Método para exportar los datos a un archivo
    private void exportarDatos(String formato) {
        try {
            // Generar nombre del archivo basado en fecha y hora
            String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "PROD_" + fechaHora + "." + formato;

            DefaultTableModel modeloTabla = componentesEstilizados.getModeloTabla();

            switch (formato.toLowerCase()) {
                case "csv":
                    exportarComoCSV(nombreArchivo);
                    break;
                case "xml":
                    exportarComoXML(nombreArchivo);
                    break;
                case "json":
                    exportarComoJSON(nombreArchivo);
                    break;
                default:
                    throw new IllegalArgumentException("Formato no soportado: " + formato);
            }

            JOptionPane.showMessageDialog(this, "Archivo exportado correctamente: " + nombreArchivo, "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al exportar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void exportarComoCSV(String nombreArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Consultar datos desde el controlador
            ArrayList<String[]> datosInventario = Controlador.consultar(
                "Productos",
                null,
                null,  // Esto traerá todos los campos del producto
                null
            );
    
            // Escribir encabezados
            if (!datosInventario.isEmpty()) {
                String[] encabezados = datosInventario.get(0);
                for (int i = 0; i < encabezados.length; i++) {
                    writer.write(encabezados[i] + (i == encabezados.length - 1 ? "\n" : ","));
                }
    
                // Escribir filas
                for (int i = 0; i < datosInventario.size(); i++) {
                    String[] fila = datosInventario.get(i);
                    for (int j = 0; j < fila.length; j++) {
                        writer.write((fila[j] != null ? fila[j] : "N/A") + (j == fila.length - 1 ? "\n" : ","));
                    }
                }
            }
        }
    }
    
    private void exportarComoXML(String nombreArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Consultar datos desde el controlador
            ArrayList<String[]> datosInventario = Controlador.consultar(
                "Productos",
                null,
                null,  // Esto traerá todos los campos del producto
                null
            );
    
            writer.write("<Inventario>\n");
    
            // Escribir los datos en formato XML
            for (String[] fila : datosInventario) {
                writer.write("  <Producto>\n");
                for (int j = 0; j < fila.length; j++) {
                    writer.write("    <" + "Campo" + j + ">" + // Cambiar "Campo" por el nombre del campo correspondiente si es necesario
                            (fila[j] != null ? fila[j] : "N/A") +
                            "</Campo" + j + ">\n");
                }
                writer.write("  </Producto>\n");
            }
    
            writer.write("</Inventario>");
        }
    }
    
    private void exportarComoJSON(String nombreArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Consultar datos desde el controlador
            ArrayList<String[]> datosInventario = Controlador.consultar(
                "Productos",
                null,
                null,  // Esto traerá todos los campos del producto
                null
            );
    
            writer.write("[\n");
    
            // Escribir los datos en formato JSON
            for (int i = 0; i < datosInventario.size(); i++) {
                writer.write("  {\n");
                String[] fila = datosInventario.get(i);
                for (int j = 0; j < fila.length; j++) {
                    writer.write("    \"" + "Campo" + j + "\": \"" + // Cambiar "Campo" por el nombre del campo correspondiente si es necesario
                            (fila[j] != null ? fila[j] : "N/A") + "\"" +
                            (j == fila.length - 1 ? "\n" : ",\n"));
                }
                writer.write("  }" + (i == datosInventario.size() - 1 ? "\n" : ",\n"));
            }
    
            writer.write("]");
        }
    }
    
}
