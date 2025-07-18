/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.*;
import java.util.*;

import factory.ConnectionFactory;
import modelo.Alunos;

public class AlunosDAO {
    private Connection connection;

    public AlunosDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }

    public void inserir(Alunos aluno) throws SQLException {
        String sql = "INSERT INTO alunos (nome, cpf, telefone, email, data_nascimento, ativo, idCurso) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getTelefone());
            stmt.setString(4, aluno.getEmail());
            stmt.setDate(5, aluno.getDataNascimento());
            stmt.setBoolean(6, aluno.isAtivo());
            stmt.setInt(7, aluno.getIdCurso());

            stmt.executeUpdate();

            // Recupera o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                aluno.setIdAluno(rs.getInt(1)); // Atualiza o objeto com o ID correto
            }

            rs.close();
        }
    }


    public List<Alunos> listarTodos(){
        List<Alunos> lista = new ArrayList<>();

        String sql = """
            SELECT a.idAluno, a.nome, a.cpf, a.telefone, a.email, a.data_nascimento,
                   a.ativo, a.idCurso, c.nome AS nomeCurso
            FROM alunos a
            JOIN cursos c ON a.idCurso = c.idCurso
        """;

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alunos aluno = new Alunos();
                aluno.setIdAluno(rs.getInt("idAluno"));
                aluno.setNome(rs.getString("nome"));
                aluno.setCpf(rs.getString("cpf"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setEmail(rs.getString("email"));
                aluno.setDataNascimento(rs.getDate("data_nascimento"));
                aluno.setAtivo(rs.getBoolean("ativo"));
                aluno.setIdCurso(rs.getInt("idCurso"));
                aluno.setNomeCurso(rs.getString("nomeCurso")); // novo campo
                lista.add(aluno);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public void deletar(int idAluno) {
        String sql = "DELETE FROM alunos WHERE idAluno = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar aluno", e);
        }
    }

        public void atualizar(Alunos aluno) {
        String sql = """
        UPDATE alunos SET nome = ?, cpf = ?, telefone = ?, email = ?, 
                          data_nascimento = ?, ativo = ?, idCurso = ?
        WHERE idAluno = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getTelefone());
            stmt.setString(4, aluno.getEmail());
            stmt.setDate(5, aluno.getDataNascimento());
            stmt.setBoolean(6, aluno.isAtivo());
            stmt.setInt(7, aluno.getIdCurso());
            stmt.setInt(8, aluno.getIdAluno());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno", e);
        }
    }

    public int contarAlunosPorCurso(int idCurso) {
        String sql = "SELECT COUNT(*) FROM alunos WHERE idCurso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar alunos por curso", e);
        }
        return 0;
    }

    public int contarAlunosAtivosPorCurso(int idCurso) {
        String sql = "SELECT COUNT(*) FROM alunos WHERE idCurso = ? AND ativo = true";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar alunos ativos por curso", e);
        }
        return 0;
    }

    public void setAlunosInativosPorCurso(int idCurso) {
        String sql = "UPDATE alunos SET ativo = false WHERE idCurso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inativar alunos do curso", e);
        }
    }
}

