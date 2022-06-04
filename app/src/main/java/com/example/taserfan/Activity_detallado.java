package com.example.taserfan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Clases.Coche;
import com.example.taserfan.Clases.Color;
import com.example.taserfan.Clases.Estado;
import com.example.taserfan.Clases.TipoVehiculo;
import com.example.taserfan.Clases.Vehiculo;
import com.example.taserfan.Preferencias.GestionPreferencias;
import com.example.taserfan.Preferencias.PreferenciasActivity;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;

import java.util.ArrayList;

public class Activity_detallado extends BaseActivity implements CallInterface {

    Result<Coche> resultC;
    TextView mat,prh,col,mar,bat,est,id;
    ArrayList color,estado;
    Button editar;
    Result result;
    ArrayAdapter aColor, aEstado;
    TipoVehiculo tipoVehiculo;
    Vehiculo vehiculo;
    String matr;
    private String url = GestionPreferencias.getInstance().getIp(this) + ":" + GestionPreferencias.getInstance().getPuerto(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallado);

        matr = getIntent().getExtras().getString("matricula");
        mat = findViewById(R.id.textMat);
        prh = findViewById(R.id.textPh);
        col = findViewById(R.id.textCol);
        mar = findViewById(R.id.textMarc);
        bat = findViewById(R.id.textBat);
        est = findViewById(R.id.textsEtado);
        id = findViewById(R.id.textIdCarnet);
        editar = findViewById(R.id.editar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        executeCall(this);
    }

    @Override
    public void doInBackground() {
        vehiculo = Connector.getConector().get(Vehiculo.class, url + API.Routes.VEHICULO + "?matricula=" + matr);
    }

    @Override
    public void doInUI() {

        mat.setText("" + vehiculo.getMatricula());
        mar.setText("" + vehiculo.getMarca());
        prh.setText("" + vehiculo.getPreciohora());
        col.setText("" + vehiculo.getColor());
        bat.setText("" + vehiculo.getBateria());
        est.setText("" + vehiculo.getEstado());
        id.setText("" + vehiculo.getIdCarnet());

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String matricula,marca,desc,changedBy;
                int precioh,bateria,idCarnet;
                Color color;
                Estado estado;
                matricula= mat.getText().toString();
                precioh= Integer.parseInt(prh.getText().toString());
                marca= mar.getText().toString();
                desc= " ";
                color= Color.valueOf(col.getText().toString());
                bateria= Integer.parseInt(bat.getText().toString());
                estado= Estado.valueOf(est.getText().toString());
                idCarnet= Integer.parseInt(id.getText().toString());
                changedBy=" ";
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);

                try {
                    Integer.parseInt(prh.getText().toString());
                }catch (NumberFormatException nfe){
                    builder.setMessage("Debes introducir un numero en el precio")
                            .setTitle("Error")
                            .setPositiveButton("Ok",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                if (mar.equals("")) {
                    builder.setMessage("El campo de la marca del coche esta vacio")
                            .setTitle("Error")
                            .setPositiveButton("Ok",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if(!bat.getText().toString().matches("[0-9]+")){
                    builder.setMessage("Debes introducir una numero en la bateria")
                            .setTitle("Error")
                            .setPositiveButton("Ok",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else if (id.equals("")) {
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
                                   v = new Vehiculo(matricula, precioh, marca, desc, color, bateria, estado, idCarnet,changedBy,tv);
                                   result = Connector.getConector().put(Vehiculo.class, v, url + API.Routes.VEHICULO);
                               }

                               @Override
                               public void doInUI() {
                                   if (resultC instanceof Result.Success) {
                                       AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
                                       builder.setTitle("Actualizado")
                                               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                       finish();
                                                   }
                                               });
                                       AlertDialog alertDialog = builder.create();
                                       alertDialog.show();
                                   } else {
                                       Result.Error error = (Result.Error) resultC;
                                       AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
                                       builder.setMessage("Error "+error.getCode()+ ": "+error.getError())
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
                                    TipoVehiculo tv=TipoVehiculo.COCHE;
                                    v = new Vehiculo(matricula, precioh, marca, desc, color, bateria, estado, idCarnet,changedBy,tv);
                                    result = Connector.getConector().put(Vehiculo.class, v, url + API.Routes.VEHICULO);
                                }

                                @Override
                                public void doInUI() {
                                    if (resultC instanceof Result.Success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
                                        builder.setTitle("Actualizado")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    } else {
                                        Result.Error error = (Result.Error) resultC;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
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
                                    v = new Vehiculo(matricula, precioh, marca, desc, color, bateria, estado, idCarnet,changedBy,tv);
                                    result = Connector.getConector().put(Vehiculo.class, v, url + API.Routes.VEHICULO);
                                }

                                @Override
                                public void doInUI() {
                                    if (resultC instanceof Result.Success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
                                        builder.setTitle("Actualizado")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    } else {
                                        Result.Error error = (Result.Error) resultC;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
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
                                    v = new Vehiculo(matricula, precioh, marca, desc, color, bateria, estado, idCarnet,changedBy,tv);
                                    result = Connector.getConector().put(Vehiculo.class, v, url + API.Routes.VEHICULO);
                                }

                                @Override
                                public void doInUI() {
                                    if (resultC instanceof Result.Success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
                                        builder.setTitle("Actualizado")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    } else {
                                        Result.Error error = (Result.Error) resultC;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
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