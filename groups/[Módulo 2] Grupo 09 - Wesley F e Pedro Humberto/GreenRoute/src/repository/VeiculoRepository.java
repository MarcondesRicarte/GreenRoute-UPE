package repository;

import model.Veiculo;
import java.util.ArrayList;

public class VeiculoRepository {
    private ArrayList<Veiculo> veiculos = new ArrayList<>();
    private int proximoId = 0;


    // CRUD: Cadastro de Veículos:
    public void cadastrarVeiculo(Veiculo veiculo){
        veiculo.setId(proximoId++);
        veiculos.add(veiculo);
    }

    // CRUD: Buscar veiculo por ID:
    public  Veiculo buscarPorId(int id) {
        for (Veiculo veiculo : veiculos) {

            if (veiculo.getId() == id) {
                return veiculo;
            }
        }
        return null;
    }

    // CRUD: Listar todos os veículos:
    public ArrayList<Veiculo> listarTodosVeiculos() {
        return veiculos;
    }

    // CRUD: Atualizar Veículo:
    public boolean atualizarVeiculo(int id, Veiculo veiculoAtualizado){
        for (int i = 0; i < veiculos.size(); i++){
            if (veiculos.get(i).getId() == id){
                veiculoAtualizado.setId(id);
                veiculos.set(i, veiculoAtualizado);
                return true;
            }
        }
        return false;
    }

    // CRUD: Apagar Veículo:
    public boolean apagarVeiculo(int id) {
        for (int i = 0; i < veiculos.size(); i++) {
            if (veiculos.get(i).getId() == id) {
                veiculos.remove(i);
                return true;
            }
        }
        return false;
    }
}
