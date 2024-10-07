package Libreria;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Librerias {
    public Icon EtiquetaImagen(String archivoimagen, int x, int y) {
        String ruta = archivoimagen;
        ImageIcon imagen = new ImageIcon(ruta);
        Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(x, y, Image.SCALE_DEFAULT));
        return icono;
    }
}
