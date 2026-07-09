package controller;

import model.Veiculo;
import repository.VeiculoRepository;
import java.util.ArrayList;

public class VeiculoController {
    private VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    // CRUD: Cadastrar Veículo (Create)
    public void cadastrarVeiculoController(Veiculo veiculo) {
        veiculoRepository.cadastrarVeiculo(veiculo);
    }

    // CRUD: Listar todos os Veículos (Read)
    public ArrayList<Veiculo> listarTodosVeiculosController() {
        return veiculoRepository.listarTodosVeiculos();
    }

    // CRUD: Buscar Veículo por ID (Read)
    public Veiculo buscarVeiculoPorId(int id) {
        return veiculoRepository.buscarPorId(id);
    }

    // CRUD: Atualizar Veículo (Update)
    public boolean atualizarVeiculoController(int id, Veiculo veiculoAtualizado) {
        return veiculoRepository.atualizarVeiculo(id, veiculoAtualizado);
    }

    // CRUD: Apagar Veículo (Delete)
    public boolean apagarVeiculoController(int id) {
        return veiculoRepository.apagarVeiculo(id);
    }
}
