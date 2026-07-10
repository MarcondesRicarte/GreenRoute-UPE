package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Cidade;

public class CidadePersistencia {

    // Define o nome do arquivo texto que guardará as cidades
    private final String NOME_ARQUIVO = "cidades.txt";

    // MÉTODO 1: Salva a lista inteira de cidades no arquivo TXT
    public void salvar(ArrayList<Cidade> lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            
            for (Cidade cidade : lista) {
                // Junta os atributos separados por ponto e vírgula (;)
                String linha = cidade.getId() + ";" +
                               cidade.getNome() + ";" +
                               cidade.getDistanciaDaCapital();
                
                writer.write(linha);
                writer.newLine(); // Pula para a próxima linha do arquivo
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar as cidades no arquivo: " + e.getMessage());
        }
    }

    // MÉTODO 2: Lê o arquivo TXT e reconstrói a lista de objetos Cidade na memória
    public ArrayList<Cidade> carregar() {
        ArrayList<Cidade> lista = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);

        // Se o arquivo ainda não existir (primeira vez rodando), devolve a lista vazia
        if (!arquivo.exists()) {
            return lista;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            
            // Lê o arquivo linha por linha
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue; // Pula linhas vazias por segurança

                // Recorta a linha nos pontos e vírgulas
                String[] dados = linha.split(";");
                
                // Transforma os textos de volta nos tipos certos
                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                double distancia = Double.parseDouble(dados[2]);

                // Instancia o objeto Cidade com os dados do arquivo e adiciona na lista
                Cidade cidade = new Cidade(id, nome, distancia);
                lista.add(cidade);
            }
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar as cidades do arquivo: " + e.getMessage());
        }

        return lista;
    }
}
