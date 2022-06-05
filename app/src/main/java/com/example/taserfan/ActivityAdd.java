package com.example.taserfan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Clases.Color;
import com.example.taserfan.Clases.Estado;
import com.example.taserfan.Clases.TipoVehiculo;
import com.example.taserfan.Clases.Vehiculo;
import com.example.taserfan.Preferencias.GestionPreferencias;
import com.example.taserfan.Preferencias.PreferenciasActivity;
import com.example.taserfan.Preferencias.ThemeSetup;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;

public class ActivityAdd extends BaseActivity implements CallInterface {

    EditText matri, marc,precio, bateria, idCarnet,color,estado;
    Vehiculo v;
    TipoVehiculo tipoVehiculo;
    Button anyadir;
    Result result;
    private final String url = "http://" +GestionPreferencias.getInstance().getIp(this)+":"+ GestionPreferencias.getInstance().getPuerto(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        matri = findViewById(R.id.editTextMatricula);
        precio = findViewById(R.id.editTextPrecioH);
        color = findViewById(R.id.editTextColor);
        marc = findViewById(R.id.editTextMarca);
        bateria = findViewById(R.id.editTextBateria);
        estado = findViewById(R.id.editTextEstado);
        idCarnet = findViewById(R.id.editTextId);
        anyadir = findViewById(R.id.buttonAnyadir);

        ThemeSetup.applyPreferenceTheme(getApplicationContext());

        GestionPreferencias.getInstance().getTheme(getApplicationContext());
        GestionPreferencias.getInstance().getIp(getApplicationContext());
        GestionPreferencias.getInstance().getPuerto(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
        executeCall(this);
    }

    @Override
    public void doInBackground() {
       v = Connector.getConector().get(Vehiculo.class, url + API.Routes.VEHICULOS);
    }

    @Override
    public void doInUI() {
        hideProgress();
        matri.setText("" + v.getMatricula());
        precio.setText("" + v.getMarca());
        color.setText("" + v.getPreciohora());
        marc.setText("" + v.getColor());
        bateria.setText("" + v.getBateria());
        estado.setText("" + v.getEstado());
        idCarnet.setText("" + v.getIdCarnet());

        anyadir.setOnClickListener(view -> {

            String matricula,marca,desc,changedBy;
            int precioh,bat,id;
            Color col;
            Estado est;
            matricula= matri.getText().toString();
            marca= marc.getText().toString();
            precioh= Integer.parseInt(precio.getText().toString());
            desc= " ";
            col= Color.valueOf(color.getText().toString());
            bat= Integer.parseInt(bateria.getText().toString());
            est= Estado.valueOf(estado.getText().toString());
            id= Integer.parseInt(idCarnet.getText().toString());
            changedBy=" ";

            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);

            try {
                Integer.parseInt(precio.getText().toString());
            }catch (NumberFormatException nfe){
                builder.setMessage("Debes introducir un numero en el precio")
                        .setTitle("Error")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            try {
                Integer.parseInt(bateria.getText().toString());
            }catch (NumberFormatException nfe){
                builder.setMessage("Debes introducir un numero en la bateria")
                        .setTitle("Error")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            if (marca.equals("")) {
                builder.setMessage("Debes introducir una marca en la marca")
                        .setTitle("Error")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else if (idCarnet.equals("")) {
                builder.setMessage("El campo del ID del coche esta vacio")
                        .setTitle("Error")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {

                switch (tipoVehiculo) {
                    case MOTO:
                        executeCall(new CallInterface() {
                            Vehiculo v;
                            @Override
                            public void doInBackground() {
                                TipoVehiculo tv=TipoVehiculo.MOTO;
                                v = new Vehiculo(matricula, precioh, marca, desc, col, bat, est, id,changedBy,tv);
                                result = Connector.getConector().post(Vehiculo.class, v, url + API.Routes.VEHICULO);
                            }

                            @Override
                            public void doInUI() {
                                if (result instanceof Result.Success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setTitle("Actualizado")
                                            .setPositiveButton("Ok", (dialogInterface, i) -> finish());
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    Result.Error error = (Result.Error) result;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setMessage("Error: "+error.getCode()+ "( "+error.getError()+" )")
                                            .setTitle("Error")
                                            .setPositiveButton("Ok",null);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
                        break;
                    case COCHE:
                        executeCall(new CallInterface() {
                            Vehiculo v;
                            @Override
                            public void doInBackground() {
                                TipoVehiculo tv =TipoVehiculo.COCHE;
                                v = new Vehiculo(matricula, precioh, marca, desc, col, bat, est, id,changedBy,tv);
                                result = Connector.getConector().post(Vehiculo.class, v, url + API.Routes.VEHICULO);
                            }

                            @Override
                            public void doInUI() {
                                if (result instanceof Result.Success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setTitle("Actualizado")
                                            .setPositiveButton("Ok", (dialogInterface, i) -> finish());
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    Result.Error error = (Result.Error) result;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setMessage("Error "+error.getCode()+ ": "+error.getError())
                                            .setTitle("Error")
                                            .setPositiveButton("Ok",null);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
                        break;
                    case BICICLETA:
                        executeCall(new CallInterface() {
                            Vehiculo v;
                            @Override
                            public void doInBackground() {
                                TipoVehiculo tv=TipoVehiculo.BICICLETA;
                                v = new Vehiculo(matricula, precioh, marca, desc, col, bat, est, id,changedBy,tv);
                                result = Connector.getConector().post(Vehiculo.class, v, url + API.Routes.VEHICULO);
                            }

                            @Override
                            public void doInUI() {
                                if (result instanceof Result.Success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setTitle("Actualizado")
                                            .setPositiveButton("Ok", (dialogInterface, i) -> finish());
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    Result.Error error = (Result.Error) result;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setMessage("Error "+error.getCode()+ ": "+error.getError())
                                            .setTitle("Error")
                                            .setPositiveButton("Ok",null);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
                        break;
                    case PATINETE:
                        executeCall(new CallInterface() {
                            Vehiculo v;
                            @Override
                            public void doInBackground() {
                                TipoVehiculo tv=TipoVehiculo.PATINETE;
                                v = new Vehiculo(matricula, precioh, marca, desc, col, bat, est, id,changedBy,tv);
                                result= Connector.getConector().post(Vehiculo.class, v, url + API.Routes.VEHICULO);
                            }

                            @Override
                            public void doInUI() {
                                if (result instanceof Result.Success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setTitle("Actualizado")
                                            .setPositiveButton("Ok", (dialogInterface, i) -> finish());
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    Result.Error error = (Result.Error) result;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdd.this);
                                    builder.setMessage("Error "+error.getCode()+ ": "+error.getError())
                                            .setTitle("Error")
                                            .setPositiveButton("Ok",null);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
                        break;
                }
            }
        });
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