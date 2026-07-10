package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Eletroposto;

public class EletropostoPersistencia {

    private final String NOME_ARQUIVO = "eletropostos.txt";

    public void salvar(ArrayList<Eletroposto> lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            
            for (Eletroposto e : lista) {
                String linha = e.getId() + ";" +
                               e.getNome() + ";" +
                               e.getLocalizacao() + ";" +
                               e.getCidadeId() + ";" +
                               e.getTiposConectoresDisponiveis() + ";" +
                               e.getPotenciaCargaKw() + ";" +
                               e.getPrecoPorKwh() + ";" +
                               e.getVagasDisponiveis();
                
                writer.write(linha);
                writer.newLine();
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar eletropostos: " + e.getMessage());
        }
    }

    public ArrayList<Eletroposto> carregar() {
        ArrayList<Eletroposto> lista = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);

        if (!arquivo.exists()) {
            return lista;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(";");
                
                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                String localizacao = dados[2];
                int cidadeId = Integer.parseInt(dados[3]);
                String conectores = dados[4];
                double potencia = Double.parseDouble(dados[5]);
                double preco = Double.parseDouble(dados[6]);
                int vagas = Integer.parseInt(dados[7]);

                Eletroposto eletroposto = new Eletroposto(id, nome, localizacao, cidadeId, conectores, potencia, preco, vagas);
                lista.add(eletroposto);
            }
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar eletropostos: " + e.getMessage());
        }

        return lista;
    }
}
