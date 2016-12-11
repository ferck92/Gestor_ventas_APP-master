package ii.inge.app.fernando.com.gestor_ventas.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ii.inge.app.fernando.com.gestor_ventas.R;
import ii.inge.app.fernando.com.gestor_ventas.modelo.*;
import ii.inge.app.fernando.com.gestor_ventas.sqlite.OperacionesBaseDatos;


public class SeleccionCliente extends AppCompatActivity {

    public OperacionesBaseDatos datos;
    public Spinner spinnerClientes;
    public Cliente selectedCliente = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_cliente);

        getApplicationContext().deleteDatabase("pedidos.db");
        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        new TareaPruebaDatos().execute();

        cargarSpinnerClientes();


    }

    public void cargarSpinnerClientes(){

        spinnerClientes = (Spinner)findViewById(R.id.spn_clientes);

        List<Cliente> list = new ArrayList<>();
        //Se crea un objeto cliente vacio
        Cliente clienteVacio = new Cliente("00","-Seleccione un Cliente-", "","", "");
        //Se añade el cliente vacio a la lista como primer elemento
        list.add(0,clienteVacio);

        Cursor cursorClientes = datos.obtenerClientes();

        while (cursorClientes.moveToNext()) {
            Cliente cliente = new Cliente(cursorClientes.getString(1), cursorClientes.getString(2),
                                            cursorClientes.getString(3), cursorClientes.getString(4),
                                            cursorClientes.getString(5));

            list.add(cliente);
        }

        // Definimos el adaptador para el Spinner
        ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(this, R.layout.support_simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(R.layout.textview_spinner);

        //Asignamos el adaptador al Spinner
        spinnerClientes.setAdapter(adapter);

        // Asignamos el listener al Spinner
        spinnerClientes.setOnItemSelectedListener(new SpinnerListener());

    }

    // Denificion del listener
    public class SpinnerListener implements AdapterView.OnItemSelectedListener {

        // Metodo onItemSelected en el que indicamos lo que queremos hacer
        // cuando sea seleccionado un elemento del Spinner
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            //recuperamos el objeto seleccionado
            selectedCliente = (Cliente)spinnerClientes.getSelectedItem();

            //se muestran los datos del cliente en los textView
            TextView nombreCliente = (TextView)findViewById(R.id.txt_mostrarNombre);
            nombreCliente.setText(selectedCliente.nombres);

            TextView apellidoCliente = (TextView)findViewById(R.id.txt_mostrarApellido);
            apellidoCliente.setText(selectedCliente.apellidos);

            TextView direccionCliente = (TextView)findViewById(R.id.txt_mostrarDireccion);
            direccionCliente.setText(selectedCliente.direccion);

            TextView telefonoCliente = (TextView)findViewById(R.id.txt_mostrarTelefono);
            telefonoCliente.setText(selectedCliente.telefono);

        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }



    public void Pedido(View vista){

        if (selectedCliente.idCliente.equals("00")){
            Toast.makeText(getApplicationContext(),"Seleccione un Cliente!",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(this, hacerPedido.class);
        i.putExtra("cliente",selectedCliente);
        startActivity(i);

    }


    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {


            try {

                datos.getDb().beginTransaction();

                // Inserción Clientes
                datos.insertarCliente(new Cliente(null, "Veronica", "Gonsalez","Asunción", "4552000"));
                datos.insertarCliente(new Cliente(null, "Carlos", "Villagran","Luque", "4440000"));
                datos.insertarCliente(new Cliente(null, "Juan", "Perez","Limpio", "3565000"));


                // Inserción Productos
                datos.insertarProducto(new Producto(null, "Manzana", 2000, 100));
                datos.insertarProducto(new Producto(null, "Pera", 3000, 230));
                datos.insertarProducto(new Producto(null, "Guayaba", 1500, 55));
                datos.insertarProducto(new Producto(null, "Maní", 4000, 60));



                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            return null;
        }
    }



}


