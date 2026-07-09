package view;

import Gemini.CadastroVeiculoIA;
import controller.CidadeController;
import controller.EletropostoController;
import controller.RotaController;
import controller.VeiculoController;
import exceptions.CidadeNaoEncontradaException;
import exceptions.EletropostoNaoEncontradoException;
import exceptions.ValorInvalidoException;
import exceptions.VeiculoNaoEncontradoException;
import model.Cidade;
import model.Eletroposto;
import model.Veiculo;
import model.VeiculoEletrico;
import model.VeiculoHibrido;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private VeiculoController veiculoController;
    private EletropostoController eletropostoController;
    private CidadeController cidadeController;
    private RotaController rotaController;
    private CadastroVeiculoIA cadastroVeiculoIA;
    private Scanner sc = new Scanner(System.in);

    public Menu(VeiculoController veiculoController, EletropostoController eletropostoController, CidadeController cidadeController, RotaController rotaController, CadastroVeiculoIA cadastroVeiculoIA) {
        this.veiculoController = veiculoController;
        this.eletropostoController = eletropostoController;
        this.cidadeController = cidadeController;
        this.rotaController = rotaController;
        this.cadastroVeiculoIA = cadastroVeiculoIA;
    }


    // MENU PRINCIPAL
    public void exibirMenuPrincipal() {
        int opcao;
        while (true) {
            System.out.println("======== Menu Principal GreenRoute ========\n");
            System.out.println("Escolha uma das opções abaixo:\n");
            System.out.println("1 - Gerenciar seu Veículo");
            System.out.println("2 - Gerenciar sua Cidade");
            System.out.println("3 - Gerenciar seu Eletroposto");
            System.out.println("4 - Teste de Autonomia");
            System.out.println("0 - Fechar o Programa");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: exibirMenuVeiculos(); break;
                case 2: exibirMenuCidades(); break;
                case 3: exibirMenuEletropostos(); break;
                case 4: exibirMenuSimularRota(); break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }


    // MENU: VEÍCULOS
    public void exibirMenuVeiculos() {
        int opcao;
        while (true) {
            System.out.println("======== Menu de Veículos ========\n");
            System.out.println("1 - Cadastrar um Novo Veículo");
            System.out.println("2 - Listar todos os Veículos Registrados");
            System.out.println("3 - Atualizar Dados de um Veículo Registrado");
            System.out.println("4 - Apagar um Veículo Registrado");
            System.out.println("5 - Cadastro Rápido por IA");
            System.out.println("0 - Voltar para o Menu Principal");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: exibirMenuCadastrarVeiculos(); break;
                case 2: exibirMenuListarVeiculos(); break;
                case 3: exibirMenuAtualizarVeiculos(); break;
                case 4: exibirMenuApagarVeiculo(); break;
                case 5: exibirMenuCadastroRapidoIA(); break;
                case 0: return;
                default: System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    public void exibirMenuCadastrarVeiculos() {
        int opcao;
        while (true) {
            System.out.println("======== Cadastrar um Novo Veículo ========\n");
            System.out.println("1 - Elétrico");
            System.out.println("2 - Híbrido");
            System.out.println("0 - Retornar ao Menu Anterior");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: exibirMenuCadastrarVeiculoEletrico(); break;
                case 2: exibirMenuCadastrarVeiculoHibrido(); break;
                case 0: return;
                default: System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    public void exibirMenuCadastrarVeiculoEletrico() {
        try {
            System.out.println("======== Cadastrar Veículo Elétrico ========\n");
            System.out.print("Modelo: ");
            String modelo = sc.nextLine();
            System.out.print("Autonomia máxima (km): ");
            double autonomiaMaxima = sc.nextDouble();
            sc.nextLine();
            System.out.print("Carga da bateria (0 a 100): ");
            double cargaBateriaAtual = sc.nextDouble();
            sc.nextLine();
            System.out.print("Consumo kWh/km: ");
            double consumoKwhPorKm = sc.nextDouble();
            sc.nextLine();
            System.out.print("Tempo de recarga completa (min): ");
            int tempoRecargaCompleta = sc.nextInt();
            sc.nextLine();
            System.out.print("Tipo de conector (ex: Tipo2, CCS2, CHAdeMO): ");
            String tipoConector = sc.nextLine();
            System.out.print("Tempo de recarga rápida (min): ");
            int tempoRecargaRapida = sc.nextInt();
            sc.nextLine();

            VeiculoEletrico novoVeiculo = new VeiculoEletrico(0, modelo, autonomiaMaxima, cargaBateriaAtual, consumoKwhPorKm, tempoRecargaCompleta, tipoConector, tempoRecargaRapida);
            veiculoController.cadastrarVeiculoController(novoVeiculo);
            System.out.println("Veículo elétrico cadastrado com sucesso!");

        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
        }
    }

    public void exibirMenuCadastrarVeiculoHibrido() {
        try {
            System.out.println("======== Cadastrar Veículo Híbrido ========\n");
            System.out.print("Modelo: ");
            String modelo = sc.nextLine();
            System.out.print("Autonomia máxima (km): ");
            double autonomiaMaxima = sc.nextDouble();
            System.out.print("Carga da bateria (0 a 100): ");
            double cargaBateriaAtual = sc.nextDouble();
            System.out.print("Consumo kWh/km: ");
            double consumoKwhPorKm = sc.nextDouble();
            System.out.print("Tempo de recarga completa (min): ");
            int tempoRecargaCompleta = sc.nextInt();
            System.out.print("Consumo de combustível (km/l): ");
            double consumoCombustivel = sc.nextDouble();
            sc.nextLine();
            System.out.print("Tipo de combustível (ex: Gasolina, Etanol): ");
            String tipoCombustivel = sc.nextLine();
            System.out.print("Capacidade do tanque (litros): ");
            double capacidadeTanqueCombustivel = sc.nextDouble();
            sc.nextLine();

            VeiculoHibrido novoVeiculo = new VeiculoHibrido(0, modelo, autonomiaMaxima, cargaBateriaAtual, consumoKwhPorKm, tempoRecargaCompleta, capacidadeTanqueCombustivel, consumoCombustivel, tipoCombustivel);
            veiculoController.cadastrarVeiculoController(novoVeiculo);
            System.out.println("Veículo híbrido cadastrado com sucesso!");

        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
        }
    }

    public void exibirMenuListarVeiculos() {
        System.out.println("======== Lista de Veículos ========\n");
        ArrayList<Veiculo> veiculos = veiculoController.listarTodosVeiculosController();

        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo registrado.");
            return;
        }

        for (Veiculo veiculo : veiculos) {
            System.out.println("-----------------------------------");
            System.out.println("ID: " + veiculo.getId());
            System.out.println("Modelo: " + veiculo.getModelo());
            System.out.println("Autonomia Máxima: " + veiculo.getAutonomiaMaxima() + " km");
            System.out.println("Carga da Bateria: " + veiculo.getCargaBateriaAtual() + "%");
            System.out.println("Consumo kWh/km: " + veiculo.getConsumoKwhPorKm());
            System.out.println("Tempo de Recarga Completa: " + veiculo.getTempoRecargaCompleta() + " min");
            if (veiculo instanceof VeiculoEletrico) {
                VeiculoEletrico eletrico = (VeiculoEletrico) veiculo;
                System.out.println("Tipo: Elétrico");
                System.out.println("Tipo de Conector: " + eletrico.getTipoConector());
                System.out.println("Tempo de Recarga Rápida: " + eletrico.getTempoRecargaRapida() + " min");
            } else if (veiculo instanceof VeiculoHibrido) {
                VeiculoHibrido hibrido = (VeiculoHibrido) veiculo;
                System.out.println("Tipo: Híbrido");
                System.out.println("Capacidade do Tanque: " + hibrido.getCapacidadeTanqueCombustivel() + " L");
                System.out.println("Consumo de Combustível: " + hibrido.getConsumoCombustivel() + " km/l");
                System.out.println("Tipo de Combustível: " + hibrido.getTipoCombustivel());
            }
        }
        System.out.println("-----------------------------------");
    }

    public void exibirMenuAtualizarVeiculos() {
        try {
            System.out.println("======== Atualizar Veículo ========\n");
            System.out.print("Digite o ID do Veículo: ");
            int id = sc.nextInt();
            sc.nextLine();
            veiculoController.buscarVeiculoPorId(id);

            System.out.println("Tipo do veículo:\n1 - Elétrico\n2 - Híbrido");
            int tipoDoVeiculo = sc.nextInt();
            sc.nextLine();

            System.out.print("Modelo: ");
            String modelo = sc.nextLine();
            System.out.print("Autonomia máxima (km): ");
            double autonomiaMaxima = sc.nextDouble();
            System.out.print("Carga da bateria (0 a 100): ");
            double cargaBateriaAtual = sc.nextDouble();
            System.out.print("Consumo kWh/km: ");
            double consumoKwhPorKm = sc.nextDouble();
            System.out.print("Tempo de recarga completa (min): ");
            int tempoRecargaBateria = sc.nextInt();
            sc.nextLine();

            if (tipoDoVeiculo == 1) {
                System.out.print("Tipo do conector (ex: Tipo2, CCS2, CHAdeMO): ");
                String tipoDoConector = sc.nextLine();
                System.out.print("Tempo de recarga rápida (min): ");
                int tempoRecargaRapida = sc.nextInt();
                sc.nextLine();
                VeiculoEletrico veiculoAtualizado = new VeiculoEletrico(id, modelo, autonomiaMaxima, cargaBateriaAtual, consumoKwhPorKm, tempoRecargaBateria, tipoDoConector, tempoRecargaRapida);
                veiculoController.atualizarVeiculoController(id, veiculoAtualizado);
            } else if (tipoDoVeiculo == 2) {
                System.out.print("Capacidade do tanque (litros): ");
                double capacidadeTanque = sc.nextDouble();
                System.out.print("Consumo de combustível (km/l): ");
                double consumoCombustivel = sc.nextDouble();
                sc.nextLine();
                System.out.print("Tipo de combustível: ");
                String tipoCombustivel = sc.nextLine();
                VeiculoHibrido veiculoAtualizado = new VeiculoHibrido(id, modelo, autonomiaMaxima, cargaBateriaAtual, consumoKwhPorKm, tempoRecargaBateria, capacidadeTanque, consumoCombustivel, tipoCombustivel);
                veiculoController.atualizarVeiculoController(id, veiculoAtualizado);
            } else {
                System.out.println("Tipo inválido! Nenhuma alteração realizada.");
                return;
            }
            System.out.println("Veículo atualizado com sucesso!");

        } catch (VeiculoNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao atualizar veículo: " + e.getMessage());
        }
    }

    public void exibirMenuApagarVeiculo() {
        try {
            System.out.println("======== Apagar Veículo ========\n");
            System.out.print("Digite o ID do veículo que deseja apagar: ");
            int id = sc.nextInt();
            sc.nextLine();
            veiculoController.apagarVeiculoController(id);
            System.out.println("Veículo apagado com sucesso!");
        } catch (VeiculoNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void exibirMenuCadastroRapidoIA() {
        System.out.println("\n======== Cadastro Rápido com IA ========");
        System.out.println("Descreva o veículo em texto corrido (modelo, tipo, autonomia, etc.):");
        String descricao = sc.nextLine();

        System.out.println("\nConsultando com o Gemini...");
        try {
            Veiculo veiculo = cadastroVeiculoIA.interpretarDescricao(descricao);

            System.out.println("\n--- Dados interpretados pela IA ---");
            System.out.println("Modelo: " + veiculo.getModelo());
            System.out.println("Autonomia: " + veiculo.getAutonomiaMaxima() + " km");
            System.out.println("Carga da bateria: " + veiculo.getCargaBateriaAtual() + "%");

            veiculoController.cadastrarVeiculoController(veiculo);
            System.out.println("\nVeículo cadastrado com sucesso!");

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("\nNão foi possível cadastrar o veículo automaticamente.");
            System.out.println("Motivo: " + e.getMessage());
            System.out.println("Tente novamente ou cadastre manualmente pelas opções 1 ou 2.");
        }
    }


    // MENU: CIDADES
    public void exibirMenuCidades() {
        int opcao;
        while (true) {
            System.out.println("======== Menu de Cidades ========\n");
            System.out.println("1 - Cadastrar uma Nova Cidade");
            System.out.println("2 - Listar todas as Cidades Registradas");
            System.out.println("3 - Atualizar uma Cidade Registrada");
            System.out.println("4 - Apagar uma Cidade Registrada");
            System.out.println("0 - Voltar para o Menu Principal");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: exibirMenuCadastrarCidade(); break;
                case 2: exibirMenuListarCidades(); break;
                case 3: exibirMenuAtualizarCidade(); break;
                case 4: exibirMenuApagarCidade(); break;
                case 0: return;
                default: System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    public void exibirMenuCadastrarCidade() {
        try {
            System.out.println("======== Cadastrar uma Nova Cidade ========");
            System.out.print("Nome da cidade: ");
            String nome = sc.nextLine();
            System.out.print("Estado (sigla, ex: PE): ");
            String estado = sc.nextLine();
            System.out.print("Distância da capital (km): ");
            double distanciaCapital = sc.nextDouble();
            sc.nextLine();

            Cidade novaCidade = new Cidade(0, nome, estado, distanciaCapital);
            cidadeController.cadastrarCidadeController(novaCidade);
            System.out.println("Cidade cadastrada com sucesso!");

        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao cadastrar cidade: " + e.getMessage());
        }
    }

    public void exibirMenuListarCidades() {
        System.out.println("======== Lista de Cidades ========");
        ArrayList<Cidade> cidades = cidadeController.listarCidadesController();

        if (cidades.isEmpty()) {
            System.out.println("Nenhuma cidade cadastrada.");
            return;
        }

        for (Cidade cidade : cidades) {
            System.out.println("-----------------------------------");
            System.out.println("ID: " + cidade.getId());
            System.out.println("Nome: " + cidade.getNome());
            System.out.println("Estado: " + cidade.getEstado());
            System.out.println("Distância da capital: " + cidade.getDistanciaDaCapital() + " km");
        }
        System.out.println("-----------------------------------");
    }

    public void exibirMenuAtualizarCidade() {
        try {
            System.out.println("\n======== Atualizar Cidade ========");
            System.out.print("Digite o ID da cidade que deseja atualizar: ");
            int id = sc.nextInt();
            sc.nextLine();
            cidadeController.buscarCidadePorIdController(id);

            System.out.print("Novo nome: ");
            String novoNome = sc.nextLine();
            System.out.print("Novo estado (sigla): ");
            String novoEstado = sc.nextLine();
            System.out.print("Nova distância da capital (km): ");
            double novaDistancia = sc.nextDouble();
            sc.nextLine();

            Cidade cidadeAtualizada = new Cidade(id, novoNome, novoEstado, novaDistancia);
            cidadeController.atualizarCidadeController(id, cidadeAtualizada);
            System.out.println("Cidade atualizada com sucesso!");

        } catch (CidadeNaoEncontradaException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao atualizar cidade: " + e.getMessage());
        }
    }

    public void exibirMenuApagarCidade() {
        try {
            System.out.println("\n======== Apagar Cidade ========");
            System.out.print("Digite o ID da cidade que deseja apagar: ");
            int id = sc.nextInt();
            sc.nextLine();
            cidadeController.apagarCidadeController(id);
            System.out.println("Cidade apagada com sucesso!");
        } catch (CidadeNaoEncontradaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // MENU: ELETROPOSTOS
    public void exibirMenuEletropostos() {
        int opcao;
        while (true) {
            System.out.println("\n======== Menu de Eletropostos ========");
            System.out.println("1 - Cadastrar um Novo Eletroposto");
            System.out.println("2 - Listar todos os Eletropostos Registrados");
            System.out.println("3 - Atualizar um Eletroposto Registrado");
            System.out.println("4 - Apagar um Eletroposto Registrado");
            System.out.println("0 - Voltar para o Menu Principal");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: exibirMenuCadastrarEletroposto(); break;
                case 2: exibirMenuListarEletropostos(); break;
                case 3: exibirMenuAtualizarEletroposto(); break;
                case 4: exibirMenuApagarEletroposto(); break;
                case 0: return;
                default: System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    public void exibirMenuCadastrarEletroposto() {
        try {
            System.out.println("\n======== Cadastrar Eletroposto ========");
            System.out.print("Nome do eletroposto: ");
            String nome = sc.nextLine();
            System.out.print("Localização: ");
            String localizacao = sc.nextLine();
            System.out.print("ID da cidade onde está localizado: ");
            int cidadeId = sc.nextInt();
            sc.nextLine();
            cidadeController.buscarCidadePorIdController(cidadeId);

            System.out.print("Tipos de conectores (ex: Tipo2, CCS2): ");
            String tiposConectores = sc.nextLine();
            System.out.print("Potência de carga (kW): ");
            double potenciaCarga = sc.nextDouble();
            System.out.print("Preço por kWh (R$): ");
            double precoKwh = sc.nextDouble();
            System.out.print("Vagas disponíveis: ");
            int vagasDisponiveis = sc.nextInt();
            sc.nextLine();

            Eletroposto novoEletroposto = new Eletroposto(0, nome, localizacao, cidadeId, tiposConectores, potenciaCarga, precoKwh, vagasDisponiveis);
            eletropostoController.cadastrarEletropostoController(novoEletroposto);
            System.out.println("Eletroposto cadastrado com sucesso!");

        } catch (CidadeNaoEncontradaException e) {
            System.out.println("Erro: " + e.getMessage() + " Cadastre a cidade primeiro.");
        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao cadastrar eletroposto: " + e.getMessage());
        }
    }

    public void exibirMenuListarEletropostos() {
        System.out.println("======== Lista de Eletropostos ========");
        ArrayList<Eletroposto> eletropostos = eletropostoController.listarEletropostoController();

        if (eletropostos.isEmpty()) {
            System.out.println("Nenhum eletroposto cadastrado.");
            return;
        }

        for (Eletroposto e : eletropostos) {
            System.out.println("\n-----------------------------------");
            System.out.println("ID: " + e.getId());
            System.out.println("Nome: " + e.getNome());
            System.out.println("Localização: " + e.getLocalizacao());
            System.out.println("ID da cidade: " + e.getCidadeId());
            System.out.println("Conectores: " + e.getTiposConectoresDisponiveis());
            System.out.println("Potência: " + e.getPotenciaCargaKw() + " kW");
            System.out.println("Preço por kWh: R$" + e.getPrecoPorKwh());
            System.out.println("Vagas disponíveis: " + e.getVagasDisponiveis());
        }
        System.out.println("-----------------------------------");
    }

    public void exibirMenuAtualizarEletroposto() {
        try {
            System.out.println("\n======== Atualizar Eletroposto ========");
            System.out.print("Digite o ID do eletroposto que deseja atualizar: ");
            int id = sc.nextInt();
            sc.nextLine();
            eletropostoController.buscarEletropostoPorIdController(id);

            System.out.print("Novo nome: ");
            String novoNome = sc.nextLine();
            System.out.print("Nova localização: ");
            String novaLocalizacao = sc.nextLine();
            System.out.print("Novo ID da cidade: ");
            int novoCidadeId = sc.nextInt();
            sc.nextLine();
            System.out.print("Novos tipos de conectores: ");
            String novosTiposConectores = sc.nextLine();
            System.out.print("Nova potência de carga (kW): ");
            double novaPotencia = sc.nextDouble();
            System.out.print("Novo preço por kWh (R$): ");
            double novoPreco = sc.nextDouble();
            System.out.print("Novas vagas disponíveis: ");
            int novasVagas = sc.nextInt();
            sc.nextLine();

            Eletroposto atualizado = new Eletroposto(id, novoNome, novaLocalizacao, novoCidadeId, novosTiposConectores, novaPotencia, novoPreco, novasVagas);
            eletropostoController.atualizarEletropostoController(id, atualizado);
            System.out.println("Eletroposto atualizado com sucesso!");

        } catch (EletropostoNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao atualizar eletroposto: " + e.getMessage());
        }
    }

    public void exibirMenuApagarEletroposto() {
        try {
            System.out.println("\n======== Apagar Eletroposto ========");
            System.out.print("Digite o ID do eletroposto que deseja apagar: ");
            int id = sc.nextInt();
            sc.nextLine();
            eletropostoController.apagarEletropostoController(id);
            System.out.println("Eletroposto apagado com sucesso!");
        } catch (EletropostoNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // MENU: SIMULAÇÃO DE ROTA
    public void exibirMenuSimularRota() {
        System.out.println("\n======== Simular Autonomia ========");
        System.out.println("Digite o ID do veículo: ");
        int veiculoId = sc.nextInt();
        sc.nextLine();

        if (veiculoController.buscarVeiculoPorId(veiculoId) == null) {
            System.out.println("Nenhum veículo encontrado com o ID " + veiculoId + ". Verifique o ID.");
            return;
        }

        System.out.println("Digite o ID da cidade de destino: ");
        int cidadeId = sc.nextInt();
        sc.nextLine();

        if (cidadeController.buscarCidadePorIdController(cidadeId) == null) {
            System.out.println("Nenhuma cidade encontrada com o ID " + cidadeId + ". Verifique o ID.");
            return;
        }

        try {
            String resultado = rotaController.simularRota(veiculoId, cidadeId);
            System.out.println(resultado);
        } catch (exceptions.AutonomiaInsuficienteException | exceptions.ConectorIncompativelException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}