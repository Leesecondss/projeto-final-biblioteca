package main.java.com.equipe.biblioteca.repository;

import main.java.com.equipe.biblioteca.model.Emprestimo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmprestimoRepositoryMemoria implements EmprestimoRepository {
    private final Map<String, Emprestimo> banco = new HashMap<>();

    @Override
    public Emprestimo salvar(Emprestimo emprestimo) {
        banco.put(emprestimo.getId(), emprestimo);
        return emprestimo;
    }

    @Override
    public Optional<Emprestimo> buscarPorId(String id) {
        return Optional.ofNullable(banco.get(id));
    }

    @Override
    public List<Emprestimo> buscarAtivosPorUsuario(String usuarioId) {
        List<Emprestimo> ativos = new ArrayList<>();
        for (Emprestimo e : banco.values()) {
            if (e.getUsuarioId().equals(usuarioId) && !e.isDevolvido()) {
                ativos.add(e);
            }
        }
        return ativos;
    }

    @Override
    public Optional<Emprestimo> buscarAtivoPorLivro(String livroId) {
        for (Emprestimo e : banco.values()) {
            if (e.getLivro().getId().equals(livroId) && !e.isDevolvido()) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(banco.values());
    }
}