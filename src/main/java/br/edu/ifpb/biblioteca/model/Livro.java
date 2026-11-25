package br.edu.ifpb.biblioteca.model;

import java.util.Objects;

public class Livro {
    private final String id;
    private final String titulo;

    public Livro(String id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof br.edu.ifpb.biblioteca.model.Livro livro)) return false;
        return Objects.equals(id, livro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}