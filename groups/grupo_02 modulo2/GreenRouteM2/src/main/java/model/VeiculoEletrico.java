package model;

public class VeiculoEletrico extends Veiculo {

    private String tipoConector;
    private int tempoRecargaRapida;

    public VeiculoEletrico(int id,
                           String modelo,
                           double autonomiaMaxima,
                           double cargaBateriaAtual,
                           double consumoKwhPorKm,
                           int tempoRecargaCompleta,
                           String tipoConector,
                           int tempoRecargaRapida) {

        super(id,
                modelo,
                autonomiaMaxima,
                cargaBateriaAtual,
                consumoKwhPorKm,
                tempoRecargaCompleta);

        this.tipoConector = tipoConector;
        this.tempoRecargaRapida = tempoRecargaRapida;
    }

    public String getTipoConector() {

        return tipoConector;
    }

    public void setTipoConector(String tipoConector) {

        this.tipoConector = tipoConector;
    }

    public int getTempoRecargaRapida() {

        return tempoRecargaRapida;
    }

    public void setTempoRecargaRapida(int tempoRecargaRapida) {

        this.tempoRecargaRapida = tempoRecargaRapida;
    }

    @Override
    public String toString() {

        return "Veiculo Eletrico" +
                " - ID: " + getId() +
                " - Modelo: " + getModelo() +
                " - Autonomia Máxima: " + getAutonomiaMaxima() +
                " km - Bateria: " + getCargaBateriaAtual() +
                "% - Tipo de Conector: " + tipoConector;
    }
}
