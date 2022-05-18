package com.example.taserfan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.base.AuthenticatonData;
import com.example.taserfan.base.BaseActivity;
import com.example.taserfan.base.CallInterface;

public class LoginActivity extends BaseActivity implements CallInterface, View.OnClickListener {

    EditText email;
    EditText pssword;
    Button button;
    Result<Empleado> result;

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
    protected void onResume() {
        super.onResume();
        executeCall(this);
    }

    @Override
    public void doInBackground() {
        result = Connector.getConector().post(Empleado.class, new AuthenticatonData("pepa@mordo.es", "1111"), API.Routes.AUTHENTICATE);
    }

    @Override
    public void doInUI() {
        Toast.makeText(this,"", Toast.LENGTH_SHORT).show();
        if(result instanceof Result.Success){
            LoggedInUserRepository.getInstance().login(((Result.Success<Empleado>) result).getData());
            Result.Success<Empleado> resultado = (Result.Success<Empleado>) result;
            Toast.makeText(this, resultado.getData().getNombre(), Toast.LENGTH_SHORT).show();

        }else if(result instanceof Result.Error){
            Toast.makeText(this, "No se ha podido logear", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.login){
            String tEmail = email.getText().toString().trim();
            String tPassword = pssword.getText().toString().trim();

           /* result = Connector.getConector().post(Empleado.class, new AuthenticatonData(tEmail, tPassword), "/authenticate");
            if(result instanceof Result.Success){
                LoggedInUserRepository.getInstance().login(((Result.Success<Empleado>) result).getData());
                Result.Success<Empleado> resultado = (Result.Success<Empleado>) result;
                Toast.makeText(this, resultado.getData().getNombre(), Toast.LENGTH_SHORT).show();

            } else if(result instanceof Result.Error){
                LoggedInUserRepository.getInstance().logout();
                Toast.makeText(this, "No se ha podido logear", Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}