package app.controller;

import dao.AlunosDAO;
import dao.CursosDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    
    @FXML private Label totalCursosLabel;
    @FXML private Label totalAlunosLabel;
    
    @FXML
    public void initialize() {
        carregarEstatisticas();
    }
    
    public void carregarEstatisticas() {
        try {
            CursosDAO cursosDAO = new CursosDAO();
            AlunosDAO alunosDAO = new AlunosDAO();
            
            int totalCursos = cursosDAO.listarTodos().size();
            int totalAlunos = alunosDAO.listarTodos().size();
            
            totalCursosLabel.setText(String.valueOf(totalCursos));
            totalAlunosLabel.setText(String.valueOf(totalAlunos));
            
        } catch (Exception e) {
            totalCursosLabel.setText("0");
            totalAlunosLabel.setText("0");
            e.printStackTrace();
        }
    }
    
    public void atualizarEstatisticas() {
        carregarEstatisticas();
    }
}
