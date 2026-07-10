package controller;

import java.util.List;
import model.Cidade;
import Repository.GreenRouteRepository;

public class CidadeController {
    private GreenRouteRepository repo = new GreenRouteRepository();

    public void cadastrarCidade(Cidade c) {
    	repo.salvarCidade(c);
    }

    public List<Cidade> listarCidades() {
        return repo.listarCidades();
    }

    public Cidade buscarCidade(int id) {
        for (Cidade c : repo.listarCidades()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
}