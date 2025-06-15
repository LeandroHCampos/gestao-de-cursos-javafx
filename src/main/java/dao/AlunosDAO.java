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
        String sql = "INSERT INTO alunos (idAluno, nome, cpf, telefone, email, data_nascimento, idCurso) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, aluno.getIdAluno());
        stmt.setString(2, aluno.getNome());
        stmt.setString(3, aluno.getCpf());
        stmt.setString(4, aluno.getTelefone());
        stmt.setString(5, aluno.getEmail());
        stmt.setDate(6, new java.sql.Date(aluno.getDataNascimento().getTime()));
        stmt.setInt(7, aluno.getIdCurso());
        stmt.executeUpdate();
        stmt.close();
    }

    public List<Alunos> listarTodos(){
        List<Alunos> lista = new ArrayList<>();

        String sql = "SELECT idAluno, nome, cpf, telefone, email, data_nascimento, idCurso FROM alunos";

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
                aluno.setIdCurso(rs.getInt("idCurso"));
                lista.add(aluno);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}

