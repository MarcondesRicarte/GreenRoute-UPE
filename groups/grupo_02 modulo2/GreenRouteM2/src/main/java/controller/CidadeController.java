package controller;

import java.util.ArrayList;

import model.Cidade;
import repository.CidadeRepository;

public class CidadeController {

    private CidadeRepository cidadeRepository;

    public CidadeController(CidadeRepository cidadeRepository) {

        this.cidadeRepository = cidadeRepository;
    }

    public void cadastrar(Cidade cidade) {

        cidadeRepository.cadastrar(cidade);
    }

    public Cidade buscarPorId(int id) {

        return cidadeRepository.buscarPorId(id);
    }

    public ArrayList<Cidade> listar() {

        return cidadeRepository.listar();
    }

    public boolean excluir(int id) {

        return cidadeRepository.excluir(id);
    }

    public boolean atualizar(int id, Cidade cidadeAtualizada) {

        return cidadeRepository.atualizar(id, cidadeAtualizada);
    }

    public int getQuantidade() {

        return cidadeRepository.getQuantidade();
    }
}
