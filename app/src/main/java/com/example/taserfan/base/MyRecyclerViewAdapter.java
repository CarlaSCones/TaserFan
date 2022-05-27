package com.example.taserfan.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taserfan.API.API;
import com.example.taserfan.API.AuthenticatonData;
import com.example.taserfan.API.Connector;
import com.example.taserfan.API.Result;
import com.example.taserfan.Empleado;
import com.example.taserfan.R;
import com.example.taserfan.Vehiculo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private final List<List> list;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private Context context;
    EditText vehiculo;
    EditText matriucla;
    EditText precio;
    EditText marca;
    EditText color;
    Result<Empleado> result;


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

        String tVehiculo = vehiculo.getText().toString().trim();
        String tMatricula = matriucla.getText().toString().trim();
        String tPrecio = precio.getText().toString().trim();
        String tMarca = marca.getText().toString().trim();
        String tColor = color.getText().toString().trim();

        //Llamar a la query:
        result = Connector.getConector().get(Vehiculo.class, new AuthenticatonData(tEmail, tPassword), API.Routes.AUTHENTICATE);

        //Lamadas a la bd
        String url = Parameters.ICON_URL_PRE + list.get(position).weather.get(0).icon + Parameters.ICON_URL_POST;
        ImageDownloader.downloadImage( url,holder.image);
        Date date = new Date((long)list.get(position).dt*1000);
        SimpleDateFormat dayFor = new SimpleDateFormat("EEEE",new Locale( "es" , "ES" ));
        SimpleDateFormat timeFor = new SimpleDateFormat("hh:mm");
        SimpleDateFormat dateFor = new SimpleDateFormat("dd/MM/yyyy");
        String stringDay= dayFor.format(date);
        String stringTime = timeFor.format(date);
        holder.day.setText(stringDay);
        holder.date.setText(dateFor.format(date));
        holder.time.setText(stringTime);
        holder.description.setText(String.valueOf(list.get(position).weather.get(0).description));
        holder.tmax.setText(String.valueOf(list.get(position).main.temp_max).concat("º"));
        holder.tmin.setText(String.valueOf(list.get(position).main.temp_min).concat("º"));
        holder.temp.setText(String.valueOf(list.get(position).main.temp).concat("º"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView day;
        TextView date;
        TextView time;
        TextView description;
        TextView temp;
        TextView tmax;
        TextView tmin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagen);
            day = itemView.findViewById(R.id.dia);
            date = itemView.findViewById(R.id.textDia);
            description = itemView.findViewById(R.id.descripcion);
            time = itemView.findViewById(R.id.hora);
            temp = itemView.findViewById(R.id.temp);
            tmax = itemView.findViewById(R.id.max);
            tmin = itemView.findViewById(R.id.min);
        }

    }

}
