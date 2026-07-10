package controller;

import java.util.ArrayList;

import model.Eletroposto;
import repository.EletropostoRepository;

public class EletropostoController {

    private EletropostoRepository eletropostoRepository;

    public EletropostoController(EletropostoRepository eletropostoRepository) {

        this.eletropostoRepository = eletropostoRepository;
    }

    public void cadastrar(Eletroposto eletroposto) {

        eletropostoRepository.cadastrar(eletroposto);
    }

    public Eletroposto buscarPorId(int id) {

        return eletropostoRepository.buscarPorId(id);
    }

    public ArrayList<Eletroposto> listar() {

        return eletropostoRepository.listar();
    }

    public boolean excluir(int id) {

        return eletropostoRepository.excluir(id);
    }

    public boolean atualizar(int id, Eletroposto eletroposto) {

        return eletropostoRepository.atualizar(id, eletroposto);
    }
}
