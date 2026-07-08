package com.greenroute.gemini;

import com.greenroute.model.Cidade;



public class CidadeIA {

    private ConexaoGemini conexaoGemini;



    public CidadeIA() {
        conexaoGemini = new ConexaoGemini();
    }


    public Cidade gerarCidade(String descricao) {
        String prompt = """
                Você é um assistente responsável por cadastrar cidades.

                A partir da descrição abaixo, extraia apenas:

                ID:
                Nome:
                Estado:
                Distância da Capital:

                Responda exatamente neste formato:

                ID: ...
                Nome: ...
                Estado: ...
                Distância da Capital: ... km

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
                    "A IA não conseguiu interpretar a descrição informada."
            );
        }

        String[] linhas = resposta.split("\\R");


        if (linhas.length < 4) {
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

            String estado = linhas[2]
                    .replace("Estado:", "")
                    .trim();

            String distanciaTexto = linhas[3]
                    .replace("Distância da Capital:", "")
                    .replace("km", "")
                    .replace(",", ".")
                    .trim();

            double distancia = Double.parseDouble(distanciaTexto);

            return new Cidade(
                    id,
                    nome,
                    estado,
                    distancia
            );


        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "A IA retornou um ID ou uma distância inválidos."
            );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Erro ao interpretar a resposta da IA."
            );
        }

    }
}