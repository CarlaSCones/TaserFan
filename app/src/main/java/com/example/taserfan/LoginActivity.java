package com.example.taserfan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;
import com.example.taserfan.base.Inicio;

public class LoginActivity extends BaseActivity implements CallInterface, View.OnClickListener {

    EditText email;
    EditText pssword;
    Button button;
    Result<Empleado> result;
    Empleado empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.username);
        pssword=findViewById(R.id.password);
        button=findViewById(R.id.login);
        button.setOnClickListener(this);
    }

    @Override
    public void doInBackground() {

        String tEmail = email.getText().toString().trim();
        String tPassword = pssword.getText().toString().trim();

        //Llamar a la query:
        result = Connector.getConector().post(Empleado.class, new com.example.taserfan.base.AuthenticatonData(tEmail, tPassword), API.Routes.AUTHENTICATE);

    }

    @Override
    public void doInUI() {
        if(result instanceof Result.Success){
            LoggedInUserRepository.getInstance().login(((Result.Success<Empleado>) result).getData());
            Intent intent = new Intent(getApplicationContext(), Inicio.class);
            intent.putExtra("usuario" , empleado);
            startActivity(intent);

        } else if(result instanceof Result.Error){
            LoggedInUserRepository.getInstance().logout();
            Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.login)
            executeCall(this);
    }
}