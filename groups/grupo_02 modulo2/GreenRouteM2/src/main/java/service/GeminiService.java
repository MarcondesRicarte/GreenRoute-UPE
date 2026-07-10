package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiService implements IAPlannerService {

    private String apiKey;
    private String modelo;

    public GeminiService() {

        Map<String, String> env =
                carregarEnv();

        apiKey =
                buscarVariavel(env, "GEMINI_API_KEY");

        modelo =
                buscarVariavel(env, "GEMINI_MODEL");

        if (modelo == null || modelo.trim().isEmpty()) {
            modelo = "gemini-2.5-flash";
        }
    }

    @Override
    public String interpretarCadastroRapido(String entidade, String textoLivre) {

        if (entidade == null || textoLivre == null || textoLivre.trim().isEmpty()) {
            throw new IllegalArgumentException("Digite as informações para o cadastro rápido por IA.");
        }

        String entidadeMinuscula =
                entidade.toLowerCase();

        if (entidadeMinuscula.contains("cidade")) {
            return enviarPrompt(montarPromptCidade(textoLivre));
        }

        if (entidadeMinuscula.contains("ve")) {
            return enviarPrompt(montarPromptVeiculo(textoLivre));
        }

        if (entidadeMinuscula.contains("eletroposto")) {
            return enviarPrompt(montarPromptEletroposto(textoLivre));
        }

        throw new IllegalArgumentException("Tipo de cadastro não reconhecido.");
    }

    @Override
    public String gerarPlanejamentoRota(String dadosRota) {

        if (dadosRota == null || dadosRota.trim().isEmpty()) {
            throw new IllegalArgumentException("Não há dados suficientes para planejar a rota.");
        }

        String prompt =
                "Você é a IA do sistema GreenRoute.\n" +
                        "Analise os dados da rota abaixo e dê uma resposta clara para o usuário.\n" +
                        "Considere autonomia, bateria, distância, clima simulado, trânsito simulado e eletropostos cadastrados.\n" +
                        "Diga se a viagem é possível, se precisa de recarga e quais cuidados tomar.\n" +
                        "Responda em português, de forma objetiva.\n\n" +
                        dadosRota;

        return enviarPrompt(prompt);
    }

    private String montarPromptCidade(String textoLivre) {

        return "Extraia os dados de uma cidade para cadastro no sistema GreenRoute.\n" +
                "Responda somente neste formato, sem explicação e sem markdown:\n" +
                "id=\n" +
                "nome=\n" +
                "estado=\n" +
                "distancia=\n" +
                "Use apenas número no campo distancia.\n" +
                "Se não souber o id, deixe vazio.\n\n" +
                "Texto:\n" +
                textoLivre;
    }

    private String montarPromptVeiculo(String textoLivre) {

        return "Extraia os dados de um veículo para cadastro no sistema GreenRoute.\n" +
                "Responda somente neste formato, sem explicação e sem markdown:\n" +
                "id=\n" +
                "tipo=\n" +
                "modelo=\n" +
                "autonomia=\n" +
                "bateria=\n" +
                "consumoKwh=\n" +
                "tempoRecarga=\n" +
                "conector=\n" +
                "tempoRapido=\n" +
                "tanque=\n" +
                "consumoCombustivel=\n" +
                "combustivel=\n" +
                "O tipo deve ser Elétrico ou Híbrido.\n" +
                "Use apenas números nos campos numéricos.\n" +
                "Se não souber algum dado, deixe vazio.\n\n" +
                "Texto:\n" +
                textoLivre;
    }

    private String montarPromptEletroposto(String textoLivre) {

        return "Extraia os dados de um eletroposto para cadastro no sistema GreenRoute.\n" +
                "Responda somente neste formato, sem explicação e sem markdown:\n" +
                "id=\n" +
                "nome=\n" +
                "localizacao=\n" +
                "cidadeId=\n" +
                "conectores=\n" +
                "potencia=\n" +
                "preco=\n" +
                "vagas=\n" +
                "Use apenas números nos campos id, cidadeId, potencia, preco e vagas.\n" +
                "Se não souber algum dado, deixe vazio.\n\n" +
                "Texto:\n" +
                textoLivre;
    }

    private String enviarPrompt(String prompt) {

        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("GEMINI_API_KEY não encontrada no arquivo .env.");
        }

        if (apiKey.startsWith("sk-")) {
            throw new IllegalStateException("Essa chave parece ser da OpenAI. Use uma chave do Google AI Studio para Gemini.");
        }

        try {

            String endpoint =
                    "https://generativelanguage.googleapis.com/v1beta/models/" +
                            modelo +
                            ":generateContent";

            URL url =
                    new URL(endpoint);

            HttpURLConnection conexao =
                    (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conexao.setRequestProperty("x-goog-api-key", apiKey);
            conexao.setDoOutput(true);
            conexao.setConnectTimeout(20000);
            conexao.setReadTimeout(30000);

            String corpo =
                    "{\"contents\":[{\"parts\":[{\"text\":\"" +
                            escaparJson(prompt) +
                            "\"}]}]}";

            try (OutputStream saida = conexao.getOutputStream()) {
                saida.write(corpo.getBytes(StandardCharsets.UTF_8));
            }

            int codigo =
                    conexao.getResponseCode();

            InputStream entrada;

            if (codigo >= 200 && codigo < 300) {
                entrada = conexao.getInputStream();
            } else {
                entrada = conexao.getErrorStream();
            }

            String resposta =
                    lerStream(entrada);

            if (codigo < 200 || codigo >= 300) {
                throw new IllegalStateException("Erro na API Gemini: " + codigo + " - " + resposta);
            }

            String texto =
                    extrairTextoResposta(resposta);

            if (texto == null || texto.trim().isEmpty()) {
                throw new IllegalStateException("A API Gemini não retornou texto válido.");
            }

            return texto.trim();

        } catch (IllegalStateException erro) {

            throw erro;

        } catch (Exception erro) {

            throw new IllegalStateException("Erro ao consultar a API Gemini: " + erro.getMessage());
        }
    }

    private Map<String, String> carregarEnv() {

        Map<String, String> valores =
                new HashMap<>();

        File arquivo =
                localizarEnv();

        if (arquivo == null) {
            return valores;
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                linha =
                        linha.trim();

                if (linha.isEmpty() || linha.startsWith("#") || !linha.contains("=")) {
                    continue;
                }

                int posicao =
                        linha.indexOf("=");

                String chave =
                        linha.substring(0, posicao).trim();

                String valor =
                        linha.substring(posicao + 1).trim();

                if (valor.startsWith("\"") && valor.endsWith("\"") && valor.length() >= 2) {
                    valor = valor.substring(1, valor.length() - 1);
                }

                valores.put(chave, valor);
            }

        } catch (Exception erro) {

            return valores;
        }

        return valores;
    }

    private File localizarEnv() {

        Path caminho =
                Paths.get("").toAbsolutePath();

        for (int i = 0; i < 6 && caminho != null; i++) {

            File arquivo =
                    caminho.resolve(".env").toFile();

            if (arquivo.exists()) {
                return arquivo;
            }

            caminho =
                    caminho.getParent();
        }

        return null;
    }

    private String buscarVariavel(Map<String, String> env, String chave) {

        String valorSistema =
                System.getenv(chave);

        if (valorSistema != null && !valorSistema.trim().isEmpty()) {
            return valorSistema.trim();
        }

        String valorArquivo =
                env.get(chave);

        if (valorArquivo != null && !valorArquivo.trim().isEmpty()) {
            return valorArquivo.trim();
        }

        return "";
    }

    private String lerStream(InputStream entrada) throws Exception {

        if (entrada == null) {
            return "";
        }

        StringBuilder conteudo =
                new StringBuilder();

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {
                conteudo.append(linha);
            }
        }

        return conteudo.toString();
    }

    private String extrairTextoResposta(String respostaJson) {

        Pattern padrao =
                Pattern.compile("\"text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"", Pattern.DOTALL);

        Matcher matcher =
                padrao.matcher(respostaJson);

        if (matcher.find()) {
            return limparTextoJson(matcher.group(1));
        }

        return "";
    }

    private String limparTextoJson(String texto) {

        StringBuilder resultado =
                new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {

            char caractere =
                    texto.charAt(i);

            if (caractere == '\\' && i + 1 < texto.length()) {

                char proximo =
                        texto.charAt(i + 1);

                if (proximo == 'n') {
                    resultado.append('\n');
                    i++;
                } else if (proximo == 't') {
                    resultado.append('\t');
                    i++;
                } else if (proximo == 'r') {
                    resultado.append('\r');
                    i++;
                } else if (proximo == '"') {
                    resultado.append('"');
                    i++;
                } else if (proximo == '\\') {
                    resultado.append('\\');
                    i++;
                } else if (proximo == 'u' && i + 5 < texto.length()) {

                    String codigo =
                            texto.substring(i + 2, i + 6);

                    try {
                        int valor =
                                Integer.parseInt(codigo, 16);

                        resultado.append((char) valor);
                        i += 5;

                    } catch (NumberFormatException erro) {
                        resultado.append(caractere);
                    }

                } else {
                    resultado.append(proximo);
                    i++;
                }

            } else {
                resultado.append(caractere);
            }
        }

        return resultado.toString();
    }

    private String escaparJson(String texto) {

        StringBuilder resultado =
                new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {

            char caractere =
                    texto.charAt(i);

            switch (caractere) {

                case '"':
                    resultado.append("\\\"");
                    break;

                case '\\':
                    resultado.append("\\\\");
                    break;

                case '\n':
                    resultado.append("\\n");
                    break;

                case '\r':
                    resultado.append("\\r");
                    break;

                case '\t':
                    resultado.append("\\t");
                    break;

                default:
                    resultado.append(caractere);
                    break;
            }
        }

        return resultado.toString();
    }
}