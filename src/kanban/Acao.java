package kanban;

import java.time.LocalDateTime;
import java.time.Duration;

public class Acao {

    String nome;
    String descricao;
    LocalDateTime dataInicio;
    LocalDateTime dataTermino;
    String areaResponsavel;
    String usuarioResponsavel;
    String loginResponsavel;
    double percentConclusao;
    StatusAcao status;
    Tarefa tarefaAssociada;

    public enum StatusAcao {
        NAO_INICIADA,
        EM_ANDAMENTO,
        CONCLUIDA
    }

    public Acao(String nome, String descricao, String areaResponsavel, String usuarioResponsavel, String loginResponsavel, Tarefa tarefaAssociada, double percentConclusao, StatusAcao status) {
        this.nome = nome;
        this.descricao = descricao;
        this.areaResponsavel = areaResponsavel;
        this.usuarioResponsavel = usuarioResponsavel;
        this.tarefaAssociada = tarefaAssociada;
        this.percentConclusao = percentConclusao;
        this.status = status;
        this.dataInicio = LocalDateTime.now();
        this.loginResponsavel = loginResponsavel;
    }

    public void definirDataTerminoAoConcluir() {
        if (this.percentConclusao == 100) {
            this.status = StatusAcao.CONCLUIDA;
            this.dataTermino = LocalDateTime.now();
        }
    }

    public void concluirAcao() {
        this.status = StatusAcao.CONCLUIDA;
        this.percentConclusao = 100;
        this.dataTermino = LocalDateTime.now();

        if (tarefaAssociada != null) {
            tarefaAssociada.atualizarConclusaoTarefa();
        }

        Duration duracao = calcularDuracao();
        System.out.println("Data de Término: " + this.dataTermino);
        System.out.println("Tempo Decorrido: " + duracao.toMinutes() + " minutos");
    }
    
    public Duration calcularDuracao() {
    if (dataInicio != null && dataTermino != null) {
        return Duration.between(dataInicio, dataTermino);
    } else {
        return Duration.ZERO;
    }
}

    public boolean podeSerEditadaPorUsuario(Usuario usuario) {
        return true;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public boolean usuarioLogadoEhResponsavel(Usuario usuarioLogado) {
        return usuarioLogado != null && usuarioResponsavel.equals(usuarioLogado.getNome());
    }

    public LocalDateTime getDataTermino() {
        return dataTermino;
    }

    public String getAreaResponsavel() {
        return areaResponsavel;
    }

    public String getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public String getLoginResponsavel() {
        return loginResponsavel;
    }

    public double getPercentConclusao() {
        return percentConclusao;
    }

    public StatusAcao getStatus() {
        return status;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPercentConclusao(double novaPorcentagem) {
        this.percentConclusao = novaPorcentagem;

        if (tarefaAssociada != null) {
            tarefaAssociada.calcularPercentConclusao();
        }
    }

    public void setStatus(StatusAcao novoStatus) {
        this.status = novoStatus;

        if (tarefaAssociada != null) {
            tarefaAssociada.calcularStatusTarefa();
        }
    }

    public void associarTarefa(Tarefa tarefa) {
        this.tarefaAssociada = tarefa;
    }

    public void setStatusFromPercentage(double percentConclusaoAcao) {
        if (percentConclusaoAcao == 0) {
            this.status = StatusAcao.NAO_INICIADA;
        } else if (percentConclusaoAcao < 100) {
            this.status = StatusAcao.EM_ANDAMENTO;
        } else if (percentConclusaoAcao == 100) {
            this.status = StatusAcao.CONCLUIDA;
        }
    }
}
