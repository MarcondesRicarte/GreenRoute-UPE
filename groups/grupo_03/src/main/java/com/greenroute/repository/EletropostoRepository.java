package com.greenroute.repository;

import com.greenroute.model.Eletroposto;

import java.util.ArrayList;

public class EletropostoRepository {

    private ArrayList<Eletroposto> eletropostos = new ArrayList<>();

    public ArrayList<Eletroposto> getEletropostos() {
        return eletropostos;
    }

    public int getQuantidadeEletropostos() {
        return eletropostos.size();
    }

    // CADASTRAR ELETROPOSTO
    public void cadastrarEletroposto(Eletroposto eletroposto) {
        eletropostos.add(eletroposto);
    }

    // LISTAR ELETROPOSTOS
    public void listarEletropostos() {

        if (eletropostos.isEmpty()) {
            System.out.println("Nenhum eletroposto cadastrado!");
            return;
        }

        for (Eletroposto eletroposto : eletropostos) {
            System.out.println(eletroposto);
        }
    }

    // REMOVER ELETROPOSTO
    public boolean removerEletroposto(int id) {

        for (int i = 0; i < eletropostos.size(); i++) {

            if (id == eletropostos.get(i).getId()) {
                eletropostos.remove(i);
                return true;
            }
        }

        return false;
    }

    // ATUALIZAR ELETROPOSTO
    public boolean atualizarEletroposto(Eletroposto eletropostoAtualizado) {

        for (int i = 0; i < eletropostos.size(); i++) {

            if (eletropostoAtualizado.getId() == eletropostos.get(i).getId()) {
                eletropostos.set(i, eletropostoAtualizado);
                return true;
            }
        }

        return false;
    }

    // BUSCAR ELETROPOSTO
    public Eletroposto buscarEletroposto(int id) {

        for (Eletroposto eletroposto : eletropostos) {

            if (id == eletroposto.getId()) {
                return eletroposto;
            }
        }

        return null;
    }
}