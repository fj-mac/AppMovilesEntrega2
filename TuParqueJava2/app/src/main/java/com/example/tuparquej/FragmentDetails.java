package com.example.tuparquej;


import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetails extends Fragment {



    public FragmentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment_details, container, false);
        TextView inform=(TextView) view.findViewById(R.id.textViewInfo);
        Bundle args = getArguments();
        String detail = args.getString("key");
        inform.setText(detail);
        // Inflate the layout for this fragment
        return view;
    }

}
