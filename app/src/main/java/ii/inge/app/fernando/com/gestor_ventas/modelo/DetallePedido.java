package ii.inge.app.fernando.com.gestor_ventas.modelo;

public class DetallePedido {

    public String idCabeceraPedido;

    public int secuencia;

    public String idProducto;

    public int cantidad;

    public float precio;

    public DetallePedido(String idCabeceraPedido, int secuencia,
                         String idProducto, int cantidad, float precio) {
        this.idCabeceraPedido = idCabeceraPedido;//cursor.getString(1)
        this.secuencia = secuencia;//2
        this.idProducto = idProducto;//3
        this.cantidad = cantidad;//4
        this.precio = precio;//5
    }

}
