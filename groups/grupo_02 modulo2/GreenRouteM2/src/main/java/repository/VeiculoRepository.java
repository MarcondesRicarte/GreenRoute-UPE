package repository;

import java.util.ArrayList;

import model.Veiculo;

public class VeiculoRepository {

    private ArrayList<Veiculo> veiculos;

    public VeiculoRepository() {

        veiculos = new ArrayList<>();
    }

    public void cadastrar(Veiculo veiculo) {

        veiculos.add(veiculo);
    }

    public Veiculo buscarPorId(int id) {

        for (int i = 0; i < veiculos.size(); i++) {

            Veiculo veiculo = veiculos.get(i);

            if (veiculo.getId() == id) {
                return veiculo;
            }
        }

        return null;
    }

    public ArrayList<Veiculo> listar() {

        return veiculos;
    }

    public boolean excluir(int id) {

        for (int i = 0; i < veiculos.size(); i++) {

            Veiculo veiculo = veiculos.get(i);

            if (veiculo.getId() == id) {
                veiculos.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean atualizar(int id, Veiculo novoVeiculo) {

        for (int i = 0; i < veiculos.size(); i++) {

            Veiculo veiculo = veiculos.get(i);

            if (veiculo.getId() == id) {
                veiculos.set(i, novoVeiculo);
                return true;
            }
        }

        return false;
    }

    public int getQuantidade() {

        return veiculos.size();
    }
}
