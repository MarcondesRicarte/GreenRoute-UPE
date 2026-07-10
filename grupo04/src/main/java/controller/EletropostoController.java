package controller;

import java.util.ArrayList;
import model.Eletroposto;
import repository.EletropostoRepository;

public class EletropostoController {

    private EletropostoRepository repository;

    // Construtor unificado
    public EletropostoController(EletropostoRepository repository) {
        this.repository = repository;
    }

    public boolean cadastrarEletroposto(Eletroposto eletroposto) {
        return repository.cadastrar(eletroposto);
    }

    public Eletroposto buscarEletroposto(int id) {
        return repository.buscarPorId(id);
    }

    public ArrayList<Eletroposto> listarEletropostos() {
        return repository.listar();
    }

    public boolean atualizarEletroposto(int id, Eletroposto novoEletroposto) {
        return repository.atualizar(id, novoEletroposto);
    }

    public boolean excluirEletroposto(int id) {
        return repository.excluir(id);
    }

    // Método corrigido e fechado corretamente
    public int quantidadeEletropostos() {
        return repository.listar().size(); 
        // Nota: Se o seu repository tiver uma função específica chamada quantidade(), 
        // você pode trocar a linha acima por: return repository.quantidade();
    }
}
