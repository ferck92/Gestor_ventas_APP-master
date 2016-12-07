package ii.inge.app.fernando.com.gestor_ventas.ui;


import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import ii.inge.app.fernando.com.gestor_ventas.R;
import ii.inge.app.fernando.com.gestor_ventas.modelo.Cliente;
import ii.inge.app.fernando.com.gestor_ventas.sqlite.OperacionesBaseDatos;

public class listaPedidos extends AppCompatActivity {

    public OperacionesBaseDatos datos;
    String idCliente;
    String nombreCliente;
    String producto;
    String precio;
    String cantidad;
    String idDetallePedido;
    String idProducto;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        cargarListPedidos();



    }

    public void cargarListPedidos() {



        TextView textViewPedidos;
        TextView textViewCabecera;



        Cursor cursorPedidos = datos.obtenerDetallesPedido();

        int nroColumnas = 5;
        TableLayout lista = (TableLayout) findViewById(R.id.tl_listaPedidos);

        TableRow cabecera = new TableRow(this);
        lista.addView(cabecera);
        String [] cadenaCabecera = {"Cliente","Producto","Precio","Cantidad","Total"};
        for (int j = 0; j < nroColumnas; j++) {
            textViewCabecera = new TextView(this);
            textViewCabecera.setGravity(Gravity.CENTER_VERTICAL);
            textViewCabecera.setGravity(Gravity.CENTER);
            textViewCabecera.setPadding(15,15,15,15);
            textViewCabecera.setTextSize(11);
            textViewCabecera.setBackgroundResource(R.drawable.border_style_cabecera);
            textViewCabecera.setText(cadenaCabecera[j]);
            textViewCabecera.setTextColor(Color.WHITE);
            cabecera.addView(textViewCabecera);
        }



        //Recorremos el cursor de DetallePedidos
        while (cursorPedidos.moveToNext()) {

            idDetallePedido = cursorPedidos.getString(1);//obtenemos el id de detallePedido
            idProducto = cursorPedidos.getString(3);//obtenemos el id de producto
            cantidad = cursorPedidos.getString(4);//obtenemos la cantidad del producto
            precio = cursorPedidos.getString(5);//obtenemos el precio por unidad del producto

            int total =  Integer.parseInt(cantidad)* Integer.parseInt(precio);//calculamos el total


            Cursor cursorProducto = datos.obtenerProductoPorId(idProducto);
            while (cursorProducto.moveToNext()) {
                producto = cursorProducto.getString(2);

            }

            Cursor cursorCabecera = datos.obtenerCabeceraPorId(idDetallePedido);
            while (cursorCabecera.moveToNext()) {
                idCliente = cursorCabecera.getString(3);

            }

            Cursor cursorCliente = datos.obtenerClientePorId(idCliente);
            while (cursorCliente.moveToNext()) {
                nombreCliente = cursorCliente.getString(2)+" "+cursorCliente.getString(3);
            }

            String[] cadena ={nombreCliente, producto, precio, cantidad, (total+" gs.")};

            TableRow fila = new TableRow(this);
            lista.addView(fila);

            for (int j = 0; j < nroColumnas; j++) {
                    textViewPedidos = new TextView(this);
                    textViewPedidos.setGravity(Gravity.CENTER_VERTICAL);
                    textViewPedidos.setGravity(Gravity.CENTER);
                    textViewPedidos.setPadding(15,15,15,15);
                    textViewPedidos.setTextSize(9);
                    textViewPedidos.setText(cadena[j]);
                    textViewPedidos.setTextColor(Color.WHITE);
                    textViewPedidos.setBackgroundResource(R.drawable.border_style);
                    fila.addView(textViewPedidos);
            }


        }




    }



    public void Atras(View vista) {
        this.finish();

    }

    }
