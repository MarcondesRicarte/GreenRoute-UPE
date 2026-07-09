package repository;

import model.Cidade;
import java.util.ArrayList;

public class CidadeRepository {
    private ArrayList<Cidade> cidades = new ArrayList<>();
    private int proximoId = 1;

    // CRUD: Cadastrar Cidade
    public void cadastrarCidade(Cidade cidade) {
        cidade.setId(proximoId++);
        cidades.add(cidade);
    }

    // CRUD: Buscar Cidade por ID
    public Cidade buscarPorId(int id) {
        for (Cidade cidade : cidades) {
            if (cidade.getId() == id) {
                return cidade;
            }
        }
        return null;
    }

    // CRUD: Listar todas as Cidades
    public ArrayList<Cidade> listarCidades() {
        return cidades;
    }

    // CRUD: Atualizar Cidade
    public boolean atualizarCidade(int id, Cidade cidadeAtualizada) {
        for (int i = 0; i < cidades.size(); i++) {
            if (cidades.get(i).getId() == id) {
                cidadeAtualizada.setId(id);
                cidades.set(i, cidadeAtualizada);
                return true;
            }
        }
        return false;
    }

    // CRUD: Apagar Cidade
    public boolean apagarCidade(int id) {
        for (int i = 0; i < cidades.size(); i++) {
            if (cidades.get(i).getId() == id) {
                cidades.remove(i);
                return true;
            }
        }
        return false;
    }
}
