package model;

public class VeiculoHibrido extends Veiculo {
    private double capacidadeTanqueCombustivel;
    private double consumoGasolina; 
    private String tipoConectorHibrido;

    public VeiculoHibrido(int id, String modelo, double autonomiaMaxima, double cargaBateriaAtual, 
                          double capacidadeTanqueCombustivel, double consumoGasolina, String tipoConectorHibrido) {
        super(id, modelo, autonomiaMaxima, cargaBateriaAtual);
        this.capacidadeTanqueCombustivel = capacidadeTanqueCombustivel;
        this.consumoGasolina = consumoGasolina;
        this.tipoConectorHibrido = tipoConectorHibrido;
    }

    public String getTipoConector() { return tipoConectorHibrido; }

    @Override
    public String toString() {
        return super.toString() + " (Híbrido) | Tanque: " + capacidadeTanqueCombustivel + "L | Consumo: " + consumoGasolina + "km/l";
    }
}
