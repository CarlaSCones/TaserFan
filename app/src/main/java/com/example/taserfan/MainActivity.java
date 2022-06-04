package com.example.taserfan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Clases.Vehiculo;
import com.example.taserfan.Preferencias.PreferenciasActivity;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;
import com.example.taserfan.base.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements CallInterface, View.OnClickListener{

    RecyclerView recycler;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    List<Vehiculo> vehiculos, aux;
    Button add;
    EditText filtrar;
    Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerVehiculos);
        add = findViewById(R.id.addVehiculo);
        vehiculos= new ArrayList<Vehiculo>();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recycler.addItemDecoration(dividerItemDecoration);

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, vehiculos);
        myRecyclerViewAdapter.setListener(this);
        recycler.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager myLinearLayaoutManager=new LinearLayoutManager(this);
        recycler.setLayoutManager(myLinearLayaoutManager);
        recycler.setOnClickListener(this);

        filtrar = findViewById(R.id.editTextFiltrar);

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
                        result= Connector.getConector().delete(Vehiculo.class,(API.Routes.URL+ API.Routes.VEHICULO +"?matricula="+v.getMatricula()));
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
        myRecyclerViewAdapter.setNewData(aux);
    }

    @Override
    public void onClick(View view) {
        Intent intent =new Intent(getApplicationContext(),Activity_detallado.class);
        int position=recycler.getChildAdapterPosition(view);
        intent.putExtra("posicion",position);
        intent.putExtra("tipoVehiculo",vehiculos.get(position).getTipoVehiculo());
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