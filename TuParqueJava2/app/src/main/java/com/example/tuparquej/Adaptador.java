package com.example.tuparquej;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {
    private Context context;
    private ArrayList<Entidad> listItems;
    private Entidad item;

    public Adaptador(Context context, ArrayList<Entidad> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getItemCount() {
        //Numero de datos a cargar (en este caso 10 mas cercanos)
        return listItems.size();

    }

    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item=(Entidad) getItem(position);
        if(item.getImagen()!=null)
        {
            holder.btnFoto.setBackground(null);
            Picasso.get().load(item.getImagen()).fit().into(holder.btnFoto);
        }


        holder.nombre.setText(item.getNombre());
        holder.barrio.setText(item.getBarrio());

        double e=item.getEstrellas();
        if(e==0)
        {
            holder.estrellas.setImageResource(R.drawable.eceroestrellas);
        }
        else if(e<=1)
        {
            holder.estrellas.setImageResource(R.drawable.eunaestrella);
        }
        else if(e<=2)
        {
            holder.estrellas.setImageResource(R.drawable.edosestrellas);
        }
        else if(e<=3)
        {
            holder.estrellas.setImageResource(R.drawable.etresestrellas);
        }
        else if(e<=4)
        {
            holder.estrellas.setImageResource(R.drawable.ecuatroestrellas);
        }
        else{
            holder.estrellas.setImageResource(R.drawable.ecincoestrellas);
        }


        holder.distancia.setText(item.getDistanciaActual()+" metros");

        holder.btnFoto.setTag(position);
        holder.btnFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent =new Intent(context, parque.class);
                Bundle b =new Bundle();
                b.putInt("key",(Integer) v.getTag());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton btnFoto;
        TextView nombre;
        TextView barrio;
        ImageView estrellas;
        TextView distancia;

        public ViewHolder(View view) {
            super(view);
            btnFoto= (ImageButton)view.findViewById(R.id.buttonParque);
            nombre=view.findViewById(R.id.textViewNombre);
            barrio=view.findViewById(R.id.textViewBarrio);
            estrellas=view.findViewById(R.id.imageViewEstrellas);
            distancia=view.findViewById(R.id.textViewDistancia);
        }
    }


}
