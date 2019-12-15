package com.example.tuparquej;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAhora extends Fragment {
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private TextView calidadAire;
    private TextView comentarioCalidadAire;
    private TextView seguridadActual;
    private int index;


    public FragmentAhora() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        index = args.getInt("id", 0);
        View view=inflater.inflate(R.layout.fragment_fragment_ahora, container, false);
        calidadAire= (TextView) view.findViewById(R.id.calidadDeAireActual);
        comentarioCalidadAire=(TextView) view.findViewById(R.id.comentarioCalidadAire);
        seguridadActual=(TextView) view.findViewById(R.id.seguridadActual);


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String temp =now+"";
        temp=temp.substring(0, 10);
        DocumentReference calidadDelAireDoc=db.collection("Parques").document(index+"").collection("calidadAire").document(temp+"");
        calidadDelAireDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String calidadAireActualEnParque = document.getString("calidad");
                        int numeroCalidad =Integer.parseInt(calidadAireActualEnParque);
                        calidadAire.setText("La calidad de aire actual es: "+calidadAireActualEnParque);
                        if(numeroCalidad <=50)
                        {
                            comentarioCalidadAire.setText("Buena: No se anticipan impactos a la salud cuando la calidad del aire se encuentra en este intervalo.");
                        }
                        else if(numeroCalidad <=100)
                        {
                            comentarioCalidadAire.setText("Moderada: Las personas extraordinariamente sensitivas deben considerar limitación de los esfuerzos físicos excesivos y prolongados al aire libre.");
                        }
                        else if(numeroCalidad <=150)
                        {
                            comentarioCalidadAire.setText("Dañina a la Salud de los Grupos Sensitivos: Los niños y adultos activos, y personas con enfermedades respiratorias tales como el asma, deben evitar los esfuerzos físicos excesivos y prolongados al aire libre.");
                        }
                        else if(numeroCalidad <=200)
                        {
                            comentarioCalidadAire.setText("Dañina a la Salud: Los niños y adultos activos, y personas con enfermedades respiratorias tales como el asma, deben evitar los esfuerzos excesivos prolongados al aire libre; las demás personas, especialmente los niños, deben limitar los esfuerzos físicos excesivos y prolongados al aire libre.");
                        }
                        else if(numeroCalidad <=300)
                        {
                            comentarioCalidadAire.setText("Muy Dañina a la Salud: Los niños y adultos activos, y personas con enfermedades respiratorias tales como el asma, deben evitar todos los esfuerzos excesivos al aire libre; las demás personas, especialmente los niños, deben limitar los esfuerzos físicos excesivos al aire libre.");
                        }
                        else
                        {
                            comentarioCalidadAire.setText("Arriesgado: Es muy arriesgado salir actualmente");
                        }

                    } else {
                        Log.d("LOGGER", "Documento nulo ", task.getException());
                    }
                } else {
                    Log.d("LOGGER", "Se jodio en doc ", task.getException());
                }
            }
        });



        DocumentReference seguridad=db.collection("Ahora").document("seguridad");
        seguridad.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String nivelSeguridadActual = document.getString(index+"");
                        seguridadActual.setText("El nivel de seguridad actual es de: "+ nivelSeguridadActual);

                    } else {
                        Log.d("LOGGER", "Documento nulo ", task.getException());
                    }
                } else {
                    Log.d("LOGGER", "Se jodio en doc ", task.getException());
                }
            }
        });


        
        //seguridadActual.setText("el numero obtenido es: "+ calidadAireActualEnParque[0]+ "que al traducirlo queda: "+ numeroCalidad);



        return view;

    }




}
