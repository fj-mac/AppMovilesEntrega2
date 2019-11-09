package com.example.tuparquej;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class parqueAhora extends AppCompatActivity {

    private ImageButton reviews;
    private ImageButton details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parque_ahora);

        reviews=(ImageButton) findViewById(R.id.imageButtonReviews);
        details=(ImageButton) findViewById(R.id.imageButtonDetails);

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviews();
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails();
            }
        });
    }

    public void openReviews(){
        Intent intent =new Intent(this, parqueReviews.class);
        startActivity(intent);
    }

    public void openDetails(){
        Intent intent =new Intent(this, parqueDetails.class);
        startActivity(intent);
    }

}
