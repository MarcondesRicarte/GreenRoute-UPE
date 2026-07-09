package model;

import exceptions.ValorInvalidoException;

public abstract class Veiculo {
    private int id;
    private String modelo;
    private double autonomiaMaxima;
    private double cargaBateriaAtual;
    private double consumoKwhPorKm;
    private int tempoRecargaCompleta;

    public Veiculo(int id, String modelo, double autonomiaMaxima, double cargaBateriaAtual, double consumoKwhPorKm, int tempoRecargaCompleta) {
        this.id = id;
        this.modelo = modelo;
        setAutonomiaMaxima(autonomiaMaxima);
        setCargaBateriaAtual(cargaBateriaAtual);
        setConsumoKwhPorKm(consumoKwhPorKm);
        setTempoRecargaCompleta(tempoRecargaCompleta);
    }

    // Getters
    public int getId() { return id; }
    public String getModelo() { return modelo; }
    public double getAutonomiaMaxima() { return autonomiaMaxima; }
    public double getCargaBateriaAtual() { return cargaBateriaAtual; }
    public double getConsumoKwhPorKm() { return consumoKwhPorKm; }
    public int getTempoRecargaCompleta() { return tempoRecargaCompleta; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public void setAutonomiaMaxima(double autonomiaMaxima) {
        if (autonomiaMaxima > 0) {
            this.autonomiaMaxima = autonomiaMaxima;
        } else {
            throw new ValorInvalidoException("Autonomia não pode ser menor ou igual a 0.");
        }
    }

    public void setCargaBateriaAtual(double cargaBateriaAtual) {
        if (cargaBateriaAtual >= 0 && cargaBateriaAtual <= 100) {
            this.cargaBateriaAtual = cargaBateriaAtual;
        } else {
            throw new ValorInvalidoException("A carga da bateria deve estar entre 0 e 100.");
        }
    }

    public void setConsumoKwhPorKm(double consumoKwhPorKm) {
        if (consumoKwhPorKm > 0) {
            this.consumoKwhPorKm = consumoKwhPorKm;
        } else {
            throw new ValorInvalidoException("Consumo não pode ser menor ou igual a 0.");
        }
    }

    public void setTempoRecargaCompleta(int tempoRecargaCompleta) {
        if (tempoRecargaCompleta >= 0) {
            this.tempoRecargaCompleta = tempoRecargaCompleta;
        } else {
            throw new ValorInvalidoException("Tempo de recarga não pode ser menor que 0.");
        }
    }

    @Override
    public String toString(){
        return modelo + " (ID " + id + ")";
    }

}