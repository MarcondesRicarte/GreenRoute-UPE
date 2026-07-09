package model;

public abstract class Veiculo {
    private int id;
    private String modelo;
    private double autonomiaMaxima;
    private double cargaBateriaAtual; // em porcentagem (0 a 100)

    public Veiculo(int id, String modelo, double autonomiaMaxima, double cargaBateriaAtual) {
        this.id = id;
        this.modelo = modelo;
        this.autonomiaMaxima = autonomiaMaxima;
        this.cargaBateriaAtual = cargaBateriaAtual;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModelo() { return modelo; }
    public double getAutonomiaMaxima() { return autonomiaMaxima; }
    public double getCargaBateriaAtual() { return cargaBateriaAtual; }
    public void setCargaBateriaAtual(double cargaBateriaAtual) { this.cargaBateriaAtual = cargaBateriaAtual; }

    @Override
    public String toString() {
        return "ID: " + id + " | Modelo: " + modelo + " | Autonomia Max: " + autonomiaMaxima + "km | Carga: " + cargaBateriaAtual + "%";
    }
}
