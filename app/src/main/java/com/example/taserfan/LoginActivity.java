package com.example.taserfan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Clases.AuthenticatonData;
import com.example.taserfan.Clases.Empleado;
import com.example.taserfan.Preferencias.GestionPreferencias;
import com.example.taserfan.Preferencias.PreferenciasActivity;
import com.example.taserfan.Preferencias.ThemeSetup;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;

public class LoginActivity extends BaseActivity implements CallInterface, View.OnClickListener {

    ImageView imageView;
    EditText email;
    EditText password;
    Button buttonLogin;
    Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.username);
        password =findViewById(R.id.password);
        imageView = findViewById(R.id.imageUser);
        imageView.setImageResource(R.mipmap.usuario_launcher_foreground);
        buttonLogin =findViewById(R.id.login);
        buttonLogin.setOnClickListener(this);

        ThemeSetup.applyPreferenceTheme(getApplicationContext());

        GestionPreferencias.getInstance().getTheme(getApplicationContext());
        GestionPreferencias.getInstance().getIp(getApplicationContext());
        GestionPreferencias.getInstance().getPuerto(getApplicationContext());
    }

    @Override
    public void doInBackground() {
        String tEmail = email.getText().toString().trim();
        String tPassword = password.getText().toString().trim();
        result = Connector.getConector().postAuth(Empleado.class, new AuthenticatonData(tEmail, tPassword), API.Routes.AUTHENTICATE);
    }

    @Override
    public void doInUI() {
        if(result instanceof Result.Success){
            LoggedInUserRepository.getInstance().login(((Result.Success<Empleado>) result).getData());
            Intent intent = new Intent(getApplicationContext() , MainActivity.class);
            startActivity(intent);

        } else if(result instanceof Result.Error){
            Result.Error error=(Result.Error) result;
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Error " + error.getCode() + ": " + error.getError())
                   .setTitle("Error al iniciar")
                   .setPositiveButton("Ok", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        executeCall(this);
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