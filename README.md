# Sistema de Gestão de Cursos e Alunos

## Descrição
Sistema JavaFX para gestão de cursos e alunos, desenvolvido seguindo boas práticas de arquitetura com separação de responsabilidades.

## Arquitetura Refatorada

### Estrutura de Controllers
- **MainController**: Gerencia navegação e menu lateral
- **DashboardController**: Exibe estatísticas gerais do sistema
- **CursosController**: Gerencia operações CRUD de cursos
- **AlunosController**: Gerencia operações CRUD de alunos

### Estrutura de Views (FXML)
- **Main.fxml**: Layout principal com menu lateral
- **DashboardView.fxml**: Tela de dashboard com estatísticas
- **CursosView.fxml**: Tela de gerenciamento de cursos
- **AlunosView.fxml**: Tela de gerenciamento de alunos

## Funcionalidades

### Dashboard
- Exibe total de cursos cadastrados
- Exibe total de alunos cadastrados
- Atualização automática das estatísticas

### Gestão de Cursos
- ✅ Cadastrar novo curso
- ✅ Listar todos os cursos
- ✅ Editar curso existente
- ✅ Excluir curso
- Campos: Nome, Carga Horária, Limite de Alunos, Status Ativo

### Gestão de Alunos
- ✅ Cadastrar novo aluno
- ✅ Listar todos os alunos
- ✅ Editar aluno existente
- ✅ Excluir aluno
- Campos: Nome, CPF, Telefone, Email, Data de Nascimento, Curso

## Benefícios da Refatoração

1. **Separação de Responsabilidades**: Cada controller tem uma responsabilidade específica
2. **Manutenibilidade**: Código mais organizado e fácil de manter
3. **Reutilização**: Controllers podem ser reutilizados em diferentes contextos
4. **Escalabilidade**: Fácil adicionar novas funcionalidades
5. **Testabilidade**: Cada componente pode ser testado independentemente

## Como Executar

1. Certifique-se de ter o Java 17+ instalado
2. Configure o banco de dados MySQL
3. Execute a classe `app.Main`
4. Use o menu lateral para navegar entre as telas

## Tecnologias Utilizadas

- Java 17
- JavaFX
- MySQL
- Maven

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   ├── app/
│   │   │   ├── controller/
│   │   │   │   ├── MainController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── CursosController.java
│   │   │   │   └── AlunosController.java
│   │   │   └── Main.java
│   │   ├── dao/
│   │   │   ├── AlunosDAO.java
│   │   │   └── CursosDAO.java
│   │   ├── factory/
│   │   │   └── ConnectionFactory.java
│   │   └── modelo/
│   │       ├── Alunos.java
│   │       └── Cursos.java
│   └── resources/
│       └── app/
│           └── view/
│               ├── Main.fxml
│               ├── DashboardView.fxml
│               ├── CursosView.fxml
│               └── AlunosView.fxml
```

## Melhorias Implementadas

- ✅ Separação de FXMLs por funcionalidade
- ✅ Controllers específicos para cada tela
- ✅ Carregamento dinâmico de telas
- ✅ Implementação completa de CRUD
- ✅ Interface responsiva com menu colapsável
- ✅ Validação de dados
- ✅ Tratamento de erros
- ✅ Confirmação para exclusões
