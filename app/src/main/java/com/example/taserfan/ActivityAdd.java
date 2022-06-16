package com.example.taserfan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Clases.Bicicleta;
import com.example.taserfan.Clases.Coche;
import com.example.taserfan.Clases.Color;
import com.example.taserfan.Clases.Estado;
import com.example.taserfan.Clases.Moto;
import com.example.taserfan.Clases.Patinete;
import com.example.taserfan.Clases.TipoVehiculo;
import com.example.taserfan.Preferencias.PreferenciasActivity;
import com.example.taserfan.Preferencias.ThemeSetup;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;

import java.sql.Date;
import java.util.Calendar;

public class ActivityAdd extends BaseActivity implements AdapterView.OnItemSelectedListener, CallInterface {

    EditText matricula,precio, marca,descripcion,bateria,fecha, idCarnet, numPuertas, numPlazas, velmax, cilindrada,tipo, numRuedas, tamanyo;
    Button anyadir;
    Spinner sTipo, sColor,sEstado;
    ConstraintLayout layouCoche, layoutMoto,layoutBicicleta,layoutPatinete;

    ArrayAdapter<String> tipoV,colorV,estadoV;
    String[] tipoVehiculo = {"COCHE", "MOTO", "BICICLETA", "PATINETE"};
    String[] colorVehiculo = {"rojo", "amarillo", "verde", "azul", "blanco", "negro"};
    String[] estadoVehiculo = {"baja", "taller", "preparado", "reservado", "alquilado"};

    Context context;
    Coche coche;
    Moto moto;
    Bicicleta bicicleta;
    Patinete patinete;
    Color col;
    Estado est;
    Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        context = this;

        matricula = findViewById(R.id.editTextMatricula);
        precio = findViewById(R.id.editTextPrecioH);
        marca = findViewById(R.id.editTextMarca);
        descripcion = findViewById(R.id.editTextDescripcion);
        bateria = findViewById(R.id.editTextBateria);
        fecha = findViewById(R.id.editTextFech);
        idCarnet = findViewById(R.id.editTextId);

        numPuertas = findViewById(R.id.eTextNumPuertas);
        numPlazas = findViewById(R.id.eTextNumPlazas);
        velmax = findViewById(R.id.eTextVelMax);
        cilindrada = findViewById(R.id.eTextCilindrada);
        tipo = findViewById(R.id.eTextTipo);
        numRuedas = findViewById(R.id.eTextNumRuedas);
        tamanyo = findViewById(R.id.eTextTamanyo);

        layouCoche = findViewById(R.id.layoutCoche);
        layoutMoto = findViewById(R.id.layoutMoto);
        layoutBicicleta = findViewById(R.id.layoutBicicleta);
        layoutPatinete = findViewById(R.id.layoutPatinete);

        anyadir = findViewById(R.id.buttonAnyadir);
        anyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!matricula.getText().toString().isEmpty() && !marca.getText().toString().isEmpty() && !bateria.getText().toString().isEmpty()){
                    switch (sColor.getSelectedItem().toString()){
                        case "rojo":
                            col = Color.ROJO;
                            break;
                        case "amarillo":
                            col = Color.AMARILLO;
                            break;
                        case "verde":
                            col = Color.VERDE;
                            break;
                        case "azul":
                            col = Color.AZUL;
                            break;
                        case "blanco":
                            col = Color.BLANCO;
                            break;
                        case "negro":
                            col = Color.NEGRO;
                            break;
                    }

                    switch (sEstado.getSelectedItem().toString()){
                        case "baja":
                            est = Estado.BAJA;
                            break;
                        case "taller":
                            est = Estado.TALLER;
                            break;
                        case "preparado":
                            est = Estado.PREPARADO;
                            break;
                        case "reservado":
                            est = Estado.RESERVADO;
                            break;
                        case "alquilado":
                            est = Estado.ALQUILADO;
                            break;
                    }

                    switch (sTipo.getSelectedItem().toString()){
                        case "COCHE":
                            if (!numPuertas.getText().toString().isEmpty() && !numPlazas.getText().toString().isEmpty()) {
                                coche = new Coche(matricula.getText().toString(), Integer.parseInt(precio.getText().toString()), marca.getText().toString(), descripcion.getText().toString(), col, Integer.parseInt(bateria.getText().toString()),new Date(Calendar.getInstance().getTime().getTime()), est,Integer.parseInt(idCarnet.getText().toString()),TipoVehiculo.COCHE, Integer.parseInt(numPuertas.getText().toString()), Integer.parseInt(numPlazas.getText().toString()));
                                executeCall(ActivityAdd.this);
                            }
                            break;
                        case "MOTO":
                            if (!velmax.getText().toString().isEmpty() && !cilindrada.getText().toString().isEmpty()) {
                                moto = new Moto(matricula.getText().toString(), Integer.parseInt(precio.getText().toString()), marca.getText().toString(), descripcion.getText().toString(), col, Integer.parseInt(bateria.getText().toString()),new Date(Calendar.getInstance().getTime().getTime()), est,Integer.parseInt(idCarnet.getText().toString()),TipoVehiculo.COCHE,
                                        Integer.parseInt(velmax.getText().toString()), Integer.parseInt(cilindrada.getText().toString()));
                                executeCall(ActivityAdd.this);
                            }
                            break;
                        case "BICICLETA":
                            if (!tipo.getText().toString().isEmpty()) {
                                bicicleta = new Bicicleta(matricula.getText().toString(), Integer.parseInt(precio.getText().toString()), marca.getText().toString(),
                                        descripcion.getText().toString(), col, Integer.parseInt(bateria.getText().toString()),
                                        new Date(Calendar.getInstance().getTime().getTime()), est,Integer.parseInt(idCarnet.getText().toString()),
                                        TipoVehiculo.COCHE, tipo.getText().toString());
                                executeCall(ActivityAdd.this);
                            }
                            break;
                        case "PATINETE":
                            if (!numRuedas.getText().toString().isEmpty()) {
                                patinete = new Patinete(matricula.getText().toString(), Integer.parseInt(precio.getText().toString()), marca.getText().toString(),
                                        descripcion.getText().toString(), col, Integer.parseInt(bateria.getText().toString()),
                                        new Date(Calendar.getInstance().getTime().getTime()), est,Integer.parseInt(idCarnet.getText().toString()),TipoVehiculo.COCHE,
                                        Integer.parseInt(numRuedas.getText().toString()), Integer.parseInt(tamanyo.getText().toString()));
                                executeCall(ActivityAdd.this);
                            }
                            break;
                        default:
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityAdd.this);
                            builder.setMessage("Tipo de vehiculo no valido")
                                    .setTitle("Error")
                                    .setPositiveButton("Ok", null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                    }
                }
            }
        });


        sTipo = findViewById(R.id.spinnerTipoVehiculo);
        sTipo.setOnItemSelectedListener(this);
        sColor = findViewById(R.id.spinnerColor);
        sEstado = findViewById(R.id.spinnerEstado);

        tipoV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoVehiculo);
        tipoV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colorVehiculo);
        colorV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadoVehiculo);
        estadoV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sTipo.setAdapter(tipoV);
        sColor.setAdapter(colorV);
        sEstado.setAdapter(estadoV);
        ThemeSetup.applyPreferenceTheme(getApplicationContext());
    }


    @Override
    public void doInBackground() {
        if (coche != null)
            result = Connector.getConector().post(Coche.class, coche, API.Routes.COCHE);
        else if (moto != null)
            result = Connector.getConector().post(Moto.class, moto, API.Routes.MOTO);
        else if (bicicleta != null)
            result = Connector.getConector().post(Bicicleta.class, bicicleta, API.Routes.BICICLETA);
        else if (patinete != null)
            result = Connector.getConector().post(Patinete.class, patinete, API.Routes.PATINETE);
    }

    @Override
    public void doInUI() {
        if (result instanceof Result.Success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
            builder.setTitle("Vehiculo añadido")
                    .setPositiveButton("Ok", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
            builder.setMessage("No se ha podido añadir el vehiculo")
                    .setTitle("Error")
                    .setPositiveButton("Ok", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        Intent intent = new Intent(ActivityAdd.this, MainActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected =adapterView.getSelectedItem().toString();
        switch (selected){
            case "COCHE":
                layouCoche.setVisibility(View.VISIBLE);
                layoutMoto.setVisibility(View.GONE);
                layoutBicicleta.setVisibility(View.GONE);
                layoutPatinete.setVisibility(View.GONE);
                break;
            case "MOTO":
                layouCoche.setVisibility(View.GONE);
                layoutMoto.setVisibility(View.VISIBLE);
                layoutBicicleta.setVisibility(View.GONE);
                layoutPatinete.setVisibility(View.GONE);
                break;
            case "BICICLETA":
                layouCoche.setVisibility(View.GONE);
                layoutMoto.setVisibility(View.GONE);
                layoutBicicleta.setVisibility(View.VISIBLE);
                layoutPatinete.setVisibility(View.GONE);
                break;
            case "PATINETE":
                layouCoche.setVisibility(View.GONE);
                layoutMoto.setVisibility(View.GONE);
                layoutBicicleta.setVisibility(View.GONE);
                layoutPatinete.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("matricula", matricula.getText().toString());
        outState.putString("precio", precio.getText().toString());
        outState.putString("marca", marca.getText().toString());
        outState.putString("descripcion", descripcion.getText().toString());
        outState.putInt("sColor", sColor.getSelectedItemPosition());
        outState.putString("bateria", bateria.getText().toString());
        outState.putInt("sEstado", sEstado.getSelectedItemPosition());
        outState.putString("idCarnet", idCarnet.getText().toString());
        outState.putInt("sTipo", sTipo.getSelectedItemPosition());

        outState.putString("numPuertas", numPuertas.getText().toString());
        outState.putString("numPlazas", numPlazas.getText().toString());

        outState.putString("velocidadMax", velmax.getText().toString());
        outState.putString("cilindrada", cilindrada.getText().toString());

        outState.putString("tipo", tipo.getText().toString());

        outState.putString("numRuedas", numRuedas.getText().toString());
        outState.putString("tamanyo", tamanyo.getText().toString());

    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        matricula.setText(savedInstanceState.getString("matricula"));
        precio.setText(savedInstanceState.getString("precio"));
        marca.setText(savedInstanceState.getString("marca"));
        descripcion.setText(savedInstanceState.getString("descripcion"));
        sColor.setSelection(savedInstanceState.getInt("sColor"));
        bateria.setText(savedInstanceState.getString("bateria"));
        sEstado.setSelection(savedInstanceState.getInt("sEstado"));
        idCarnet.setText(savedInstanceState.getString("idCarnet"));
        sTipo.setSelection(savedInstanceState.getInt("sTipo"));

        numPlazas.setText(savedInstanceState.getString("numPuertas"));
        numPuertas.setText(savedInstanceState.getString("numPlazas"));

        velmax.setText(savedInstanceState.getString("velocidadMax"));
        cilindrada.setText(savedInstanceState.getString("cilindrada"));

        tipo.setText(savedInstanceState.getString("tipo"));

        numRuedas.setText(savedInstanceState.getString("numRuedas"));
        tamanyo.setText(savedInstanceState.getString("tamanyo"));
    }

}