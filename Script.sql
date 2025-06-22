DROP DATABASE IF EXISTS universidade;
CREATE DATABASE universidade;
USE universidade;

-- Tabela de Cursos
CREATE TABLE cursos (
    idCurso INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cargaHoraria INT NOT NULL CHECK (cargaHoraria >= 20),
    limiteAlunos INT NOT NULL CHECK (limiteAlunos >= 1),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE alunos (
    idAluno INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(16) NOT NULL,
    email VARCHAR(120) NOT NULL,
    data_nascimento DATE NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    idCurso INT NOT NULL,
    FOREIGN KEY (idCurso) REFERENCES cursos(idCurso) ON DELETE CASCADE
);

INSERT INTO cursos (nome, cargaHoraria, limiteAlunos, ativo) VALUES
('Orientação a objetos', 40, 5, TRUE),
('Spring e SpringBoot', 60, 5, TRUE),
('Curso de Lógica de Programação', 30, 5, TRUE),
('APIs REST com Java', 50, 5, TRUE),
('Banco de Dados com JDBC', 45, 5, TRUE);


INSERT INTO alunos (nome, cpf, telefone, email, data_nascimento, ativo, idCurso) VALUES
('Pedro', '15052791194', '(11) 91111-1111', 'pedro@email.com', '2001-01-01', TRUE, 12),
('Joao', '79497100205', '(11) 92222-2222', 'joao@email.com', '2002-02-02', TRUE, 12),
('Matheus', '84276725275', '(11) 93333-3333', 'matheus@email.com', '2003-03-03', TRUE, 13),
('Paulo', '91157837409', '(11) 94444-4444', 'paulo@email.com', '2004-04-04', TRUE, 15),
('Jorge', '46098364915', '(11) 95555-5555', 'jorge@email.com', '2005-05-05', TRUE, 16),
('Henrique', '12667729013', '(11) 96666-6666', 'henrique@email.com', '2001-06-06', TRUE, 12),
('Augusto', '01937214575', '(11) 97777-7777', 'augusto@email.com', '2002-07-07', TRUE, 13),
('Claudia', '40862671604', '(11) 98888-8888', 'claudia@email.com', '2003-08-08', TRUE, 14),
('Isabel', '73385533279', '(11) 99999-9999', 'isabel@email.com', '2004-09-09', TRUE, 15),
('Sara', '85340983790', '(11) 90000-0000', 'sara@email.com', '2005-10-10', TRUE, 16),
('Maria', '60585418594', '(11) 90111-1111', 'maria@email.com', '2001-11-11', TRUE, 12),
('Ana', '33782949650', '(11) 90222-2222', 'ana@email.com', '2002-12-12', TRUE, 13),
('Lucas', '39055023302', '(11) 90333-3333', 'lucas@email.com', '2003-01-13', TRUE, 14),
('Adriano', '52972624297', '(11) 90444-4444', 'adriano@email.com', '2004-02-14', TRUE, 15),
('Leonardo', '75037897513', '(11) 90555-5555', 'leonardo@email.com', '2005-03-15', TRUE, 16);
