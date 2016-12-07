package ii.inge.app.fernando.com.gestor_ventas.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ii.inge.app.fernando.com.gestor_ventas.R;
import ii.inge.app.fernando.com.gestor_ventas.modelo.CabeceraPedido;
import ii.inge.app.fernando.com.gestor_ventas.modelo.Cliente;
import ii.inge.app.fernando.com.gestor_ventas.modelo.DetallePedido;
import ii.inge.app.fernando.com.gestor_ventas.modelo.Producto;
import ii.inge.app.fernando.com.gestor_ventas.sqlite.OperacionesBaseDatos;

public class hacerPedido extends AppCompatActivity {

    public static Cliente cliente;
    public Spinner spinnerProductos;
    public OperacionesBaseDatos datos;
    public Producto selectedProducto = null;
    public NumberPicker picker;

    public int cantidad;
    public int precio;
    public int total;
    public int Stock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacer_pedido);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        //identificamos el spinner en el que se quieren mostrar los productos
        spinnerProductos = (Spinner)findViewById(R.id.spn_productos);
        //identificamos el editTexrt donde se ingresa la cantidad
        picker = (NumberPicker)findViewById(R.id.pick_cantidad);

        recuperarCliente();
        cargarSpinnerProductos();



    }

    public void recuperarCliente(){

        //recuperamos el cliente de la activity anterior
        cliente = (Cliente)getIntent().getExtras().getSerializable("cliente");

    }

    public void cargarNumberPicker(){

        picker.setMinValue(0);
        picker.setMaxValue(Stock);
        picker.setWrapSelectorWheel(true);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                cantidad = newVal;
                calcularTotal();
            }
        });
    }


    public void cargarSpinnerProductos(){

        List<Producto> list = new ArrayList<>();

        Producto productoVacio = new Producto("00","-Seleccione un Producto-",0,0);

        list.add(0,productoVacio);

        Cursor cursorProductos = datos.obtenerProductos();

        while (cursorProductos.moveToNext()) {
            Producto producto = new Producto(cursorProductos.getString(1), cursorProductos.getString(2)
                    ,cursorProductos.getInt(3),cursorProductos.getInt(4));

            list.add(producto);
        }

        // Definimos el adaptador para el Spinner
        ArrayAdapter<Producto> adapter = new ArrayAdapter<Producto>(this, R.layout.support_simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(R.layout.textview_spinner);

        //Asignamos el adaptador al Spinner
        spinnerProductos.setAdapter(adapter);

        // Asignamos el listener al Spinner
        spinnerProductos.setOnItemSelectedListener(new SpinnerListener());

    }

    public class SpinnerListener implements AdapterView.OnItemSelectedListener {

        // Metodo onItemSelected en el que indicamos lo que queremos hacer
        // cuando sea seleccionado un elemento del Spinner
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            //obtenemos el producto seleccionado
            selectedProducto = (Producto)spinnerProductos.getSelectedItem();
            precio = selectedProducto.precio;
            Stock = selectedProducto.existencias;

            //se muestra el precio en el textView
            TextView precioProducto = (TextView)findViewById(R.id.txt_mostrarPrecio);
            precioProducto.setText(""+precio+ " gs.");

            //se muestra el stock en el textView
            TextView stockProducto = (TextView)findViewById(R.id.txt_mostrarStock);
            stockProducto.setText("" + Stock);

            cargarNumberPicker();

        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }



    public void calcularTotal(){

        total = cantidad*precio;

        //se muestra el total en el textView
        TextView totalProducto = (TextView)findViewById(R.id.txt_mostrarTotal);
        totalProducto.setText(""+total+" gs.");


    }

    public void registrar(View vista){

        if (selectedProducto.idProducto.equals("00")){
            Toast.makeText(getApplicationContext(),"Seleccione un Producto!",Toast.LENGTH_LONG).show();
            return;
        }

        if(cantidad == 0){
            Toast.makeText(getApplicationContext(),"Ingrese una cantidad!",Toast.LENGTH_LONG).show();
            return;
        }

        if (cantidad > Stock){
            Toast.makeText(getApplicationContext(),"No hay suficientes productos!",Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Verifique la cantidad disponible",Toast.LENGTH_LONG).show();
            return;
        }

        new registrarDatosPedido().execute();
        reiniciarActivity(this);
        Toast.makeText(getApplicationContext(),"El pedido se ha registrado correctamente!",Toast.LENGTH_LONG).show();



    }

    //Registramos en la base de datos el nuevo pedido
    public class registrarDatosPedido extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {

                datos.getDb().beginTransaction();

                // Inserción Pedidos
                String pedido1 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual,cliente.idCliente));

                // Inserción Detalles
                datos.insertarDetallePedido(new DetallePedido(pedido1, 1, selectedProducto.idProducto, cantidad,precio));

                //se obtiene el nuevo stock de producto
                Stock = Stock - cantidad;
                //se actualiza el registro de producto en la base de datos con su nuevo stock
                datos.actualizarProducto(new Producto(selectedProducto.idProducto,selectedProducto.nombre,precio,Stock));


                datos.getDb().setTransactionSuccessful();

            } finally {
                datos.getDb().endTransaction();
            }


            return null;
        }
    }

    public void verListaPedidos(View vista){

            Intent i = new Intent(this, listaPedidos.class);
            startActivity(i);

    }

    public void Atras(View vista) {

        this.finish();

    }

    //reinicia una Activity
    public static void reiniciarActivity(Activity actividad){
        Intent intent=new Intent();
        intent.setClass(actividad, actividad.getClass());
        intent.putExtra("cliente",cliente);
        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();
    }
}
