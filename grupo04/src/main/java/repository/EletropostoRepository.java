package repository;

import java.util.ArrayList;
import model.Eletroposto;
import persistencia.EletropostoPersistencia;

public class EletropostoRepository {

    private ArrayList<Eletroposto> eletropostos;
    private EletropostoPersistencia persistencia;

    public EletropostoRepository() {
        // Inicializa a persistência e puxa os dados do txt para o ArrayList
        persistencia = new EletropostoPersistencia();
        eletropostos = persistencia.carregar();
    }

    public boolean existeId(int id) {

        for (Eletroposto eletroposto : eletropostos) {

            if (eletroposto.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean cadastrar(Eletroposto eletroposto) {

        if (existeId(eletroposto.getId())) {
            return false;
        }

        eletropostos.add(eletroposto);
        
        // Salva as alterações no arquivo txt
        persistencia.salvar(eletropostos);

        return true;
    }

    public Eletroposto buscarPorId(int id) {

        for (Eletroposto eletroposto : eletropostos) {

            if (eletroposto.getId() == id) {
                return eletroposto;
            }
        }

        return null;
    }

    public ArrayList<Eletroposto> listar() {
        return new ArrayList<>(eletropostos);
    }

    public boolean atualizar(int id, Eletroposto novoEletroposto) {

        for (int i = 0; i < eletropostos.size(); i++) {

            if (eletropostos.get(i).getId() == id) {

                eletropostos.set(i, novoEletroposto);
                
                // Salva as alterações no arquivo txt
                persistencia.salvar(eletropostos);

                return true;
            }
        }

        return false;
    }

    public boolean excluir(int id) {

        for (int i = 0; i < eletropostos.size(); i++) {

            if (eletropostos.get(i).getId() == id) {

                eletropostos.remove(i);
                
                // Salva as alterações no arquivo txt
                persistencia.salvar(eletropostos);

                return true;
            }
        }

        return false;
    }

    public int quantidade() {
        return eletropostos.size();
    }
}
