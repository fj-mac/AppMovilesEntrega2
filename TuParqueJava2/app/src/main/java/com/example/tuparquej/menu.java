package com.example.tuparquej;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
            Toast.makeText(this, "Para sugerir debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }
    }
    private void verFavoritos(){
        //Cargar solo Favoritos
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            
            if(Login.user!=null)
            {
                parques.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e !=null){
                            Toast.makeText(menu.this, "Error entrando a base de datos", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Home.listItems=new ArrayList<>();
                        for(QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots){
                            try{
                                Entidad entid=documentSnapshots.toObject(Entidad.class);
                                if(Login.listaFavoritos.contains(entid.getId()))
                                {
                                    Home.listItems.add(entid);
                                    Toast.makeText(menu.this, "Se agrego los favoritos", Toast.LENGTH_SHORT).show();


                                }
                            }catch (Exception exep)
                            {
                                Toast.makeText(menu.this, "Parece que estamos teniendo problemas. Discúlpanos e intenta más tarde", Toast.LENGTH_SHORT).show();
                                
                            }

                        }
                        Intent intent =new Intent(menu.this, Home.class);
                        intent.putExtra("VieneDe","favoritos");
                        startActivity(intent);
                        finish();
                        //ver favoritos
                       
                    }
                });
            }
            else{
                Toast.makeText(this, "Debe iniciar sesión para poder ver sus favoritos", Toast.LENGTH_SHORT).show();
            }
            
        }
        else{

            Toast.makeText(this, "Para ver favoritos debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }



    }
    private void verParques(){
        Intent intent =new Intent(menu.this, Home.class);
        intent.putExtra("VieneDe","home");
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
            Toast.makeText(this, "Para realizar iniciar sesión debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }
    }
    public void irALogOut(){
        FirebaseAuth.getInstance().signOut();
        Login.user=null;
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
    }
}
