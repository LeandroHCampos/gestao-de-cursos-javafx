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

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

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

    @FXML private VBox formularioAluno;
    @FXML private TextField campoNomeAluno;
    @FXML private TextField campoCpfAluno;
    @FXML private TextField campoTelefoneAluno;
    @FXML private TextField campoEmailAluno;
    @FXML private DatePicker campoDataNascimento;
    @FXML private ComboBox<Cursos> comboCurso;

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
    
    private boolean isMenuExpanded = false;
    private Alunos alunoEditando;
    private static final double EXPANDED_WIDTH = 200;
    private static final double COLLAPSED_WIDTH = 45;

    @FXML
    public void initialize() throws SQLException {
        carregarCursosNoComboBox();
        // Inicialmente mostra o dashboard
        showDashboard();
        
        // Configura o menu inicial
        menuContainer.setPrefWidth(COLLAPSED_WIDTH);
        
        // Configura a visibilidade inicial dos textos
        dashboardText.setVisible(false);
        dashboardText.setManaged(false);
        cursosText.setVisible(false);
        cursosText.setManaged(false);
        alunosText.setVisible(false);
        alunosText.setManaged(false);

        // Configura o alinhamento inicial dos gráficos
        dashboardGraphic.setAlignment(Pos.CENTER);
        cursosGraphic.setAlignment(Pos.CENTER);
        alunosGraphic.setAlignment(Pos.CENTER);

        // Criando a coluna de ações (editar/deletar)
        TableColumn<Alunos, Void> acoesColuna = new TableColumn<>("Ações");

        acoesColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button btnEditar = new Button();
            private final Button btnExcluir = new Button();
            private final HBox painel = new HBox(3, btnEditar, btnExcluir);

            {
                Label editarEmoji = new Label("✏️");
                editarEmoji.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-font-size: 8px;");
                btnEditar.setGraphic(editarEmoji);
                btnEditar.setStyle("-fx-background-color: transparent; -fx-cursor: hand; ");
                btnEditar.setMinWidth(40);

                Label excluirEmoji = new Label("🗑️");
                excluirEmoji.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-font-size: 8px;");
                btnExcluir.setGraphic(excluirEmoji);
                btnExcluir.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnExcluir.setMinWidth(40);

                btnEditar.setOnAction(e -> {
                    Alunos aluno = getTableView().getItems().get(getIndex());
                    editarAluno(aluno); // chama diretamente o métod que você já tem
                    toggleFormularioAluno(); // exibe o formulário para edição
                });


                btnExcluir.setOnAction(e -> {
                    Alunos aluno = getTableView().getItems().get(getIndex());
                    deletarAluno(aluno); // métod que você vai criar
                });

            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio) {
                    setGraphic(null);
                } else {
                    setGraphic(painel);
                }
            }
        });

        alunosTable.getColumns().add(acoesColuna);



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
        alunoCursoColumn.setCellValueFactory(new PropertyValueFactory<>("nomeCurso"));

        alunosTable.setItems(FXCollections.observableArrayList(alunosDAO.listarTodos()));
    }


    @FXML
    private void toggleFormularioAluno() {
        boolean mostrando = formularioAluno.isVisible();
        formularioAluno.setVisible(!mostrando);
        formularioAluno.setManaged(!mostrando); // remove espaço quando invisível
    }

    @FXML
    private void cancelarFormularioAluno() {
        formularioAluno.setVisible(false);
        formularioAluno.setManaged(false);
        limparCamposFormularioAluno();
    }

    private void limparCamposFormularioAluno() {
        campoNomeAluno.clear();
        campoCpfAluno.clear();
        campoTelefoneAluno.clear();
        campoEmailAluno.clear();
        campoDataNascimento.setValue(null);
        comboCurso.getSelectionModel().clearSelection();
    }

    @FXML
    private void salvarAluno() {
        String nome = campoNomeAluno.getText();
        String cpf = campoCpfAluno.getText();
        String telefone = campoTelefoneAluno.getText();
        String email = campoEmailAluno.getText();
        Date dataNascimentoValue = Date.valueOf(campoDataNascimento.getValue());
        Cursos cursoSelecionado = comboCurso.getValue();

        // Validação
        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || email.isEmpty() || dataNascimentoValue == null || cursoSelecionado == null) {
            mostrarAlerta("Preencha todos os campos corretamente.");
            return;
        }

        try {
            Date dataNascimento = Date.valueOf(dataNascimentoValue.toLocalDate()); // java.sql.Date

            // Criar objeto Aluno
            Alunos aluno = new Alunos();
            aluno.setNome(nome);
            aluno.setCpf(cpf);
            aluno.setTelefone(telefone);
            aluno.setEmail(email);
            aluno.setDataNascimento(dataNascimento);
            aluno.setIdCurso(cursoSelecionado.getIdCurso());

            AlunosDAO dao = new AlunosDAO();

            if (alunoEditando != null) {
                aluno.setIdAluno(alunoEditando.getIdAluno()); // mantém o mesmo ID
                dao.atualizar(aluno); // atualiza no banco
                int index = alunosTable.getItems().indexOf(alunoEditando);
                alunosTable.getItems().set(index, aluno); // atualiza na tabela
                alunoEditando = null; // limpa o modo de edição
            } else {
                dao.inserir(aluno); // insere no banco
                alunosTable.getItems().add(aluno); // adiciona na tabela
            }

            limparCamposFormularioAluno();
            cancelarFormularioAluno(); // Esconde o formulário
            mostrarAlerta("Aluno salvo com sucesso!");

        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar aluno: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    private void carregarCursosNoComboBox() {
        try {
            CursosDAO dao = new CursosDAO();
            List<Cursos> listaCursos = dao.listarTodos();
            comboCurso.getItems().addAll(listaCursos);
        }catch (Exception e) {
            mostrarAlerta("Erro ao carregar cursos: " + e.getMessage());
            e.printStackTrace();
        }
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

    private void deletarAluno(Alunos aluno) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Deseja excluir o aluno " + aluno.getNome() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                AlunosDAO dao = new AlunosDAO();
                dao.deletar(aluno.getIdAluno());
                alunosTable.getItems().remove(aluno);
                mostrarAlerta("Aluno deletado com sucesso!");
            } catch (Exception e) {
                mostrarAlerta("Erro ao deletar aluno: " + e.getMessage());
            }
        }
    }
    private void editarAluno(Alunos aluno) {
        campoNomeAluno.setText(aluno.getNome());
        campoCpfAluno.setText(aluno.getCpf());
        campoTelefoneAluno.setText(aluno.getTelefone());
        campoEmailAluno.setText(aluno.getEmail());
        campoDataNascimento.setValue(aluno.getDataNascimento().toLocalDate());

        // Seleciona o curso correto no ComboBox
        for (Cursos curso : comboCurso.getItems()) {
            if (curso.getIdCurso() == aluno.getIdCurso()) {
                comboCurso.setValue(curso);
                break;
            }
        }

        // Armazene o aluno sendo editado para depois salvar
        alunoEditando = aluno;
    }
}