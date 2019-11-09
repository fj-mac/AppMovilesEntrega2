package com.example.tuparquej;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity {
    private ListView lvItems;
    private Adaptador adaptador;
    private ImageButton menu;
    private ImageButton sort;
    public static ArrayList<Entidad> listItems;

    private static final String TAG = "Home";
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference parques=db.collection("Parques");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        menu=findViewById(R.id.imageButtonIrAMenu);
        lvItems=findViewById(R.id.lvItems);
        sort=(ImageButton)findViewById(R.id.imageView3);


        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irAMenu();
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ordenar();
            }
        });

        //mes
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            Date finish=new Date(System.currentTimeMillis());
            long tiempo=Math.abs(finish.getTime() - MainActivity.startTime.getTime());
            long diferencia= TimeUnit.MINUTES.convert(tiempo,TimeUnit.MILLISECONDS);
            Map<String, Object> horariosNuevo;


            horariosNuevo = new HashMap<>();
            horariosNuevo.put("tiempo",diferencia);
            db.collection("TiempoDeUso").document().set(horariosNuevo);

        }catch (Exception e){

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent myIntent = getIntent(); // gets the previously created intent
        String mensajeDe = myIntent.getStringExtra("VieneDe");
        Log.d("ACA","es: "+mensajeDe);
        if(mensajeDe.equals("home"))
        {
            parques.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e !=null){

                        Log.d(TAG, e.toString());
                        return;
                    }
                    listItems=new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots){
                        try{
                            Entidad entid=documentSnapshots.toObject(Entidad.class);
                            entid.calcularDistancia();
                            listItems.add(entid);
                        }catch (Exception w)
                        {

                        }

                    }
                    adaptador=new Adaptador(Home.this,listItems);
                    lvItems.setAdapter(adaptador);

                }
            });
        }
        else{
            adaptador=new Adaptador(Home.this,listItems );
            lvItems.setAdapter(adaptador);
        }
    }



    //Metodo de carga de parques

    private ArrayList<Entidad> getArrayList(){
        return listItems;
    }

    private void irAMenu(){
        Intent intent =new Intent(this, menu.class);
        startActivity(intent);
    }
    private void ordenar(){

        SortPorDistancia sort=new SortPorDistancia(listItems);
        listItems=sort.getSortByDistance();
        adaptador=null;
        adaptador=new Adaptador(Home.this,listItems );
        lvItems.setAdapter(adaptador);

    }
    public void cargarParques(){

        parques.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots){
                            Entidad entid=documentSnapshots.toObject(Entidad.class);
                            listItems.add(entid);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, e.toString());
                    }
                });

    }

}
