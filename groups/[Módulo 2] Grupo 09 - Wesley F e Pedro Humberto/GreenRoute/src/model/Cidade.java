package model;

import exceptions.ValorInvalidoException;

public class Cidade {
    private int id;
    private String nome;
    private String estado;
    private double distanciaDaCapital;

    public Cidade(int id, String nome, String estado, double distanciaDaCapital) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        setDistanciaDaCapital(distanciaDaCapital);
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEstado() { return estado; }
    public double getDistanciaDaCapital() { return distanciaDaCapital; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEstado(String estado) { this.estado = estado; }

    public void setDistanciaDaCapital(double distanciaDaCapital) {
        if (distanciaDaCapital >= 0) {
            this.distanciaDaCapital = distanciaDaCapital;
        } else {
            throw new ValorInvalidoException("A distância não pode ser menor que 0.");
        }
    }

    @Override
    public String toString(){
        return nome + " - " + estado;
    }
}