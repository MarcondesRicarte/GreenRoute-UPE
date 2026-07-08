package controller;

import java.util.List;

import exception.AutonomiaInsuficienteException;
import model.Cidade;
import model.Eletroposto;
import model.Veiculo;
import repository.CidadeRepository;

public class CidadeController {
    private CidadeRepository repository = new CidadeRepository();

    public void cadastrar(Cidade c) { repository.adicionar(c); }
    public List<Cidade> listar() { return repository.listar(); }
    public Cidade buscar(int id) { return repository.buscar(id); }
    public boolean atualizar(int id, Cidade c) { return repository.atualizar(id, c); }
    public boolean excluir(int id) { return repository.excluir(id); }

    public List<Eletroposto> simularViagem(Veiculo veiculo, int cidadeIdDestino, EletropostoController eletropostoCtrl) throws AutonomiaInsuficienteException {
        Cidade cidade = repository.buscar(cidadeIdDestino);

        if (veiculo == null || cidade == null) {
            throw new IllegalArgumentException("Veículo ou Cidade não encontrados.");
        }

        double autonomiaAtual = veiculo.getAutonomiaMaxima() * (veiculo.getCargaBateriaAtual() / 100.0);
        double distanciaNecessaria = cidade.getDistanciaDaCapital();

        if (autonomiaAtual < distanciaNecessaria) {
            String msg = "Autonomia insuficiente! A carga atual permite " + autonomiaAtual + "km, mas o destino exige " + distanciaNecessaria + "km.";
            throw new AutonomiaInsuficienteException(msg);
        }

        return null;
    }
}
