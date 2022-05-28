package com.example.taserfan.base;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taserfan.API.API;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Coche;
import com.example.taserfan.Empleado;
import com.example.taserfan.R;
import java.util.List;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private final List<List> list;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private Context context;
    Result<Coche> result;

    public MyRecyclerViewAdapter(Context context, List<List> list){
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_vehiculos,parent,false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Llamar a la query:
        result = Connector.getConector().get(Coche.class, API.Routes.COCHES);

        //Mostrar resultados
        Result.Success<Coche> resultado = (Result.Success<Coche>) result;

        holder.vehiculo.setText("Coche");
        holder.matriucla.setText(resultado.getData().getMatricula());
        holder.datoA.setText(String.valueOf(resultado.getData().getNumPlazas()));
        holder.datoB.setText(String.valueOf(resultado.getData().getNumPuertas()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        EditText vehiculo;
        EditText matriucla;
        EditText datoA;
        EditText datoB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.icono);
            vehiculo = itemView.findViewById(R.id.tipoVvehiculo);
            matriucla = itemView.findViewById(R.id.textMatricula);
            datoA = itemView.findViewById(R.id.dato1);
            datoB = itemView.findViewById(R.id.dato2);
        }

    }

}
