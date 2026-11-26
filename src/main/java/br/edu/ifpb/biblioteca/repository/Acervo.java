package br.edu.ifpb.biblioteca.repository;

import br.edu.ifpb.biblioteca.model.Livro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Acervo {
    private final Map<String, Livro> livros = new HashMap<>();

    public void salvar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }
        livros.put(livro.getId(), livro);
    }

    public Livro buscarPorId(String id) {
        return livros.get(id);
    }

    public List<Livro> listarTodos() {
        return new ArrayList<>(livros.values());
    }
}