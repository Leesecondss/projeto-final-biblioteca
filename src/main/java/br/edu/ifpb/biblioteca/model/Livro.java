package br.edu.ifpb.biblioteca.model;

public class Livro {
    private final String id;
    private final String titulo;
    private boolean disponivel;
    private String usuarioEmprestimo;

    public Livro(String id, String titulo) {
        if (id == null || id.isBlank() || titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("ID e título do livro são obrigatórios.");
        }
        this.id = id;
        this.titulo = titulo;
        this.disponivel = true;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public String getUsuarioEmprestimo() {
        return usuarioEmprestimo;
    }

    public void emprestarPara(String usuarioId) {
        if (!disponivel) {
            throw new IllegalStateException("O livro já está emprestado.");
        }
        this.disponivel = false;
        this.usuarioEmprestimo = usuarioId;
    }

    public void devolverPor(String usuarioId) {
        if (disponivel) {
            throw new IllegalStateException("O livro já está disponível no acervo.");
        }
        if (!this.usuarioEmprestimo.equals(usuarioId)) {
            throw new SecurityException("Apenas o usuário que realizou o empréstimo pode devolver este livro.");
        }
        this.disponivel = true;
        this.usuarioEmprestimo = null;
    }
}