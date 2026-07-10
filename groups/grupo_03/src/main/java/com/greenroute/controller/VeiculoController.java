package com.greenroute.controller;

import java.util.ArrayList;

import com.greenroute.model.Veiculo;
import com.greenroute.repository.VeiculoRepository;

public class VeiculoController {

    private VeiculoRepository repository;

    public VeiculoController() {
        repository = new VeiculoRepository();
    }

    public void cadastrar(Veiculo veiculo) {
        repository.adicionar(veiculo);
    }

    public Veiculo procurarId(int id) {
        return repository.buscar(id);
    }

    public void atualizarVeiculo(int id, Veiculo novoVeiculo) {
        repository.atualizar(id, novoVeiculo);
    }

    public boolean remover(int id) {
        return repository.remover(id);
    }

    public ArrayList<Veiculo> listar() {
        return repository.listar();
    }

    public boolean existeId(int id) {
        return repository.existeId(id);
    }
}