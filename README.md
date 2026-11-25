# Sistema de Controle de Biblioteca — Projeto Final (Testes de Software)

## Integrantes
- Jonathan Barbosa de Farias
- João Gabriel Alves de Souza Bazílio
- André de Oliveira

## Descrição
Aplicação Java desenvolvida para a disciplina de **Testes de Software** (IFPB — Campus Campina Grande) com foco na validação de regras de negócio de empréstimo e devolução de livros em uma biblioteca, aplicando testes automatizados unitários, parametrizados, de integração, segurança e desempenho, além de integração contínua via GitHub Actions.

---

## Regras de Negócio

- **R1 — Limite de empréstimos simultâneos:** Cada usuário pode possuir no máximo **3** empréstimos ativos ao mesmo tempo.
- **R2 — Prazo de devolução:** O prazo do empréstimo deve ser de no mínimo **1 dia** e no máximo **30 dias**.
- **R3 — Validação de dados obrigatórios:** Empréstimos com identificação de usuário vazia/nula, livro inválido ou prazo fora do intervalo permitido devem ser rejeitados (`IllegalArgumentException`).
- **R4 — Disponibilidade do exemplar:** Um livro com empréstimo ativo (ainda não devolvido) não pode ser emprestado novamente até que sua devolução seja registrada (`IllegalStateException`).
- **R5 — Controle de permissão na devolução (Segurança):** Apenas o próprio usuário que realizou o empréstimo ou um perfil `BIBLIOTECARIO` pode registrar a devolução (`SecurityException` para usuários não autorizados).

---

## Tecnologias Utilizadas
- **Java 17**
- **Maven**
- **JUnit 5 (JUnit Jupiter)**
- **Git e GitHub**
- **GitHub Actions** (CI)

---

## Como Executar o Projeto e os Testes

### Pré-requisitos
- JDK 17 instalado
- Apache Maven instalado (ou via IDE como IntelliJ IDEA)

### Compilar o projeto
```bash
mvn clean compile