package Vista.Ventas;

import Vista.Patrones.ComponentesEstilizados;
import Controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VentanaPago extends JDialog {

    private JTextField campoNombreCliente;
    private JComboBox<String> comboMetodoPago;
    private JTextField campoNumeroTarjeta;
    private JTextField campoCVV;
    private JTextField campoVigencia;
    private JTextArea areaRecibo;
    private JButton botonConfirmarPago;
    private String montoTotal;

    public VentanaPago(Frame owner, String montoTotal, ArrayList<String[]> Productos) {
        super(owner, "Pago", true);
        this.montoTotal = montoTotal;

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(30, 30, 30));

        // Panel superior: Nombre del cliente
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(30, 30, 30));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelNombreCliente = ComponentesEstilizados.crearEtiqueta("Nombre del Cliente:");
        campoNombreCliente = ComponentesEstilizados.crearCampoTexto();

        JPanel panelNombre = new JPanel(new BorderLayout());
        panelNombre.setBackground(new Color(30, 30, 30));
        panelNombre.add(labelNombreCliente, BorderLayout.WEST);
        panelNombre.add(campoNombreCliente, BorderLayout.CENTER);

        panelSuperior.add(panelNombre, BorderLayout.NORTH);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 30, 30));

        // Panel izquierdo: Método de pago
        JPanel panelIzquierdo = new JPanel(new GridLayout(4, 2, 10, 10));
        panelIzquierdo.setBackground(new Color(30, 30, 30));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelMetodoPago = ComponentesEstilizados.crearEtiqueta("Método de Pago:");
        comboMetodoPago = ComponentesEstilizados.crearComboBox(new String[]{"Efectivo", "Tarjeta"});
        comboMetodoPago.addActionListener(e -> habilitarCamposTarjeta(comboMetodoPago.getSelectedItem().toString()));

        JLabel labelNumeroTarjeta = ComponentesEstilizados.crearEtiqueta("Número de Tarjeta:");
        campoNumeroTarjeta = ComponentesEstilizados.crearCampoTexto();
        campoNumeroTarjeta.setEnabled(false);

        JLabel labelCVV = ComponentesEstilizados.crearEtiqueta("CVV:");
        campoCVV = ComponentesEstilizados.crearCampoTexto();
        campoCVV.setEnabled(false);

        JLabel labelVigencia = ComponentesEstilizados.crearEtiqueta("Vigencia:");
        campoVigencia = ComponentesEstilizados.crearCampoTexto();
        campoVigencia.setEnabled(false);

        panelIzquierdo.add(labelMetodoPago);
        panelIzquierdo.add(comboMetodoPago);
        panelIzquierdo.add(labelNumeroTarjeta);
        panelIzquierdo.add(campoNumeroTarjeta);
        panelIzquierdo.add(labelCVV);
        panelIzquierdo.add(campoCVV);
        panelIzquierdo.add(labelVigencia);
        panelIzquierdo.add(campoVigencia);

        panelCentral.add(panelIzquierdo, BorderLayout.WEST);

        // Panel derecho: Vista del recibo
        areaRecibo = ComponentesEstilizados.crearAreaTexto();
        JScrollPane scrollRecibo = ComponentesEstilizados.crearScrollPane(areaRecibo);

        JPanel panelRecibo = new JPanel(new BorderLayout());
        panelRecibo.setBackground(new Color(30, 30, 30));
        panelRecibo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Vista del Recibo",
                0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE
        ));
        panelRecibo.add(scrollRecibo, BorderLayout.CENTER);

        panelCentral.add(panelRecibo, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // Botón Confirmar Pago
        botonConfirmarPago = ComponentesEstilizados.crearBoton("Pagar");
        botonConfirmarPago.addActionListener(e -> procesarPago(Productos));

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(30, 30, 30));
        panelBoton.add(botonConfirmarPago);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void habilitarCamposTarjeta(String metodoPago) {
        boolean esTarjeta = metodoPago.equals("Tarjeta");
        campoNumeroTarjeta.setEnabled(esTarjeta);
        campoCVV.setEnabled(esTarjeta);
        campoVigencia.setEnabled(esTarjeta);
    }

    private void procesarPago(ArrayList<String[]> Productos) {
        String nombreCliente = campoNombreCliente.getText().trim();
        String metodoPago = (String) comboMetodoPago.getSelectedItem();

        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("Tarjeta".equals(metodoPago)) {
            String numeroTarjeta = campoNumeroTarjeta.getText().trim();
            String cvv = campoCVV.getText().trim();
            String vigencia = campoVigencia.getText().trim();

            if (numeroTarjeta.isEmpty() || cvv.isEmpty() || vigencia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos de la tarjeta son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Generar recibo
        areaRecibo.setText(generarRecibo(nombreCliente, metodoPago));

        // Exportar a .txt
        exportarRecibo(nombreCliente, metodoPago);

        // Crear la venta en el controlador



        Controlador.registrarVenta(montoTotal, metodoPago, nombreCliente, Productos);

        JOptionPane.showMessageDialog(this, "Pago procesado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private String generarRecibo(String nombreCliente, String metodoPago) {
        StringBuilder recibo = new StringBuilder();
        recibo.append("Recibo de Pago\n");
        recibo.append("=====================\n");
        recibo.append("Cliente: ").append(nombreCliente).append("\n");
        recibo.append("Método de Pago: ").append(metodoPago).append("\n");
        recibo.append("Monto Total: $").append(montoTotal).append("\n");
        recibo.append("=====================\n");
        recibo.append("Gracias por su compra!\n");
        return recibo.toString();
    }

    private void exportarRecibo(String nombreCliente, String metodoPago) {
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String nombreArchivo = "Venta_" + fechaActual + ".txt";

        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write(generarRecibo(nombreCliente, metodoPago));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar el recibo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
