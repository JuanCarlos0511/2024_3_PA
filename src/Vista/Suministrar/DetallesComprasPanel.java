package Vista.Suministrar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Controlador.Controlador;
import Vista.Patrones.ComponentesEstilizados;
import Vista.Patrones.Interfaces.InterfaceActualizar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DetallesComprasPanel extends JPanel implements InterfaceActualizar {

    private ComponentesEstilizados componentesEstilizados;
    private JTextField filtroProveedorField;
    private JTextField filtroFechaField;
    private JButton buscarButton;
    private JButton exportarDetallesButton;

    public DetallesComprasPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));

        // Crear instancia de ComponentesEstilizados
        componentesEstilizados = new ComponentesEstilizados();

        // Configurar la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Fecha", "Proveedor", "Producto", "ID_Empleado", "Cantidad"}, 0
        );
        componentesEstilizados.setModeloTabla(modeloTabla);

        // Agregar barra superior con filtros
        JPanel filtroPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        filtroPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        filtroPanel.setBackground(new Color(45, 45, 45));

        filtroPanel.add(ComponentesEstilizados.crearEtiqueta("Filtro por Proveedor:"));
        filtroProveedorField = ComponentesEstilizados.crearCampoTexto();
        filtroPanel.add(filtroProveedorField);

        filtroPanel.add(ComponentesEstilizados.crearEtiqueta("Filtro por Fecha:"));
        filtroFechaField = ComponentesEstilizados.crearCampoTexto();
        filtroPanel.add(filtroFechaField);

        buscarButton = ComponentesEstilizados.crearBoton("Buscar");
        filtroPanel.add(buscarButton);

        add(filtroPanel, BorderLayout.NORTH);

        // Agregar tabla al centro
        add(componentesEstilizados, BorderLayout.CENTER);

        // Agregar botón de exportar en la parte inferior
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        exportarDetallesButton = ComponentesEstilizados.crearBoton("Exportar Detalles");
        buttonPanel.add(exportarDetallesButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar funcionalidad al botón buscar
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTabla();
            }
        });

        // Inicializar tabla con todos los datos
        actualizarTabla();
    }

    public void agregarExportarDetallesListener(ActionListener controlador) {
        exportarDetallesButton.addActionListener(controlador);
    }


    // Método para actualizar la tabla
    private void actualizarTabla() {
        String filtroProveedor = filtroProveedorField.getText().trim();
        String filtroFecha = filtroFechaField.getText().trim();

        // Construir la condición para la consulta
        StringBuilder condicion = new StringBuilder("COMPRAS.IDPRODUCTO = PRODUCTOS.IDPRODUCTO");
        if (!filtroProveedor.isEmpty()) {
            condicion.append(" AND COMPRAS.PROVEEDOR LIKE '%").append(filtroProveedor).append("%'");
        }
        if (!filtroFecha.isEmpty()) {
            condicion.append(" AND COMPRAS.FECHA >= '").append(filtroFecha).append("'");
        }

        // Consultar datos desde la tabla compras
        ArrayList<String[]> datos = Controlador.consultar(
            "COMPRAS",
            "PRODUCTOS",
            "DISTINCT COMPRAS.IDCompra, COMPRAS.FECHA, COMPRAS.PROVEEDOR, PRODUCTOS.NOMBRE, COMPRAS.IDEMPLEADO, COMPRAS.CANTIDAD",
            condicion.toString()
        );

        // Actualizar la tabla con los datos obtenidos
        DefaultTableModel modeloTabla = componentesEstilizados.getModeloTabla();
        modeloTabla.setRowCount(0); // Limpiar filas existentes
        for (String[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    @Override
    public void actualizar() {
        actualizarTabla();
    }
}
