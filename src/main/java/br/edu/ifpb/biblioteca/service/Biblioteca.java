package br.edu.ifpb.biblioteca.service;

import br.edu.ifpb.biblioteca.model.Livro;
import br.edu.ifpb.biblioteca.repository.Acervo;

public class Biblioteca {
    public static final int LIMITE_EMPRESTIMOS = 3;
    private final Acervo acervo;

    public Biblioteca(Acervo acervo) {
        this.acervo = acervo;
    }

    public boolean podeEmprestar(int quantidadeAtual) {
        return quantidadeAtual >= 0 && quantidadeAtual < LIMITE_EMPRESTIMOS;
    }

    public void emprestarLivro(String livroId, String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new IllegalArgumentException("Identificação do usuário é obrigatória.");
        }

        Livro livro = acervo.buscarPorId(livroId);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }

        int emprestimosAtivos = contarEmprestimosDoUsuario(usuarioId);
        if (!podeEmprestar(emprestimosAtivos)) {
            throw new IllegalStateException("Limite máximo de 3 livros por usuário atingido.");
        }

        livro.emprestarPara(usuarioId);
        acervo.salvar(livro);
    }

    public void devolverLivro(String livroId, String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new IllegalArgumentException("Identificação do usuário é obrigatória.");
        }

        Livro livro = acervo.buscarPorId(livroId);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }

        livro.devolverPor(usuarioId);
        acervo.salvar(livro);
    }

    public int contarEmprestimosDoUsuario(String usuarioId) {
        int total = 0;
        for (Livro livro : acervo.listarTodos()) {
            if (!livro.isDisponivel() && usuarioId.equals(livro.getUsuarioEmprestimo())) {
                total++;
            }
        }
        return total;
    }
}