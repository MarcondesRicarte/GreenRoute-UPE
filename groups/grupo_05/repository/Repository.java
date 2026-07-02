package repository;

import model.Veiculo;
import model.Eletroposto;
import model.Cidade;

public class Repository {
    private Veiculo[] veiculos = new Veiculo[2];
    private int qtdVeiculos = 0;

    private Eletroposto[] eletropostos = new Eletroposto[2];
    private int qtdEletropostos = 0;

    private Cidade[] cidades = new Cidade[2];
    private int qtdCidades = 0;

    public void adicionarVeiculo(Veiculo v) {
        if (qtdVeiculos == veiculos.length) {
            Veiculo[] novoArray = new Veiculo[veiculos.length * 2];
            System.arraycopy(veiculos, 0, novoArray, 0, veiculos.length);
            veiculos = novoArray;
        }
        veiculos[qtdVeiculos++] = v;
    }

    public Veiculo[] listarVeiculos() {
        Veiculo[] ativos = new Veiculo[qtdVeiculos];
        System.arraycopy(veiculos, 0, ativos, 0, qtdVeiculos);
        return ativos;
    }

    public Veiculo buscarVeiculo(int id) {
        for (int i = 0; i < qtdVeiculos; i++) {
            if (veiculos[i].getId() == id) return veiculos[i];
        }
        return null;
    }

    public boolean atualizarVeiculo(int id, Veiculo novoVeiculo) {
        for (int i = 0; i < qtdVeiculos; i++) {
            if (veiculos[i].getId() == id) {
                veiculos[i] = novoVeiculo;
                veiculos[i].setId(id);
                return true;
            }
        }
        return false;
    }

    public boolean excluirVeiculo(int id) {
        for (int i = 0; i < qtdVeiculos; i++) {
            if (veiculos[i].getId() == id) {
                for (int j = i; j < qtdVeiculos - 1; j++) {
                    veiculos[j] = veiculos[j + 1];
                }
                veiculos[--qtdVeiculos] = null;
                return true;
            }
        }
        return false;
    }

    public void adicionarEletroposto(Eletroposto e) {
        if (qtdEletropostos == eletropostos.length) {
            Eletroposto[] novoArray = new Eletroposto[eletropostos.length * 2];
            System.arraycopy(eletropostos, 0, novoArray, 0, eletropostos.length);
            eletropostos = novoArray;
        }
        eletropostos[qtdEletropostos++] = e;
    }

    public Eletroposto[] listarEletropostos() {
        Eletroposto[] ativos = new Eletroposto[qtdEletropostos];
        System.arraycopy(eletropostos, 0, ativos, 0, qtdEletropostos);
        return ativos;
    }

    public Eletroposto buscarEletroposto(int id) {
        for (int i = 0; i < qtdEletropostos; i++) {
            if (eletropostos[i].getId() == id) return eletropostos[i];
        }
        return null;
    }

    public boolean atualizarEletroposto(int id, Eletroposto novoEletroposto) {
        for (int i = 0; i < qtdEletropostos; i++) {
            if (eletropostos[i].getId() == id) {
                eletropostos[i] = novoEletroposto;
                eletropostos[i].setId(id);
                return true;
            }
        }
        return false;
    }

    public boolean excluirEletroposto(int id) {
        for (int i = 0; i < qtdEletropostos; i++) {
            if (eletropostos[i].getId() == id) {
                for (int j = i; j < qtdEletropostos - 1; j++) {
                    eletropostos[j] = eletropostos[j + 1];
                }
                eletropostos[--qtdEletropostos] = null;
                return true;
            }
        }
        return false;
    }

    public void adicionarCidade(Cidade c) {
        if (qtdCidades == cidades.length) {
            Cidade[] novoArray = new Cidade[cidades.length * 2];
            System.arraycopy(cidades, 0, novoArray, 0, cidades.length);
            cidades = novoArray;
        }
        cidades[qtdCidades++] = c;
    }

    public Cidade[] listarCidades() {
        Cidade[] ativas = new Cidade[qtdCidades];
        System.arraycopy(cidades, 0, ativas, 0, qtdCidades);
        return ativas;
    }

    public Cidade buscarCidade(int id) {
        for (int i = 0; i < qtdCidades; i++) {
            if (cidades[i].getId() == id) return cidades[i];
        }
        return null;
    }

    public boolean atualizarCidade(int id, Cidade novaCidade) {
        for (int i = 0; i < qtdCidades; i++) {
            if (cidades[i].getId() == id) {
                cidades[i] = novaCidade;
                cidades[i].setId(id);
                return true;
            }
        }
        return false;
    }

    public boolean excluirCidade(int id) {
        for (int i = 0; i < qtdCidades; i++) {
            if (cidades[i].getId() == id) {
                for (int j = i; j < qtdCidades - 1; j++) {
                    cidades[j] = cidades[j + 1];
                }
                cidades[--qtdCidades] = null;
                return true;
            }
        }
        return false;
    }
}
