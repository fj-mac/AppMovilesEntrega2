package com.example.tuparquej;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class parqueReviews extends AppCompatActivity {

    private ImageButton details;
    private ImageButton ahora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parque_reviews);
        details=(ImageButton) findViewById(R.id.imageButtonDetails);
        ahora=(ImageButton) findViewById(R.id.imageButtonAhora);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails();
            }
        });
        ahora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAhora();
            }
        });
    }

    public void openDetails(){
        Intent intent =new Intent(this, parqueDetails.class);
        startActivity(intent);
    }

    public void openAhora(){
        Intent intent =new Intent(this, parqueAhora.class);
        startActivity(intent);
    }
    
}
