package com.greenroute.repository;

import com.greenroute.model.Veiculo;
import java.util.ArrayList;

public class VeiculoRepository {

    private ArrayList<Veiculo> veiculos;
    private int quantidade;

    public VeiculoRepository() {
        veiculos = new ArrayList<>();
        quantidade = 0;
    }

    public void adicionar(Veiculo veiculo) {
        veiculos.add(veiculo);
        quantidade++;
    }

    public Veiculo buscar(int id) {
        Veiculo veiculo = null;
        for (Veiculo v : veiculos) {
            if (v.getId() == id) {
                veiculo = v;
            }
        }

        return veiculo;
    }

    public void atualizar(int id, Veiculo veiculo) {
        int count = 0;
        for (Veiculo v : veiculos) {
            if (v.getId() == id) {
                veiculos.set(count, veiculo);
            }
            count++;
        }

        System.out.println("Veículo não encontrado");
    }

    public boolean remover(int id) {
        for (int i = 0; i < veiculos.size(); i++) {

            if (veiculos.get(i).getId() == id) {

                veiculos.remove(i);
                quantidade--;
                return true;

            }

        }

        return false;
    }

    public ArrayList<Veiculo> listar() {
        return veiculos;
        /* 
        System.out.println("=== LISTA DE VEÍCULOS ===");
        for (Veiculo veiculo : veiculos) {
            System.out.println("Veículo: " + veiculo.getModelo() + " - ID: " + veiculo.getId() + " - Autonomia: " + veiculo.calcularAutonomia() + " km");
        }
        */
    }

    public int getQuantidade() {
        return quantidade;
    }

    public boolean existeId(int id) {
        for (Veiculo veiculo : veiculos) {

            if (veiculo.getId() == id) {
                return true;
            }

        }

        return false;
    }
}