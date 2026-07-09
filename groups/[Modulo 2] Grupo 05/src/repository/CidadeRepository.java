package repository;

import model.Cidade;
import java.util.ArrayList;
import java.util.List;

public class CidadeRepository {
    private List<Cidade> cidades = new ArrayList<>();

    public void adicionar(Cidade c) {
        cidades.add(c);
    }

    public List<Cidade> listar() {
        return new ArrayList<>(cidades);
    }

    public Cidade buscar(int id) {
        for (Cidade c : cidades) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public boolean atualizar(int id, Cidade novaCidade) {
        Cidade c = buscar(id);
        if (c != null) {
            int index = cidades.indexOf(c);
            novaCidade.setId(id);
            cidades.set(index, novaCidade);
            return true;
        }
        return false;
    }

    public boolean excluir(int id) {
        Cidade c = buscar(id);
        if (c != null) {
            return cidades.remove(c);
        }
        return false;
    }
}
