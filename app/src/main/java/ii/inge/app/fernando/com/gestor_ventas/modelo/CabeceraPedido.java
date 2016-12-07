package ii.inge.app.fernando.com.gestor_ventas.modelo;

public class CabeceraPedido {

    public String idCabeceraPedido;

    public String fecha;

    public String idCliente;


    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String idCliente) {
        this.idCabeceraPedido = idCabeceraPedido;//1
        this.fecha = fecha;//2
        this.idCliente = idCliente;//3
    }
}
