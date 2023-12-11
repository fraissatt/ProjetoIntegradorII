package kanban;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.util.Calendar;

public class ProjetoIntegrador {

    private static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
    private static List<Usuario> usuarios = new ArrayList<>();
    private static Usuario usuarioLogado = null;
    private static List<Empresa> empresas = new ArrayList<>();
    private static List<Departamento> departamentos = new ArrayList<>();
    private static String usuarioResponsavelAcao;

    private static void inicializarPrograma() {
        Usuario adminGeral = new Usuario("Administrador Geral", "adm", "admsenha", TipoUsuario.ADMINISTRADOR);
        usuarios.add(adminGeral);
    }

    private static void encerrarPrograma() {
        System.out.println("Saindo do programa.");
        System.exit(0);
    }

    private static Empresa minhaEmpresa = null;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        KanbanBoard kanbanBoard = new KanbanBoard();

        inicializarPrograma();

        int opcao;
        do {
            exibirMenuPrincipal(input);

            try {
                opcao = input.nextInt();
                input.nextLine();

                switch (opcao) {
                    case 1:
                        if (usuarioLogado == null) {
                            System.out.println("Faça login primeiro.");
                        } else if (usuarioLogado != null && (usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR)) {
                            cadastrarNovoProjeto(input, empresas, kanbanBoard);
                        } else if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.COLABORADOR) {
                            alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
                        } else {
                            System.out.println("Você não tem permissão para cadastrar projetos ou alterar as porcentagem das ações.");
                        }
                        break;

                    case 2:
                        if (usuarioLogado == null) {
                            System.out.println("[1] - Prosseguir com login");
                            System.out.println("[0] - Sair");

                            int opcaoLogin = input.nextInt();
                            input.nextLine();

                            switch (opcaoLogin) {
                                case 1:
                                    usuarioLogado = fazerLogin(input);
                                    break;
                                case 0:
                                    encerrarPrograma();
                                    break;
                                default:
                                    System.out.println("Opção inválida. Tente novamente.");
                                    break;
                            }

                            if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                System.out.println("Administrador Geral logado com sucesso!");
                            }
                        } else {
                            adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
                        }
                        break;

                    case 3:
                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                            visualizarDetalhesProjeto(input, kanbanBoard);
                        } else {
                            System.out.println("Você não tem permissão para visualizar projetos.");
                        }
                        break;

                    case 4:
                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.COLABORADOR) {
                            alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
                        } else {
                            System.out.println("Você não tem permissão para alterar as porcentagem das ações.");
                        }
                        break;

                    case 5:
                        if (usuarioLogado == null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR || usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER) {
                            if (empresas.isEmpty()) {
                                System.out.println("Nenhuma empresa cadastrada.");
                            } else {
                                System.out.println("Empresas cadastradas:");
                                for (Empresa empresa : empresas) {
                                    System.out.println(empresa.getNomeEmpresa());
                                }
                            }
                        } else {
                            if (usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR || usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER) {
                                if (empresas.isEmpty()) {
                                    System.out.println("Nenhuma empresa cadastrada.");
                                } else {
                                    System.out.println("Empresas cadastradas:");
                                    for (Empresa empresa : empresas) {
                                        System.out.println(empresa.getNomeEmpresa());
                                    }
                                }
                            } else {
                                if (minhaEmpresa == null) {
                                    minhaEmpresa = criarEmpresa(input);
                                } else {
                                    System.out.println("Empresa já cadastrada: " + minhaEmpresa.getNomeEmpresa());
                                }
                            }
                        }
                        break;

                    case 6:
                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                            criarNovaEmpresa(input);
                        }
                        break;

                    case 7:
                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                            if (departamentos.isEmpty()) {
                                System.out.println("Nenhum departamento cadastrado.");
                            } else {
                                System.out.println("Departamentos cadastrados:");
                                for (Departamento departamento : departamentos) {
                                    System.out.println(departamento.getNomeDepartamento());
                                }
                            }
                        } else {
                            System.out.println("Você não tem permissão para listar os departamentos.");
                        }
                        listarDepartamentos(departamentos);
                        break;

                    case 8:
                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                            novoDepartamento(input);
                        }
                        break;

                    case 9:
                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                            cadastrarNovoUsuario(input);
                        }
                        break;

                    case 0:
                        if (usuarioLogado == null) {
                            encerrarPrograma();
                        } else {
                            usuarioLogado = null;
                            System.out.println("Desconectado. Voltando para a tela de login.");
                            exibirMenuPrincipal(input);
                            opcao = input.nextInt();
                            switch (opcao) {
                                case 1:
                                    if (usuarioLogado == null) {
                                        cadastrarNovoUsuario(input);
                                    } else if (usuarioLogado != null && (usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR)) {
                                        cadastrarNovoProjeto(input, empresas, kanbanBoard);
                                    } else {
                                        System.out.println("Você não tem permissão para cadastrar projetos.");
                                    }
                                    break;
                                case 2:
                                    if (usuarioLogado == null) {
                                        System.out.println("[1] - Prosseguir com login");
                                        System.out.println("[0] - Sair");

                                        int opcaoLogin = input.nextInt();
                                        input.nextLine();

                                        switch (opcaoLogin) {
                                            case 1:
                                                usuarioLogado = fazerLogin(input);
                                                break;
                                            case 0:
                                                encerrarPrograma();
                                                break;
                                            default:
                                                System.out.println("Opção inválida. Tente novamente.");
                                                break;
                                        }

                                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                            System.out.println("Administrador Geral logado com sucesso!");
                                        }
                                    } else {
                                        adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
                                    }
                                    break;
                                case 3:
                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                        visualizarDetalhesProjeto(input, kanbanBoard);
                                    } else {
                                        System.out.println("Você não tem permissão para visualizar projetos.");
                                    }
                                    break;
                                case 4:
                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.COLABORADOR) {
                                        alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
                                    } else {
                                        System.out.println("Você não tem permissão para alterar as porcentagem das ações.");
                                    }
                                    break;
                                case 0:
                                    if (usuarioLogado == null) {
                                        encerrarPrograma();
                                    } else {
                                        usuarioLogado = null;
                                        System.out.println("Desconectado. Voltando para a tela de login.");
                                        exibirMenuPrincipal(input);
                                        opcao = input.nextInt();
                                    }
                                    break;
                                default:
                                    if (usuarioLogado == null) {
                                        System.out.println("Faça login primeiro.");
                                        break;
                                    }
                                    if (usuarioLogado.podeAcessarOpcoesAvancadas()) {
                                        switch (opcao) {
                                            case 1:
                                                if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                                    cadastrarNovoProjeto(input, empresas, kanbanBoard);
                                                } else {
                                                    System.out.println("Você não tem permissão para cadastrar projetos.");
                                                }
                                                break;
                                            case 2:
                                                if (usuarioLogado == null) {
                                                    System.out.println("[1] - Prosseguir com login");
                                                    System.out.println("[0] - Sair");

                                                    int opcaoLogin = input.nextInt();
                                                    input.nextLine();

                                                    switch (opcaoLogin) {
                                                        case 1:
                                                            usuarioLogado = fazerLogin(input);
                                                            break;
                                                        case 0:
                                                            encerrarPrograma();
                                                            break;
                                                        default:
                                                            System.out.println("Opção inválida. Tente novamente.");
                                                            break;
                                                    }

                                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                                        System.out.println("Administrador Geral logado com sucesso!");
                                                    }
                                                } else {
                                                    adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
                                                }
                                                break;
                                            case 3:
                                                if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                                    visualizarDetalhesProjeto(input, kanbanBoard);
                                                } else {
                                                    System.out.println("Você não tem permissão para visualizar projetos.");
                                                }
                                                break;
                                            case 4:
                                                if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.COLABORADOR) {
                                                    alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
                                                } else {
                                                    System.out.println("Você não tem permissão para alterar as porcentagem das ações.");
                                                }
                                                break;
                                            case 0:
                                                usuarioLogado = null;
                                                System.out.println("Desconectado. Voltando para a tela de login.");
                                                break;
                                            default:
                                                System.out.println("Opção inválida. Tente novamente.");
                                        }
                                    } else {
                                        System.out.println("Você não tem permissão para acessar essas opções.");
                                    }
                            }
                        }
                        break;
                    default:
                        if (usuarioLogado == null) {
                            System.out.println("Faça login primeiro.");
                            break;
                        }

                        if (usuarioLogado.podeAcessarOpcoesAvancadas()) {
                            switch (opcao) {
                                case 1:
                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                        cadastrarNovoProjeto(input, empresas, kanbanBoard);
                                    } else {
                                        System.out.println("Você não tem permissão para cadastrar projetos.");
                                    }
                                    break;
                                case 2:
                                    if (usuarioLogado == null) {
                                        System.out.println("[1] - Prosseguir com login");
                                        System.out.println("[0] - Sair");

                                        int opcaoLogin = input.nextInt();
                                        input.nextLine();

                                        switch (opcaoLogin) {
                                            case 1:
                                                usuarioLogado = fazerLogin(input);
                                                break;
                                            case 0:
                                                encerrarPrograma();
                                                break;
                                            default:
                                                System.out.println("Opção inválida. Tente novamente.");
                                                break;
                                        }

                                        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                            System.out.println("Administrador Geral logado com sucesso!");
                                        }
                                    } else {
                                        adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
                                    }
                                    break;
                                case 3:
                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                        visualizarDetalhesProjeto(input, kanbanBoard);
                                    } else {
                                        System.out.println("Você não tem permissão para visualizar projetos.");
                                    }
                                    break;
                                case 4:
                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.COLABORADOR) {
                                        alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
                                    } else {
                                        System.out.println("Você não tem permissão para alterar as porcentagem das ações.");
                                    }
                                    break;
                                case 0:
                                    usuarioLogado = null;
                                    System.out.println("Desconectado. Voltando para a tela de login.");
                                    break;
                                default:
                                    System.out.println("Opção inválida. Tente novamente.");
                            }
                        } else {
                            System.out.println("Você não tem permissão para acessar essas opções.");
                        }
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número válido.");
                input.nextLine(); // Limpar o buffer
                opcao = -1; // Atribuir um valor inválido para continuar no loop
            }
        } while (opcao != 0);
    }

    private static void exibirMenuPrincipal(Scanner input) {
        if (usuarioLogado == null) {
            System.out.println("Escolha uma opção:");
            System.out.println("[2] - Fazer login");
            System.out.println("[0] - Sair");
        } else {
            switch (usuarioLogado.getTipoUsuario()) {
                case ADMINISTRADOR:
                    System.out.println("=============== MENU DO ADM ===================");
                    System.out.println("[1] - Inserir novo projeto");
                    System.out.println("[2] - Adicionar mais tarefas e ações a um projeto existente");
                    System.out.println("[3] - Visualizar detalhes de um projeto existente");
                    System.out.println("[4] - Alterar porcentagem ou status de uma ação existente");
                    System.out.println("[5] - Visualizar empresas cadastradas");
                    System.out.println("[6] - Cadastrar empresa");
                    System.out.println("[7] - Visualizar departamentos cadastrados");
                    System.out.println("[8] - Cadastrar departamento");
                    System.out.println("[9] - Cadastrar novo usuário");
                    System.out.println("[0] - Sair");
                    break;
                case LIDER:
                    System.out.println("=============== MENU DO LIDER ===================");
                    System.out.println("[1] - Inserir novo projeto");
                    System.out.println("[2] - Adicionar mais tarefas e ações a um projeto existente");
                    System.out.println("[3] - Visualizar detalhes de um projeto existente");
                    System.out.println("[0] - Sair");
                    break;
                case COLABORADOR:
                    System.out.println("=============== MENU DO COLABORADOR ===================");
                    System.out.println("[1] - Alterar porcentagem ou status de uma ação existente");
                    System.out.println("[0] - Sair");
                    break;
            }
            System.out.println("===================================================");
        }
        System.out.print("Opção: ");
    }

    private static void alterarPorcentagemEStatusAcaoExistente(Scanner input, KanbanBoard kanbanBoard, Usuario usuarioLogado) {
        System.out.println("Projetos disponíveis para alterar a porcentagem ou status de uma ação:");
        kanbanBoard.mostrarProjetos();

        System.out.println("Digite o nome do projeto que contém a ação que deseja alterar:");
        String nomeProjeto = input.nextLine();

        Projeto projetoExistente = kanbanBoard.encontrarProjeto(nomeProjeto);

        if (projetoExistente != null && projetoExistente.podeSerEditadoPorUsuario(usuarioLogado) && usuarioLogado.temPermissao(Usuario.Permissao.EDITAR)) {
            System.out.println("Digite o nome da tarefa que contém a ação que deseja alterar:");
            String nomeTarefa = input.nextLine();

            Tarefa tarefaExistente = projetoExistente.encontrarTarefa(nomeTarefa);

            if (tarefaExistente != null && tarefaExistente.podeSerEditadaPorUsuario(usuarioLogado) && usuarioLogado.temPermissao(Usuario.Permissao.EDITAR)) {
                System.out.println("Digite o nome da ação que deseja alterar:");
                String nomeAcao = input.nextLine();

                Acao acaoExistente = tarefaExistente.encontrarAcao(nomeAcao);

                if (acaoExistente != null && acaoExistente.podeSerEditadaPorUsuario(usuarioLogado) && usuarioLogado.temPermissao(Usuario.Permissao.EDITAR)) {
                    // Verificar se o usuário logado é o responsável pela ação
                    if (acaoExistente.usuarioLogadoEhResponsavel(usuarioLogado)) {
                        // Solicitar a nova descrição da ação
                        System.out.println("Digite a nova descrição da ação:");
                        String novaDescricao = input.nextLine();

                        // Alterar porcentagem e status
                        alterarPorcentagemEStatus(acaoExistente, input, novaDescricao);
                    } else {
                        System.out.println("Você não é o responsável por esta ação e não pode editá-la.");
                    }
                } else {
                    System.out.println("Ação não encontrada ou você não tem permissão para editá-la.");
                }
            } else {
                System.out.println("Tarefa não encontrada ou você não tem permissão para editá-la.");
            }
        } else {
            System.out.println("Projeto não encontrado ou você não tem permissão para editá-lo.");
        }
    }

    private static void alterarPorcentagemEStatus(Acao acao, Scanner input, String novaDescricao) {
        try {
            System.out.println("Deseja alterar a porcentagem ou o status da ação? ([1] - Porcentagem, [3] - Nenhum):");
            int opcao = input.nextInt();
            input.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    System.out.println("Digite a nova porcentagem da ação (de 0 a 100):");
                    double novaPorcentagem = input.nextDouble();
                    input.nextLine(); // Limpar o buffer
                    if (novaPorcentagem < 0 || novaPorcentagem > 100) {
                        throw new IllegalArgumentException("A porcentagem deve estar no intervalo de 0 a 100.");
                    }

                    // Atualizar a porcentagem
                    acao.setPercentConclusao(novaPorcentagem);

                    // Atualizar o status com base na nova porcentagem
                    if (novaPorcentagem == 0) {
                        acao.setStatus(Acao.StatusAcao.NAO_INICIADA);
                    } else if (novaPorcentagem < 100) {
                        acao.setStatus(Acao.StatusAcao.EM_ANDAMENTO);
                    } else {
                        acao.setStatus(Acao.StatusAcao.CONCLUIDA);

                        // Definir a data de término ao atingir 100% de conclusão
                        acao.setDescricao(novaDescricao);
                        acao.definirDataTerminoAoConcluir();
                    }
                    break;
                default:

                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, insira um número válido.");
            input.nextLine(); // Limpar o buffer em caso de entrada inválida
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void criarNovaEmpresa(Scanner input) {
        if (usuarioLogado != null && usuarioLogado.podeCriarNovaEmpresa()) {
            try {
                System.out.println("Digite o nome da empresa:");
                String nomeEmpresa = input.nextLine();

                // Loop para garantir que o usuário forneça um ID válido
                int idEmpresa;
                while (true) {
                    try {
                        System.out.println("Digite o ID (senha) da empresa:");
                        idEmpresa = Integer.parseInt(input.nextLine());
                        break;  // Saia do loop se a conversão for bem-sucedida
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido. Por favor, digite um número válido.");
                    }
                }

                Empresa novaEmpresa = new Empresa(idEmpresa, nomeEmpresa);
                empresas.add(novaEmpresa);

                System.out.println("Empresa cadastrada com sucesso: " + novaEmpresa.getNomeEmpresa());
            } catch (NullPointerException e) {
                System.out.println("Entrada inválida. Certifique-se de fornecer um nome válido para a empresa.");
            }
        } else {
            System.out.println("Você não tem permissão para criar uma nova empresa.");
        }
    }

    private static void novoDepartamento(Scanner input) {
        if (usuarioLogado != null && usuarioLogado.podeCriarDepartamento()) {
            System.out.println("Digite o nome do novo departamento:");
            String nomeDepartamento = input.nextLine();

            // Outros dados do departamento podem ser solicitados aqui, dependendo do seu sistema
            Departamento novoDepartamento = new Departamento(nomeDepartamento);
            departamentos.add(novoDepartamento); // Adiciona o novo departamento à lista
            System.out.println("Departamento cadastrado com sucesso: " + novoDepartamento.getNomeDepartamento());
        } else {
            System.out.println("Você não tem permissão para criar um novo departamento.");
        }
    }

    private static void listarDepartamentos(List<Departamento> departamentos) {
        System.out.println("Departamentos disponíveis:");
        for (int i = 0; i < departamentos.size(); i++) {
            System.out.println("[" + (i + 1) + "] - " + departamentos.get(i).getNomeDepartamento());
        }
    }

    private static void cadastrarNovoProjeto(Scanner input, List<Empresa> empresas, KanbanBoard kanbanBoard) {
    try {
        if (empresas.isEmpty()) {
            System.out.println("Nenhuma empresa cadastrada. Crie uma empresa antes de adicionar projetos.");
            return;
        }

        System.out.println("Escolha a empresa para a qual deseja adicionar um novo projeto:");
        listarEmpresas(empresas);

        int opcaoEmpresa = input.nextInt();
        input.nextLine(); // Consumir a quebra de linha

        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            System.out.println("Opção inválida. Tente novamente.");
            return;
        }

        Empresa minhaEmpresa = empresas.get(opcaoEmpresa - 1);

        System.out.println("Digite o ID (senha) da empresa:");
        int senhaEmpresa = input.nextInt();
        input.nextLine(); // Consumir a quebra de linha

        if (!minhaEmpresa.verificarSenha(senhaEmpresa)) {
            System.out.println("Senha da empresa incorreta. Operação cancelada.");
            return;
        }

        boolean projetoNomeValido = false;
        Projeto projeto = null;

        while (!projetoNomeValido) {
            System.out.println("Digite o nome do projeto:");
            String nomeProjeto = input.nextLine().trim();

            if (nomeProjeto.isEmpty()) {
                System.out.println("Nome do projeto não pode ser vazio. Tente novamente.");
            } else if (existeProjeto(nomeProjeto, minhaEmpresa.getProjetos())) {
                System.out.println("Já existe um projeto com esse nome na empresa. Escolha um nome único.");
            } else {
                projetoNomeValido = true;
                System.out.println("Digite uma pequena descrição do projeto:");
                String descricaoProjeto = input.nextLine().trim();
                if (descricaoProjeto.isEmpty()) {
                    throw new IllegalArgumentException("Descrição do projeto não pode ser vazia");
                }

                Usuario responsavelProjeto = usuarioLogado;

                projeto = new Projeto(nomeProjeto, descricaoProjeto, responsavelProjeto, minhaEmpresa);

                cadastrarTarefasAcoes(input, projeto);

                // Adicionando o projeto à empresa
                minhaEmpresa.adicionarProjeto(projeto);

                // Adicionando o projeto ao kanbanBoard
                kanbanBoard.adicionarProjeto(projeto);

                kanbanBoard.mostrarProjetos();
            }
        }
    } catch (IllegalArgumentException e) {
        System.out.println("Erro ao cadastrar projeto: " + e.getMessage());
    }
}
    
    private static boolean existeProjeto(String nomeProjeto, List<Projeto> projetos) {
        for (Projeto projeto : projetos) {
            if (projeto.getNome().equalsIgnoreCase(nomeProjeto)) {
                return true;
            }
        }
        return false;
    }

    private static void listarEmpresas(List<Empresa> empresas) {
        System.out.println("Empresas disponíveis:");
        for (int i = 0; i < empresas.size(); i++) {
            System.out.println("[" + (i + 1) + "] - " + empresas.get(i).getNomeEmpresa());
        }
        System.out.print("Escolha: ");
    }

    private static void visualizarDetalhesProjeto(Scanner input, KanbanBoard kanbanBoard) {
        System.out.println("Projetos disponíveis para visualização:");
        kanbanBoard.mostrarProjetos();

        System.out.println("Digite o nome do projeto que deseja visualizar:");
        String nomeProjeto = input.nextLine();

        Projeto projetoExistente = kanbanBoard.encontrarProjeto(nomeProjeto);

        if (projetoExistente != null && projetoExistente.podeSerVisualizadoPorUsuario(usuarioLogado)) {
            projetoExistente.mostrarDetalhes();
        } else {
            System.out.println("Projeto não encontrado ou você não tem permissão para visualizá-lo.");
        }
    }

    private static void cadastrarTarefasAcoes(Scanner input, Projeto projeto) {
    try {
        String adicionarTarefa;
        do {
            System.out.println("Deseja adicionar uma nova tarefa ao projeto? (Digite 'Sim' ou 'Não' e pressione Enter)");
            adicionarTarefa = input.nextLine().trim();

            if (!adicionarTarefa.equalsIgnoreCase("Sim") && !adicionarTarefa.equalsIgnoreCase("Não")) {
                System.out.println("Opção inválida. Por favor, responda com 'Sim' ou 'Não'.");
            }
        } while (!adicionarTarefa.equalsIgnoreCase("Sim") && !adicionarTarefa.equalsIgnoreCase("Não"));

        if (adicionarTarefa.equalsIgnoreCase("Sim")) {
            boolean tarefaExistente;
            do {
                System.out.println("Digite o nome da nova tarefa:");
                String nomeTarefa = input.nextLine().trim();

                if (projeto.existeTarefa(nomeTarefa)) {
                    System.out.println("Já existe uma tarefa com esse nome. Escolha um nome único.");
                    tarefaExistente = true;
                } else {
                    Tarefa tarefa = new Tarefa(nomeTarefa, usuarioLogado);
                    projeto.adicionarTarefa(tarefa);

                    // Agora, permita adicionar ações à nova tarefa
                    System.out.println("Quantas ações deseja cadastrar para a tarefa?");
                    int numAcoes = input.nextInt();
                    input.nextLine(); // Limpar o buffer

                    for (int j = 0; j < numAcoes; j++) {
                        cadastrarAcao(input, tarefa);
                    }

                    tarefaExistente = false;
                }
            } while (tarefaExistente);
        } else if (adicionarTarefa.equalsIgnoreCase("Não")) {
            System.out.println("Projeto criado sem tarefas e ações.");
        }
    } catch (InputMismatchException e) {
        System.out.println("Por favor, insira apenas uma das opções válidas.");
        input.nextLine(); // Limpar o buffer
    }
}

    private static void adicionarTarefasAcoesProjetoExistente(Scanner input, KanbanBoard kanbanBoard) {
        try {
            System.out.println("Projetos disponíveis para adicionar tarefas e ações:");
            kanbanBoard.mostrarProjetos();

            System.out.println("Digite o nome do projeto ao qual deseja adicionar tarefas e ações:");
            String nomeProjeto = input.nextLine();

            Projeto projetoExistente = kanbanBoard.encontrarProjeto(nomeProjeto);

            if (projetoExistente != null && projetoExistente.podeSerEditadoPorUsuario(usuarioLogado)) {
                cadastrarTarefasAcoes(input, projetoExistente);
            } else {
                System.out.println("Projeto não encontrado ou você não tem permissão para editá-lo.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, insira apenas uma das opções válidas.");
            input.nextLine(); // Limpar o buffer
        }
    }

    private static boolean validarDataInicio(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dataInicio = sdf.parse(data);

            // Obter a data de ontem
            Calendar ontem = Calendar.getInstance();
            ontem.add(Calendar.DAY_OF_MONTH, -1);

            // Verificar se a data de início é igual ou após ontem
            return !dataInicio.before(ontem.getTime());
        } catch (ParseException e) {
            return false;
        }
    }

    private static boolean validarDataTermino(String dataInicio, String dataTermino) {
        try {
            java.util.Date dataInicioDate = FORMATO_DATA.parse(dataInicio);
            java.util.Date dataTerminoDate = FORMATO_DATA.parse(dataTermino);
            return !dataTerminoDate.before(dataInicioDate);  // Retorna verdadeiro se a data de término for no mesmo dia ou depois da data de início
        } catch (ParseException e) {
            return false;  // Formato de data inválido
        }
    }

    private static void cadastrarAcao(Scanner input, Tarefa tarefa) {
        input.nextLine();  // Consumir a nova linha pendente
        System.out.println("Digite o nome da ação:");
        String nomeAcao = input.nextLine();

        System.out.println("Digite a descrição da ação:");
        String descricaoAcao = input.nextLine();

        try {
            System.out.println("Digite a data de início da ação (formato dd/MM/yyyy):");
            String dataInicioAcao = input.nextLine();
            while (!validarFormatoData(dataInicioAcao) || !validarDataInicio(dataInicioAcao)) {
                System.out.println("Data de início inválida ou anterior ao dia de hoje. Digite novamente (formato dd/MM/yyyy):");
                dataInicioAcao = input.nextLine();
            }

            // Utilizando o método listarDepartamentos para permitir que o usuário escolha um departamento
            listarDepartamentos(departamentos);

            int escolhaDepartamento = 0;
            boolean escolhaValida = false;

            while (!escolhaValida) {
                try {
                    System.out.print("Escolha o número do departamento responsável pela ação: ");
                    escolhaDepartamento = input.nextInt();
                    input.nextLine(); // Limpar o buffer do scanner

                    if (escolhaDepartamento > 0 && escolhaDepartamento <= departamentos.size()) {
                        escolhaValida = true;
                    } else {
                        System.out.println("Escolha inválida. Por favor, selecione um número válido.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, digite um número válido.");
                    input.nextLine(); // Limpar o buffer do scanner
                }
            }

            String areaResponsavelAcao = departamentos.get(escolhaDepartamento - 1).getNomeDepartamento();

            System.out.println(" ");
            listarUsuarios(usuarios);

            int escolhaUsuario = 0;
            boolean escolhaUsuarioValida = false;

            while (!escolhaUsuarioValida) {
                try {
                    System.out.print("Escolha o número do usuário responsável pela ação: ");
                    escolhaUsuario = input.nextInt();
                    input.nextLine(); // Limpar o buffer do scanner

                    if (escolhaUsuario > 1 && escolhaUsuario <= usuarios.size()) {
                        escolhaUsuarioValida = true;
                    } else {
                        System.out.println("Escolha inválida. Por favor, selecione um número válido (exceto 1, que é o Administrador).");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, digite um número válido.");
                    input.nextLine(); // Limpar o buffer do scanner
                }
            }

            String usuarioResponsavelAcao = null;
            String loginResponsavelAcao = null;

            if (escolhaUsuario > 0 && escolhaUsuario <= usuarios.size()) {
                Usuario usuarioEscolhido = usuarios.get(escolhaUsuario - 1);
                usuarioResponsavelAcao = usuarioEscolhido.getNome();
                loginResponsavelAcao = usuarioEscolhido.getLogin(); // Obtenha o login do usuário escolhido
            } else {
                System.out.println("Escolha inválida. Por favor, selecione um número válido.");
            }

            double percentConclusaoAcao = 0;
            boolean inputValido = false;
            while (!inputValido) {
                try {
                    System.out.println("Digite o percentual inicial da(s) ação/ações: ");
                    percentConclusaoAcao = input.nextDouble();

                    if (percentConclusaoAcao != 0) {
                        System.out.println("O percentual inicial deve ser 0.");
                    } else {
                        inputValido = true;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, digite um número válido.");
                    input.nextLine(); // Limpa o buffer do scanner
                }
            }

            // Lógica para definir o status com base na porcentagem
            Acao.StatusAcao statusEnum;
            statusEnum = null;
            if (percentConclusaoAcao == 0) {
                statusEnum = Acao.StatusAcao.NAO_INICIADA;
            } else if (percentConclusaoAcao < 100) {
                statusEnum = Acao.StatusAcao.EM_ANDAMENTO;
            } else if (percentConclusaoAcao == 100) {
                statusEnum = Acao.StatusAcao.CONCLUIDA;
            }

            // Criar uma nova Acao com o construtor adequado
            Acao acao = new Acao(nomeAcao, descricaoAcao, areaResponsavelAcao, usuarioResponsavelAcao, loginResponsavelAcao, null, percentConclusaoAcao, statusEnum);

            // Adicionar a nova Acao à Tarefa
            tarefa.adicionarAcao(acao);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar ação: " + e.getMessage());
        }
    }

    private static boolean validarFormatoData(String data) {
        try {
            FORMATO_DATA.parse(data);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    private static boolean existeLogin(String login, List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equalsIgnoreCase(login)) {
                return true;
            }
        }
        return false;
    }

    private static void cadastrarNovoUsuario(Scanner input) {
        // Verifica se o usuário logado é um administrador
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
            System.out.println("Apenas administradores podem cadastrar novos usuários.");
            return;
        }

        String nome = lerNome(input);

        // Tratamento para o login
        String login = null;
        boolean loginExistente = false;

        do {
            try {
                System.out.println("Digite o login do usuário:");
                login = input.nextLine().trim();

                if (login.isEmpty()) {
                    throw new IllegalArgumentException("Login não pode ser um espaço em branco");
                }

                // Verifica se o login já existe
                if (existeLogin(login, usuarios)) {
                    System.out.println("Já existe um usuário com esse login. Escolha um login único.");
                    loginExistente = true;
                } else {
                    loginExistente = false;
                }

            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                return; // Retorna para evitar o cadastro com valores inválidos
            }
        } while (loginExistente);

        // Tratamento para a senha
        String senha = null;
        try {
            System.out.println("Digite a senha do usuário:");
            senha = input.nextLine().trim();
            if (senha.isEmpty()) {
                throw new IllegalArgumentException("Senha não pode ser um espaço em branco");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return; // Retorna para evitar o cadastro com valores inválidos
        }

        // Tratamento para o tipo de usuário
        int tipoUsuario = 0;
        try {
            System.out.println("Digite o tipo de usuário ([1] Líder | [2] Colaborador):");
            String tipoUsuarioStr = input.nextLine().trim();
            if (tipoUsuarioStr.isEmpty()) {
                throw new IllegalArgumentException("Tipo de usuário não pode ser um espaço em branco");
            }
            tipoUsuario = Integer.parseInt(tipoUsuarioStr);

            if (tipoUsuario < 1 || tipoUsuario > 2) {
                throw new IllegalArgumentException("Tipo de usuário inválido. Será cadastrado como Colaborador.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return; // Retorna para evitar o cadastro com valores inválidos
        }

        TipoUsuario tipoUsuarioEnum;
        switch (tipoUsuario) {
            case 1:
                tipoUsuarioEnum = TipoUsuario.LIDER;
                break;
            case 2:
                tipoUsuarioEnum = TipoUsuario.COLABORADOR;
                break;
            default:
                System.out.println("Tipo de usuário inválido. Será cadastrado como Colaborador.");
                tipoUsuarioEnum = TipoUsuario.COLABORADOR;
        }

        Usuario novoUsuario = new Usuario(nome, login, senha, tipoUsuarioEnum);
        System.out.println("Usuário cadastrado com sucesso!");
        usuarios.add(novoUsuario);
    }

    private static void listarUsuarios(List<Usuario> usuarios) {
        System.out.println("Lista de Usuários:");

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);

            // Adiciona uma condição para não listar o Administrador
            if (usuario.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
                System.out.println("[" + (i + 1) + "] - Nome: " + usuario.getNome()
                        + "\nLogin: " + usuario.getLogin()
                        + "\nTipo de Usuário: " + usuario.getTipoUsuario()
                        + "\n");
            }
        }
    }

    private static Empresa criarEmpresa(Scanner input) {
        try {
            System.out.println("Digite o ID da empresa:");
            int idEmpresa = input.nextInt();
            input.nextLine(); // Consumir a quebra de linha

            String nomeEmpresa;
            while (true) {
                System.out.println("Digite o nome da empresa:");
                nomeEmpresa = input.nextLine();

                if (nomeEmpresa != null && !nomeEmpresa.trim().isEmpty()) {
                    break;  // Saia do loop se o nome não for vazio ou nulo
                } else {
                    System.out.println("Nome inválido. Por favor, forneça um nome válido.");
                }
            }

            return new Empresa(idEmpresa, nomeEmpresa);
        } catch (Exception e) {
            System.out.println("Entrada inválida. Certifique-se de fornecer valores válidos.");
            input.nextLine(); // Limpar o buffer do scanner
            return null;
        }
    }

    private static String lerNome(Scanner input) {
        System.out.println("Digite o nome do usuário:");
        while (true) {
            try {
                String nome = input.nextLine();
                if (nome.matches("[A-Za-zÀ-ÖØ-öø-ÿÇç ]+")) {
                    return nome;
                } else {
                    throw new IllegalArgumentException("Nome inválido. Use apenas letras, acentos e 'ç'.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int lerTipoUsuario(Scanner input) {
        System.out.println("Escolha o tipo de usuário (1 - Líder, 2 - Colaborador):");
        while (true) {
            try {
                int tipoUsuario = input.nextInt();
                input.nextLine(); // Limpar o buffer
                if (tipoUsuario >= 1 && tipoUsuario <= 2) {
                    return tipoUsuario;
                } else {
                    throw new IllegalArgumentException("Tipo de usuário inválido. Escolha entre 1 e 2.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Usuario fazerLogin(Scanner input) {
        System.out.println("Digite o login:");
        String login = input.nextLine();

        System.out.println("Digite a senha:");
        String senha = input.nextLine();

        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login) && usuario.getSenha().equals(senha)) {
                System.out.println("Login bem-sucedido. Bem-vindo, " + usuario.getNome() + "!");
                return usuario;
            }
        }

        System.out.println("Login falhou. Verifique suas credenciais.");
        return null;
    }
}
