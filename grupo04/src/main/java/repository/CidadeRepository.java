package repository;

import java.util.ArrayList;
import model.Cidade;
import persistencia.CidadePersistencia;

public class CidadeRepository {

    private ArrayList<Cidade> cidades;
    private CidadePersistencia persistencia;

    public CidadeRepository() {
        // Inicializa a persistência e carrega os dados salvos no txt para a memória
        persistencia = new CidadePersistencia();
        cidades = persistencia.carregar();
    }

    public boolean existeId(int id) {

        for (Cidade cidade : cidades) {

            if (cidade.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean cadastrar(Cidade cidade) {

        if (existeId(cidade.getId())) {
            return false;
        }

        cidades.add(cidade);
        
        // Salva as alterações no arquivo txt
        persistencia.salvar(cidades);

        return true;
    }

    public Cidade buscarPorId(int id) {

        for (Cidade cidade : cidades) {

            if (cidade.getId() == id) {
                return cidade;
            }
        }

        return null;
    }

    public ArrayList<Cidade> listar() {
        return new ArrayList<>(cidades);
    }

    public boolean atualizar(int id, Cidade novaCidade) {

        for (int i = 0; i < cidades.size(); i++) {

            if (cidades.get(i).getId() == id) {

                cidades.set(i, novaCidade);
                
                // Salva as alterações no arquivo txt
                persistencia.salvar(cidades);

                return true;
            }
        }

        return false;
    }

    public boolean excluir(int id) {

        for (int i = 0; i < cidades.size(); i++) {

            if (cidades.get(i).getId() == id) {

                cidades.remove(i);
                
                // Salva as alterações no arquivo txt
                persistencia.salvar(cidades);

                return true;
            }
        }

        return false;
    }

    public int quantidade() {
        return cidades.size();
    }
}
