package model;

public class VeiculoEletrico extends Veiculo {
    private double consumoKwh;
    private int tempoRecargaMinutos;
    private String tipoConector;
    private double capacidadeBateriaKwh;

    public VeiculoEletrico(int id, String modelo, double autonomiaMaxima, double cargaBateriaAtual, 
                           double consumoKwh, int tempoRecargaMinutos, String tipoConector, double capacidadeBateriaKwh) {
        super(id, modelo, autonomiaMaxima, cargaBateriaAtual);
        this.consumoKwh = consumoKwh;
        this.tempoRecargaMinutos = tempoRecargaMinutos;
        this.tipoConector = tipoConector;
        this.capacidadeBateriaKwh = capacidadeBateriaKwh;
    }

    public String getTipoConector() { return tipoConector; }

    @Override
    public String toString() {
        return super.toString() + " (Elétrico) | Conector: " + tipoConector + " | Bateria: " + capacidadeBateriaKwh + "kWh";
    }
}
