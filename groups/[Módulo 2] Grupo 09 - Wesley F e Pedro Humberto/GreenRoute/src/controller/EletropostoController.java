package controller;

import model.Eletroposto;
import repository.EletropostoRepository;
import java.util.ArrayList;

public class EletropostoController {
    private EletropostoRepository eletropostoRepository;

    public EletropostoController(EletropostoRepository eletropostoRepository) {
        this.eletropostoRepository = eletropostoRepository;
    }

    // CRUD: Cadastrar Eletroposto (Create)
    public void cadastrarEletropostoController(Eletroposto eletroposto) {
        eletropostoRepository.cadastrarEletroposto(eletroposto);
    }

    // CRUD: Listar todos os Eletropostos (Read)
    public ArrayList<Eletroposto> listarEletropostoController() {
        return eletropostoRepository.listarEletropostos();
    }

    // CRUD: Buscar Eletroposto por ID (Read)
    public Eletroposto buscarEletropostoPorIdController(int id) {
        return eletropostoRepository.buscarPorId(id);
    }

    // CRUD: Buscar Eletropostos por Cidade (Read)
    public ArrayList<Eletroposto> buscarEletropostosPorCidadeController(int cidadeId) {
        return eletropostoRepository.buscarPorCidadeId(cidadeId);
    }

    // CRUD: Atualizar Eletroposto (Update)
    public boolean atualizarEletropostoController(int id, Eletroposto eletropostoAtualizado) {
        return eletropostoRepository.atualizarEletroposto(id, eletropostoAtualizado);
    }

    // CRUD: Apagar Eletroposto (Delete)
    public boolean apagarEletropostoController(int id) {
        return eletropostoRepository.apagarEletroposto(id);
    }
}
