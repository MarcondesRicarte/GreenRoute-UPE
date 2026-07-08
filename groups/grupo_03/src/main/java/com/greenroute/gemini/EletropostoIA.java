package com.greenroute.gemini;

import com.greenroute.model.Eletroposto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EletropostoIA {

    private ConexaoGemini conexaoGemini;


    public EletropostoIA() {
        conexaoGemini = new ConexaoGemini();
    }
    public Eletroposto gerarEletroposto(String descricao) {

        String prompt = """
                Você é um assistente responsável por cadastrar eletropostos.

                A partir da descrição abaixo, extraia apenas:

                ID:
                Nome:
                Localização:
                Cidade ID:
                Conectores:
                Potência:
                Preço por kWh:
                Vagas Disponíveis:

                Responda exatamente neste formato:

                ID: ...
                Nome: ...
                Localização: ...
                Cidade ID: ...
                Conectores: ...
                Potência: ... kW
                Preço por kWh: ...
                Vagas Disponíveis: ...

                Caso não consiga identificar as informações, responda apenas:

                ERRO

                Descrição:
                """ + descricao;

        String resposta = conexaoGemini.perguntar(prompt);

        System.out.println(resposta);

        if (resposta == null || resposta.isBlank()) {
            throw new IllegalArgumentException(
                    "A IA não retornou nenhuma resposta."
            );
        }


        if (resposta.trim().equalsIgnoreCase("ERRO")) {
            throw new IllegalArgumentException(
                    "A IA não conseguiu interpretar a descrição."
            );
        }

        String[] linhas = resposta.split("\\R");

        if (linhas.length < 8) {
            throw new IllegalArgumentException(
                    "Resposta da IA em formato inválido."
            );
        }



        try {

            int id = Integer.parseInt(
                    linhas[0]
                            .replace("ID:", "")
                            .trim()
            );

            String nome = linhas[1]
                    .replace("Nome:", "")
                    .trim();

            String localizacao = linhas[2]
                    .replace("Localização:", "")
                    .trim();

            int cidadeId = Integer.parseInt(
                    linhas[3]
                            .replace("Cidade ID:", "")
                            .trim()
            );

            String conectores = linhas[4]
                    .replace("Conectores:", "")
                    .trim();

            double potencia = extrairNumero(linhas[5]);



            double preco = extrairNumero(linhas[6]);
            int vagas = Integer.parseInt(
                    linhas[7]
                            .replace("Vagas Disponíveis:", "")
                            .trim()
            );


            return new Eletroposto(
                    id,
                    nome,
                    localizacao,
                    cidadeId,
                    conectores,
                    potencia,
                    preco,
                    vagas
            );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Erro ao interpretar a resposta da IA."
            );
        }
    }



    // Pega o primeiro número (inteiro ou decimal) que aparecer na linha.
    // Ignora o que tiver junto, tipo "R$", "reais", "kWh", "kW".

    private double extrairNumero(String linha) {

        String linhaComPonto = linha.replace(",", ".");
        Pattern padrao = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");


        Matcher matcher = padrao.matcher(linhaComPonto);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }



        throw new IllegalArgumentException(
                "Não foi possível encontrar um número válido na resposta da IA."
        );


    }
}