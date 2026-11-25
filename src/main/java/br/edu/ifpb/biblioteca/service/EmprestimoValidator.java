package com.equipe.biblioteca.service;

import main.java.com.equipe.biblioteca.model.Livro;

public class EmprestimoValidator {
    public static final int prazo_minimo_dias = 1;
    public static final int prazo_maximo_dias = 30;
    public static final int max_emprestimos_ativos = 3;

    public void validarDadosEmprestimo(String usuarioId, Livro livro, int diasPrazo) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new IllegalArgumentException("Identificação do usuário é obrigatória.");
        }
        if (livro == null || livro.getId() == null || livro.getId().isBlank()) {
            throw new IllegalArgumentException("Livro válido é obrigatório.");
        }
        if (diasPrazo < prazo_minimo_dias || diasPrazo > prazo_maximo_dias) {
            throw new IllegalArgumentException(
                    "Prazo deve estar entre " + prazo_minimo_dias + " e " + prazo_maximo_dias + " dias."
            );
        }
    }

    public void validarLimiteEmprestimosAtivos(int quantidadeAtivos) {
        if (quantidadeAtivos >= max_emprestimos_ativos) {
            throw new IllegalStateException(
                    "Usuário já atingiu o limite de " + max_emprestimos_ativos + " empréstimos ativos."
            );
        }
    }
}