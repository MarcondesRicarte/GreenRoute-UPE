package Gemini;

public class VeiculoIADTO {
    private String tipo;
    private String modelo;
    private double autonomiaMaxima;
    private double cargaBateriaAtual;
    private double consumoKwhPorKm;
    private int tempoRecargaCompleta;

    // Atributos exclusivos pra veiculos eletricos
    private int tempoRecargaRapida;
    private String tipoConector;
    // Atributos exclusivos pra veiculos hibridos
    private double capacidadeTanqueCombustivel;
    private double consumoCombustivel;
    private String tipoCombustivel;

    // Getters e Gson
    public String getTipo() {return tipo;}
    public String getModelo() {return modelo;}
    public double getAutonomiaMaxima() {return autonomiaMaxima;}
    public double getCargaBateriaAtual() {return cargaBateriaAtual;}
    public double getConsumoKwhPorKm() {return consumoKwhPorKm;}
    public int getTempoRecargaCompleta() {return tempoRecargaCompleta;}
    public int getTempoRecargaRapida() {return tempoRecargaRapida;}
    public String getTipoConector() {return tipoConector;}
    public double getCapacidadeTanqueCombustivel() {return capacidadeTanqueCombustivel;}
    public double getConsumoCombustivel() {return consumoCombustivel;}
    public String getTipoCombustivel() {return tipoCombustivel;}

}
