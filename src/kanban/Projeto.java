package kanban;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class Projeto {

    String nome;
    String descricao;
    Usuario responsavel;
    List<Tarefa> tarefas;
    Empresa empresa;
    private LocalDateTime dataHoraAbertura;
    private LocalDateTime dataHoraConclusao;
    private String usuarioResponsavelAcao;

    public Projeto(String nome, String descricao, Usuario responsavel, Empresa empresa) {
        this.nome = nome;
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.tarefas = new ArrayList<>();
        this.empresa = empresa;
        this.empresa.adicionarProjeto(this);
        this.dataHoraAbertura = LocalDateTime.now();
    }

    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        tarefa.setProjetoAssociado(this);
    }

    private void concluirTodasAsTarefas() {
        for (Tarefa tarefa : tarefas) {
            tarefa.concluirTarefa();
        }
    }

    public void concluirProjeto() {
        if (dataHoraConclusao == null) {
            this.dataHoraConclusao = LocalDateTime.now();
            concluirTodasAsTarefas();
        }
    }

    public void atualizarConclusaoProjeto() {
        boolean todasConcluidas = true;

        for (Tarefa tarefa : tarefas) {
            if (tarefa.getStatus() != Tarefa.StatusTarefa.CONCLUIDA) {
                todasConcluidas = false;
                break;
            }
        }

        if (todasConcluidas && dataHoraConclusao == null) {
            concluirProjeto();
        }
    }

    public Duration calcularDuracao() {
        if (dataHoraAbertura != null && dataHoraConclusao != null) {
            return Duration.between(dataHoraAbertura, dataHoraConclusao);
        } else if (dataHoraAbertura != null) {
            return Duration.between(dataHoraAbertura, LocalDateTime.now());
        } else {
            return Duration.ZERO;
        }
    }

    public void mostrarTarefas() {
        System.out.println("Tarefas do Projeto: " + nome + " - Descrição: " + descricao);
        for (Tarefa tarefa : tarefas) {
            System.out.println("Nome da Tarefa: " + tarefa.getNome() + " - % de Conclusão: " + tarefa.getPercentConclusao() + "%");
        }
    }

    public String getDescricao() {
        return descricao;
    }
    
    public boolean existeTarefa(String nomeTarefa) {
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getNome().equalsIgnoreCase(nomeTarefa)) {
                return true;
            }
        }
        return false;
    }
    
    public String getNome() {
        return nome;
    }
    
    public List<Tarefa> getTarefas() {
    return tarefas;
}

    public Tarefa encontrarTarefa(String nomeTarefa) {
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getNome().equals(nomeTarefa)) {
                return tarefa;
            }
        }
        return null;
    }

    public boolean podeSerVisualizadoPorUsuario(Usuario usuario) {
        return usuario.podeAcessarTodasAsAcoesETarefas() || this.responsavel.equals(usuario);
    }

    public boolean podeSerEditadoPorUsuario(Usuario usuario) {
        return usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR || usuario.equals(responsavel) || usuario.temPermissao(Usuario.Permissao.EDITAR);
    }

    public void mostrarDetalhes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm");
        System.out.println("Empresa Responsável: " + empresa.getNomeEmpresa());
        System.out.println("Detalhes do Projeto: " + nome + " - Descrição: " + descricao);

        int totalTarefas = tarefas.size();
        int totalAcoes = 0;
        double somaPercentagens = 0;

        for (Tarefa tarefa : tarefas) {
            totalAcoes += tarefa.getAcoes().size();
            for (Acao acao : tarefa.getAcoes()) {
                somaPercentagens += acao.getPercentConclusao();
                usuarioResponsavelAcao = acao.getUsuarioResponsavel();

                System.out.println("    * Nome da Ação: " + acao.getNome()
                        + "\n      - Descrição: " + acao.getDescricao()
                        + "\n      - Data de Início: " + acao.getDataInicio().format(formatter)
                        + "\n      - Data de Término: " + (acao.getDataTermino() != null ? acao.getDataTermino().format(formatter) : "Não concluída")
                        + "\n      - Departamento Responsável: " + acao.getAreaResponsavel()
                        + "\n      - Usuário Responsável: " + acao.getUsuarioResponsavel()
                        + "\n      - % de Conclusão: " + acao.getPercentConclusao() + "%"
                        + "\n      - Status: " + getStatusAcaoString(acao.getStatus())
                        + "\n      - Tempo Decorrido: " + formatarDuracao(acao.calcularDuracao())
                        + "\n");
            }

            System.out.println("  - Nome da Tarefa: " + tarefa.getNome() + " - % de Conclusão: " + tarefa.getPercentConclusao() + "%");
            System.out.println("    * Tempo Decorrido para a Tarefa " + tarefa.getNome() + ": " + formatarDuracao(tarefa.calcularDuracao()));

        }

        double percentagemTotal = totalAcoes > 0 ? somaPercentagens / totalAcoes : 0;

        System.out.println("% de Conclusão do Projeto: " + percentagemTotal + "%");

        System.out.println("Tempo Decorrido para o Projeto: " + formatarDuracao(calcularDuracao()));
    }

    private String getStatusAcaoString(Acao.StatusAcao status) {
        switch (status) {
            case NAO_INICIADA:
                return "Não Iniciada";
            case EM_ANDAMENTO:
                return "Em Andamento";
            case CONCLUIDA:
                return "Finalizado";
            default:
                return "Status Inválido";
        }
    }

    private String formatarDuracao(Duration duracao) {
        long hours = duracao.toHours();
        long minutes = duracao.toMinutesPart();
        long seconds = duracao.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
