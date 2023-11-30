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

    private static void encerrarPrograma() {
        System.out.println("Saindo do programa.");
        System.exit(0);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        KanbanBoard kanbanBoard = new KanbanBoard();

        int opcao;
        do {
            exibirMenuPrincipal();

            try {
                opcao = input.nextInt();
                input.nextLine(); // Limpar o buffer

                switch (opcao) {
                    case 1:
                        if (usuarioLogado == null) {
                            cadastrarNovoUsuario(input);

                        } else if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                            Empresa minhaEmpresa = criarEmpresa();
                            String nomeDaEmpresa = minhaEmpresa.getNomeEmpresa();
                            System.out.println("Nome da Empresa: " + nomeDaEmpresa);
                        } else if (usuarioLogado != null && (usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR)) {
                            cadastrarNovoProjeto(input, kanbanBoard);
                        } else {
                            System.out.println("Você não tem permissão para cadastrar projetos.");
                        }
                        break;
                    case 2:
                        if (usuarioLogado == null) {
                            usuarioLogado = fazerLogin(input);
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
                        }
                        break;
                    case 0:
                        if (usuarioLogado == null) {
                            encerrarPrograma();
                        } else {
                            usuarioLogado = null;
                            System.out.println("Desconectado. Voltando para a tela de login.");
                            exibirMenuPrincipal();
                            opcao = input.nextInt();
                            switch (opcao) {
                                case 1:
                                    if (usuarioLogado == null) {
                                        cadastrarNovoUsuario(input);
                                    } else if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                        cadastrarNovoProjeto(input, kanbanBoard);
                                    } else {
                                        System.out.println("Você não tem permissão para cadastrar projetos.");
                                    }
                                    break;
                                case 2:
                                    if (usuarioLogado == null) {
                                        usuarioLogado = fazerLogin(input);
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
                                    }
                                    break;
                                case 0:
                                    if (usuarioLogado == null) {
                                        encerrarPrograma();
                                    } else {
                                        usuarioLogado = null;
                                        System.out.println("Desconectado. Voltando para a tela de login.");
                                        exibirMenuPrincipal();
                                        opcao = input.nextInt();

                                    }
                                    break;
                                default:
                                    if (usuarioLogado == null) {
                                        System.out.println("Faça login primeiro.");
                                        break;
                                    }

                                    // Verificar permissões do usuário logado
                                    if (usuarioLogado.podeAcessarOpcoesAvancadas()) {
                                        // Exibir opções avançadas
                                        switch (opcao) {
                                            case 1:
                                                if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                                    cadastrarNovoProjeto(input, kanbanBoard);
                                                } else {
                                                    System.out.println("Você não tem permissão para cadastrar projetos.");
                                                }
                                                break;
                                            case 2:
                                                adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
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
                                                }
                                                break;
                                            case 0:
                                                // Voltar para a tela de login ao apertar 0 estando logado
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

                        // Verificar permissões do usuário logado
                        if (usuarioLogado.podeAcessarOpcoesAvancadas()) {
                            // Exibir opções avançadas
                            switch (opcao) {
                                case 1:
                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                        cadastrarNovoProjeto(input, kanbanBoard);
                                    } else {
                                        System.out.println("Você não tem permissão para cadastrar projetos.");
                                    }
                                    break;
                                case 2:
                                    if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.LIDER || usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                                        adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
                                    } else {
                                        System.out.println("Você não tem permissão para adicionar tarefas e/ou ações.");
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
                                    }
                                    break;
                                case 0:
                                    // Voltar para a tela de login ao apertar 0 estando logado
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

    private static void exibirMenuPrincipal() {
        if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            System.out.println("=== Cadastrar Empresa ===");
            Empresa minhaEmpresa = criarEmpresa();
            String nomeDaEmpresa = minhaEmpresa.getNomeEmpresa();
            System.out.println("Nome da Empresa: " + nomeDaEmpresa);
        }
        if (usuarioLogado == null) {
            System.out.println("Escolha uma opção:");
            System.out.println("[1] - Cadastrar novo usuário");
            System.out.println("[2] - Fazer login");
            System.out.println("[0] - Sair");
        } else {
            System.out.println("Escolha uma opção:");
            System.out.println("[1] - Inserir novo projeto");
            System.out.println("[2] - Adicionar mais tarefas e ações a um projeto existente");
            System.out.println("[3] - Visualizar detalhes de um projeto existente");
            System.out.println("[4] - Alterar porcentagem ou status de uma ação existente");
            System.out.println("[0] - Sair");
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
                    alterarPorcentagemEStatus(acaoExistente, input);
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

    private static void alterarPorcentagemEStatus(Acao acao, Scanner input) {
        try {
            System.out.println("Deseja alterar a porcentagem ou o status da ação? ([1] - Porcentagem, [3] - Nenhum):");
            int opcao = input.nextInt();
            input.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    System.out.println("Digite a nova porcentagem da ação (de 0 a 100):");
                    int novaPorcentagem = input.nextInt();
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
                    }
                    break;

                default:
                    // Nenhuma alteração
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, insira um número válido.");
            input.nextLine(); // Limpar o buffer em caso de entrada inválida
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cadastrarNovoProjeto(Scanner input, KanbanBoard kanbanBoard) {
        try {
            System.out.println("Digite o nome do projeto:");
            String nomeProjeto = input.nextLine();
            if (nomeProjeto.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do projeto não pode ser vazio");
            }
            System.out.println("Digite uma pequena descrição do projeto:");
            String descricaoProjeto = input.nextLine();

            if (descricaoProjeto.trim().isEmpty()) {
                throw new IllegalArgumentException("Descrição do projeto não pode ser vazia");
            }

            // Crie um usuário responsável pelo projeto (pode ser obtido de alguma forma)
            Usuario responsavelProjeto = usuarioLogado;  // Ou de alguma outra forma dependendo da lógica do seu sistema

            Projeto projeto = new Projeto(nomeProjeto, descricaoProjeto, responsavelProjeto);

            cadastrarTarefasAcoes(input, projeto);

            kanbanBoard.adicionarProjeto(projeto);
            kanbanBoard.mostrarProjetos();
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar projeto: " + e.getMessage());
        }
    }

    private static void adicionarTarefasAcoesProjetoExistente(Scanner input, KanbanBoard kanbanBoard) {
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
            System.out.println("Quantas tarefas deseja cadastrar para o projeto?");
            int numTarefas = input.nextInt();
            input.nextLine(); // Limpar o buffer

            for (int i = 0; i < numTarefas; i++) {
                System.out.println("Digite o nome da tarefa " + (i + 1) + ":");
                String nomeTarefa = input.nextLine();

                // Alteração aqui: fornecer o Usuario ao criar a Tarefa
                Tarefa tarefa = new Tarefa(nomeTarefa, usuarioLogado);

                System.out.println("Quantas ações deseja cadastrar para a tarefa?");
                int numAcoes = input.nextInt();
                input.nextLine(); // Limpar o buffer

                for (int j = 0; j < numAcoes; j++) {
                    cadastrarAcao(input, tarefa);
                }

                projeto.adicionarTarefa(tarefa);
            }

        } catch (InputMismatchException e) {
            System.out.println("Por favor, insira um número válido.");
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

    private static Date obterDataAtual() {
        return new Date();
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

            System.out.println("Digite a data de término da ação (formato dd/MM/yyyy):");
            String dataTerminoAcao = input.nextLine();
            while (!validarFormatoData(dataTerminoAcao) || !validarDataTermino(dataInicioAcao, dataTerminoAcao)) {
                System.out.println("Data de término inválida ou anterior à data de início. Digite novamente (formato dd/MM/yyyy):");
                dataTerminoAcao = input.nextLine();
            }

            System.out.println("Digite a área responsável pela ação:");
            String areaResponsavelAcao = input.nextLine();

            System.out.println("Digite o usuário responsável pela ação:");
            String usuarioResponsavelAcao = input.nextLine();

            
            
            System.out.println("Digite o percentual inicial da(s) ação/ações: ");
            int percentConclusaoAcao = 0;
            boolean inputValido = false;

            while (!inputValido) {
                try {
                    percentConclusaoAcao = Integer.parseInt(input.nextLine());

                    if (percentConclusaoAcao < 0 || percentConclusaoAcao > 100) {
                        throw new IllegalArgumentException("O percentual deve estar entre 0 e 100.");
                    }

                    inputValido = true;
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite um número válido.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
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
            Acao acao = new Acao(nomeAcao, descricaoAcao, dataInicioAcao, dataTerminoAcao, areaResponsavelAcao, usuarioResponsavelAcao, percentConclusaoAcao, statusEnum, null, null);

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

    private static void cadastrarNovoUsuario(Scanner input) {
    boolean cadastroValido = false;

    while (!cadastroValido) {
        String nome = null;
        try {
            nome = lerNome(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            continue; // Continua na validação do nome
        }

        String login = null;
        boolean loginValido = false;
        while (!loginValido) {
            try {
                System.out.println("Digite o login do usuário:");
                login = input.nextLine();
                if (login.trim().isEmpty()) {
                    throw new IllegalArgumentException("Login não pode ser um espaço em branco");
                }
                loginValido = true; // Se chegou aqui, o login é válido
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        String senha = null;
        boolean senhaValida = false;
        while (!senhaValida) {
            try {
                System.out.println("Digite a senha do usuário:");
                senha = input.nextLine();
                if (senha.trim().isEmpty()) {
                    throw new IllegalArgumentException("Senha não pode ser um espaço em branco");
                }
                senhaValida = true; // Se chegou aqui, a senha é válida
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        // Tratamento para o CPF
        String cpf = null;
        boolean formatoInvalido = true;
        while (formatoInvalido) {
            try {
                System.out.println("Digite o CPF (apenas números, 11 dígitos):");
                cpf = input.nextLine().trim();

                // Verifica se o CPF tem exatamente 11 dígitos
                if (cpf.length() == 11 && cpf.matches("\\d+")) {
                    formatoInvalido = false;
                } else {
                    throw new IllegalArgumentException("O CPF deve conter exatamente 11 números.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        int tipoUsuario = 0;
        boolean tipoUsuarioValido = false;
        while (!tipoUsuarioValido) {
            try {
                System.out.println("Digite o tipo de usuário (1 para Administrador, 2 para Líder, 3 para Colaborador):");
                String tipoUsuarioStr = input.nextLine().trim();
                if (tipoUsuarioStr.isEmpty()) {
                    throw new IllegalArgumentException("Tipo de usuário não pode ser um espaço em branco");
                }
                tipoUsuario = Integer.parseInt(tipoUsuarioStr);

                if (tipoUsuario < 1 || tipoUsuario > 3) {
                    throw new IllegalArgumentException("Tipo de usuário inválido. Será cadastrado como Colaborador.");
                }

                tipoUsuarioValido = true; // Se chegou aqui, o tipo de usuário é válido
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        TipoUsuario tipoUsuarioEnum;
        switch (tipoUsuario) {
            case 1:
                tipoUsuarioEnum = TipoUsuario.ADMINISTRADOR;
                break;
            case 2:
                tipoUsuarioEnum = TipoUsuario.LIDER;
                break;
            case 3:
                tipoUsuarioEnum = TipoUsuario.COLABORADOR;
                break;
            default:
                System.out.println("Tipo de usuário inválido. Será cadastrado como Colaborador.");
                tipoUsuarioEnum = TipoUsuario.COLABORADOR;
        }

        Usuario novoUsuario = new Usuario(nome, login, senha, cpf, tipoUsuarioEnum);
        // Adicione a lógica para armazenar o novo usuário (se necessário).

        System.out.println("Usuário cadastrado com sucesso!");
        usuarios.add(novoUsuario);
        cadastroValido = true;
    }
}

    private static Empresa criarEmpresa() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o nome da empresa:");
        String nomeEmpresa = scanner.nextLine();

        // Criando e retornando a instância da classe Empresa
        return new Empresa(nomeEmpresa);
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

    private static String lerCPF(Scanner input) {
        System.out.println("Digite o CPF do usuário:");
        while (true) {
            try {
                String cpf = input.nextLine();
                if (cpf.matches("\\d+")) {
                    return cpf;
                } else {
                    throw new IllegalArgumentException("CPF inválido. Use apenas números.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int lerTipoUsuario(Scanner input) {
        System.out.println("Escolha o tipo de usuário (1 - Administrador, 2 - Líder, 3 - Colaborador):");
        while (true) {
            try {
                int tipoUsuario = input.nextInt();
                input.nextLine(); // Limpar o buffer
                if (tipoUsuario >= 1 && tipoUsuario <= 3) {
                    return tipoUsuario;
                } else {
                    throw new IllegalArgumentException("Tipo de usuário inválido. Escolha entre 1 e 3.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Usuario fazerLogin(Scanner input) {
    int tentativas = 3;
    while (tentativas > 0) {
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

        System.out.println("Login falhou. Verifique suas credenciais. Tentativas restantes: " + (--tentativas));
    }

    System.out.println("Número máximo de tentativas atingido. Saindo do programa.");
    encerrarPrograma();
    return null;
}
}
