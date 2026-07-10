package repository;

import java.util.ArrayList;

import model.Cidade;

public class CidadeRepository {

    private ArrayList<Cidade> cidades;

    public CidadeRepository() {

        cidades = new ArrayList<>();
    }

    public void cadastrar(Cidade cidade) {

        cidades.add(cidade);
    }

    public Cidade buscarPorId(int id) {

        for (int i = 0; i < cidades.size(); i++) {

            Cidade cidade = cidades.get(i);

            if (cidade.getId() == id) {
                return cidade;
            }
        }

        return null;
    }

    public ArrayList<Cidade> listar() {

        return cidades;
    }

    public boolean excluir(int id) {

        for (int i = 0; i < cidades.size(); i++) {

            Cidade cidade = cidades.get(i);

            if (cidade.getId() == id) {
                cidades.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean atualizar(int id, Cidade novaCidade) {

        for (int i = 0; i < cidades.size(); i++) {

            Cidade cidade = cidades.get(i);

            if (cidade.getId() == id) {
                cidades.set(i, novaCidade);
                return true;
            }
        }

        return false;
    }

    public int getQuantidade() {

        return cidades.size();
    }
}
