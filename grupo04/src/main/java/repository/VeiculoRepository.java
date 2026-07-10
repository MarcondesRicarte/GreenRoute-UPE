package repository;

import java.util.ArrayList;

import model.Veiculo;
import persistencia.VeiculoPersistencia;

public class VeiculoRepository {

    private ArrayList<Veiculo> veiculos;
    private VeiculoPersistencia persistencia;

    public VeiculoRepository() {

        persistencia = new VeiculoPersistencia();
        veiculos = persistencia.carregar();
    }

    public boolean existeId(int id) {

        for (Veiculo veiculo : veiculos) {

            if (veiculo.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean cadastrar(Veiculo veiculo) {

        if (existeId(veiculo.getId())) {
            return false;
        }

        veiculos.add(veiculo);

        persistencia.salvar(veiculos);

        return true;
    }

    public Veiculo buscarPorId(int id) {

        for (Veiculo veiculo : veiculos) {

            if (veiculo.getId() == id) {
                return veiculo;
            }
        }

        return null;
    }

    public ArrayList<Veiculo> listar() {

        return new ArrayList<>(veiculos);
    }

    public boolean atualizar(int id, Veiculo novoVeiculo) {

        for (int i = 0; i < veiculos.size(); i++) {

            if (veiculos.get(i).getId() == id) {

                veiculos.set(i, novoVeiculo);

                persistencia.salvar(veiculos);

                return true;
            }
        }

        return false;
    }

    public boolean excluir(int id) {

        for (int i = 0; i < veiculos.size(); i++) {

            if (veiculos.get(i).getId() == id) {

                veiculos.remove(i);

                persistencia.salvar(veiculos);

                return true;
            }
        }

        return false;
    }

    public int quantidade() {

        return veiculos.size();
    }
}
