package app.controller;

import dao.CursosDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modelo.Cursos;
import java.util.List;


public class CursosController {
    
    @FXML private TableView<Cursos> cursosTable;
    @FXML private TableColumn<Cursos, Integer> cursoIdColumn;
    @FXML private TableColumn<Cursos, String> cursoNomeColumn;
    @FXML private TableColumn<Cursos, Integer> cursoCargaHorariaColumn;
    @FXML private TableColumn<Cursos, Integer> cursoLimiteAlunosColumn;
    @FXML private TableColumn<Cursos, Boolean> cursoAtivoColumn;
    @FXML private TableColumn<Cursos, Integer> cursoQuantidadeDeAlunos;
    
    @FXML private VBox formularioCurso;
    @FXML private TextField campoNomeCursoColumn;
    @FXML private TextField campoCargaHorariaColumn;
    @FXML private TextField campoLimiteAlunosColumn;
    @FXML private ComboBox<String> campoAtivoColumn;
    
    @FXML private ComboBox<String> filtroStatusCursoComboBox;
    
    private Cursos cursoEditando;
    private MainController mainController;
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarCursos();
        configurarDoubleClick();
        configurarFiltroStatusCurso();
    }
    
    private void configurarDoubleClick() {
        cursosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Cursos cursoSelecionado = cursosTable.getSelectionModel().getSelectedItem();
                if (cursoSelecionado != null && mainController != null) {
                    mainController.switchToAlunosWithCourseFilter(cursoSelecionado);
                }
            }
        });
    }
    
    private void configurarComboBox() {
        campoAtivoColumn.getItems().addAll("Ativo", "Inativo");
    }
    
    private void configurarTabela() {
        cursoIdColumn.setCellValueFactory(new PropertyValueFactory<>("IdCurso"));
        cursoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cursoCargaHorariaColumn.setCellValueFactory(new PropertyValueFactory<>("cargaHoraria"));
        cursoLimiteAlunosColumn.setCellValueFactory(new PropertyValueFactory<>("limiteAlunos"));
        cursoAtivoColumn.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        cursoQuantidadeDeAlunos.setCellValueFactory(new PropertyValueFactory<>("quantidadeAlunos"));
        cursoAtivoColumn.setCellFactory(coluna -> new TableCell<Cursos, Boolean>() {
            @Override
            protected void updateItem(Boolean ativo, boolean vazio) {
                super.updateItem(ativo, vazio);
                if (vazio || ativo == null) {
                    setText("");
                } else {
                    setText(ativo ? "Ativo" : "Inativo");
                    setStyle(ativo ? "-fx-text-fill: green; -fx-font-weight: bold;" : "-fx-text-fill: red; -fx-font-weight: bold;");
                }
            }
        });
        
        adicionarColunaAcoes();

        for (TableColumn<Cursos, ?> column : cursosTable.getColumns()) {
            column.setStyle("-fx-alignment: CENTER;");
        }
    }
    
    private void adicionarColunaAcoes() {
        TableColumn<Cursos, Void> acoesColuna = new TableColumn<>("Ações");
        
        acoesColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button btnEditar = new Button();
            private final Button btnExcluir = new Button();
            private final HBox painel = new HBox(3, btnEditar, btnExcluir);
            
            {
                Label editarEmoji = new Label("✏️");
                editarEmoji.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-font-size: 8px;");
                btnEditar.setGraphic(editarEmoji);
                btnEditar.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnEditar.setMinWidth(40);
                
                Label excluirEmoji = new Label("🗑️");
                excluirEmoji.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-font-size: 8px;");
                btnExcluir.setGraphic(excluirEmoji);
                btnExcluir.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnExcluir.setMinWidth(40);
                
                btnEditar.setOnAction(e -> {
                    Cursos curso = getTableView().getItems().get(getIndex());
                    editarCurso(curso);
                    toggleFormularioCurso();
                });
                
                btnExcluir.setOnAction(e -> {
                    Cursos curso = getTableView().getItems().get(getIndex());
                    deletarCurso(curso);
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
        
        cursosTable.getColumns().add(acoesColuna);
    }
    
    public void carregarCursos() {
        try {
            CursosDAO cursosDAO = new CursosDAO();
            List<Cursos> cursos = cursosDAO.listarTodos();
            cursosTable.setItems(FXCollections.observableArrayList(cursos));
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar cursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void toggleFormularioCurso() {
        boolean mostrando = formularioCurso.isVisible();
        formularioCurso.setVisible(!mostrando);
        formularioCurso.setManaged(!mostrando);
    }
    
    @FXML
    private void cancelarFormularioCurso() {
        formularioCurso.setVisible(false);
        formularioCurso.setManaged(false);
        limparCamposFormularioCurso();
        cursoEditando = null;
    }
    
    private void limparCamposFormularioCurso() {
        campoNomeCursoColumn.clear();
        campoCargaHorariaColumn.clear();
        campoLimiteAlunosColumn.clear();
        campoAtivoColumn.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void adicionarCurso() {
        String nome = campoNomeCursoColumn.getText();
        String cargaHorariaStr = campoCargaHorariaColumn.getText();
        String limiteAlunosStr = campoLimiteAlunosColumn.getText();
        String ativoStr = campoAtivoColumn.getValue();
        
        // Validação
        if (nome.isEmpty() || cargaHorariaStr.isEmpty() || limiteAlunosStr.isEmpty() || ativoStr == null) {
            mostrarAlerta("Preencha todos os campos corretamente.");
            return;
        }

        // Validação de Tamanho do Nome
        if (!Cursos.validarTamanhoNome(nome)) {
            mostrarAlerta("Nome do curso deve ter pelo menos 3 caracteres.");
            return;
        }
        
        try {
            int cargaHoraria = Integer.parseInt(cargaHorariaStr);
            int limiteAlunos = Integer.parseInt(limiteAlunosStr);
            boolean ativo = "Ativo".equals(ativoStr);
            
            // Validação de Carga Horária
            if (!Cursos.validarCargaHoraria(cargaHoraria)) {
                mostrarAlerta("Carga horária deve ser de pelo menos 20 horas.");
                return;
            }
            
            Cursos curso = new Cursos();
            curso.setNome(nome);
            curso.setCargaHoraria(cargaHoraria);
            curso.setLimiteAlunos(limiteAlunos);
            curso.setAtivo(ativo);
            
            CursosDAO dao = new CursosDAO();
            
            if (cursoEditando != null) {
                curso.setIdCurso(cursoEditando.getIdCurso());
                if (cursoEditando.isAtivo() != curso.isAtivo()) {
                    dao.setCursoAtivo(curso.getIdCurso(), curso.isAtivo());
                }
                dao.atualizar(curso);
                int index = cursosTable.getItems().indexOf(cursoEditando);
                cursosTable.getItems().set(index, curso);
                cursoEditando = null;
            } else {
                dao.inserir(curso);
                cursosTable.getItems().add(curso);
            }
            
            limparCamposFormularioCurso();
            cancelarFormularioCurso();
            mostrarAlerta("Curso salvo com sucesso!");
            
            // Atualizar ComboBoxes de cursos no AlunosController
            if (mainController != null && mainController.getAlunosController() != null) {
                mainController.getAlunosController().carregarCursosNoComboBox();
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Carga horária e limite de alunos devem ser números inteiros.");
        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void editarCurso(Cursos curso) {
        campoNomeCursoColumn.setText(curso.getNome());
        campoCargaHorariaColumn.setText(String.valueOf(curso.getCargaHoraria()));
        campoLimiteAlunosColumn.setText(String.valueOf(curso.getLimiteAlunos()));
        campoAtivoColumn.setValue(curso.isAtivo() ? "Ativo" : "Inativo");
        
        cursoEditando = curso;
    }
    
    private void deletarCurso(Cursos curso) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Deseja excluir o curso " + curso.getNome() + "?");
        
        java.util.Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                CursosDAO dao = new CursosDAO();
                dao.deletar(curso.getIdCurso());
                cursosTable.getItems().remove(curso);
                mostrarAlerta("Curso deletado com sucesso!");
                
                // Atualizar ComboBoxes de cursos no AlunosController
                if (mainController != null && mainController.getAlunosController() != null) {
                    mainController.getAlunosController().carregarCursosNoComboBox();
                }
            } catch (Exception e) {
                mostrarAlerta("Erro ao deletar curso: " + e.getMessage());
            }
        }
    }
    
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


      private void configurarFiltroStatusCurso() {
        filtroStatusCursoComboBox.getItems().addAll("Todos", "Ativo", "Inativo");
        filtroStatusCursoComboBox.getSelectionModel().select("Todos");
    }

    @FXML
    private void aplicarFiltroStatusCurso() {
        String status = filtroStatusCursoComboBox.getValue();
        List<Cursos> cursos = new CursosDAO().listarTodos();
        if ("Ativo".equals(status)) {
            cursos.removeIf(c -> !c.isAtivo());
        } else if ("Inativo".equals(status)) {
            cursos.removeIf(Cursos::isAtivo);
        }
        cursosTable.setItems(FXCollections.observableArrayList(cursos));
    }

    @FXML
    private void limparFiltroStatusCurso() {
        filtroStatusCursoComboBox.getSelectionModel().select("Todos");
        cursosTable.setItems(FXCollections.observableArrayList(new CursosDAO().listarTodos()));
    }
}
