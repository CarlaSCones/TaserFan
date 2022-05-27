package com.example.taserfan;

public class Vehiculo {

    private String nombre;
    private String matricula;
    private float preciohora;
    private String marca;
    private String color;

    public Vehiculo(String nombre, String matricula, float preciohora, String marca, String color) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.preciohora = preciohora;
        this.marca = marca;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public float getPreciohora() {
        return preciohora;
    }

    public void setPreciohora(float preciohora) {
        this.preciohora = preciohora;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
