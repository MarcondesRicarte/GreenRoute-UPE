package com.greenroute.repository;

import com.greenroute.model.Cidade;

import java.util.ArrayList;

public class CidadeRepository {

    private ArrayList<Cidade> cidades = new ArrayList<>();

    // CADASTRAR NOVA CIDADE
    public void cadastrarCidade(Cidade cidade) {
        cidades.add(cidade);
    }

    // LISTAR AS CIDADES CADASTRADAS
    public void listarCidade() {

        if (cidades.isEmpty()) {
            System.out.println("Nenhuma cidade cadastrada!");
            return;
        }

        for (Cidade cidade : cidades) {
            System.out.println(cidade);
        }
    }

    public ArrayList<Cidade> getCidades() {
        return cidades;
    }

    // REMOVER A CIDADE PELO ID
    public boolean removerCidade(int id) {

        for (int i = 0; i < cidades.size(); i++) {

            if (id == cidades.get(i).getId()) {
                cidades.remove(i);
                return true;
            }
        }

        return false;
    }

    // ATUALIZAR A CIDADE PELO ID
    public boolean atualizarCidade(Cidade cidadeAtualizada) {

        for (int i = 0; i < cidades.size(); i++) {

            if (cidadeAtualizada.getId() == cidades.get(i).getId()) {
                cidades.set(i, cidadeAtualizada);
                return true;
            }
        }

        return false;
    }

    // BUSCAR CIDADE PELO ID
    public Cidade buscarCidade(int id) {

        for (Cidade cidade : cidades) {

            if (id == cidade.getId()) {
                return cidade;
            }
        }

        return null;
    }
}