package com.example.tuparquej;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Button aceptarAuto;
    private Button aceptarManual;
    private TextView ciudadActual;
    private Button login;
    private Button logout;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String ciudad;
    public static Date startTime;
    public static LocationManager locationManager;
    public static double longitude=0;
    public static double latitude=0;
    private Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        aceptarAuto = (Button) findViewById(R.id.button);
        aceptarManual = (Button) findViewById(R.id.button2);
        login = (Button) findViewById(R.id.buttonLogIn);
        logout=(Button) findViewById(R.id.buttonLogOut);
        ciudadActual=(TextView) findViewById(R.id.textView3);
        startTime = new Date(System.currentTimeMillis());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        spinner=(Spinner) findViewById(R.id.editText);
        addItemsOnSpinner();
        new Thread(new Runnable() {
            public void run() {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

                onLocationChanged(location);
            }
        }).start();


        aceptarAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        aceptarManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome2();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogIn();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogOut();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Login.user!=null)
        {
            login.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.VISIBLE);
        }
        else
        {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            Date finish=new Date(System.currentTimeMillis());
            long tiempo=Math.abs(finish.getTime() - startTime.getTime());
            long diferencia= TimeUnit.MINUTES.convert(tiempo,TimeUnit.MILLISECONDS);

        }catch (Exception e){

        }
    }

    public void openHome(){

        if(ciudadActual.getText().toString().equals("Bogotá"))
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                Intent intent =new Intent(this, Home.class);
                intent.putExtra("VieneDe","home");
                startActivity(intent);
            }
            else{
                if(Home.listItems==null)
                {
                    Toast.makeText(this, "Actualmente no dispone del servicio de red. Por favor Intente más tarde", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent =new Intent(this, Home.class);
                    intent.putExtra("VieneDe","home");
                    startActivity(intent);
                }

            }
        }
        else {
            Toast.makeText(this, "Actualmente solo contamos con cobertura en Bogotá, Colombia", Toast.LENGTH_LONG).show();
        }


    }
    public void openHome2(){

        if(spinner.getSelectedItem().toString().equals("Bogotá"))
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                Intent intent =new Intent(this, Home.class);
                intent.putExtra("VieneDe","home");
                startActivity(intent);
            }
            else{
                if(Home.listItems==null)
                {
                    Toast.makeText(this, "Actualmente no dispone del servicio de red. Por favor Intente más tarde", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent =new Intent(this, Home.class);
                    intent.putExtra("VieneDe","home");
                    startActivity(intent);
                }

            }
        }
        else
        {
            Toast.makeText(this, "En este momento únicamente tenemos soporte en Bogotá. ", Toast.LENGTH_SHORT).show();
        }


    }
    public void openLogIn(){

        if(Login.user==null)
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                Intent intent =new Intent(this, Login.class);
                startActivity(intent);
            }
            else{
                //Snackbar snackbar = new Snackbar.make(findViewById(android.R.id.content), "Para realizar el LogIn debe tener conexion a internet. Verifique e intente mas tarde", Snackbar.LENGTH_INDEFINITE);
                //Snackbar snackbar =new Snackbar.make(this,"a",Snackbar.LENGTH_LONG);
                Toast.makeText(this, "Para iniciar sesión debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
            }
        }

    }
    public void openLogOut(){
        FirebaseAuth.getInstance().signOut();
        Login.user=null;
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);

    }

    public void getCurrentLocation(Location location){
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

    }
    private void addItemsOnSpinner(){
        List<String> lista=new ArrayList<String>();
        lista.add("Bogotá");
        lista.add("Cali");
        lista.add("Cartagena");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lista);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
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
    public void actualizarLocation()
    {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            getCiudad(location);
                        }
                        else
                        {
                            Log.d("YACAMBIO", "No sirvio");
                        }
                    }
                });
    }

    public void getCiudad(Location location){
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            ciudad=addresses.get(0).getLocality();
            ciudadActual.setText(ciudad);
            ciudadActual.setBackground(null);
            Log.d("YACAMBIO", "La ciudad es: "+ ciudad);

        }
        else {
            // do your stuff
            Log.d("YACAMBIO", "else no srivio");
        }
    }
    public void paraPoblarDatos(){
        Map<String, Object> horariosNuevo;


        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Parque Virrey");
        horariosNuevo.put("barrio","El virrey");
        horariosNuevo.put("details","Parque en la calle 87 entre la carrera 7 y la autopista. Ofrece diferentes servicios y es muy conocido por los eventos que allí se realizan.");
        horariosNuevo.put("estrellas",3);
        horariosNuevo.put("id",10);
        horariosNuevo.put("imagen","https://www.elespectador.com/sites/default/files/parque_el_virrey_5582_0.jpg");
        horariosNuevo.put("latitud",4.673878);
        horariosNuevo.put("longitud",-74.055714);
        db.collection("Parques").document(10+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Parque de la Pradera Norte");
        horariosNuevo.put("barrio","San Cristobal");
        horariosNuevo.put("details","Parque ubicado entre la calle 161 y la 164 y la carrera 16, cuenta con distintas adecuaciones como cancha de baloncesto");
        horariosNuevo.put("estrellas",4);
        horariosNuevo.put("id",11);
        horariosNuevo.put("imagen","https://www.metrocuadrado.com/noticias/sites/default/files/styles/imagen_carousel/public/field/gallery_image/parque-toberin.jpg?itok=oWqEiFHx");
        horariosNuevo.put("latitud",4.740526);
        horariosNuevo.put("longitud",-74.035192);
        db.collection("Parques").document(11+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Parque Altablanca");
        horariosNuevo.put("barrio","Cedritos");
        horariosNuevo.put("details","Parque ubicado al norte de Bogotá, cuenta con cancha de hockey");
        horariosNuevo.put("estrellas",4);
        horariosNuevo.put("id",12);
        horariosNuevo.put("imagen","https://www.metrocuadrado.com/noticias/sites/default/files/parque_altablanca_opt.jpg");
        horariosNuevo.put("latitud",4.734023);
        horariosNuevo.put("longitud",-74.028645);
        db.collection("Parques").document(12+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Rincón de los Andes");
        horariosNuevo.put("barrio","lejos");
        horariosNuevo.put("details","parque parque parque para hacer hacer hacer hacer ejercicio ejercicio ejercicio");
        horariosNuevo.put("estrellas",2);
        horariosNuevo.put("id",13);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipMjzvXNsW9g9yhPxiABxcCVAC3G-GsUW8ArI2E=w426-h240-k-no");
        horariosNuevo.put("latitud",4.685363);
        horariosNuevo.put("longitud",-74.066927);
        db.collection("Parques").document(13+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Urb. Calle 100");
        horariosNuevo.put("barrio","puente largo");
        horariosNuevo.put("details","pequeño, suficiente, descripcion del parque, recomendado, se puede ir a cualuiqer hora, cerca del carulla, cerca de los 3 elefantes");
        horariosNuevo.put("estrellas",4);
        horariosNuevo.put("id",14);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipN14WjMyQmqCmhLZrs9rr6qGWcv0UMSebYXRvKK=w408-h544-k-no");
        horariosNuevo.put("latitud",4.689053);
        horariosNuevo.put("longitud",-74.059887);
        db.collection("Parques").document(14+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Santa Margarita");
        horariosNuevo.put("barrio","Pasadena");
        horariosNuevo.put("details","Parque en barrio residencial bien quipado con esto y aquello");
        horariosNuevo.put("estrellas",4);
        horariosNuevo.put("id",15);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipNe68QGQw-UWdhvOqqlVzQad1eK5VWfVZHNKB3e=w433-h240-k-no");
        horariosNuevo.put("latitud",4.693591);
        horariosNuevo.put("longitud",-74.059119);
        db.collection("Parques").document(15+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Puente Largo");
        horariosNuevo.put("barrio","pasadena");
        horariosNuevo.put("details","Un barrio muy bonito con un parque muy bonito para que los niños jueguen jueguitos");
        horariosNuevo.put("estrellas",5);
        horariosNuevo.put("id",16);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipMMAY029obaiNThcbxKMffYQwckN68Ym1kMz6f2=w408-h310-k-no");
        horariosNuevo.put("latitud",4.694751);
        horariosNuevo.put("longitud",-74.058448);
        db.collection("Parques").document(16+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Parque Alhambra");
        horariosNuevo.put("barrio","Alhambra");
        horariosNuevo.put("details","parque muy grnde, con muchas cosas, muchos arbolitos, muy bonito, todo perfecto, huele un poco a medio dia por el caño");
        horariosNuevo.put("estrellas",3);
        horariosNuevo.put("id",17);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipPxSzAUqs0-rGY8H63TRGNPDO-Rk3a2UfxDhVHf=w408-h306-k-no");
        horariosNuevo.put("latitud",4.697291);
        horariosNuevo.put("longitud",-74.059939);
        db.collection("Parques").document(17+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","San patricio");
        horariosNuevo.put("barrio","Alhambra");
        horariosNuevo.put("details","grande, bonito, vale la pena ir., seguro, con arbolitos");
        horariosNuevo.put("estrellas",3);
        horariosNuevo.put("id",18);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipNa2z1fE1eYvDx2kJH8UapfrVMgAt2duQt0Eg1d=w427-h240-k-no");
        horariosNuevo.put("latitud",4.697409);
        horariosNuevo.put("longitud",-74.054049);
        db.collection("Parques").document(18+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Fryderyk Chopin");
        horariosNuevo.put("barrio","Chicó Navarra");
        horariosNuevo.put("details","muy bonito, acogedor, lindo, recomendado, pueden ir, pequeño, para los del barrio");
        horariosNuevo.put("estrellas",5);
        horariosNuevo.put("id",19);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipOkY3Xuwy6UFyrUS4UXONAubjSZeum_GgvZ5ZY-=w408-h306-k-no");
        horariosNuevo.put("latitud",4.692212);
        horariosNuevo.put("longitud",-74.05376);
        db.collection("Parques").document(19+"").set(horariosNuevo);
        horariosNuevo = new HashMap<>();
        horariosNuevo.put("nombre","Parque puente largo");
        horariosNuevo.put("barrio","puente largo");
        horariosNuevo.put("details","muuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuy grande, chevere, para ir con amigos, con hijos, con el que sea, a la hora que sea, gente bien");
        horariosNuevo.put("estrellas",3);
        horariosNuevo.put("id",20);
        horariosNuevo.put("imagen","https://lh5.googleusercontent.com/p/AF1QipMplF3PlvPIlNefQN8DC-Q-k4icBBZWIgW1jwAW=w408-h725-k-no");
        horariosNuevo.put("latitud",4.696109);
        horariosNuevo.put("longitud",-74.065556);
        db.collection("Parques").document(20+"").set(horariosNuevo);









    }

    @Override
    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        latitude=location.getLatitude();
        actualizarLocation();
        Log.d("dist", "La longitud es de: "+ longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
