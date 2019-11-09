package com.example.tuparquej;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarReview extends AppCompatActivity {

    private static final String TAG = "AgregarReview";
    private static final String KEY_NOMBRE="nombre";
    private static final String KEY_REVIEW="review";

    private EditText editTextReview;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_review);
        editTextReview=findViewById(R.id.editText_Review);
    }

    public void saveReview(View v){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if(editTextReview.getText().toString().length()<10)
            {
                Toast.makeText(this, "Debe incluir al menos 10 caracteres", Toast.LENGTH_SHORT).show();
            }
            else{
                String nombre=null;
                if (Login.user!=null)
                {
                    nombre=Login.user.getDisplayName();
                }
                else{
                    nombre="Anonimo";
                }
                String review=editTextReview.getText().toString();
                Map<String, Object> note=new HashMap<>();
                note.put(KEY_NOMBRE,nombre);
                note.put (KEY_REVIEW, review);
                db.collection("Comentarios").add(note);
                Toast.makeText(this, "Se ha enviado su comentario", Toast.LENGTH_LONG).show();
                finish();
            }

        }
        else {
            Toast.makeText(this, "Debe tener conexión a internet", Toast.LENGTH_SHORT).show();
            finish();
        }



    }
}
