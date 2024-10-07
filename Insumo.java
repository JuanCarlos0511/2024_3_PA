package Modelo;

public class Insumo {
    private String idProducto;
    private String producto;
    private String idCategoria;

    public Insumo(String idProducto, String producto, String idCategoria) {
        this.producto = producto;
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }
    
    public String toLinea() {
        return idProducto + "," + producto + "," + idCategoria;
    }
    

    @Override
    public String toString() {
        return idProducto + "\t" + producto + "\t" + idCategoria;
    }
}
