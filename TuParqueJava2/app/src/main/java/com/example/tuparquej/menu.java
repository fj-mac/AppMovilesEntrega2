package com.example.tuparquej;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class menu extends AppCompatActivity {

    private ImageButton volver;
    private ImageButton sugerir;
    private ImageButton favoritos;
    private ImageButton encontrarParques;
    private ImageButton login;
    private ImageButton logout;
    public static ArrayList<Entidad> nuevaLista;
    private Snackbar mensaje;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference parques=db.collection("Parques");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        volver=findViewById(R.id.imageButtonVolver);
        sugerir=findViewById(R.id.imageButtonSugerir);
        favoritos=findViewById(R.id.imageButton4);
        encontrarParques=findViewById(R.id.imageView11);
        login=findViewById(R.id.imageButtonLogInMenu);
        logout=findViewById(R.id.imageButtonLogOutMenu);

        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                volver();
            }
        });
        sugerir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sugerir();
            }
        });
        favoritos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                verFavoritos();
            }
        });
        encontrarParques.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                verParques();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irALogin();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irALogOut();
            }
        });
        verificarLogIn();
    }
    private void volver(){
        finish();
    }
    private void sugerir(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Intent intent =new Intent(this, AgregarReview.class);
            startActivity(intent);
            finish();
        }
        else{
            //Snackbar snackbar = new Snackbar.make(findViewById(android.R.id.content), "Para realizar el LogIn debe tener conexion a internet. Verifique e intente mas tarde", Snackbar.LENGTH_INDEFINITE);
            //Snackbar snackbar =new Snackbar.make(this,"a",Snackbar.LENGTH_LONG);
            mensaje = Snackbar.make(findViewById(android.R.id.content), "Para sugerir debe tener conexión a internet. Verifique e intente más tarde", Snackbar.LENGTH_INDEFINITE);
            mensaje.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mensaje.dismiss();
                }
            });
            mensaje.show();

            //Toast.makeText(this, "Para sugerir debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }
    }
    private void verFavoritos(){
        //Cargar solo Favoritos
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            
            if(Login.user!=null)
            {
                nuevaLista=new ArrayList<>();
                for(int i=0;i<Home.listItems.size();i++)
                {
                    for (int j=0;j<Login.listaFavoritos.size();j++)
                    {
                        if(Home.listItems.get(i).getId()==Login.listaFavoritos.get(j))
                        {
                            nuevaLista.add(Home.listItems.get(i));
                        }
                    }

                }
                Login.parquesFavoritos=new ArrayList<>();
                Login.parquesFavoritos=nuevaLista;
                Login.dehome=1;
                finish();
            }
            else{
                mensaje = Snackbar.make(findViewById(android.R.id.content), "Debe iniciar sesión para poder ver sus favoritos", Snackbar.LENGTH_INDEFINITE);
                mensaje.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mensaje.dismiss();
                    }
                });
                mensaje.show();
                //Toast.makeText(this, "Debe iniciar sesión para poder ver sus favoritos", Toast.LENGTH_SHORT).show();
            }
            
        }
        else{
            mensaje = Snackbar.make(findViewById(android.R.id.content), "Para ver favoritos debe tener conexión a internet. Verifique e intente más tarde", Snackbar.LENGTH_INDEFINITE);
            mensaje.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mensaje.dismiss();
                }
            });
            mensaje.show();
            //Toast.makeText(this, "Para ver favoritos debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }



    }
    private void verParques(){
        Login.dehome=0;
        Intent intent =new Intent(menu.this, Home.class);
        startActivity(intent);
        finish();
    }
    public void verificarLogIn(){
        if(Login.user!=null)
        {
            login.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.VISIBLE);
        }
        else {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.INVISIBLE);
        }
    }
    public void irALogin(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Intent intent =new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
        else{
            //Snackbar snackbar = new Snackbar.make(findViewById(android.R.id.content), "Para realizar el LogIn debe tener conexion a internet. Verifique e intente mas tarde", Snackbar.LENGTH_INDEFINITE);
            //Snackbar snackbar =new Snackbar.make(this,"a",Snackbar.LENGTH_LONG);
            mensaje = Snackbar.make(findViewById(android.R.id.content), "Para realizar iniciar sesión debe tener conexión a internet. Verifique e intente más tarde", Snackbar.LENGTH_INDEFINITE);
            mensaje.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mensaje.dismiss();
                }
            });
            mensaje.show();
            //Toast.makeText(this, "Para realizar iniciar sesión debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }
    }
    public void irALogOut(){
        FirebaseAuth.getInstance().signOut();
        Login.user=null;
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
    }

}
