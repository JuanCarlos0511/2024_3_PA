package Vista.Patrones.Interfaces;
import java.util.ArrayList;

public class ControlActualizacion {

    private static ArrayList<InterfaceActualizar> listeners = new ArrayList<>();

    public static void registrar(InterfaceActualizar ventana) {
        listeners.add(ventana);
    }

    public static void actualizarTodo() {
        for (InterfaceActualizar listener : listeners) {
            listener.actualizar();
        }
    }
}
