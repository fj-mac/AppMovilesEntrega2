package com.example.tuparquej;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class parqueDetails extends AppCompatActivity {

    private ImageButton reviews;
    private ImageButton ahora;
    private String nombre;
    private TextView nomb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parque_details);
        reviews=(ImageButton) findViewById(R.id.imageButtonReviews);
        ahora=(ImageButton) findViewById(R.id.imageButtonAhora);
        nomb=(TextView) findViewById(R.id.textView6);


        Bundle b=getIntent().getExtras();
        if(b!=null)
            nombre=b.getString("key");

        nomb.setText(nombre);

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviews();
            }
        });
        ahora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAhora();
            }
        });
    }

    public void openReviews(){
        Intent intent =new Intent(this, parqueReviews.class);
        startActivity(intent);
    }

    public void openAhora(){
        Intent intent =new Intent(this, parqueAhora.class);
        startActivity(intent);
    }

}
