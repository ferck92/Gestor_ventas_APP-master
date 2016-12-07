package ii.inge.app.fernando.com.gestor_ventas.modelo;


public class Producto {

    public String idProducto;

    public String nombre;

    public int precio;

    public int existencias;

    public Producto(String idProducto, String nombre, int precio, int existencias) {
        this.idProducto = idProducto;//1
        this.nombre = nombre;//2
        this.precio = precio;//3
        this.existencias = existencias;//4
    }

    public String toString(){
        return nombre;
    }
}
