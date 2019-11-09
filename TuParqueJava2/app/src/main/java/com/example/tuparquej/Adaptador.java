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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    private Context context;
    private ArrayList<Entidad> listItems;
    private Entidad item;

    public Adaptador(Context context, ArrayList<Entidad> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        //Numero de datos a cargar (en este caso 10 mas cercanos)
        return listItems.size();

    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item=(Entidad) getItem(position);

        convertView= LayoutInflater.from(context).inflate(R.layout.item, null);

        ImageButton btnFoto= (ImageButton)convertView.findViewById(R.id.buttonParque);
        TextView nombre=convertView.findViewById(R.id.textViewNombre);
        TextView barrio=convertView.findViewById(R.id.textViewBarrio);
        ImageView estrellas=convertView.findViewById(R.id.imageViewEstrellas);
        TextView distancia=convertView.findViewById(R.id.textViewDistancia);


        if(item.getImagen()!=null)
        {
            btnFoto.setBackground(null);
            Picasso.get().load(item.getImagen()).fit().into(btnFoto);
        }


        nombre.setText(item.getNombre());
        barrio.setText(item.getBarrio());

        double e=item.getEstrellas();
        if(e==0)
        {
            estrellas.setImageResource(R.drawable.eceroestrellas);
        }
        else if(e<=1)
        {
            estrellas.setImageResource(R.drawable.eunaestrella);
        }
        else if(e<=2)
        {
            estrellas.setImageResource(R.drawable.edosestrellas);
        }
        else if(e<=3)
        {
            estrellas.setImageResource(R.drawable.etresestrellas);
        }
        else if(e<=4)
        {
            estrellas.setImageResource(R.drawable.ecuatroestrellas);
        }
        else{
            estrellas.setImageResource(R.drawable.ecincoestrellas);
        }


        distancia.setText(item.getDistanciaActual()+" metros");
        btnFoto.setTag(position);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent =new Intent(context, parque.class);
                Bundle b =new Bundle();
                b.putInt("key",(Integer) v.getTag());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });



        return convertView;
    }



}
