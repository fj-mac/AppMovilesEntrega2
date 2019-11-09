package com.example.tuparquej;

public class Review {
    private String nombre;
    private String review;

    public Review() {
    }

    public Review(String nombre, String review) {
        this.nombre = nombre;
        this.review = review;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
