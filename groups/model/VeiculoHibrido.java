package model;

public class VeiculoHibrido {
    
    private int id;
    private String modelo;
    private int percentualBateria; // Foco na carga da bateria para a simulação

    public VeiculoHibrido(int id, String modelo, int percentualBateria) {
        this.id = id;
        this.modelo = modelo;
        this.percentualBateria = percentualBateria;
    }

    // Getters e Setters simples
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getPercentualBateria() { return percentualBateria; }
    public void setPercentualBateria(int percentualBateria) { this.percentualBateria = percentualBateria; }
}