package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Veiculo;
import model.VeiculoEletrico;

public class VeiculoPersistencia {

    private final String NOME_ARQUIVO = "veiculos.txt";

    // MÉTODO 1: Salva a lista de veículos no arquivo TXT
    public void salvar(ArrayList<Veiculo> lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            
            for (Veiculo v : lista) {
                // Como VeiculoEletrico herda de Veiculo, verificamos o tipo para salvar os campos extras
                if (v instanceof VeiculoEletrico) {
                    VeiculoEletrico ve = (VeiculoEletrico) v;
                    
                    String linha = "ELETRICO;" +
                                   ve.getId() + ";" +
                                   ve.getModelo() + ";" +
                                   ve.getAutonomiaMaxima() + ";" +
                                   ve.getCargaBateriaAtual() + ";" +
                                   ve.getConsumoKwhPorKm() + ";" +
                                   ve.getTempoRecargaCompleta() + ";" +
                                   ve.getTipoConector() + ";" +
                                   ve.getTempoRecargaRapida();
                    
                    writer.write(linha);
                }
                writer.newLine(); // Pula para a próxima linha
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar veículos: " + e.getMessage());
        }
    }

    // MÉTODO 2: Lê o arquivo TXT e reconstrói os objetos corretos na memória
    public ArrayList<Veiculo> carregar() {
        ArrayList<Veiculo> lista = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);

        if (!arquivo.exists()) {
            return lista;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(";");
                String tipo = dados[0];

                // Identifica se a linha guardava um veículo elétrico
                if (tipo.equals("ELETRICO")) {
                    int id = Integer.parseInt(dados[1]);
                    String modelo = dados[2];
                    double autonomiaMax = Double.parseDouble(dados[3]);
                    double cargaAtual = Double.parseDouble(dados[4]);
                    double consumo = Double.parseDouble(dados[5]);
                    int tempoCompleto = Integer.parseInt(dados[6]);
                    String tipoConector = dados[7];
                    int tempoRapido = Integer.parseInt(dados[8]);

                    // Instancia a classe filha concreta corretamente
                    VeiculoEletrico ve = new VeiculoEletrico(id, modelo, autonomiaMax, cargaAtual, consumo, tempoCompleto, tipoConector, tempoRapido);
                    lista.add(ve);
                }
            }
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar veículos: " + e.getMessage());
        }

        return lista;
    }
}
