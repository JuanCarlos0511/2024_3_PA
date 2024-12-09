package Vista.Inventario;

import Vista.Patrones.ComponentesEstilizados;
import Vista.Patrones.Interfaces.InterfaceActualizar;
import Controlador.Controlador;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ConfigurarAlertasPanel extends JPanel implements InterfaceActualizar {

    private JTable alertasTable;
    private JTextField campoBusqueda;
    private DefaultTableModel modeloTabla;

    public ConfigurarAlertasPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        // Usamos el componente de búsqueda y el panel superior
        JPanel topPanel = crearPanelSuperior();
        add(topPanel, BorderLayout.NORTH);

        // Configuración de la tabla reutilizando los estilos comunes
        modeloTabla = new DefaultTableModel(new String[]{"ID de Producto", "Nombre", "Cantidad Actual", "Nivel de Alerta"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Hacer editable solo la columna de "Nivel de Alerta"
            }
        };

        // Creamos la tabla con el estilo reutilizable
        alertasTable = crearTablaConEstilo(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(alertasTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        // Cargar productos al inicio
        actualizar();

        // Agregar un KeyListener para detectar cuando se presiona Enter
        alertasTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int fila = alertasTable.getSelectedRow();
                    if (fila >= 0 && alertasTable.isCellEditable(fila, 3)) {
                        String idProducto = (String) modeloTabla.getValueAt(fila, 0);
                        int nuevoNivelAlerta = getNivelAlerta(fila);

                        // Actualizar el nivel de alerta en la base de datos
                        boolean exito = Controlador.actualizarAlerta(idProducto, nuevoNivelAlerta);
                        if (!exito) {
                            JOptionPane.showMessageDialog(ConfigurarAlertasPanel.this, "Error al actualizar el nivel de alerta para el producto ID: " + idProducto, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Agregar un TableModelListener para escuchar cambios en las celdas
        modeloTabla.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Verificar si la celda editada es la columna "Nivel de Alerta"
                if (e.getColumn() == 3) {
                    int fila = e.getFirstRow();
                    String idProducto = (String) modeloTabla.getValueAt(fila, 0);
                    int nuevoNivelAlerta = getNivelAlerta(fila);

                    // Actualizar el nivel de alerta en la base de datos
                    boolean exito = Controlador.actualizarAlerta(idProducto, nuevoNivelAlerta);
                    if (!exito) {
                        JOptionPane.showMessageDialog(ConfigurarAlertasPanel.this, "Error al actualizar el nivel de alerta para el producto ID: " + idProducto, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    // Método para crear el panel superior con el campo de búsqueda
    private JPanel crearPanelSuperior() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(45, 45, 45));

        // Campo de búsqueda
        JLabel labelBusqueda = new JLabel("Buscar por nombre:");
        labelBusqueda.setForeground(Color.WHITE);
        topPanel.add(labelBusqueda);

        campoBusqueda = new JTextField(20);
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        campoBusqueda.setBackground(new Color(60, 60, 60));
        campoBusqueda.setForeground(Color.WHITE);
        campoBusqueda.setCaretColor(Color.WHITE);
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizar();
            }
        });
        topPanel.add(campoBusqueda);

        return topPanel;
    }

    // Método reutilizable para crear tablas con estilo
    private JTable crearTablaConEstilo(DefaultTableModel modelo) {
        JTable table = new JTable(modelo);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setBackground(new Color(45, 45, 45));
        table.setForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(60, 60, 60));
        table.getTableHeader().setForeground(Color.LIGHT_GRAY);

        // Deshabilitar la posibilidad de mover las columnas
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public int getNivelAlerta(int row) {
        try {
            return Integer.parseInt(modeloTabla.getValueAt(row, 3).toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void actualizar() {
        // Limpiar la tabla antes de llenarla de nuevo
        modeloTabla.setRowCount(0);

        // Filtrar la búsqueda por nombre (si está vacío, no se filtra)
        String busqueda = campoBusqueda.getText().toLowerCase();

        // Consultar los productos y sus niveles de alerta
        ArrayList<String[]> productos = Controlador.consultar("PRODUCTOS", null, "IDPRODUCTO, NOMBRE, STOCK, NIVELSTOCK", null);

        for (String[] producto : productos) {
            if (producto[1].toLowerCase().contains(busqueda)) {
                // Agregar productos a la tabla si coinciden con la búsqueda
                modeloTabla.addRow(new Object[]{
                    producto[0], // IDProducto
                    producto[1], // Nombre
                    producto[2], // Stock
                    producto[3] != null ? producto[3] : "0" // Nivel de Alerta (valor por defecto 0)
                });
            }
        }
    }

    // Guardar configuración de alertas
    public void guardarConfiguracion() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String idProducto = (String) modeloTabla.getValueAt(i, 0);
            int nuevoNivelAlerta = getNivelAlerta(i);

            // Actualizar el nivel de alerta en la base de datos
            boolean exito = Controlador.actualizarAlerta(idProducto, nuevoNivelAlerta);
            if (!exito) {
                JOptionPane.showMessageDialog(this, "Error al guardar configuración para el producto ID: " + idProducto, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(this, "Configuración guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
