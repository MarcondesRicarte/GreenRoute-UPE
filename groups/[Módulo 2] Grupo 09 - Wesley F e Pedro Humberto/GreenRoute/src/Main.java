import controller.CidadeController;
import controller.EletropostoController;
import controller.RotaController;
import controller.VeiculoController;
import repository.CidadeRepository;
import repository.EletropostoRepository;
import repository.VeiculoRepository;
import Gemini.GeminiService;
import Gemini.CadastroVeiculoIA;
import Gemini.IAPlannerService;
import Gemini.GeminiIAPlannerService;
import view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Repositories
        VeiculoRepository veiculoRepository = new VeiculoRepository();
        CidadeRepository cidadeRepository = new CidadeRepository();
        EletropostoRepository eletropostoRepository = new EletropostoRepository();

        // Controllers
        VeiculoController veiculoController = new VeiculoController(veiculoRepository);
        CidadeController cidadeController = new CidadeController(cidadeRepository);
        EletropostoController eletropostoController = new EletropostoController(eletropostoRepository);

        // IA
        GeminiService geminiService = new GeminiService();
        CadastroVeiculoIA cadastroVeiculoIA = new CadastroVeiculoIA(geminiService);
        IAPlannerService iaPlannerService = new GeminiIAPlannerService(geminiService);


        // RotaController recebe os três controllers que precisa
        RotaController rotaController = new RotaController(veiculoController, cidadeController, eletropostoController, iaPlannerService);

        // View
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(veiculoController, cidadeController, eletropostoController,rotaController, cadastroVeiculoIA);
            frame.setVisible(true);
        });
    }
}
