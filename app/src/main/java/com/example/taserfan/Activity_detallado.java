package com.example.taserfan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Clases.Vehiculo;
import com.example.taserfan.Preferencias.PreferenciasActivity;
import com.example.taserfan.Preferencias.ThemeSetup;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;

import java.io.Serializable;
import java.util.List;

public class Activity_detallado extends BaseActivity implements CallInterface, Serializable {

    List<Vehiculo> vehiculos;
    Vehiculo vehiculo = null;
    Result<Vehiculo> vehiculoResult;
    int index;

    ImageView imageV;
    TextView matricula, tMarca, tColor, tBateria, tCarnet, tEstado, tFecha, precio;
    EditText editTextMarca, editTextBateria, editTextPrecio;
    Button editar,button;
    Result result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallado);

        index = getIntent().getExtras().getInt("index", 0);
        vehiculos = (List<Vehiculo>) getIntent().getExtras().getSerializable("lista");

        imageV = findViewById(R.id.imageVehiculo);
        matricula = findViewById(R.id.textMatricula);
        tMarca = findViewById(R.id.textMarca);
        tColor = findViewById(R.id.textColor);
        tCarnet = findViewById(R.id.textId);
        tBateria = findViewById(R.id.textBateria);
        tEstado = findViewById(R.id.textEstado);
        tFecha = findViewById(R.id.textFecha);
        precio = findViewById(R.id.textPrecioHora);

        editTextMarca = findViewById(R.id.editTextMarc);
        editTextBateria = findViewById(R.id.editTextBat);
        editTextPrecio = findViewById(R.id.editTextPrecioHora);

        editar = findViewById(R.id.editar);
        button = findViewById(R.id.updateokbtn);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextMarca.setText(tMarca.getText());
                editTextMarca.setVisibility(View.VISIBLE);
                tMarca.setVisibility(View.GONE);

                editTextBateria.setText(tBateria.getText());
                editTextBateria.setVisibility(View.VISIBLE);
                tBateria.setVisibility(View.GONE);

                editTextPrecio.setText(precio.getText().toString().substring(0, precio.getText().toString().length()-1));
                editTextPrecio.setVisibility(View.VISIBLE);
                precio.setVisibility(View.GONE);

            }
        });
        editar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextMarca.getText().toString().isEmpty() && !editTextBateria.getText().toString().isEmpty()) {

                    vehiculo = new Vehiculo(vehiculo.getMatricula(), Integer.parseInt(editTextPrecio.getText().toString()), editTextMarca.getText().toString(), vehiculo.getDescripcion(), vehiculo.getColor(), Integer.parseInt(editTextBateria.getText().toString()),vehiculo.getFechaadq(), vehiculo.getEstado(), vehiculo.getIdCarnet(),vehiculo.getTipoVehiculo());

                    executeCall(new CallInterface() {
                        @Override
                        public void doInBackground() {
                            result = Connector.getConector().put(Vehiculo.class, vehiculo, API.Routes.VEHICULO);

                        }

                        @Override
                        public void doInUI() {
                            if (result instanceof Result.Success) {
                                tMarca.setText(editTextMarca.getText());
                                tMarca.setVisibility(View.VISIBLE);
                                editTextMarca.setVisibility(View.GONE);

                                tBateria.setText(editTextBateria.getText());
                                tBateria.setVisibility(View.VISIBLE);
                                editTextBateria.setVisibility(View.GONE);

                                precio.setText(editTextPrecio.getText().toString() + "€");
                                precio.setVisibility(View.VISIBLE);
                                editTextPrecio.setVisibility(View.GONE);

                                editar.setVisibility(View.VISIBLE);
                                button.setVisibility(View.GONE);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
                                builder.setMessage("No se pueden actualizar los datos")
                                        .setTitle("Error al actualizar")
                                        .setPositiveButton("Ok", null);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_detallado.this);
                    builder.setMessage("Hay campos vacios ")
                            .setTitle("Error al actualizar")
                            .setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        executeCall(this);
        ThemeSetup.applyPreferenceTheme(getApplicationContext());

    }

    @Override
    public void doInBackground() {
        vehiculo= vehiculos.get(index);
        vehiculoResult = Connector.getConector().get(Vehiculo.class, API.Routes.VEHICULO + "?matricula=" + vehiculo.getMatricula());

    }

    @Override
    public void doInUI() {

        matricula.setText(vehiculo.getMatricula());
        tMarca.setText(vehiculo.getMarca());
        tColor.setText(vehiculo.getColor().getStringColor());
        tCarnet.setText(vehiculo.getIdCarnet());
        tBateria.setText(vehiculo.getBateria());
        tEstado.setText(vehiculo.getEstado().getStringEstado());
        tFecha.setText(vehiculo.getFechaadq().toString());
        precio.setText(vehiculo.getPrecioHora() + "€");
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