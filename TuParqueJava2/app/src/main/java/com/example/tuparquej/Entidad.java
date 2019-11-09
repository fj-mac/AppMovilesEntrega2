package com.example.tuparquej;

import android.util.Log;

public class Entidad implements Comparable <Entidad> {
    private String imagen;
    private int id;
    private String nombre;
    private String barrio;
    private String details;
    private int estrellas;
    private double latitud;
    private double longitud;
    private int distanciaActual;

    public Entidad() {
    }

    public Entidad(int id, String imagen, String nombre, String barrio, String details, int estrellas, double latitud, double longitud) {
        this.id=id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.barrio = barrio;
        this.details = details;
        this.estrellas = estrellas;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getDistanciaActual() {
        return distanciaActual;
    }

    public void setDistanciaActual(int distanciaActual) {
        this.distanciaActual = distanciaActual;
        Log.d("Distancia Actual", "La distancia del parque: "+ nombre + " es de: "+distanciaActual);
    }
    @Override
    public int compareTo(Entidad o) {
        return this.distanciaActual - o.distanciaActual;
    }
    public void calcularDistancia(){
        int dist=distance(MainActivity.latitude,this.getLatitud(), MainActivity.longitude, this.getLongitud());
        setDistanciaActual(dist);
    }
    public int distance(double lat1, double lat2, double lon1,
                               double lon2) {
        if(lat1==0)
        {
            lat1=4.602834;
            lon1=-74.064783;
        }
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return (int) dist;

    }
}
