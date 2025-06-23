package app.controller;

import dao.AlunosDAO;
import dao.CursosDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import modelo.Alunos;
import modelo.Cursos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Pos;

public class AlunosController {
    
    @FXML private TableView<Alunos> alunosTable;
    @FXML private TableColumn<Alunos, Integer> idAlunoColumn;
    @FXML private TableColumn<Alunos, String> alunoNomeColumn;
    @FXML private TableColumn<Alunos, String> alunoCpfColumn;
    @FXML private TableColumn<Alunos, String> alunoTelefoneColumn;
    @FXML private TableColumn<Alunos, String> alunoEmailColumn;
    @FXML private TableColumn<Alunos, Date> alunoDateColumn;
    @FXML private TableColumn<Alunos, Boolean> alunoAtivoColumn;
    @FXML private TableColumn<Alunos, Integer> alunoCursoColumn;
    
    @FXML private VBox formularioAluno;
    @FXML private TextField campoNomeAluno;
    @FXML private TextField campoCpfAluno;
    @FXML private TextField campoTelefoneAluno;
    @FXML private TextField campoEmailAluno;
    @FXML private DatePicker campoDataNascimento;
    @FXML private ComboBox<Cursos> comboCurso;
    @FXML private ComboBox<String> campoAtivoColumn;

    @FXML private ComboBox<Cursos> filtroCursoComboBox;
    @FXML private ComboBox<String> filtroStatusComboBox;
    
    private Alunos alunoEditando;
    
    private static final String TODOS_STATUS = "Todos os Status";
    private static final String ATIVO_STATUS = "Ativo";
    private static final String INATIVO_STATUS = "Inativo";
    private static final String TODOS_CURSOS_LABEL = "Todos os Cursos";
    private static final Cursos TODOS_CURSOS = new Cursos(-1, TODOS_CURSOS_LABEL, 0, 0, true);
    
    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarCursosNoComboBox();
        configurarFiltros();
        adicionarColunaAcoes();
        configurarMascaraCPF();
        configurarMascaraTelefone();
        filtroStatusComboBox.getSelectionModel().select(ATIVO_STATUS);
        filtroCursoComboBox.getSelectionModel().select(TODOS_CURSOS);
        aplicarFiltros();
    }

    private void configurarComboBox() {
        campoAtivoColumn.getItems().addAll("Ativo", "Inativo");
    }
    
    private void configurarTabela() {
        idAlunoColumn.setCellValueFactory(new PropertyValueFactory<>("idAluno"));
        alunoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        alunoCpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        alunoCpfColumn.setCellFactory(coluna -> new TableCell<Alunos, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatarCPF(item));
                }
                setAlignment(Pos.CENTER);
            }
        });
        alunoTelefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        alunoTelefoneColumn.setCellFactory(coluna -> new TableCell<Alunos, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatarTelefone(item));
                }
                setAlignment(Pos.CENTER);
            }
        });
        alunoEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        alunoDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        alunoAtivoColumn.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        alunoAtivoColumn.setCellFactory(coluna -> new TableCell<Alunos, Boolean>() {
            protected void updateItem(Boolean ativo, boolean vazio) {
                super.updateItem(ativo, vazio);
                if (vazio || ativo == null) {
                    setText("");
                } else {
                    setText(ativo ? "Ativo" : "Inativo");
                    setStyle(ativo ? "-fx-text-fill: green; -fx-font-weight: bold;" : "-fx-text-fill: red; -fx-font-weight: bold;");
                }
                setAlignment(Pos.CENTER);
            }
        });
        alunoCursoColumn.setCellValueFactory(new PropertyValueFactory<>("nomeCurso"));
        
        alunoDateColumn.setCellFactory(coluna -> new TableCell<Alunos, Date>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item.toLocalDate()));
                }
                setAlignment(Pos.CENTER);
            }
        });
        
        centralizarConteudo(idAlunoColumn);
        centralizarConteudo(alunoNomeColumn);
        centralizarConteudo(alunoEmailColumn);
        centralizarConteudo(alunoCursoColumn);
    }
    
    private <T> void centralizarConteudo(TableColumn<Alunos, T> coluna) {
        coluna.setCellFactory(c -> new TableCell<Alunos, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
                setAlignment(Pos.CENTER);
            }
        });
    }
    
    private String formatarCPF(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null || !telefone.matches("\\d{10,11}")) {
            return telefone;
        }
        if (telefone.length() == 11) {
            return "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 7) + "-" + telefone.substring(7);
        } else { // 10 dígitos
            return "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 6) + "-" + telefone.substring(6);
        }
    }
    
    private void configurarFiltros() {
        filtroStatusComboBox.getItems().clear();
        filtroStatusComboBox.getItems().addAll(TODOS_STATUS, ATIVO_STATUS, INATIVO_STATUS);
        filtroStatusComboBox.getSelectionModel().select(TODOS_STATUS);
    }
    
    private void adicionarColunaAcoes() {
        TableColumn<Alunos, Void> acoesColuna = new TableColumn<>("Ações");
        
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
                    Alunos aluno = getTableView().getItems().get(getIndex());
                    editarAluno(aluno);
                    toggleFormularioAluno();
                });
                
                btnExcluir.setOnAction(e -> {
                    Alunos aluno = getTableView().getItems().get(getIndex());
                    deletarAluno(aluno);
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
    
    public void carregarAlunosAtivosPorPadrao() {
        try {
            AlunosDAO alunosDAO = new AlunosDAO();
            List<Alunos> alunos = alunosDAO.listarTodos();
            alunos.removeIf(a -> !a.isAtivo());
            alunosTable.setItems(FXCollections.observableArrayList(alunos));
            filtroStatusComboBox.getSelectionModel().select(ATIVO_STATUS);
            filtroCursoComboBox.getSelectionModel().select(TODOS_CURSOS);
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar alunos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void carregarCursosNoComboBox() {
        try {
            CursosDAO dao = new CursosDAO();
            List<Cursos> listaCursos = dao.listarTodos();

            comboCurso.getItems().clear();
            comboCurso.getItems().addAll(listaCursos);

            filtroCursoComboBox.getItems().clear();
            filtroCursoComboBox.getItems().add(TODOS_CURSOS);
            filtroCursoComboBox.getItems().addAll(listaCursos);
            filtroCursoComboBox.getSelectionModel().select(TODOS_CURSOS);
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar cursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void toggleFormularioAluno() {
        boolean mostrando = formularioAluno.isVisible();
        formularioAluno.setVisible(!mostrando);
        formularioAluno.setManaged(!mostrando);
    }
    
    @FXML
    private void cancelarFormularioAluno() {
        formularioAluno.setVisible(false);
        formularioAluno.setManaged(false);
        limparCamposFormularioAluno();
        alunoEditando = null;
    }
    
    private void limparCamposFormularioAluno() {
        campoNomeAluno.clear();
        campoCpfAluno.clear();
        campoTelefoneAluno.clear();
        campoEmailAluno.clear();
        campoDataNascimento.setValue(null);
        comboCurso.getSelectionModel().clearSelection();
    }
    
    private void configurarMascaraCPF() {
        campoCpfAluno.textProperty().addListener((obs, oldText, newText) -> {
            String numeros = newText.replaceAll("[^\\d]", "");
            StringBuilder formatado = new StringBuilder();
            int len = numeros.length();
            for (int i = 0; i < len && i < 11; i++) {
                if (i == 3 || i == 6) formatado.append('.');
                if (i == 9) formatado.append('-');
                formatado.append(numeros.charAt(i));
            }
            String novoTexto = formatado.toString();
            if (!newText.equals(novoTexto)) {
                campoCpfAluno.setText(novoTexto);
                int pos = Math.min(novoTexto.length(), campoCpfAluno.getText().length());
                try {
                    campoCpfAluno.positionCaret(pos);
                } catch (Exception ignored) {}
            }
        });
    }

    private void configurarMascaraTelefone() {
        campoTelefoneAluno.textProperty().addListener((obs, oldText, newText) -> {
            String numeros = newText.replaceAll("[^\\d]", "");
            StringBuilder formatado = new StringBuilder();
            int len = numeros.length();
            for (int i = 0; i < len && i < 11; i++) {
                if (i == 0) formatado.append('(');
                if (i == 2) formatado.append(") ");
                if (i == 7) formatado.append('-');
                formatado.append(numeros.charAt(i));
            }
            String novoTexto = formatado.toString();
            if (!newText.equals(novoTexto)) {
                campoTelefoneAluno.setText(novoTexto);
                int pos = Math.min(novoTexto.length(), campoTelefoneAluno.getText().length());
                try {
                    campoTelefoneAluno.positionCaret(pos);
                } catch (Exception ignored) {}
            }
        });
    }

    @FXML
    private void salvarAluno() {
        String nome = campoNomeAluno.getText();
        String cpf = campoCpfAluno.getText().replaceAll("[^\\d]", "");
        String telefone = campoTelefoneAluno.getText().replaceAll("[^\\d]", "");
        String email = campoEmailAluno.getText();
        String ativoStr = campoAtivoColumn.getValue();
        
        if (campoDataNascimento.getValue() == null) {
            mostrarAlerta("Selecione uma data de nascimento.");
            return;
        }
        
        Date dataNascimentoValue = Date.valueOf(campoDataNascimento.getValue());
        Cursos cursoSelecionado = comboCurso.getValue();
        
        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || email.isEmpty() || ativoStr == null || cursoSelecionado == null) {
            mostrarAlerta("Preencha todos os campos corretamente.");
            return;
        }

        if (!Alunos.validarCPF(cpf)) {
            mostrarAlerta("CPF inválido! Verifique se o CPF está correto.");
            return;
        }

        if (!Alunos.validarEmail(email)) {
            mostrarAlerta("E-mail inválido! Verifique se o e-mail está correto.");
            return;
        }

        if (!Alunos.validarIdadeMinima(dataNascimentoValue)) {
            mostrarAlerta("Aluno deve ter pelo menos 16 anos de idade.");
            return;
        }


        if (!Alunos.validarTamanhoNome(nome)) {
            mostrarAlerta("Nome deve ter pelo menos 3 caracteres.");
            return;
        }

        if (!cursoSelecionado.isAtivo()) {
            mostrarAlerta("Não é possível adicionar aluno em um curso inativo.");
            return;
        }

        try {
            AlunosDAO alunosDAO = new AlunosDAO();
            CursosDAO cursosDAO = new CursosDAO();
            int limite = cursoSelecionado.getLimiteAlunos();

            // Novo aluno ativo: checar limite
            if (alunoEditando == null && "Ativo".equals(ativoStr)) {
                int ativosNoCurso = alunosDAO.contarAlunosAtivosPorCurso(cursoSelecionado.getIdCurso());
                if (ativosNoCurso >= limite) {
                    mostrarAlerta("O curso selecionado já atingiu o limite máximo de alunos ativos (" + limite + "). Não é possível cadastrar mais alunos ativos neste curso.");
                    return;
                }
            }

            // Atualização: se reativando
            boolean reativando = alunoEditando != null && !alunoEditando.isAtivo() && "Ativo".equals(ativoStr);
            if (reativando) {
                int ativosNoCurso = alunosDAO.contarAlunosAtivosPorCurso(cursoSelecionado.getIdCurso());
                if (ativosNoCurso >= limite) {
                    mostrarAlerta("O curso selecionado já atingiu o limite máximo de alunos ativos (" + limite + "). Não é possível reativar este aluno.");
                    return;
                }
            }

            // Impedir que aluno inativo troque de curso
            if (alunoEditando != null && !alunoEditando.isAtivo() && alunoEditando.getIdCurso() != cursoSelecionado.getIdCurso()) {
                mostrarAlerta("Alunos inativos não podem ser transferidos para outro curso. Ative o aluno antes de trocar de curso.");
                return;
            }

            // Criar objeto Aluno
            Alunos aluno = new Alunos();
            aluno.setNome(nome);
            aluno.setCpf(cpf);
            aluno.setTelefone(telefone);
            aluno.setEmail(email);
            aluno.setDataNascimento(dataNascimentoValue);
            aluno.setAtivo("Ativo".equals(ativoStr));
            aluno.setIdCurso(cursoSelecionado.getIdCurso());
            aluno.setNomeCurso(cursoSelecionado.getNome());

            if (alunoEditando != null) {
                aluno.setIdAluno(alunoEditando.getIdAluno());
                alunosDAO.atualizar(aluno);
                int index = alunosTable.getItems().indexOf(alunoEditando);
                alunosTable.getItems().set(index, aluno);
                alunoEditando = null;
            } else {
                alunosDAO.inserir(aluno);
                alunosTable.getItems().add(aluno);
            }

            // Após salvar/inserir: ativar curso se aluno ativo
            if (aluno.isAtivo()) {
                cursosDAO.setCursoAtivo(aluno.getIdCurso(), true);
            }

            // Após salvar/inserir/atualizar: se não há mais alunos ativos, inativar curso
            int ativosNoCursoApos = alunosDAO.contarAlunosAtivosPorCurso(cursoSelecionado.getIdCurso());
            if (ativosNoCursoApos < 1) {
                cursosDAO.setCursoAtivo(cursoSelecionado.getIdCurso(), false);
            }

            limparCamposFormularioAluno();
            cancelarFormularioAluno();
            mostrarAlerta("Aluno salvo com sucesso!");
        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar aluno: " + e.getMessage());
            e.printStackTrace();
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
        
        alunoEditando = aluno;
    }
    
    private void deletarAluno(Alunos aluno) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Deseja excluir o aluno " + aluno.getNome() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                AlunosDAO dao = new AlunosDAO();
                CursosDAO cursosDAO = new CursosDAO();
                dao.deletar(aluno.getIdAluno());
                alunosTable.getItems().remove(aluno);
                // Após deletar: se não há mais alunos ativos, inativar curso
                int ativosNoCurso = dao.contarAlunosAtivosPorCurso(aluno.getIdCurso());
                if (ativosNoCurso < 1) {
                    cursosDAO.setCursoAtivo(aluno.getIdCurso(), false);
                }
                mostrarAlerta("Aluno deletado com sucesso!");
            } catch (Exception e) {
                mostrarAlerta("Erro ao deletar aluno: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void aplicarFiltros() {
        try {
            AlunosDAO alunosDAO = new AlunosDAO();
            List<Alunos> todosAlunos = alunosDAO.listarTodos();

            Cursos cursoSelecionado = filtroCursoComboBox.getSelectionModel().getSelectedItem();
            if (cursoSelecionado != null && cursoSelecionado.getIdCurso() != -1) {
                todosAlunos.removeIf(aluno -> aluno.getIdCurso() != cursoSelecionado.getIdCurso());
            }

            String statusSelecionado = filtroStatusComboBox.getSelectionModel().getSelectedItem();
            if (statusSelecionado != null && !statusSelecionado.equals(TODOS_STATUS)) {
                boolean isAtivo = ATIVO_STATUS.equals(statusSelecionado);
                todosAlunos.removeIf(aluno -> aluno.isAtivo() != isAtivo);
            }

            alunosTable.setItems(FXCollections.observableArrayList(todosAlunos));

        } catch (Exception e) {
            mostrarAlerta("Erro ao aplicar filtros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void limparFiltros() {
        filtroCursoComboBox.getSelectionModel().select(TODOS_CURSOS);
        filtroStatusComboBox.getSelectionModel().select(TODOS_STATUS);
        aplicarFiltros();
    }

    @FXML
    private void gerarTxt() {
        gerarRelatorioAlunosPorCursoStatus(null);
    }

    private void gerarRelatorioAlunosPorCursoStatus(Boolean apenasAtivos) {
        FileChooser fileChooser = new FileChooser();
        String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String tipo = apenasAtivos == null ? "todos" : (apenasAtivos ? "ativos" : "inativos");
        fileChooser.setTitle("Salvar Relatório de Alunos");
        fileChooser.setInitialFileName("relatorio-" + tipo + "alunos-por-curso-" + dataHoje + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de Texto (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(alunosTable.getScene().getWindow());
        if (file == null) return;

        try (FileWriter writer = new FileWriter(file)) {
            AlunosDAO alunosDAO = new AlunosDAO();
            CursosDAO cursosDAO = new CursosDAO();
            List<Alunos> todosAlunos = alunosDAO.listarTodos();
            List<Cursos> todosCursos = cursosDAO.listarTodos();

            // Filtros aplicados
            Cursos cursoFiltro = filtroCursoComboBox.getValue();
            String statusFiltro = filtroStatusComboBox.getValue();

            // Filtrar alunos conforme UI
            List<Alunos> alunosFiltrados = new ArrayList<>(todosAlunos);
            if (cursoFiltro != null && cursoFiltro.getIdCurso() != -1) {
                alunosFiltrados.removeIf(a -> a.getIdCurso() != cursoFiltro.getIdCurso());
            }
            if (apenasAtivos != null) {
                alunosFiltrados.removeIf(a -> a.isAtivo() != apenasAtivos);
            } else if (statusFiltro != null && !statusFiltro.equals(TODOS_STATUS)) {
                boolean isAtivo = ATIVO_STATUS.equals(statusFiltro);
                alunosFiltrados.removeIf(a -> a.isAtivo() != isAtivo);
            }

            // Resumo geral
            int totalAlunos = alunosFiltrados.size();
            long totalAtivos = alunosFiltrados.stream().filter(Alunos::isAtivo).count();
            long totalInativos = alunosFiltrados.stream().filter(a -> !a.isAtivo()).count();

            writer.write("Sistema de Gestão Acadêmica - Relatório de Alunos\n");
            writer.write("Data de geração: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n");
            writer.write("Filtros Aplicados:\n");
            writer.write("Curso: " + (cursoFiltro != null && cursoFiltro.getIdCurso() != -1 ? cursoFiltro.getNome() : "Todos") + "\n");
            writer.write("Status: " + (statusFiltro != null ? statusFiltro : "Todos os Status") + "\n\n");

            writer.write("Resumo:\n");
            writer.write("Total de Alunos: " + totalAlunos + "\n");
            writer.write("Ativos: " + totalAtivos + "\n");
            writer.write("Inativos: " + totalInativos + "\n\n");

            // Agrupar por curso
            for (Cursos curso : todosCursos) {
                List<Alunos> alunosDoCurso = new ArrayList<>();
                for (Alunos a : alunosFiltrados) {
                    if (a.getIdCurso() == curso.getIdCurso()) {
                        alunosDoCurso.add(a);
                    }
                }
                if (alunosDoCurso.isEmpty()) continue;
                long ativos = alunosDoCurso.stream().filter(Alunos::isAtivo).count();
                long inativos = alunosDoCurso.size() - ativos;
                int capacidade = curso.getLimiteAlunos();
                String ocupacao = capacidade > 0 ? (curso.getQuantidadeAlunos() + "/" + capacidade + " ocupados – " + (int)(100.0 * curso.getQuantidadeAlunos() / capacidade) + "%") : "-";
                writer.write("Curso: " + curso.getNome() + " (" + ocupacao + ")\n");
                writer.write("-------------------------------------------\n");
                writer.write("Ativos: " + ativos + " | Inativos: " + inativos + "\n");
                writer.write("ID;Nome;CPF;Telefone;E-mail;Data de Nascimento;Status\n");
                for (Alunos aluno : alunosDoCurso) {
                    writer.write(aluno.getIdAluno() + ";");
                    writer.write(aluno.getNome() + ";");
                    writer.write(aluno.getCpf() + ";");
                    writer.write(aluno.getTelefone() + ";");
                    writer.write(aluno.getEmail() + ";");
                    writer.write(aluno.getDataNascimento().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";");
                    writer.write((aluno.isAtivo() ? "Ativo" : "Inativo") + "\n");
                }
                writer.write("\n");
            }
            mostrarAlerta("Relatório salvo com sucesso!");
        } catch (IOException e) {
            mostrarAlerta("Erro ao salvar o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void generateActiveStudentsReportByCourse() {
        gerarRelatorioAlunosPorCursoStatus(true);
    }

    public void generateInactiveStudentsReportByCourse() {
        gerarRelatorioAlunosPorCursoStatus(false);
    }
    
    public void filtrarPorCurso(Cursos curso) {
        if (curso != null && curso.getIdCurso() != -1) {
            // Seleciona o objeto Cursos correto da lista do ComboBox
            for (Cursos c : filtroCursoComboBox.getItems()) {
                if (c.getIdCurso() == curso.getIdCurso()) {
                    filtroCursoComboBox.getSelectionModel().select(c);
                    break;
                }
            }
        } else {
            filtroCursoComboBox.getSelectionModel().select(TODOS_CURSOS);
        }
        filtroStatusComboBox.getSelectionModel().select(TODOS_STATUS);
        aplicarFiltros();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
