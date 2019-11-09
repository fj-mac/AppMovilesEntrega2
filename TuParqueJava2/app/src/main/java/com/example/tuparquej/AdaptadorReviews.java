package com.example.tuparquej;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorReviews extends RecyclerView.Adapter<AdaptadorReviews.ViewHolder> {
    private Context context;
    private ArrayList<Review> lista;
    private Review item;

    public AdaptadorReviews(Context context, ArrayList<Review> listItems) {
        this.context = context;
        this.lista = listItems;
    }

    public int getItemCount() {
        return lista.size();
    }

    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.review,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item=(Review) getItem(position);

        holder.nombre.setText(item.getNombre());
        holder.review.setText(item.getReview());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView review;
        private View parentView;

        public ViewHolder(View view) {
            super(view);
            this.parentView=view;
            this.nombre=view.findViewById(R.id.textViewNombreP);
            this.review=view.findViewById(R.id.textViewReview);
        }
    }
}
