package controller;

import java.util.ArrayList;

import model.Veiculo;
import repository.VeiculoRepository;

public class VeiculoController {

    private VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository) {

        this.veiculoRepository = veiculoRepository;
    }

    public void cadastrar(Veiculo veiculo) {

        veiculoRepository.cadastrar(veiculo);
    }

    public Veiculo buscarPorId(int id) {

        return veiculoRepository.buscarPorId(id);
    }

    public ArrayList<Veiculo> listar() {

        return veiculoRepository.listar();
    }

    public boolean excluir(int id) {

        return veiculoRepository.excluir(id);
    }

    public boolean atualizar(int id, Veiculo veiculo) {

        return veiculoRepository.atualizar(id, veiculo);
    }
}
