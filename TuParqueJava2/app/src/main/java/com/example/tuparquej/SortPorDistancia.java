package com.example.tuparquej;
import java.util.ArrayList;
import java.util.Collections;

public class SortPorDistancia {
    ArrayList<Entidad> parques=new ArrayList<>();

    public SortPorDistancia(ArrayList<Entidad> parques) {
        this.parques = parques;
    }

    public ArrayList<Entidad> getSortByDistance() {
            Collections.sort(parques);
            return parques;
    }
}

