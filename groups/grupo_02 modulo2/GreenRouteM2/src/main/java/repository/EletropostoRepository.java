package repository;

import java.util.ArrayList;

import model.Eletroposto;

public class EletropostoRepository {

    private ArrayList<Eletroposto> eletropostos;

    public EletropostoRepository() {

        eletropostos = new ArrayList<>();
    }

    public void cadastrar(Eletroposto eletroposto) {

        eletropostos.add(eletroposto);
    }

    public Eletroposto buscarPorId(int id) {

        for (int i = 0; i < eletropostos.size(); i++) {

            Eletroposto eletroposto = eletropostos.get(i);

            if (eletroposto.getId() == id) {
                return eletroposto;
            }
        }

        return null;
    }

    public ArrayList<Eletroposto> listar() {

        return eletropostos;
    }

    public boolean excluir(int id) {

        for (int i = 0; i < eletropostos.size(); i++) {

            Eletroposto eletroposto = eletropostos.get(i);

            if (eletroposto.getId() == id) {
                eletropostos.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean atualizar(int id, Eletroposto novoEletroposto) {

        for (int i = 0; i < eletropostos.size(); i++) {

            Eletroposto eletroposto = eletropostos.get(i);

            if (eletroposto.getId() == id) {
                eletropostos.set(i, novoEletroposto);
                return true;
            }
        }

        return false;
    }
}
