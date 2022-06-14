package com.example.taserfan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taserfan.Clases.Estado;
import com.example.taserfan.Clases.TipoVehiculo;
import com.example.taserfan.Clases.Vehiculo;

import java.io.Serializable;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements Serializable {

    private List<Vehiculo> list;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private final Context context;

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
        Estado e = v.getEstado();
        //String c = String.valueOf(v.getColor());
        holder.matricula.setText(v.getMatricula());
        holder.descripcion.setText(v.getDescripcion());
        holder.color.setText(v.getColor() + " ");

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

        switch (e){
            case TALLER:
                holder.imagenEstado.setColorFilter(ContextCompat.getColor(context,R.color.rojo));
                break;
            case BAJA:
                holder.imagenEstado.setColorFilter(ContextCompat.getColor(context,R.color.amarillo));
                break;
            case PREPARADO:
                holder.imagenEstado.setColorFilter(ContextCompat.getColor(context,R.color.verde));
                break;
            case RESERVADO:
                    holder.imagenEstado.setColorFilter(ContextCompat.getColor(context, R.color.azul));
                break;
            case ALQUILADO:
                holder.imagenEstado.setColorFilter(ContextCompat.getColor(context,R.color.morado));
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
        ImageView imagenEstado;
        TextView matricula;
        TextView descripcion;
        TextView color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.icono);
            imagenEstado = itemView.findViewById(R.id.imagenEstado);
            matricula = itemView.findViewById(R.id.matricula);
            descripcion = itemView.findViewById(R.id.descripcion);
            color = itemView.findViewById(R.id.color);
        }

    }

}
