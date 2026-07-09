package controller;

import java.util.List;

import model.Eletroposto;
import repository.EletropostoRepository;

public class EletropostoController {
    private EletropostoRepository repository = new EletropostoRepository();

    public void cadastrar(Eletroposto e) { 
        repository.adicionar(e); 
    }

    public List<Eletroposto> listar() { 
        return repository.listar(); 
    }

    public Eletroposto buscar(int id) { 
        return repository.buscar(id); 
    }

    public boolean atualizar(int id, Eletroposto e) { 
        return repository.atualizar(id, e); 
    }

    public boolean excluir(int id) { 
        return repository.excluir(id); 
    }
}
