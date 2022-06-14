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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends BaseActivity implements CallInterface, View.OnClickListener, Serializable{

    RecyclerView recycler;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    ItemTouchHelper itemTouchHelper;
    List<Vehiculo> aux;
    TipoVehiculo tipoVehiculo;
    Context context;
    Button add;

    EditText filtrar;
    Spinner spinner;

    ArrayAdapter<String> adapter;
    String[] v = {"TODOS", "COCHE", "MOTO", "BICICLETA", "PATINETE"};

   // List<String> vehiculos;
    //Result result;
    //View.OnClickListener click = this;
    //private final String url = "http://" +GestionPreferencias.getInstance().getIp(this)+":"+ GestionPreferencias.getInstance().getPuerto(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerVehiculos);
        add = findViewById(R.id.addVehiculo);
        filtrar = findViewById(R.id.editTextFiltrar);

        spinner = findViewById(R.id.elegirVehiculos);
        context = this;

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityAdd.class);
                startActivity(intent);
            }
        });
        executeCall(this);
    }

    @Override
    public void doInBackground() {

        aux = new ArrayList<>(Connector.getConector().getAsList(Vehiculo.class, API.Routes.VEHICULOS));
    }

    @Override
    public void doInUI() {

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, aux);
        myRecyclerViewAdapter.setOnClickListener(this);
        recycler.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recycler.setLayoutManager(layout);

        ThemeSetup.applyPreferenceTheme(getApplicationContext());

        GestionPreferencias.getInstance().getTheme(getApplicationContext());
        GestionPreferencias.getInstance().getIp(getApplicationContext());
        GestionPreferencias.getInstance().getPuerto(getApplicationContext());


        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {

                Vehiculo v = aux.get(holder.getAdapterPosition());
                int pos = holder.getAdapterPosition();

                executeCall(new CallInterface() {
                    @Override
                    public void doInBackground() {
                        Connector.getConector().delete(Vehiculo.class, (API.Routes.VEHICULO + "?matricula=" + v.getMatricula()));
                    }

                    @Override
                    public void doInUI() {
                        aux.remove(pos);
                        myRecyclerViewAdapter.notifyItemRemoved(pos);
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }

        });

        itemTouchHelper.attachToRecyclerView(recycler);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, v);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().equals("COCHE")){
                    List<Vehiculo> list = aux.stream().filter(v ->  v.getMatricula().contains(filtrar.getText().toString()) && v.getTipoVehiculo() == TipoVehiculo.COCHE).collect(Collectors.toList());
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(context, list);
                    myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                    recycler.setAdapter(myRecyclerViewAdapter);
                }else if(spinner.getSelectedItem().toString().equals("MOTO")){
                    List<Vehiculo> list = aux.stream().filter(v -> v.getMatricula().contains(filtrar.getText().toString()) && v.getTipoVehiculo() == TipoVehiculo.MOTO).collect(Collectors.toList());
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(context, list);
                    myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                    recycler.setAdapter(myRecyclerViewAdapter);
                }else if(spinner.getSelectedItem().toString().equals("BICICLETA")){
                    List<Vehiculo> list = aux.stream().filter(v -> v.getMatricula().contains(filtrar.getText().toString()) && v.getTipoVehiculo() == TipoVehiculo.BICICLETA).collect(Collectors.toList());
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(context, list);
                    myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                    recycler.setAdapter(myRecyclerViewAdapter);
                }else if(spinner.getSelectedItem().toString().equals("PATINETE")){
                    List<Vehiculo> list = aux.stream().filter(v -> v.getMatricula().contains(filtrar.getText().toString()) && v.getTipoVehiculo() == TipoVehiculo.PATINETE).collect(Collectors.toList());
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(context, list);
                    myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                    recycler.setAdapter(myRecyclerViewAdapter);
                }else {
                    List<Vehiculo> list = aux.stream().filter((v) -> v.getMatricula().contains(filtrar.getText().toString())).collect(Collectors.toList());
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(context,list);
                    myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                    recycler.setAdapter(myRecyclerViewAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


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
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(context,aux);
                    myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                    recycler.setAdapter(myRecyclerViewAdapter);
                }else{
                    if (!spinner.getSelectedItem().toString().equals("TODOS")) {
                        List<Vehiculo> l = aux.stream().filter((v) -> v.getMatricula().contains(editable.toString()) && v.getTipoVehiculo().equals(spinner.getSelectedItem().toString())).collect(Collectors.toList());
                        myRecyclerViewAdapter = new MyRecyclerViewAdapter(context,l);
                        myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                        recycler.setAdapter(myRecyclerViewAdapter);
                    } else {
                        List<Vehiculo> l = aux.stream().filter((v) -> v.getMatricula().contains(editable.toString())).collect(Collectors.toList());
                        myRecyclerViewAdapter = new MyRecyclerViewAdapter(context,l);
                        myRecyclerViewAdapter.setOnClickListener(MainActivity.this);
                        recycler.setAdapter(myRecyclerViewAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent =new Intent(getApplicationContext(),Activity_detallado.class);
        int index = recycler.getChildAdapterPosition(view);

        intent.putExtra("index", index);
        intent.putExtra("list", (Serializable) aux);
       // intent.putExtra("matricula",aux.get(recycler.getChildAdapterPosition(view)).getMatricula());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", (Serializable) aux);
        outState.putString("filtroMatricula", filtrar.getText().toString());
        outState.putInt("posFiltroTipo", spinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        aux = (List<Vehiculo>) savedInstanceState.getSerializable("list");
        filtrar.setText(savedInstanceState.getString("filtroMatricula"));
        spinner.setSelection(savedInstanceState.getInt("posFiltroTipo"));

        if (aux != null)
            doInUI();
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