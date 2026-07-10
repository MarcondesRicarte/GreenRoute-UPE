package controller;

import java.util.ArrayList;
import java.util.List;
import model.Eletroposto;
import Repository.GreenRouteRepository;

public class EletropostoController {
    private GreenRouteRepository repo = new GreenRouteRepository();

    public void cadastrarEletroposto(Eletroposto e) {
        
    }

    public List<Eletroposto> listarEletropostos() {
        return repo.listarEletropostos();
    }
}