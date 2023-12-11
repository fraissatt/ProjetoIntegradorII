package kanban;

enum TipoUsuario {
    ADMINISTRADOR,
    LIDER,
    COLABORADOR
}

public class Usuario {
    private String nome;
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario;

    public enum Permissao {
        VISUALIZAR,
        EDITAR
    }

    public Usuario(String nome, String login, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean podeAcessarOpcoesAvancadas() {
        return tipoUsuario == TipoUsuario.ADMINISTRADOR || tipoUsuario == TipoUsuario.LIDER;
    }
    
    public boolean podeCriarNovaEmpresa() {
        return this.getTipoUsuario() == TipoUsuario.ADMINISTRADOR;
    }
    
    public boolean podeCriarDepartamento() {
        return this.getTipoUsuario() == TipoUsuario.ADMINISTRADOR;
    }

    public boolean podeAcessarProjetos() {
        return tipoUsuario == TipoUsuario.ADMINISTRADOR || tipoUsuario == TipoUsuario.LIDER;
    }

    public boolean podeAcessarTarefasAcoes() {
        return tipoUsuario == TipoUsuario.ADMINISTRADOR || tipoUsuario == TipoUsuario.COLABORADOR;
    }

    public boolean podeAcessarTodasAsAcoesETarefas() {
        return tipoUsuario == TipoUsuario.ADMINISTRADOR || tipoUsuario == TipoUsuario.LIDER;
    }

    public boolean podeEditarProjetos() {
        return tipoUsuario == TipoUsuario.ADMINISTRADOR;
    }

    public boolean temPermissao(Permissao permissao) {
        switch (this.tipoUsuario) {
            case ADMINISTRADOR:
                return true;
            case LIDER:
                return permissao == Permissao.VISUALIZAR;
            case COLABORADOR:
                return permissao == Permissao.EDITAR;
            default:
                return false;
        }
    }
}
