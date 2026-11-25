package main.java.com.equipe.biblioteca.repository;

import main.java.com.equipe.biblioteca.model.Emprestimo;
import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository {
    Emprestimo salvar(Emprestimo emprestimo);
    Optional<Emprestimo> buscarPorId(String id);
    List<Emprestimo> buscarAtivosPorUsuario(String usuarioId);
    Optional<Emprestimo> buscarAtivoPorLivro(String livroId);
    List<Emprestimo> listarTodos();
}