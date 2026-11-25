package br.edu.ifpb.biblioteca.model;

import java.time.LocalDate;
import java.util.UUID;

public class Emprestimo {
    private final String id;
    private final String usuarioId;
    private final Livro livro;
    private final int diasPrazo;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataPrevistaDevolucao;
    private boolean devolvido;

    public Emprestimo(String usuarioId, Livro livro, int diasPrazo, LocalDate dataEmprestimo) {
        this.id = UUID.randomUUID().toString();
        this.usuarioId = usuarioId;
        this.livro = livro;
        this.diasPrazo = diasPrazo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(diasPrazo);
        this.devolvido = false;
    }

    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public Livro getLivro() {
        return livro;
    }

    public int getDiasPrazo() {
        return diasPrazo;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void marcarDevolvido() {
        this.devolvido = true;
    }
}