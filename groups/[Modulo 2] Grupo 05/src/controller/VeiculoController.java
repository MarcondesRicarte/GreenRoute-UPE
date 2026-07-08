package controller;

import repository.VeiculoRepository;
import model.Veiculo;
import java.util.List;

public class VeiculoController {
    private VeiculoRepository repository = new VeiculoRepository();

    public void cadastrar(Veiculo v) { repository.adicionar(v); }
    public List<Veiculo> listar() { return repository.listar(); }
    public Veiculo buscar(int id) { return repository.buscar(id); }
    public boolean atualizar(int id, Veiculo v) { return repository.atualizar(id, v); }
    public boolean excluir(int id) { return repository.excluir(id); }
}
