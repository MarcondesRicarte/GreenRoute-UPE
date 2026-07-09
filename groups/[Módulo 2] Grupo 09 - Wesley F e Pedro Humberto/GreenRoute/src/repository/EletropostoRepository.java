package repository;

import model.Eletroposto;
import java.util.ArrayList;

public class EletropostoRepository {
    private ArrayList<Eletroposto> eletropostos = new ArrayList<>();
    private int proximoId = 1;

    // CRUD: Cadastrar Eletroposto
    public void cadastrarEletroposto(Eletroposto eletroposto) {
        eletroposto.setId(proximoId++);
        eletropostos.add(eletroposto);
    }

    // CRUD: Buscar Eletroposto por ID
    public Eletroposto buscarPorId(int id) {
        for (Eletroposto eletroposto : eletropostos) {
            if (eletroposto.getId() == id) {
                return eletroposto;
            }
        }
        return null;
    }

    // CRUD: Buscar Eletropostos por Cidade
    public ArrayList<Eletroposto> buscarPorCidadeId(int cidadeId) {
        ArrayList<Eletroposto> resultado = new ArrayList<>();
        for (Eletroposto eletroposto : eletropostos) {
            if (eletroposto.getCidadeId() == cidadeId) {
                resultado.add(eletroposto);
            }
        }
        return resultado;
    }

    // CRUD: Listar todos os Eletropostos
    public ArrayList<Eletroposto> listarEletropostos() {
        return eletropostos;
    }

    // CRUD: Atualizar Eletroposto
    public boolean atualizarEletroposto(int id, Eletroposto eletropostoAtualizado) {
        for (int i = 0; i < eletropostos.size(); i++) {
            if (eletropostos.get(i).getId() == id) {
                eletropostoAtualizado.setId(id);
                eletropostos.set(i, eletropostoAtualizado);
                return true;
            }
        }
        return false;
    }

    // CRUD: Apagar Eletroposto
    public boolean apagarEletroposto(int id) {
        for (int i = 0; i < eletropostos.size(); i++) {
            if (eletropostos.get(i).getId() == id) {
                eletropostos.remove(i);
                return true;
            }
        }
        return false;
    }
}
