package Repository;

import java.util.ArrayList;
import java.util.List;
import model.Cidade;
import model.Eletroposto;
import model.Veiculo;

public class GreenRouteRepository {

    private List<Veiculo> veiculos = new ArrayList<>();
    private List<Eletroposto> eletropostos = new ArrayList<>();
    private List<Cidade> cidades = new ArrayList<>();

    public void salvarVeiculo(Veiculo veiculo) {
        veiculos.add(veiculo);
    }

    public List<Veiculo> listarVeiculos() {
        return veiculos;
    }

    public void salvarEletroposto(Eletroposto eletroposto) {
        eletropostos.add(eletroposto);
    }

    public List<Eletroposto> listarEletropostos() {
        return eletropostos;
    }

    public void salvarCidade(Cidade cidade) {
        cidades.add(cidade);
    }

    public List<Cidade> listarCidades() {
        return cidades;
    }
}