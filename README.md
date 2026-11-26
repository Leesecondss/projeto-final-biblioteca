# Sistema de Controle de Biblioteca

Projeto final da disciplina de Testes de Software (IFPB — Campus Campina Grande), focado na validação das regras de empréstimo e devolução de livros com testes automatizados em JUnit 5 e integração contínua no GitHub Actions.

## Integrantes
- Jonathan Barbosa de Farias
- João Gabriel Alves de Souza Bazílio
- André de Oliveira

## Regras de Negócio
- **Limite por usuário:** cada usuário pode ter no máximo **3** livros emprestados ao mesmo tempo.
- **Disponibilidade:** um livro já emprestado não pode ser emprestado de novo antes de ser devolvido.
- **Dados obrigatórios:** usuário, livro e devolução inválida devem ser bloqueados com exceção.
- **Permissão na devolução:** apenas quem pegou o livro emprestado pode devolvê-lo.

## Tecnologias
- Java 17
- Maven
- JUnit 5
- Git, GitHub e GitHub Actions

## Como Executar

Compilar:
```bash
mvn clean compile