package com.example.tuparquej;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class parque extends AppCompatActivity {

    private ImageButton reviews;
    private ImageButton ahora;
    private ImageButton details;
    private ImageButton back;
    private ImageButton goMap;
    private ImageButton favorito;
    private ImageView imagenParque;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private Snackbar mensaje;

    //Parque seleccionado
    private int pos;
    private int id;

    //Atributos del parque a mostrar
    private String nombre;
    private String barrio;

    //Componentes mostrados de parque

    private TextView nomb;
    private TextView barr;
    private int verif;

    //Tabs de fragmentos
    private ImageView verdeDetails;
    private ImageView verdeReviews;
    private ImageView verdeAhora;
    Fragment fragment;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parque);

        verif=0;

        //Para info del parque
        nomb=(TextView) findViewById(R.id.textView6);
        barr=(TextView) findViewById(R.id.textView8);

        reviews=(ImageButton) findViewById(R.id.imageButtonReviews);
        ahora=(ImageButton) findViewById(R.id.imageButtonAhora);
        details=(ImageButton) findViewById(R.id.imageButtonDetails);
        verdeDetails=(ImageView) findViewById(R.id.imageView8);
        verdeReviews=(ImageView) findViewById(R.id.imageView9);
        verdeAhora=(ImageView) findViewById(R.id.imageView10);
        back=(ImageButton) findViewById(R.id.imageButtonBack);
        goMap=(ImageButton) findViewById(R.id.imageButtonGo);
        imagenParque=(ImageView) findViewById(R.id.imageView6);
        favorito=findViewById(R.id.imageButtonFavorito);



        Bundle b=getIntent().getExtras();
        if(b!=null)
        {
            pos=b.getInt("key");
        }
        final Entidad par=Home.listItems.get(pos);
        openDetails(par);
        id=par.getId();
        verificarFavoritoActual();
        nombre=par.getNombre();
        barrio=par.getBarrio();
        //edicion info
        nomb.setText(nombre);
        barr.setText(barrio);

        Picasso.get().load(par.getImagen()).fit().into(imagenParque);

        //Controla Fragmentos De Tabs
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails2(par);
            }
        });
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviews(par);
            }
        });
        ahora.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                openAhora(par);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                back();
            }
        });
        goMap.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                irAMapa(par);
            }
        });
        favorito.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                setFavorito();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentFocus()!=null)
        {
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void openReviews(Entidad par){

            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                verdeDetails.setVisibility(View.INVISIBLE);
                verdeReviews.setVisibility(View.VISIBLE);
                verdeAhora.setVisibility(View.INVISIBLE);

                details.setImageResource(R.drawable.detailsgris);
                ahora.setImageResource(R.drawable.ahora);
                reviews.setImageResource(R.drawable.reviewsverde);
                fragment=new FragmentReviews();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putInt("id", id);
                fragment.setArguments(args);
                ft.replace(R.id.frg,fragment);
                ft.commit();
            }
            else{
                mensaje = Snackbar.make(findViewById(android.R.id.content), "Para ver los reviews del parque se requiere conexión a internet", Snackbar.LENGTH_INDEFINITE);
                mensaje.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mensaje.dismiss();
                    }
                });
                mensaje.show();
                //Toast.makeText(this, "Para ver los reviews del parque se requiere conexión a internet", Toast.LENGTH_LONG).show();
            }


    }


    public void openAhora(Entidad par){
        if(Login.user!=null)
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                verdeDetails.setVisibility(View.INVISIBLE);
                verdeReviews.setVisibility(View.INVISIBLE);
                verdeAhora.setVisibility(View.VISIBLE);
                details.setImageResource(R.drawable.detailsgris);
                ahora.setImageResource(R.drawable.ahoraverde);
                reviews.setImageResource(R.drawable.reviews);

                fragment=new FragmentAhora();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putInt("id", id);
                fragment.setArguments(args);
                ft.replace(R.id.frg,fragment);
                ft.commit();
            }
            else{
                mensaje = Snackbar.make(findViewById(android.R.id.content), "Para ver el estado actual se requiere conexión a internet", Snackbar.LENGTH_INDEFINITE);
                mensaje.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mensaje.dismiss();
                    }
                });
                mensaje.show();
                //Toast.makeText(this, "Para ver el estado actual se requiere conexión a internet", Toast.LENGTH_LONG).show();
            }
        }
        else{
            mensaje = Snackbar.make(findViewById(android.R.id.content), "Para acceder a las funciones Premium debe iniciar seseón", Snackbar.LENGTH_INDEFINITE);
            mensaje.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mensaje.dismiss();
                }
            });
            mensaje.show();
            //Toast.makeText(this, "Para acceder a las funciones Premium debe iniciar sesión", Toast.LENGTH_LONG).show();
        }
    }
    public void openDetails2(Entidad par){


        verdeDetails.setVisibility(View.VISIBLE);
        verdeReviews.setVisibility(View.INVISIBLE);
        verdeAhora.setVisibility(View.INVISIBLE);

        details.setImageResource(R.drawable.details);
        ahora.setImageResource(R.drawable.ahora);
        reviews.setImageResource(R.drawable.reviews);

        Bundle args=new Bundle();
        args.putString("key",par.getDetails());

        fragment=new FragmentDetails();
        fragment.setArguments(args);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frg,fragment);
        ft.commit();

    }
    public void openDetails(Entidad par){

        Bundle args=new Bundle();
        args.putString("key",par.getDetails());

        fragment=new FragmentDetails();
        fragment.setArguments(args);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frg,fragment);
        ft.commit();

    }
    public void back(){

       finish();
    }

    public void irAMapa(Entidad par){

        double longit=par.getLongitud();
        double latid=par.getLatitud();
        String lugar="google.streetview:cbll="+longit+", "+latid;
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latid+","+longit);

            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
            else{
                if(verif==0)
                {
                    mensaje = Snackbar.make(findViewById(android.R.id.content), "No hay conexión a internet. Si tiene mapas offline, vuelva a presionar GO para continuar", Snackbar.LENGTH_INDEFINITE);
                    mensaje.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mensaje.dismiss();
                        }
                    });
                    mensaje.show();
                    //Toast.makeText(this, "No hay conexión a internet. Si tiene mapas offline, vuelva a presionar GO para continuar", Toast.LENGTH_LONG).show();
                    verif++;
                }
                else{
                    verif=0;
                    mensaje = Snackbar.make(findViewById(android.R.id.content), "Ha sido redireccionado a Maps Offline", Snackbar.LENGTH_INDEFINITE);
                    mensaje.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mensaje.dismiss();
                        }
                    });
                    mensaje.show();
                    //Toast.makeText(this, "Ha sido redireccionado a Maps Offline", Toast.LENGTH_LONG).show();
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }




    }
    public void verificarFavoritoActual(){
        if (Login.user!=null)
        {
                if(Login.listaFavoritos.contains(id)) {
                    favorito.setImageResource(R.drawable.corazonlleno);
                }
        }
    }
    public void setFavorito(){
        if (Login.user!=null)
        {
            final String usuario=Login.user.getUid();
            Map<String, Object> note=new HashMap<>();
            String idString=id+"";

            if(Login.listaFavoritos.contains(id))
            {
                favorito.setImageResource(R.drawable.corazonvacio);
                Login.listaFavoritos.remove((Object) id);
                note.put(id+"", FieldValue.delete());
                db.collection("Usuarios").document(usuario).update(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                verificarFavoritos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mensaje = Snackbar.make(findViewById(android.R.id.content), "Parece que tenemos un problema. Inténtalo más tarde", Snackbar.LENGTH_INDEFINITE);
                                mensaje.setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mensaje.dismiss();
                                    }
                                });
                                mensaje.show();
                                //Toast.makeText(parque.this, "Parece que tenemos un problema. Inténtalo más tarde", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else{
                note.put(idString,id);
                if(Login.listaFavoritos.isEmpty())
                {
                    db.collection("Usuarios").document(usuario).set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    verificarFavoritos();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mensaje = Snackbar.make(findViewById(android.R.id.content), "Parece que tenemos un problema. Inténtalo más tarde", Snackbar.LENGTH_INDEFINITE);
                                    mensaje.setAction("Ok", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mensaje.dismiss();
                                        }
                                    });
                                    mensaje.show();
                                    //Toast.makeText(parque.this, "Parece que tenemos un problema. Inténtalo más tarde" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    db.collection("Usuarios").document(usuario).update(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    verificarFavoritos();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mensaje = Snackbar.make(findViewById(android.R.id.content), "Parece que tenemos un problema. Inténtalo más tarde", Snackbar.LENGTH_INDEFINITE);
                                    mensaje.setAction("Ok", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mensaje.dismiss();
                                        }
                                    });
                                    mensaje.show();
                                    //Toast.makeText(parque.this, "Parece que tenemos un problema. Inténtalo más tarde", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }

        }
        else
        {
            //Que hacer si el usuario no se encuentra logueado
            mensaje = Snackbar.make(findViewById(android.R.id.content), "Para poder agregar favoritos debe iniciar sesión", Snackbar.LENGTH_INDEFINITE);
            mensaje.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mensaje.dismiss();
                }
            });
            mensaje.show();
            //Toast.makeText(this, "Para poder agregar favoritos debe iniciar sesión", Toast.LENGTH_LONG).show();

        }



    }
    public void verificarFavoritos(){
        final DocumentReference favoritos=db.collection("Usuarios").document(Login.user.getUid());
        favoritos.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                if(e !=null){
                    return;
                }
                Map<String, Object> map = ds.getData();
                Login.listaFavoritos=new ArrayList<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    int parque=Integer.parseInt(entry.getValue().toString());
                    Login.listaFavoritos.add(parque);
                    Log.d("Guardando","Lo que esta guardando es: "+parque);

                }
                verificarFavoritoActual();
            }
        });

    }


}
