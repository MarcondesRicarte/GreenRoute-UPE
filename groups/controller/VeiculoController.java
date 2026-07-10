package controller;

import java.util.List;
import model.Veiculo;
import Repository.GreenRouteRepository;

public class VeiculoController {
    private GreenRouteRepository repo = new GreenRouteRepository();

    public void cadastrarVeiculo(Veiculo v) {
    }

    public List<Veiculo> listarVeiculos() {
        return repo.listarVeiculos();
    }

    public Veiculo buscarVeiculo(int id) {
        for (Veiculo v : repo.listarVeiculos()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }
}