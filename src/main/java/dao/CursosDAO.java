/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import factory.ConnectionFactory;
import modelo.Cursos;

public class CursosDAO {
    private Connection connection;

    public CursosDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }

    public void inserir(Cursos curso) {
        String sql = "INSERT INTO cursos (nome, cargaHoraria, limiteAlunos, ativo) VALUES ( ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getCargaHoraria());
            stmt.setInt(3, curso.getLimiteAlunos());
            stmt.setBoolean(4, curso.isAtivo());
            stmt.executeUpdate();

            // Recupera o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                curso.setIdCurso(rs.getInt(1)); // Atualiza o objeto com o ID correto
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cursos> listarTodos() {
        List<Cursos> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos";


        try(PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                Cursos curso = new Cursos();
                curso.setIdCurso(rs.getInt("idCurso"));
                curso.setNome(rs.getString("nome"));
                curso.setCargaHoraria(rs.getInt("cargaHoraria"));
                curso.setLimiteAlunos(rs.getInt("limiteAlunos"));
                curso.setAtivo(rs.getBoolean("ativo"));
                cursos.add(curso);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cursos;
    }

    public Cursos buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cursos WHERE idCurso = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Cursos curso = null;
        if (rs.next()) {
            curso = new Cursos();
            curso.setIdCurso(rs.getInt("idCurso"));
            curso.setNome(rs.getString("nome"));
            curso.setCargaHoraria(rs.getInt("cargaHoraria"));
            curso.setLimiteAlunos(rs.getInt("limiteAlunos"));
            curso.setAtivo(rs.getBoolean("ativo"));
        }

        rs.close();
        stmt.close();
        return curso;
    }

    public void atualizar(Cursos curso) throws SQLException {
        String sql = "UPDATE cursos SET nome = ?, cargaHoraria = ?, limiteAlunos = ?, ativo = ? WHERE idCurso = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, curso.getNome());
        stmt.setInt(2, curso.getCargaHoraria());
        stmt.setInt(3, curso.getLimiteAlunos());
        stmt.setBoolean(4, curso.isAtivo());
        stmt.setInt(5, curso.getIdCurso());
        stmt.executeUpdate();
        stmt.close();
    }

    public void deletar(int idCurso) throws SQLException {
        String sql = "DELETE FROM cursos WHERE idCurso = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idCurso);
        stmt.executeUpdate();
        stmt.close();
    }

    public void setCursoAtivo(int idCurso, boolean ativo) {
        String sql = "UPDATE cursos SET ativo = ? WHERE idCurso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, ativo);
            stmt.setInt(2, idCurso);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do curso", e);
        }
        // Se inativar o curso, inativa todos os alunos desse curso
        if (!ativo) {
            new dao.AlunosDAO().setAlunosInativosPorCurso(idCurso);
        }
    }
}