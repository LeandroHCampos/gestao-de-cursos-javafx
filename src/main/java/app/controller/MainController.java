package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import java.io.IOException;

public class MainController {
    @FXML private HBox alunosGraphic;
    @FXML private HBox cursosGraphic;
    @FXML private HBox dashboardGraphic;
    @FXML private VBox menuContainer;
    @FXML private Button toggleMenuBtn;
    @FXML private StackPane contentArea;
    @FXML private Text dashboardText;
    @FXML private Text cursosText;
    @FXML private Text alunosText;
    
    private boolean isMenuExpanded = false;
    private static final double EXPANDED_WIDTH = 200;
    private static final double COLLAPSED_WIDTH = 45;
    
    // Controllers das telas
    private DashboardController dashboardController;
    private CursosController cursosController;
    private AlunosController alunosController;
    
    // Nodes das telas
    private Node dashboardView;
    private Node cursosView;
    private Node alunosView;

    @FXML
    public void initialize() {
        try {
            carregarTelas();
            configurarMenu();
            showDashboard();
        } catch (IOException e) {
            mostrarAlerta("Erro ao carregar as telas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarTelas() throws IOException {
        // Carregar Dashboard
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/app/view/DashboardView.fxml"));
        dashboardView = dashboardLoader.load();
        dashboardController = dashboardLoader.getController();
        
        // Carregar Cursos
        FXMLLoader cursosLoader = new FXMLLoader(getClass().getResource("/app/view/CursosView.fxml"));
        cursosView = cursosLoader.load();
        cursosController = cursosLoader.getController();
        cursosController.setMainController(this);
        
        // Carregar Alunos
        FXMLLoader alunosLoader = new FXMLLoader(getClass().getResource("/app/view/AlunosView.fxml"));
        alunosView = alunosLoader.load();
        alunosController = alunosLoader.getController();
    }
    
    private void configurarMenu() {
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
        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardView);
        dashboardController.carregarEstatisticas();
    }

    @FXML
    private void showCursos() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(cursosView);
        cursosController.carregarCursos();
    }

    @FXML
    private void showAlunos() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(alunosView);
        alunosController.carregarAlunosAtivosPorPadrao();
    }
    
    public void switchToAlunosWithCourseFilter(modelo.Cursos curso) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(alunosView);
        alunosController.filtrarPorCurso(curso);
    }
    
    public AlunosController getAlunosController() {
        return alunosController;
    }
    
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}