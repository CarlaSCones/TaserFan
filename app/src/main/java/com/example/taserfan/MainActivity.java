package com.example.taserfan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Clases.TipoVehiculo;
import com.example.taserfan.Clases.Vehiculo;
import com.example.taserfan.Preferencias.GestionPreferencias;
import com.example.taserfan.Preferencias.PreferenciasActivity;
import com.example.taserfan.Preferencias.ThemeSetup;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends BaseActivity implements CallInterface, View.OnClickListener{

    RecyclerView recycler;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    List<Vehiculo> vehiculos, aux;
    TipoVehiculo tipoVehiculo;
    Button add;
    EditText filtrar;
    Result result;
    Context context;
    Spinner spinner;
    View.OnClickListener click = this;
    private final String url = "http://" +GestionPreferencias.getInstance().getIp(this)+":"+ GestionPreferencias.getInstance().getPuerto(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerVehiculos);
        add = findViewById(R.id.addVehiculo);
        filtrar = findViewById(R.id.editTextFiltrar);
        vehiculos= new ArrayList<Vehiculo>();

        spinner = findViewById(R.id.elegirVehiculos);

        ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Seleccione el tipo de vehiculo que quiere mostrar:");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (tipoVehiculo){
                    case COCHE:
                        tipoVehiculo =TipoVehiculo.COCHE;
                        break;
                    case MOTO:
                        tipoVehiculo =TipoVehiculo.MOTO;
                        break;
                    case BICICLETA:
                        tipoVehiculo =TipoVehiculo.BICICLETA;
                        break;
                    case PATINETE:
                        tipoVehiculo =TipoVehiculo.PATINETE;
                        break;
                }
                adapter.getItemId(i);
                vehiculos.stream().filter(vehiculo -> vehiculo.getTipoVehiculo() == tipoVehiculo).collect(Collectors.toList());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recycler.addItemDecoration(dividerItemDecoration);

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, vehiculos);
        myRecyclerViewAdapter.setListener(this);
        recycler.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager myLinearLayaoutManager=new LinearLayoutManager(this);
        recycler.setLayoutManager(myLinearLayaoutManager);
        recycler.setOnClickListener(this);

        ThemeSetup.applyPreferenceTheme(getApplicationContext());

        GestionPreferencias.getInstance().getTheme(getApplicationContext());
        GestionPreferencias.getInstance().getIp(getApplicationContext());
        GestionPreferencias.getInstance().getPuerto(getApplicationContext());

        filtrar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0){
                    MyRecyclerViewAdapter viewAdapter = new MyRecyclerViewAdapter(context,vehiculos);
                    viewAdapter.setOnClickListener(click);
                    recycler.setAdapter(viewAdapter);
                }else{
                    List<Vehiculo> vehiculoList = vehiculos.stream().filter((vehiculo) -> vehiculo.getMatricula().contains(editable.toString()) ||
                            vehiculo.getMarca().equals(editable.toString()) || vehiculo.getColor().equals(editable.toString())||
                            vehiculo.getTipoVehiculo().equals(editable.toString())).collect(Collectors.toList());
                    MyRecyclerViewAdapter adaptador = new MyRecyclerViewAdapter(context,vehiculoList);
                    adaptador.setOnClickListener(click);
                    recycler.setAdapter(adaptador);
                }
            }
        });

        ItemTouchHelper.SimpleCallback sck = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {

                Vehiculo v=  aux.get(holder.getAdapterPosition());
                executeCall(new CallInterface() {
                    @Override
                    public void doInBackground() {
                        result= Connector.getConector().delete(Vehiculo.class,(url+ API.Routes.VEHICULO +"?matricula="+v.getMatricula()));
                    }

                    @Override
                    public void doInUI() {
                        if (result instanceof Result.Success){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Eliminado")
                                    .setPositiveButton("Ok",null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }else {
                            Result.Error error=(Result.Error)result;
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Error "+error.getCode()+": "+error.getError())
                                    .setTitle("Error")
                                    .setPositiveButton("Ok",null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        vehiculos.remove(holder.getAdapterPosition());
                        myRecyclerViewAdapter.notifyItemRemoved(holder.getAdapterPosition());
                    }
                });

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(sck);
        itemTouchHelper.attachToRecyclerView(recycler);
        executeCall(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void doInBackground() {
        vehiculos = Connector.getConector().getAsList(Vehiculo.class, API.Routes.VEHICULOS);
    }

    @Override
    public void doInUI() {
        hideProgress();
        myRecyclerViewAdapter.setNewData(aux);
    }

    @Override
    public void onClick(View view) {
        Intent intent =new Intent(getApplicationContext(),Activity_detallado.class);
        int position=recycler.getChildAdapterPosition(view);
        intent.putExtra("matricula",vehiculos.get(recycler.getChildAdapterPosition(view)).getMatricula());
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.configuracion):
                Intent intentPreferenciasActivity = new Intent(this, PreferenciasActivity.class);
                startActivity(intentPreferenciasActivity);
                return true;
            case (R.id.exit):
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}