import controller.Controller;
import java.util.Scanner;
import model.*;

public class Main {
    private static Controller controller = new Controller();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarDadosExemplo();

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n======= GREENROUTE - MENU PRINCIPAL =======");
            System.out.println("1. Gerenciar Veículos (CRUD)");
            System.out.println("2. Gerenciar Eletropostos (CRUD)");
            System.out.println("3. Gerenciar Cidades (CRUD)");
            System.out.println("4. Simulação de Viagem (Regra de Negócio)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1: menuVeiculos(); break;
                case 2: menuEletropostos(); break;
                case 3: menuCidades(); break;
                case 4: executarSimulacao(); break;
                case 0: System.out.println("Saindo do sistema... Até logo!"); break;
                default: System.out.println("Opção inválida! Digite novamente.");
            }
        }
    }

    private static void inicializarDadosExemplo() {
        controller.cadastrarCidade(new Cidade(1, "Campinas", "SP", 90.0));
        controller.cadastrarCidade(new Cidade(2, "Ribeirão Preto", "SP", 315.0));
        
        controller.cadastrarVeiculo(new VeiculoEletrico(1, "BYD Dolphin", 300.0, 40.0, 0.15, 400, "CCS2", 30));
        controller.cadastrarVeiculo(new VeiculoHibrido(2, "Toyota Corolla Cross", 180.0, 100.0, 0.12, 120, 36.0, 17.0, "Gasolina"));
        
        controller.cadastrarEletroposto(new Eletroposto(101, "Posto Graal Shell", "Rod. Bandeirantes Km 120", 2, "CCS2, Tipo 2", 150.0, 1.95, 4));
    }


    private static void menuVeiculos() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- GERENCIAR VEÍCULOS ---");
            System.out.println("1. Cadastrar Veículo");
            System.out.println("2. Listar Veículos");
            System.out.println("3. Atualizar Veículo");
            System.out.println("4. Excluir Veículo");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            try { op = Integer.parseInt(scanner.nextLine()); } catch(Exception e) { op = -1; }

            switch (op) {
                case 1:
                    System.out.print("ID do Veículo: "); int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Modelo: "); String mod = scanner.nextLine();
                    System.out.print("Autonomia Máxima (km): "); double aut = Double.parseDouble(scanner.nextLine());
                    System.out.print("Carga Atual (% de 0 a 100): "); double carga = Double.parseDouble(scanner.nextLine());
                    System.out.print("Consumo kWh/Km: "); double cons = Double.parseDouble(scanner.nextLine());
                    System.out.print("Tempo Recarga Completa (min): "); int tempo = Integer.parseInt(scanner.nextLine());
                    
                    System.out.print("Tipo (1 - Elétrico / 2 - Híbrido): ");
                    int tipo = Integer.parseInt(scanner.nextLine());
                    if (tipo == 1) {
                        System.out.print("Tipo do Conector: "); String con = scanner.nextLine();
                        System.out.print("Tempo Recarga Rápida (min): "); int rap = Integer.parseInt(scanner.nextLine());
                        controller.cadastrarVeiculo(new VeiculoEletrico(id, mod, aut, carga, cons, tempo, con, rap));
                    } else {
                        System.out.print("Capacidade Tanque (L): "); double tan = Double.parseDouble(scanner.nextLine());
                        System.out.print("Consumo Combustível (km/L): "); double cComb = Double.parseDouble(scanner.nextLine());
                        System.out.print("Tipo Combustível: "); String tComb = scanner.nextLine();
                        controller.cadastrarVeiculo(new VeiculoHibrido(id, mod, aut, carga, cons, tempo, tan, cComb, tComb));
                    }
                    System.out.println("Veículo cadastrado com sucesso!");
                    break;
                case 2:
                    for (Veiculo v : controller.listarVeiculos()) System.out.println(v);
                    break;
                case 3:
                    System.out.print("Informe o ID do veículo para atualizar: ");
                    int idAt = Integer.parseInt(scanner.nextLine());
                    Veiculo existente = controller.buscarVeiculo(idAt);
                    if (existente != null) {
                        System.out.print("Novo Modelo: "); String m = scanner.nextLine();
                        existente.setModelo(m);
                        System.out.print("Nova Carga Atual (%): "); double c = Double.parseDouble(scanner.nextLine());
                        existente.setCargaBateriaAtual(c);
                        controller.atualizarVeiculo(idAt, existente);
                        System.out.println("Veículo atualizado!");
                    } else { System.out.println("Não encontrado!"); }
                    break;
                case 4:
                    System.out.print("ID para excluir: "); int idEx = Integer.parseInt(scanner.nextLine());
                    if (controller.excluirVeiculo(idEx)) System.out.println("Excluído!");
                    else System.out.println("Não encontrado!");
                    break;
            }
        }
    }

    private static void menuEletropostos() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- GERENCIAR ELETROPOSTOS ---");
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar");
            System.out.println("3. Excluir");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            try { op = Integer.parseInt(scanner.nextLine()); } catch(Exception e) { op = -1; }

            switch (op) {
                case 1:
                    System.out.print("ID: "); int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nome: "); String nome = scanner.nextLine();
                    System.out.print("Localização: "); String loc = scanner.nextLine();
                    System.out.print("ID Cidade: "); int cidId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Conectores: "); String cones = scanner.nextLine();
                    System.out.print("Potência (kW): "); double pot = Double.parseDouble(scanner.nextLine());
                    System.out.print("Preço por kWh: "); double preco = Double.parseDouble(scanner.nextLine());
                    System.out.print("Vagas: "); int vagas = Integer.parseInt(scanner.nextLine());
                    controller.cadastrarEletroposto(new Eletroposto(id, nome, loc, cidId, cones, pot, preco, vagas));
                    System.out.println("Cadastrado!");
                    break;
                case 2:
                    for (Eletroposto e : controller.listarEletropostos()) System.out.println(e);
                    break;
                case 3:
                    System.out.print("ID para excluir: "); int idEx = Integer.parseInt(scanner.nextLine());
                    if (controller.excluirEletroposto(idEx)) System.out.println("Excluído!");
                    break;
            }
        }
    }

    private static void menuCidades() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- GERENCIAR CIDADES ---");
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar");
            System.out.println("3. Excluir");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            try { op = Integer.parseInt(scanner.nextLine()); } catch(Exception e) { op = -1; }

            switch (op) {
                case 1:
                    System.out.print("ID (ou código IBGE): "); int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nome: "); String nome = scanner.nextLine();
                    System.out.print("Estado (UF): "); String uf = scanner.nextLine();
                    System.out.print("Distância da Capital (km): "); double dist = Double.parseDouble(scanner.nextLine());
                    controller.cadastrarCidade(new Cidade(id, nome, uf, dist));
                    System.out.println("Cidade cadastrada!");
                    break;
                case 2:
                    for (Cidade c : controller.listarCidades()) System.out.println(c);
                    break;
                case 3:
                    System.out.print("ID para excluir: "); int idEx = Integer.parseInt(scanner.nextLine());
                    if (controller.excluirCidade(idEx)) System.out.println("Excluída!");
                    break;
            }
        }
    }

    private static void executarSimulacao() {
        System.out.println("\n--- SIMULAR VIAGEM INTERMUNICIPAL ---");
        System.out.println("Veículos disponíveis:");
        for (Veiculo v : controller.listarVeiculos()) System.out.println(v);
        
        System.out.print("\nDigite o ID do veículo selecionado: ");
        int vId = Integer.parseInt(scanner.nextLine());

        System.out.println("\nCidades de destino disponíveis:");
        for (Cidade c : controller.listarCidades()) System.out.println(c);
        
        System.out.print("\nDigite o ID da cidade de destino: ");
        int cId = Integer.parseInt(scanner.nextLine());

        controller.simularViagem(vId, cId);
    }
}
