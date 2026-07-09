package repository;

import model.Eletroposto;
import java.util.ArrayList;
import java.util.List;

public class EletropostoRepository {
    private List<Eletroposto> eletropostos = new ArrayList<>();

    public void adicionar(Eletroposto e) {
        eletropostos.add(e);
    }

    public List<Eletroposto> listar() {
        return new ArrayList<>(eletropostos);
    }

    public Eletroposto buscar(int id) {
        for (Eletroposto e : eletropostos) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public boolean atualizar(int id, Eletroposto novoEletroposto) {
        Eletroposto e = buscar(id);
        if (e != null) {
            int index = eletropostos.indexOf(e);
            novoEletroposto.setId(id);
            eletropostos.set(index, novoEletroposto);
            return true;
        }
        return false;
    }

    public boolean excluir(int id) {
        Eletroposto e = buscar(id);
        if (e != null) {
            return eletropostos.remove(e);
        }
        return false;
    }
}
