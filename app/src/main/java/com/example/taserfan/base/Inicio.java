package com.example.taserfan.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taserfan.API.Connector;
import com.example.taserfan.Empleado;
import com.example.taserfan.R;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends BaseActivity implements CallInterface, View.OnClickListener {

    private RecyclerView recyclerView;
    private List<List> datos;
    private Empleado empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_main);

        if( getIntent().getExtras() != null) {

            empleado = (Empleado) getIntent().getExtras().getSerializable("usuario");

            datos = new ArrayList<>();
            recyclerView = findViewById(R.id.recycler);
            TextView usuario = findViewById(R.id.usuario);

            usuario.setText(empleado.getNombre());

            recyclerView.setOnClickListener(this);
            MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, datos);
            myRecyclerViewAdapter.setOnClickListener(this);
            recyclerView.setAdapter(myRecyclerViewAdapter);

            LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(myLinearLayoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);



        } else {
            Toast.makeText(this,"No se ha encontrado el usuaio",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
        executeCall(this);
    }

    @Override
    public void doInBackground() {
        //Ejecutar la llamanada a la bd
        String url = "forecast?lang=es&units=metric&lat="+ empleado.getLat() + "&lon="+ empleado.getLon() +"&appid="+ API;
        Log.d(TAG, url);
        root = Connector.getConector().get(Root.class,url);
    }

    @Override
    public void doInUI() {
        hideProgress();
        datos.addAll(root.list);
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Intent intent = new Intent(this, DetallesDia.class);
        intent.putExtra("root",root);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}
}
