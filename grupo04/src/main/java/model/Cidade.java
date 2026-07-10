package model;

public class Cidade {

    private int id;
    private String nome;
    private String estado;
    private double distanciaDaCapital;

    // CONSTRUTOR CORRIGIDO: Agora recebe apenas 3 parâmetros (id, nome, distanciaDaCapital)
    // Isso vai resolver os erros no Main e no CidadePersistencia na hora!
    public Cidade(int id, String nome, double distanciaDaCapital) {
        this.id = id;
        this.nome = nome;
        this.distanciaDaCapital = distanciaDaCapital;
    }

    public int getId() {
        return id;
    }

    // Caso o seu sistema precise trocar o ID depois, adicionei o setId aqui
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getDistanciaDaCapital() {
        return distanciaDaCapital;
    }

    public void setDistanciaDaCapital(double distanciaDaCapital) {
        this.distanciaDaCapital = distanciaDaCapital;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + ", Nome: " + nome
                + ", Estado: " + estado
                + ", Distância da Capital: "
                + distanciaDaCapital + " km";
    }
}
