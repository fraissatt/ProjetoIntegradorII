package kanban;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tarefa {

    private String nome;
    private List<Acao> acoes;
    private double percentConclusao;
    private StatusTarefa status;
    private Usuario usuarioResponsavel;
    private LocalDateTime dataHoraAbertura;
    private LocalDateTime dataHoraConclusao;
    private boolean concluida;
    private Projeto projetoAssociado;

    public enum StatusTarefa {
        EM_ANDAMENTO,
        CONCLUIDA
    }

    public Tarefa(String nome, Usuario usuarioResponsavel) {
        this.nome = nome;
        this.acoes = new ArrayList<>();
        this.percentConclusao = 0;
        this.status = StatusTarefa.EM_ANDAMENTO;
        this.usuarioResponsavel = usuarioResponsavel;
        this.dataHoraAbertura = LocalDateTime.now();
    }

    public void adicionarAcao(Acao acao) {
        acoes.add(acao);
        calcularPercentConclusao();
        calcularStatusTarefa();
    }
    
    public void setProjetoAssociado(Projeto projeto) {
        this.projetoAssociado = projeto;
    }


    public void calcularPercentConclusao() {
        int totalAcoes = acoes.size();
        double somaPercentagens = 0;

        for (Acao acao : acoes) {
            somaPercentagens += acao.getPercentConclusao();
        }

        percentConclusao = totalAcoes > 0 ? somaPercentagens / totalAcoes : 0;
    }

    public void mostrarAcoes() {
        System.out.println("Ações da Tarefa: " + nome);
        for (Acao acao : acoes) {
            System.out.println(acao.getNome() + " - Concluída: " + acao.getStatus());
        }
    }

    public Acao encontrarAcao(String nomeAcao) {
        for (Acao acao : acoes) {
            if (acao.getNome().equals(nomeAcao)) {
                return acao;
            }
        }
        return null;
    }

    public void mostrarDetalhes() {
        System.out.println("Detalhes da Tarefa: " + nome);
        System.out.println("% de Conclusão: " + getPercentConclusao() + "%");
        System.out.println("Status: " + status);
        System.out.println("Tempo Decorrido: " + calcularDuracao());
    }

    public String getNome() {
        return nome;
    }

    public double getPercentConclusao() {
        calcularPercentConclusao();
        return percentConclusao;
    }

    public List<Acao> getAcoes() {
        return acoes;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void calcularStatusTarefa() {
        boolean todasConcluidas = true;

        for (Acao acao : acoes) {
            if (acao.getStatus() != Acao.StatusAcao.CONCLUIDA) {
                todasConcluidas = false;
                break;
            }
        }

        if (todasConcluidas && status != StatusTarefa.CONCLUIDA) {
            status = StatusTarefa.CONCLUIDA;
            concluirTarefa(); // Adiciona chamada para concluir a tarefa
        } else {
            status = StatusTarefa.EM_ANDAMENTO;
        }
    }

    public Duration calcularDuracao() {
    if (dataHoraAbertura != null && dataHoraConclusao != null) {
        LocalDateTime ultimaConclusao = dataHoraAbertura;

        for (Acao acao : acoes) {
            LocalDateTime conclusaoAcao = acao.getDataTermino();
            if (conclusaoAcao != null && conclusaoAcao.isAfter(ultimaConclusao)) {
                ultimaConclusao = conclusaoAcao;
            }
        }

        if (ultimaConclusao.isAfter(dataHoraAbertura)) {
            return Duration.between(dataHoraAbertura, ultimaConclusao);
        } else {
            return Duration.ZERO;
        }
    } else if (dataHoraAbertura != null) {
        return Duration.between(dataHoraAbertura, LocalDateTime.now());
    } else {
        return Duration.ZERO;
    }
}

    public void concluirTarefa() {
        if (!concluida) {
            this.concluida = true;
            this.dataHoraConclusao = LocalDateTime.now();

            if (projetoAssociado != null) {
                projetoAssociado.atualizarConclusaoProjeto();
            }
        }
    }
    
    public void atualizarConclusaoTarefa() {
        calcularPercentConclusao();

        boolean todasConcluidas = true;
        for (Acao acao : acoes) {
            if (acao.getStatus() != Acao.StatusAcao.CONCLUIDA) {
                todasConcluidas = false;
                break;
            }
        }

        if (todasConcluidas && status != StatusTarefa.CONCLUIDA) {
            status = StatusTarefa.CONCLUIDA;
            concluirTarefa();
        } else {
            status = StatusTarefa.EM_ANDAMENTO;
        }
    }

    public boolean podeSerVisualizadaPorUsuario(Usuario usuario) {
        return usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR || usuario.equals(usuarioResponsavel);
    }

    public boolean podeSerEditadaPorUsuario(Usuario usuario) {
        return (usuario.getTipoUsuario() == TipoUsuario.COLABORADOR && usuario.equals(usuarioResponsavel)) || usuario.temPermissao(Usuario.Permissao.EDITAR);
    }
}
