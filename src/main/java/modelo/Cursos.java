package modelo;
public class Cursos {
    private int idCurso;
    private String nome;
    private int cargaHoraria;
    private int limiteAlunos;
    private boolean ativo;
    private int quantidadeAlunos;

    public static boolean validarTamanhoNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        
        return nome.trim().length() >= 3;
    }

    public static boolean validarCargaHoraria(int cargaHoraria) {
        return cargaHoraria >= 20;
    }

    public Cursos() {}

    public Cursos(int idCurso, String nome, int cargaHoraria, int limiteAlunos, boolean ativo) {
        this.idCurso = idCurso;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.limiteAlunos = limiteAlunos;
        this.ativo = ativo;

    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public int getLimiteAlunos() {
        return limiteAlunos;
    }

    public void setLimiteAlunos(int limiteAlunos) {
        this.limiteAlunos = limiteAlunos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(int quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }

    @Override
    public String toString() {
        return nome;
    }
}
