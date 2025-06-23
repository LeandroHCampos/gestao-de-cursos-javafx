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

### Pré-requisitos
- **Java 17+**: Certifique-se de que o JDK 17 ou superior está instalado e configurado no seu sistema.
- **Maven**: Necessário para gerenciamento de dependências e para executar a aplicação via linha de comando.
- **MySQL**: O banco de dados utilizado para persistir os dados da aplicação.

### 1. Configuração do Banco de Dados
- Crie um banco de dados no MySQL com o nome `universidade`.
- O sistema tentará se conectar utilizando as seguintes credenciais:
  - **URL**: `jdbc:mysql://localhost/universidade`
  - **Usuário**: `root`
  - **Senha**: `fatec`
- Caso suas credenciais sejam diferentes, você pode alterá-las no arquivo `src/main/java/factory/ConnectionFactory.java`.

### 2. Executando a Aplicação

Existem duas maneiras de executar a aplicação:

#### Via Linha de Comando (com Maven)
A forma mais garantida de executar, pois o Maven cuida de todas as dependências e módulos do JavaFX.
```bash
mvn clean javafx:run
```

#### Via IDE (Ex: IntelliJ, VSCode, Eclipse)
Após a refatoração que separou o `Main` do `App`, é possível executar diretamente pela IDE.
1. Abra o projeto na sua IDE de preferência.
2. Aguarde a IDE sincronizar e baixar as dependências do Maven.
3. Localize o arquivo `src/main/java/app/Main.java`.
4. Execute o método `main` deste arquivo.

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
