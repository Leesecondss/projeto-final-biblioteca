package br.edu.ifpb.biblioteca.integration;

import br.edu.ifpb.biblioteca.model.Emprestimo;
import br.edu.ifpb.biblioteca.model.Livro;
import br.edu.ifpb.biblioteca.repository.EmprestimoRepositoryMemoria;
import br.edu.ifpb.biblioteca.service.EmprestimoService;
import br.edu.ifpb.biblioteca.service.EmprestimoValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoIntegracaoTest {

    @Test
    void fluxoCompletoEmprestimoDevolucaoENovoEmprestimo_integrandoServiceValidatorERepository() {
        var repository = new EmprestimoRepositoryMemoria();
        var validator = new EmprestimoValidator();
        var service = new EmprestimoService(repository, validator);

        Livro livro = new Livro("INT-1", "Padrões de Projeto");
        LocalDate hoje = LocalDate.of(2026, 11, 20);

        // 1. Realiza empréstimo e persiste no repositório
        Emprestimo primeiro = service.realizarEmprestimo("alunoA", livro, 10, hoje);
        assertEquals(1, repository.buscarAtivosPorUsuario("alunoA").size());

        // 2. Outro usuário tenta pegar o mesmo livro ainda ativo -> falha
        assertThrows(IllegalStateException.class,
                () -> service.realizarEmprestimo("alunoB", livro, 10, hoje));

        // 3. Dono devolve o livro -> atualiza estado no repositório
        service.devolverLivro(primeiro.getId(), "alunoA", "ALUNO");
        assertTrue(repository.buscarAtivoPorLivro("INT-1").isEmpty());

        // 4. Livro volta a ficar disponível para outro usuário
        Emprestimo segundo = service.realizarEmprestimo("alunoB", livro, 7, hoje.plusDays(1));
        assertEquals("alunoB", segundo.getUsuarioId());
    }

    @Test
    void desempenho_1000OperacoesDeConsultaEEmprestimoDevemExecutarEmMenosDe500ms() {
        var repository = new EmprestimoRepositoryMemoria();
        var service = new EmprestimoService(repository, new EmprestimoValidator());
        LocalDate hoje = LocalDate.of(2026, 11, 20);

        long inicioNs = System.nanoTime();
        for (int i = 0; i < 500; i++) {
            Livro livro = new Livro("PERF-" + i, "Livro " + i);
            Emprestimo emp = service.realizarEmprestimo("user-" + (i % 200), livro, 7, hoje);
            service.devolverLivro(emp.getId(), "user-" + (i % 200), "ALUNO");
        }
        long duracaoMs = (System.nanoTime() - inicioNs) / 1_000_000;

        assertTrue(duracaoMs < 500, "Tempo excedido: " + duracaoMs + " ms");
    }
}