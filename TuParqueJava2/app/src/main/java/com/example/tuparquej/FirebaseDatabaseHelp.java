package com.example.tuparquej;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelp {
    private FirebaseDatabase mDatabase;
    private static DatabaseReference mReferenceEntidad;
    private static List<Entidad> parques=new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Entidad> parques, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }
    public FirebaseDatabaseHelp() {
        mDatabase= FirebaseDatabase.getInstance();
        mReferenceEntidad=mDatabase.getReference("Parques");
    }
    public static void readEntidad(final DataStatus dataStatus){
        mReferenceEntidad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parques.clear();
                List<String> keys =new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Entidad ent=keyNode.getValue(Entidad.class);
                    parques.add(ent);

                }
                dataStatus.DataIsLoaded(parques,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
