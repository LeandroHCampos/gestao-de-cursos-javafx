package app.controller;

import dao.AlunosDAO;
import dao.CursosDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.Node;
import modelo.Alunos;
import modelo.Cursos;

import java.sql.SQLException;

public class MainController {
    @FXML private HBox alunosGraphic;
    @FXML private HBox cursosGraphic;
    @FXML private HBox dashboardGraphic;
    @FXML private VBox menuContainer;
    @FXML private Button toggleMenuBtn;
    @FXML private StackPane contentArea;
    @FXML private VBox dashboardView;
    @FXML private VBox cursosView;
    @FXML private VBox alunosView;
    @FXML private Text dashboardText;
    @FXML private Text cursosText;
    @FXML private Text alunosText;

    @FXML private TableView<Alunos> alunosTable;
    @FXML private TableColumn<Alunos, Integer> idAlunoColumn;
    @FXML private TableColumn<Alunos, String> alunoNomeColumn;
    @FXML private TableColumn<Alunos, String> alunoCpfColumn;
    @FXML private TableColumn<Alunos, String> alunoTelefoneColumn;
    @FXML private TableColumn<Alunos, String> alunoEmailColumn;
    @FXML private TableColumn<Alunos, String> alunoDateColumn;
    @FXML private TableColumn<Alunos, Integer> alunoCursoColumn;

    @FXML private TableView<Cursos> cursosTable;
    @FXML private TableColumn<Cursos, Integer> cursoIdColumn;
    @FXML private TableColumn<Cursos, String> cursoNomeColumn;
    @FXML private TableColumn<Cursos, Integer> cursoCargaHorariaColumn;
    @FXML private TableColumn<Cursos, Integer> cursoLimiteAlunosColumn;
    @FXML private TableColumn<Cursos, Boolean> cursoAtivoColumn;
    
    private boolean isMenuExpanded = true;
    private static final double EXPANDED_WIDTH = 200;
    private static final double COLLAPSED_WIDTH = 45;

    @FXML
    public void initialize() throws SQLException {
        // Inicialmente mostra o dashboard
        showDashboard();
        AlunosDAO alunosDAO = new AlunosDAO();
        CursosDAO cursosDAO = new CursosDAO();
        
        // Configura o menu inicial
        menuContainer.setPrefWidth(EXPANDED_WIDTH);

        idAlunoColumn.setCellValueFactory(new PropertyValueFactory<>("idAluno"));
        alunoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        alunoCpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        alunoTelefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        alunoEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        alunoDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        alunoCursoColumn.setCellValueFactory(new PropertyValueFactory<>("idCurso"));

        alunosTable.setItems(FXCollections.observableArrayList(alunosDAO.listarTodos()));

        cursoIdColumn.setCellValueFactory(new PropertyValueFactory<>("IdCurso"));
        cursoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cursoCargaHorariaColumn.setCellValueFactory(new PropertyValueFactory<>("cargaHoraria"));
        cursoLimiteAlunosColumn.setCellValueFactory(new PropertyValueFactory<>("limiteAlunos"));
        cursoAtivoColumn.setCellValueFactory(new PropertyValueFactory<>("ativo"));

        cursosTable.setItems(FXCollections.observableArrayList(cursosDAO.listarTodos()));

    }

    @FXML
    private void toggleMenu() {
        isMenuExpanded = !isMenuExpanded;
        menuContainer.setPrefWidth(isMenuExpanded ? EXPANDED_WIDTH : COLLAPSED_WIDTH);

        dashboardText.setVisible(isMenuExpanded);
        dashboardText.setManaged(isMenuExpanded);
        cursosText.setVisible(isMenuExpanded);
        cursosText.setManaged(isMenuExpanded);
        alunosText.setVisible(isMenuExpanded);
        alunosText.setManaged(isMenuExpanded);

        dashboardGraphic.setAlignment(isMenuExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
        cursosGraphic.setAlignment(isMenuExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
        alunosGraphic.setAlignment(isMenuExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
    }


    @FXML
    private void showDashboard() {
        dashboardView.setVisible(true);
        cursosView.setVisible(false);
        alunosView.setVisible(false);
    }

    @FXML
    private void showCursos() throws SQLException {
        dashboardView.setVisible(false);
        cursosView.setVisible(true);
        alunosView.setVisible(false);

        CursosDAO cursosDAO = new CursosDAO();

        cursoIdColumn.setCellValueFactory(new PropertyValueFactory<>("IdCurso"));
        cursoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cursoCargaHorariaColumn.setCellValueFactory(new PropertyValueFactory<>("cargaHoraria"));
        cursoLimiteAlunosColumn.setCellValueFactory(new PropertyValueFactory<>("limiteAlunos"));
        cursoAtivoColumn.setCellValueFactory(new PropertyValueFactory<>("ativo"));

        cursosTable.setItems(FXCollections.observableArrayList(cursosDAO.listarTodos()));

    }

    @FXML
    private void showAlunos() throws SQLException {
        dashboardView.setVisible(false);
        cursosView.setVisible(false);
        alunosView.setVisible(true);

        AlunosDAO alunosDAO = new AlunosDAO();

        idAlunoColumn.setCellValueFactory(new PropertyValueFactory<>("idAluno"));
        alunoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        alunoCpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        alunoTelefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        alunoEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        alunoDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        alunoCursoColumn.setCellValueFactory(new PropertyValueFactory<>("idCurso"));


        alunosTable.setItems(FXCollections.observableArrayList(alunosDAO.listarTodos()));
    }

    @FXML
    private void adicionarCurso() {
        // TODO: Implementar adição de curso
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adicionar Curso");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de adicionar curso será implementada em breve!");
        alert.showAndWait();
    }

    @FXML
    private void adicionarAluno() {
        // TODO: Implementar adição de aluno
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adicionar Aluno");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de adicionar aluno será implementada em breve!");
        alert.showAndWait();
    }
}