package modelo;

import java.sql.Date;

public class Alunos {
    private int idAluno;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Date dataNascimento;
    private boolean ativo;
    private int idCurso;
    private String nomeCurso;

    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) {
            return false;
        }
        
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : (11 - resto);
        
        if (Character.getNumericValue(cpf.charAt(9)) != digito1) {
            return false;
        }
        
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : (11 - resto);
        
        return Character.getNumericValue(cpf.charAt(10)) == digito2;
    }

    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    public static boolean validarIdadeMinima(Date dataNascimento) {
        if (dataNascimento == null) {
            return false;
        }
        
        java.time.LocalDate hoje = java.time.LocalDate.now();
        java.time.LocalDate dataNasc = dataNascimento.toLocalDate();
        
        int idade = hoje.getYear() - dataNasc.getYear();
        
        if (hoje.getMonthValue() < dataNasc.getMonthValue() || 
            (hoje.getMonthValue() == dataNasc.getMonthValue() && hoje.getDayOfMonth() < dataNasc.getDayOfMonth())) {
            idade--;
        }
        
        return idade >= 16;
    }

    public static boolean validarTamanhoNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        
        return nome.trim().length() >= 3;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
}
