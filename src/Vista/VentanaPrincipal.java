package Vista;

import javax.swing.*;

import Controlador.Controlador;
import Modelo.ConexionBD;
import Vista.Informes.InformesComprasPanel;
import Vista.Informes.InformesInventarioPanel;
import Vista.Informes.InformesUsuariosPanel;
import Vista.Informes.InformesVentasPanel;
import Vista.Inventario.ConfigurarAlertasPanel;
import Vista.Inventario.ExportarInventarioPanel;
import Vista.Patrones.Interfaces.ControlActualizacion;
import Vista.Patrones.Interfaces.InterfaceActualizar;
import Vista.Productos.BuscarPanel;
import Vista.Productos.ModificarProductoPanel;
import Vista.Suministrar.ComprarPanel;
import Vista.Suministrar.DetallesComprasPanel;
import Vista.Usuarios.ListaUsuariosPanel;
import Vista.Usuarios.RegistroUsuariosPanel;
import Vista.Ventas.DetallesVentasPanel;
import Vista.Ventas.DevolucionesPanel;
import Vista.Ventas.NuevaVentaPanel;
import Vista.Ventas.VentanaAgregarProducto;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class VentanaPrincipal extends JFrame {

    private JPanel panelCentral;
    private CardLayout cardLayout;
    private static Map<String, JPanel> paneles;

    public VentanaPrincipal(ActionListener controlador) {
        // Configuración de colores oscuros para la barra de menú
        UIManager.put("MenuBar.background", new Color(45, 45, 45));
        UIManager.put("Menu.foreground", Color.WHITE);
        UIManager.put("Menu.background", new Color(45, 45, 45));
        UIManager.put("MenuItem.background", new Color(60, 60, 60));
        UIManager.put("MenuItem.foreground", Color.LIGHT_GRAY);


        setTitle("Punto de Venta - Estilo Oscuro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicializar paneles y menú
        paneles = new HashMap<>();
        crearMenu(controlador);
        inicializarPaneles();

        // Configuración del panel central con fondo oscuro
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(new Color(30, 30, 30));
        add(panelCentral, BorderLayout.CENTER);

        // Agregar todos los paneles al panel central
        for (Map.Entry<String, JPanel> entry : paneles.entrySet()) {
            panelCentral.add(entry.getValue(), entry.getKey());
        }

        mostrarPanel("Nueva Venta");
    }

    private void crearMenu(ActionListener controlador) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(45, 45, 45));
        menuBar.setForeground(Color.WHITE);

        // Crear menús y agregar items
        JMenu menuVentas = crearMenu("Ventas", controlador, "Nueva Venta", "Detalles de Ventas", "Devoluciones");
        JMenu menuSuministrar = crearMenu("Suministrar", controlador, "Comprar", "Detalles de Compras");
        JMenu menuInventario = crearMenu("Inventario", controlador, "Exportar como Archivo", "Configurar Alertas de Stock");
        JMenu menuUsuarios = crearMenu("Usuarios", controlador, "Registro", "Lista de Usuarios");
        JMenu menuInformes = crearMenu("Informes", controlador, "Ventas", "Compras", "Inventario", "Usuarios");
        JMenu menuProductos = crearMenu("Productos", controlador, "Buscar", "Gestionar Productos");

        menuBar.add(menuVentas);
        menuBar.add(menuProductos);
        menuBar.add(menuSuministrar);
        menuBar.add(menuInventario);
        menuBar.add(menuUsuarios);
        menuBar.add(menuInformes);

        setJMenuBar(menuBar);
    }


    private JMenu crearMenu(String nombre, ActionListener controlador, String... items) {
        JMenu menu = new JMenu(nombre);
        menu.setForeground(Color.WHITE);

        for (String item : items) {
            JMenuItem menuItem = new JMenuItem(item);
            menuItem.setBackground(new Color(60, 60, 60));
            menuItem.setForeground(Color.LIGHT_GRAY);
            menuItem.setActionCommand(item);
            menuItem.addActionListener(controlador);
            menu.add(menuItem);
        }

        return menu;
    }

    private void inicializarPaneles() {
        agregarPanel("Nueva Venta", new NuevaVentaPanel());
        agregarPanel("Detalles de Ventas", new DetallesVentasPanel());
        agregarPanel("Devoluciones", new DevolucionesPanel());
        agregarPanel("Comprar", new ComprarPanel());
        agregarPanel("Detalles de Compras", new DetallesComprasPanel());
        agregarPanel("Exportar como Archivo", new ExportarInventarioPanel());
        agregarPanel("Configurar Alertas de Stock", new ConfigurarAlertasPanel());
        agregarPanel("Registro", new RegistroUsuariosPanel());
        agregarPanel("Lista de Usuarios", new ListaUsuariosPanel());
        agregarPanel("Ventas", new InformesVentasPanel());
        agregarPanel("Compras", new InformesComprasPanel());
        agregarPanel("Inventario", new InformesInventarioPanel());
        agregarPanel("Usuarios", new InformesUsuariosPanel());
        agregarPanel("Buscar", new BuscarPanel());
        agregarPanel("Gestionar Productos", new ModificarProductoPanel());
        agregarPanel("Informe de Inventario", new InformesInventarioPanel());
        agregarPanel("Informe de Ventas", new InformesVentasPanel());
        agregarPanel("Informe de Compras", new InformesComprasPanel());
        agregarPanel("Informe de Usuarios", new InformesUsuariosPanel());
        agregarPanel("Configuración de Alertas", new ConfigurarAlertasPanel());
    }
    
    private void agregarPanel(String nombre, JPanel panel) {
    paneles.put(nombre, panel);
    if (panel instanceof InterfaceActualizar) {
        ControlActualizacion.registrar((InterfaceActualizar) panel);
    }
}

    public void mostrarPanel(String nombrePanel) {
        if (rootPaneCheckingEnabled) {
            
        }
        cardLayout.show(panelCentral, nombrePanel);
    }

    public JPanel getPanel(String nombrePanel) {
        return paneles.get(nombrePanel);
    }


    public static JPanel getComprarPanel(){
        return paneles.get("Comprar");
    }


    
    public static void main(String[] args) {
        new Controlador();
    }
} 
