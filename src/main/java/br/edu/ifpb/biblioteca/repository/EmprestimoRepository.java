package br.edu.ifpb.biblioteca.repository;

import br.edu.ifpb.biblioteca.model.Emprestimo;
import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository {
    Emprestimo salvar(Emprestimo emprestimo);
    Optional<Emprestimo> buscarPorId(String id);
    List<Emprestimo> buscarAtivosPorUsuario(String usuarioId);
    Optional<Emprestimo> buscarAtivoPorLivro(String livroId);
    List<Emprestimo> listarTodos();
}