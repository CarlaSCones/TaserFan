package com.example.taserfan.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taserfan.Clases.TipoVehiculo;
import com.example.taserfan.R;
import com.example.taserfan.Clases.Vehiculo;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Vehiculo> list;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private Context context;

    public MyRecyclerViewAdapter(Context context, List<Vehiculo> list){
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
        Vehiculo v = list.get(position);
        TipoVehiculo t= v.getTipoVehiculo();
        String c = String.valueOf(v.getColor());
        holder.matricula.setText(v.getMatricula());
        holder.marca.setText(v.getMarca());
        holder.precio.setText(v.getPreciohora() + " ");

        switch (t){
            case COCHE:
                holder.imagen.setImageResource(R.mipmap.coche_launcher_foreground);
                break;
            case MOTO:
                holder.imagen.setImageResource(R.mipmap.moto_launcher_foreground);
                break;
            case BICICLETA:
                holder.imagen.setImageResource(R.mipmap.bicicleta_launcher_foreground);
                break;
            case PATINETE:
                holder.imagen.setImageResource(R.mipmap.patinete_launcher_foreground);
                break;
        }

        switch (c){
            case "rojo":
                holder.imagen.setColorFilter(ContextCompat.getColor(context,R.color.rojo));
                break;
            case "amarillo":
                holder.imagen.setColorFilter(ContextCompat.getColor(context,R.color.amarillo));
                break;
            case "verde":
                holder.imagen.setColorFilter(ContextCompat.getColor(context,R.color.verde));
                break;
                case "azul":
                    holder.imagen.setColorFilter(ContextCompat.getColor(context, R.color.azul));
                break;
            case "negro":
                holder.imagen.setColorFilter(ContextCompat.getColor(context,R.color.negro));
                break;
            case "blanco":
                holder.imagen.setColorFilter(ContextCompat.getColor(context,R.color.blanco));
                break;


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setListener(View.OnClickListener listener){
        this.onClickListener=listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNewData(List<Vehiculo> vehiculo) {
        this.list =vehiculo;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        EditText matricula;
        EditText marca;
        EditText precio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.icono);
            matricula = itemView.findViewById(R.id.matricula);
            marca = itemView.findViewById(R.id.textMarca);
            precio = itemView.findViewById(R.id.textPrecio);
        }

    }

}
