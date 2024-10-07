package Modelo;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class ListaInsumos {
    private ArrayList<Insumo> insumos;

    public ListaInsumos() {
        this.insumos = new ArrayList<>();
    }

    public boolean agregarInsumo(Insumo insumo) {
        boolean inserto = true;
        if (buscarInsumoPorId(insumo.getIdProducto()) == null) {
            insumos.add(insumo);
        } else inserto = false;
        return inserto;
    }

    public boolean eliminarInsumoPorId(String id) {
        boolean elimino = true;
        Insumo insumo = buscarInsumoPorId(id);
        if (insumo != null) insumos.remove(insumo);
        else elimino = false;
        return elimino;
    }

    public String toString() {
        String resultado = "";
        for (Insumo insumo : insumos) {
            resultado += insumo.toString() + "\n";
        }
        return resultado;
    }

    private Insumo buscarInsumoPorId(String id) {
        for (Insumo insumo : insumos) {
            if (insumo.getIdProducto().equals(id)) {
                return insumo;
            }
        }
        return null;
    }

    public String buscarInsumo(String id) {
        for (Insumo insumo : insumos) {
            if (insumo.getIdProducto().equals(id)) {
                return insumo.getProducto();
            }
        }
        return null;
    }

    public String[] idinsumos() {
        int pos = -1;
        String[] datos = new String[this.insumos.size()];
        for (Insumo nodo : this.insumos) {
            pos++;
            datos[pos] = nodo.getIdProducto();
        }
        return datos;
    }

    public ArrayList<Insumo> getInsumos() {
        return this.insumos;
    }
    
    public String toArchivo() {
        String resultado = "";
        for (Insumo insumo : insumos) {
            resultado += insumo.toLinea() + "\n";
        }
        return resultado;
    }
    public DefaultTableModel getmodelo(ListaCategorias listac) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("Id");
        modelo.addColumn("Insumo");
        modelo.addColumn("Categoria");

        for (Insumo nodo : this.insumos) {
            String[] info = new String[3];
            info[0] = nodo.getIdProducto().trim();
            info[1] = nodo.getProducto().trim();
            info[2] = nodo.getIdCategoria().trim();
            		// listac.buscarCategoria(nodo.getIdCategoria()).trim();
            modelo.addRow(info);
        }

        return modelo;
    }
    public void cargarInsumo(ArrayList<String[]> insumosString) {
    	for (String[] categoriaString : insumosString) {
    		
    	    String id = categoriaString[0];
    	    String insumo = categoriaString[1];
    	    String idCategoria = categoriaString[2];
    	    Insumo nodo = new Insumo(id, insumo, idCategoria);
    	    this.agregarInsumo(nodo);
    	}
    }
}


