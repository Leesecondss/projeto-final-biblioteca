package br.edu.ifpb.biblioteca.service;

import br.edu.ifpb.biblioteca.model.Emprestimo;
import br.edu.ifpb.biblioteca.model.Livro;
import br.edu.ifpb.biblioteca.repository.EmprestimoRepository;
import java.time.LocalDate;

public class EmprestimoService {
    private final EmprestimoRepository repository;
    private final EmprestimoValidator validator;

    public EmprestimoService(EmprestimoRepository repository, EmprestimoValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Emprestimo realizarEmprestimo(String usuarioId, Livro livro, int diasPrazo, LocalDate dataBase) {
        validator.validarDadosEmprestimo(usuarioId, livro, diasPrazo);

        if (repository.buscarAtivoPorLivro(livro.getId()).isPresent()) {
            throw new IllegalStateException("Livro já possui empréstimo ativo.");
        }

        int ativosDoUsuario = repository.buscarAtivosPorUsuario(usuarioId).size();
        validator.validarLimiteEmprestimosAtivos(ativosDoUsuario);

        LocalDate dataReferencia = (dataBase != null) ? dataBase : LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(usuarioId, livro, diasPrazo, dataReferencia);
        return repository.salvar(emprestimo);
    }

    public void devolverLivro(String emprestimoId, String usuarioSolicitante, String perfilSolicitante) {
        if (usuarioSolicitante == null || usuarioSolicitante.isBlank()) {
            throw new SecurityException("Usuário não autenticado para registrar devolução.");
        }

        Emprestimo emprestimo = repository.buscarPorId(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));

        boolean ehDono = emprestimo.getUsuarioId().equals(usuarioSolicitante);
        boolean ehBibliotecario = "BIBLIOTECARIO".equalsIgnoreCase(perfilSolicitante);

        if (!ehDono && !ehBibliotecario) {
            throw new SecurityException("Acesso negado: usuário sem permissão para devolver este empréstimo.");
        }

        if (emprestimo.isDevolvido()) {
            throw new IllegalStateException("Empréstimo já foi devolvido.");
        }

        emprestimo.marcarDevolvido();
        repository.salvar(emprestimo);
    }
}