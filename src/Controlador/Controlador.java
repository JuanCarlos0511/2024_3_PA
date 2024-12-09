package Controlador;

import Vista.*;
import Vista.Patrones.Interfaces.ControlActualizacion;
import Vista.Patrones.Interfaces.InterfaceActualizar;
import Vista.Ventas.NuevaVentaPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Modelo.BaseDatos;
import Modelo.ConexionBD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Controlador implements ActionListener {

    private VentanaPrincipal ventanaPrincipal;
    private static NuevaVentaPanel nuevaVentaPanel;
    private static BaseDatos baseDatos;
    private Connection conexion;
    private InterfaceActualizar interfaceActualizar;

    public Controlador() {
        try {
            conexion = ConexionBD.conectarMySQL();
            baseDatos = new BaseDatos(conexion);
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }

        ventanaPrincipal = new VentanaPrincipal(this);
        inicializarPaneles();
        ventanaPrincipal.setVisible(true);
    }

    

    public static ArrayList<String[]> consultar(String tabla1, String tabla2, String campos, String condicion) {
        if (tabla2 != null) {
            return baseDatos.consultar(tabla1, tabla2, campos, condicion);
        }
        return baseDatos.consultar(tabla1, campos, condicion);
    }



    public static boolean modificar(String tabla, String campocondicion, String valorCampo, String condicion, String campoActualizar, String valorActualizar) {
        try {
            baseDatos.modificar(tabla, campocondicion, valorCampo, condicion, campoActualizar, valorActualizar);
            ControlActualizacion.actualizarTodo();
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar datos: " + e.getMessage());
            return false;
        }
    }
    

    public static void eliminarDB(String tabla, String campo, String valor){
        baseDatos.eliminar(tabla, campo, valor);
        ControlActualizacion.actualizarTodo();
    }

    public static void agregarProducto(String[] valores) {
        if (valores == null || valores.length == 0) {
            JOptionPane.showMessageDialog(null, "Datos incompletos para agregar el producto.");
            return;
        }
        baseDatos.insertar("Productos", null, valores);
    }

    
    public static boolean actualizarAlerta(String idProducto, int nuevoNivelAlerta) {
        try {
            baseDatos.modificar("Productos", "idProducto", String.valueOf(idProducto), "=", "nivelStock", String.valueOf(nuevoNivelAlerta));
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar el nivel de alerta: " + e.getMessage());
            return false;
        }
    }


    private void inicializarPaneles() {
        nuevaVentaPanel = (NuevaVentaPanel) ventanaPrincipal.getPanel("Nueva Venta");
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Nueva Venta":
            case "Detalles de Ventas":
            case "Devoluciones":
            case "Comprar":
            case "Detalles de Compras":
            case "Exportar como Archivo":
            case "Configurar Alertas de Stock":
            case "Registro":
            case "Lista de Usuarios":
            case "Ventas":
            case "Compras":
            case "Inventario":
            case "Usuarios":
            case "Buscar":
            case "Gestionar Productos":
            case "Informe de Inventario":
            case "Informe de Ventas":
            case "Informe de Compras":
            case "Informe de Usuarios":
            case "Configuración de Alertas":
                ventanaPrincipal.mostrarPanel(command);
                break;
            default:
                JOptionPane.showMessageDialog(ventanaPrincipal, "Acción no reconocida: " + command);
                break;
        }
    }

    public static void agregar(String tabla, String[] valores) {
        if (tabla == null || tabla.isEmpty() || valores == null || valores.length == 0) {
            System.err.println("La tabla o los valores proporcionados no son válidos.");
            return;
        }
    
        // La ID del registro que queremos validar
        String id = valores[0];
    
        // Consultar si ya existe el registro en la tabla
        ArrayList<String[]> resultados = consultar(tabla, null, null,null);
        for (String[] registro : resultados) {
            if (registro[0].equals(id)) {
                System.err.println("El registro con ID " + id + " ya existe en la tabla " + tabla + ".");
                return;
            }
        }
    
        // Si no existe, proceder a insertar el registro
        try {
            baseDatos.insertar(tabla, null, valores);
            System.out.println("Registro agregado exitosamente a la tabla: " + tabla);
        } catch (Exception e) {
            System.err.println("Error al agregar el registro a la tabla " + tabla + ": " + e.getMessage());
        }
    }
    


    public static void registrarVenta(String montoTotal, String metodoPago, String nombreCliente, ArrayList<String[]> productos) {
        String idVenta = generarIDVenta(); // Implementa una lógica para generar IDs únicos
        String fechaActual = LocalDate.now().toString(); // Fecha en formato YYYY-MM-DD


        String[] valoresVenta = {idVenta, fechaActual, nombreCliente, String.valueOf(montoTotal), "EMP003"};
        agregar("VENTAS", valoresVenta);

        for (String[] valores : productos) {
            // Consultar el stock actual del producto
            String idProducto = valores[0];
            ArrayList<String[]> stockProducto = consultar("Productos", null, "stock", "idProducto = '" + idProducto + "'");
            if (stockProducto.isEmpty()) {
                System.err.println("No se encontró el producto con ID " + idProducto + " en el inventario.");
                return;
            }
            String stockActualTexto = stockProducto.get(0)[0];

            // Verificar si hay suficiente stock para la venta
            int cantidadVendida = Integer.parseInt(valores[1]);
            if (Integer.parseInt(stockActualTexto) < cantidadVendida) {
                System.err.println("Stock insuficiente para el producto con ID " + idProducto + ". Stock actual: " + Integer.parseInt(stockActualTexto) + ", Cantidad solicitada: " + cantidadVendida);
                return;
            }



            // Actualizar el stock del producto en el inventario
            int nuevoStock = Integer.parseInt(stockActualTexto) - cantidadVendida;
            modificar("Productos", "idProducto", idProducto, "=", "stock", String.valueOf(nuevoStock));
            String[] valoresDetalleVenta = {idVenta, valores[0], valores[1], valores[2], valores[3]};
            agregar("DETALLEVENTA", valoresDetalleVenta);
        }

        // Vaciar el carrito de compras después de registrar la venta
        DefaultTableModel modelo = nuevaVentaPanel.getTableModel();
        modelo.setRowCount(0);
        actualizarTotalesCarrito();

    }

    public static String generarIDVenta() {
        String prefijo = "VNT_";
        ArrayList<String[]> ventas = consultar("Ventas", null, null, null);
        int siguienteNumero = ventas.size() + 1;
        return prefijo + String.format("%04d", siguienteNumero);
    }


    



    public static void agregarProductoACarrito(String[] productoSeleccionado) {
        DefaultTableModel modelo = nuevaVentaPanel.getTableModel();
    
        // Asegurarse de que el producto seleccionado tiene todos los valores necesarios
        if (productoSeleccionado.length != 5) {
            JOptionPane.showMessageDialog(null, "El producto seleccionado no tiene suficientes datos.");
            return;
        }
    
        // Agregar la fila al modelo de tabla
        modelo.addRow(productoSeleccionado);
    
        // Calcular totales después de agregar el producto
        actualizarTotalesCarrito();
    }

    private static void actualizarTotalesCarrito() {
        DefaultTableModel modelo = nuevaVentaPanel.getTableModel();
        double montoActual = 0.0;
        double ivaRate = 0.16; // Porcentaje del IVA
        double iva = 0.0;
        double total = 0.0;
    
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double subtotal = Double.parseDouble(modelo.getValueAt(i, 4).toString());
            montoActual += subtotal;
        }
    
        iva = montoActual * ivaRate;
        total = montoActual + iva;
    
        nuevaVentaPanel.actualizarTotales(montoActual, iva, total);
    }
    
    
    

}
