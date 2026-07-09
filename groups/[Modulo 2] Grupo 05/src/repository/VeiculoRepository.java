package repository;

import model.Veiculo;
import java.util.ArrayList;
import java.util.List;

public class VeiculoRepository {
    private List<Veiculo> veiculos = new ArrayList<>();

    public void adicionar(Veiculo v) {
        veiculos.add(v);
    }

    public List<Veiculo> listar() {
        return new ArrayList<>(veiculos);
    }

    public Veiculo buscar(int id) {
        for (Veiculo v : veiculos) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    public boolean atualizar(int id, Veiculo novoVeiculo) {
        Veiculo v = buscar(id);
        if (v != null) {
            int index = veiculos.indexOf(v);
            novoVeiculo.setId(id);
            veiculos.set(index, novoVeiculo);
            return true;
        }
        return false;
    }

    public boolean excluir(int id) {
        Veiculo v = buscar(id);
        if (v != null) {
            return veiculos.remove(v);
        }
        return false;
    }
}
