package com.example.tuparquej;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAhora extends Fragment {
    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    public FragmentAhora() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new Thread(new Runnable() {
            public void run() {
                actualizarUsoAhora();
            }
        }).start();
        return inflater.inflate(R.layout.fragment_fragment_ahora, container, false);
    }
    public void actualizarUsoAhora(){
        try{

        }
        catch (Exception e)
        {

        }
       //DocumentSnapshot ds= db.collection("Data").document("uso").get("ahora")
    }

}
