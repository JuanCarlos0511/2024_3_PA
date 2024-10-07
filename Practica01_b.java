package Parte1;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Libreria.Archivotxt;
import Modelo.Categoria;
import Modelo.Insumo;
import Modelo.ListaCategorias;
import Modelo.ListaInsumos;

public class Practica01_b extends JFrame implements ActionListener {
    ListaInsumos listainsumo;
    ListaCategorias listacategorias;
    Archivotxt archivocategorias;
    Archivotxt archivoinsumos;
    
    private JList ListaCategoria;
    private JTextField Tid, Tinsumo;
    private JButton Bagregar, Beliminar, Bsalir;
    private JTextArea areaProductos;
    private DefaultListModel<Categoria> modelocategoria;
    private JPanel panelFormulario;

    
    public Practica01_b() {
        super("Administracion de Productos");
        this.inicializarcategorias();
        this.listainsumo = new ListaInsumos();
        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        setBounds(0, 0, 390, 370);
        getContentPane().add(panelFormulario, BorderLayout.CENTER);

        JLabel labelCategoria = new JLabel("Categoria:");
        labelCategoria.setBounds(10, 66, 71, 20);
        
        
        
        JScrollPane scrollPane_jlist = new JScrollPane();
        scrollPane_jlist.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_jlist.setBounds(91, 61, 157, 42);
        panelFormulario.add(scrollPane_jlist);
        
        ListaCategoria = new JList();
        scrollPane_jlist.setViewportView(ListaCategoria);
        Categoria categoria1 = new Categoria("001","Mano de Obra");
        Categoria categoria2 = new Categoria("002","Materiales");
        
        modelocategoria.add(0, categoria1);
        modelocategoria.add(1, categoria2);
        ListaCategoria.setModel(this.modelocategoria);
        ListaCategoria.setEnabled(true);
        
        panelFormulario.add(labelCategoria);

        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(10, 9, 71, 20);
        this.Tid = new JTextField(10);
        this.Tid.setEditable(false);
        this.Tid.setBounds(91, 9, 147, 20);
        panelFormulario.add(labelId);
        panelFormulario.add(Tid);

        JLabel labelInsumo = new JLabel("Insumo:");
        labelInsumo.setBounds(10, 34, 71, 20);
        this.Tinsumo = new JTextField(20);
        this.Tinsumo.setEditable(false);
        this.Tinsumo.setBounds(91, 34, 147, 20);
        panelFormulario.add(labelInsumo);
        panelFormulario.add(Tinsumo);

        this.Bagregar = new JButton("Agregar");
        this.Bagregar.setBounds(20, 104, 111, 20);
        this.Bagregar.addActionListener(this);
        panelFormulario.add(Bagregar);

        this.Beliminar = new JButton("Eliminar");
        this.Beliminar.setBounds(153, 104, 111, 20);
        this.Beliminar.addActionListener(this);
        panelFormulario.add(Beliminar);

        this.Bsalir = new JButton("Salir");
        this.Bsalir.setBounds(274, 104, 79, 20);
        this.Bsalir.addActionListener(this);
        panelFormulario.add(Bsalir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 132, 357, 179);
        panelFormulario.add(scrollPane);
        this.areaProductos = new JTextArea(10, 40);
        scrollPane.setViewportView(areaProductos);
        this.areaProductos.setEditable(false);
        
     // se carga los datos al textarea si hay datos en lista insumos 
        if (this.listainsumo.toString()!=null) 
        		this.areaProductos.setText(this.listainsumo.toString());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    
    
    	public void inicializarcategorias () {

    		this.archivocategorias = new Archivotxt("Categoria");
    		this.archivoinsumos= new Archivotxt("Insumos");
    		this.listacategorias=new ListaCategorias();
    		this.listainsumo= new ListaInsumos();

    		if (this.archivocategorias.existe())
    			this.listacategorias.cargarCategorias (this.archivocategorias.cargar()); 
    		if (this.archivoinsumos.existe())
    			this.listainsumo.cargarInsumo (this.archivoinsumos.cargar());

    		modelocategoria = new DefaultListModel<Categoria> ();
    		modelocategoria= this.listacategorias.generarModeloCategorias();

    		}
       
    

   


    public Boolean esdatoscompletos() {
        boolean enc = false;
        String id, insumo, idcategoria;
        id = "";
        insumo = "";
        idcategoria = "";

        id = this.Tid.getText().trim();
        insumo = this.Tinsumo.getText().trim();
        
        if (this.ListaCategoria.getSelectedIndex() >= 0) {
            idcategoria = this.modelocategoria.get(this.ListaCategoria.getSelectedIndex()).getIdcategoria();
        }
        
        if ((!id.isEmpty()) && (!insumo.isEmpty()) && (!idcategoria.isEmpty())) {
            enc = true;
        }
        
        System.out.println(id + " " + insumo + " " + idcategoria);
        return enc;
    }

    public void Volveralinicio() {
        this.Bagregar.setText("Agregar");
        this.Bsalir.setText("Salir");
        this.Beliminar.setEnabled(true);
        this.Tid.setEditable(false);
        this.Tinsumo.setEditable(false);
        this.ListaCategoria.setEnabled(false);
        this.Tid.setText("");
        this.Tinsumo.setText("");
        this.ListaCategoria.setSelectedIndex(0);
    }

    
    public void Altas() {
    	if(this.Bagregar.getText().compareTo("Agregar")==0) {

    	 this. ListaCategoria.setSelectedIndex (0);
    	this.Bagregar.setText("Salvar");
    	this.Bsalir.setText("Cancelar");
    	this.Beliminar.setEnabled (false);
    	this.Tid.setEditable(true);
    	this.Tinsumo.setEditable (true);
    	this.ListaCategoria.setEnabled(true);
    	this. ListaCategoria.setFocusable (true);
    	}
    	else
    	{
    		if(esdatoscompletos()) {
    			String id, insumo,idcategoria; 
    			id= this. Tid.getText().trim();
    			insumo=this.Tinsumo.getText().trim();
    			idcategoria=this.modelocategoria.get(this.ListaCategoria.getSelectedIndex()).getIdcategoria (); Insumo nodo = new Insumo (id, insumo, idcategoria);
    			if (!this.listainsumo.agregarInsumo(nodo))
    			{
    				String mensaje="lo siente el id "+id+ "ya existe lo tiene asignado "+this.listainsumo.buscarInsumo (id);
    				JOptionPane.showMessageDialog(this, mensaje );
    			}
    			else 
    			{
    				//sobreescribimos el archivo cuando se agrega un nuevo elemento
    				this.archivoinsumos.guardar(this.listainsumo.toArchivo());
    				this.areaProductos.setText(this.listainsumo.toString());
    		}
    		
    	}
    		this.Volveralinicio();
    	}
     }
    	
    
    public void Eliminar() {
    	Object[] opciones=this.listainsumo.idinsumos();

    	String id=(String) JOptionPane.showInputDialog(null, "Seleccione una opción:", "Eliminacion de Insumos", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
    	if ((id!=null) || (!id.isEmpty()))
    	{
    		if(!this.listainsumo.eliminarInsumoPorId(id))
    			JOptionPane.showMessageDialog(this, "No existe este id");

    			else {
    			//sobreescribimos el archivo cuando se elimina un nuevo elemento 
    			this.archivoinsumos.guardar(this.listainsumo.toArchivo());
    			this.areaProductos.setText(this.listainsumo.toString());

    			}
    	}
    }
    
    
    
   
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bagregar) {
            this.Altas();
        } else if (e.getSource() == this.Beliminar) {
            this.Eliminar();
        } else if (e.getSource() == Bsalir) {
            if (this.Bsalir.getText().compareTo("Cancelar") == 0) {
                this.Volveralinicio();
            } else {
                this.dispose();
            }
        }
    }
    
    
    public static void main(String[] args) {
        new Practica01_b();
    }

}
