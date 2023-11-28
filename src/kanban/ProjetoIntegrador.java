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
                    } else {
                        cadastrarNovoProjeto(input, kanbanBoard);
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
                    visualizarDetalhesProjeto(input, kanbanBoard);
                    break;
                    case 4:
                    alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
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
                    } else {
                        cadastrarNovoProjeto(input, kanbanBoard);
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
                    visualizarDetalhesProjeto(input, kanbanBoard);
                    break;
                    case 4:
                    alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
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
                                cadastrarNovoProjeto(input, kanbanBoard);
                                break;
                            case 2:
                                adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
                                break;
                            case 3:
                                visualizarDetalhesProjeto(input, kanbanBoard);
                                break;
                            case 4:
                                alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
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
                                cadastrarNovoProjeto(input, kanbanBoard);
                                break;
                            case 2:
                                adicionarTarefasAcoesProjetoExistente(input, kanbanBoard);
                                break;
                            case 3:
                                visualizarDetalhesProjeto(input, kanbanBoard);
                                break;
                            case 4:
                                alterarPorcentagemEStatusAcaoExistente(input, kanbanBoard, usuarioLogado);
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
            System.out.println("Deseja alterar a porcentagem ou o status da ação? ([1] - Porcentagem, [2] - Status, [3] - Nenhum):");
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
                    acao.setPercentConclusao(novaPorcentagem);
                    break;
                case 2:
                    System.out.println("Digite o novo status da ação (1 - Não Iniciada, 2 - Em Andamento, 3 - Concluída):");
                    int novoStatus = input.nextInt();
                    input.nextLine(); // Limpar o buffer
                    if (novoStatus < 1 || novoStatus > 3) {
                        throw new IllegalArgumentException("O status deve ser 1, 2 ou 3.");
                    }
                    acao.setStatus(getStatusEnum(novoStatus));
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

        private static Acao.StatusAcao getStatusEnum(int status) {
            switch (status) {
                case 1:
                    return Acao.StatusAcao.NAO_INICIADA;
                case 2:
                    return Acao.StatusAcao.EM_ANDAMENTO;
                case 3:
                    return Acao.StatusAcao.CONCLUIDA;
                default:
                    return Acao.StatusAcao.NAO_INICIADA;
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

            if (!projeto.tarefas.isEmpty()) {
                Tarefa primeiraTarefa = projeto.tarefas.get(0);

                if (!primeiraTarefa.getAcoes().isEmpty()) {
                    primeiraTarefa.getAcoes().get(0).setConcluida(true);
                    if (primeiraTarefa.getAcoes().size() > 1) {
                        primeiraTarefa.getAcoes().get(1).setConcluida(true);
                    }
                }
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

        System.out.println("Digite o % de conclusão da ação (de 0 a 100):");
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

        System.out.println("Digite o status da ação (1 - Não Iniciada, 2 - Em Andamento, 3 - Concluída):");
        int statusAcao = 0;
        inputValido = false;

        while (!inputValido) {
            try {
                statusAcao = Integer.parseInt(input.nextLine());

                if (statusAcao < 1 || statusAcao > 3) {
                    throw new IllegalArgumentException("Opção de status inválida. Ação será considerada Não Iniciada.");
                }

                inputValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        Acao.StatusAcao statusEnum;
        switch (statusAcao) {
            case 1:
                statusEnum = Acao.StatusAcao.NAO_INICIADA;
                break;
            case 2:
                statusEnum = Acao.StatusAcao.EM_ANDAMENTO;
                break;
            case 3:
                statusEnum = Acao.StatusAcao.CONCLUIDA;
                break;
            default:
                System.out.println("Opção de status inválida. Ação será considerada Não Iniciada.");
                statusEnum = Acao.StatusAcao.NAO_INICIADA;
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
            String nome = lerNome(input);

            System.out.println("Digite o login do usuário:");
            String login = input.nextLine();

            System.out.println("Digite a senha do usuário:");
            String senha = input.nextLine();

            String cpf = lerCPF(input);

            int tipoUsuario = lerTipoUsuario(input);

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