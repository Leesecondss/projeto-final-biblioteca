package main.java.com.equipe.biblioteca.service;

import main.java.com.equipe.biblioteca.model.Emprestimo;
import main.java.com.equipe.biblioteca.model.Livro;
import main.java.com.equipe.biblioteca.repository.EmprestimoRepository;
import main.java.com.equipe.biblioteca.repository.EmprestimoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    private EmprestimoRepository repository;
    private EmprestimoValidator validator;
    private EmprestimoService service;
    private LocalDate dataFixa;

    @BeforeEach
    void setUp() {
        repository = new EmprestimoRepositoryMemoria();
        validator = new EmprestimoValidator();
        service = new EmprestimoService(repository, validator);
        dataFixa = LocalDate.of(2026, 11, 10);
    }

    @Test
    void emprestimoComPrazoMinimo1Dia_deveSerAceito() {
        Livro livro = new Livro("L1", "Clean Code");

        Emprestimo emprestimo = service.realizarEmprestimo("user1", livro, 1, dataFixa);

        assertAll(
                () -> assertNotNull(emprestimo.getId()),
                () -> assertEquals("user1", emprestimo.getUsuarioId()),
                () -> assertEquals(LocalDate.of(2026, 11, 11), emprestimo.getDataPrevistaDevolucao()),
                () -> assertFalse(emprestimo.isDevolvido())
        );
    }

    @Test
    void emprestimoComPrazoMaximo30Dias_deveSerAceito() {
        Livro livro = new Livro("L2", "Domain-Driven Design");

        Emprestimo emprestimo = service.realizarEmprestimo("user1", livro, 30, dataFixa);

        assertEquals(LocalDate.of(2026, 12, 10), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    void emprestimoComPrazo0Dias_deveSerRejeitado() {
        Livro livro = new Livro("L3", "Java Efetivo");

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo("user1", livro, 0, dataFixa));
    }

    @Test
    void emprestimoComPrazo31Dias_deveSerRejeitado() {
        Livro livro = new Livro("L4", "Arquitetura Limpa");

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo("user1", livro, 31, dataFixa));
    }

    @Test
    void emprestimoDeLivroJaEmprestado_deveLancarExcecao() {
        Livro livro = new Livro("L5", "Engenharia de Software");
        service.realizarEmprestimo("user1", livro, 7, dataFixa);

        assertThrows(IllegalStateException.class,
                () -> service.realizarEmprestimo("user2", livro, 7, dataFixa));
    }

    @Test
    void usuarioCom3EmprestimosAtivos_deveSerRejeitadoNoQuarto() {
        service.realizarEmprestimo("user1", new Livro("L1", "Livro 1"), 5, dataFixa);
        service.realizarEmprestimo("user1", new Livro("L2", "Livro 2"), 5, dataFixa);
        service.realizarEmprestimo("user1", new Livro("L3", "Livro 3"), 5, dataFixa);

        assertThrows(IllegalStateException.class,
                () -> service.realizarEmprestimo("user1", new Livro("L4", "Livro 4"), 5, dataFixa));
    }

    @Test
    void usuarioComUsuarioVazio_deveSerRejeitado() {
        Livro livro = new Livro("L6", "Testes de Software");

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo("   ", livro, 10, dataFixa));
    }

    @Test
    void devolucaoPorOutroUsuarioSemPermissao_deveLancarSecurityException() {
        Livro livro = new Livro("L7", "Algoritmos");
        Emprestimo emprestimo = service.realizarEmprestimo("user1", livro, 14, dataFixa);

        assertThrows(SecurityException.class,
                () -> service.devolverLivro(emprestimo.getId(), "user2", "ALUNO"));
    }

    @Test
    void devolucaoPorBibliotecario_deveSerPermitida() {
        Livro livro = new Livro("L8", "Redes de Computadores");
        Emprestimo emprestimo = service.realizarEmprestimo("user1", livro, 14, dataFixa);

        service.devolverLivro(emprestimo.getId(), "bib1", "BIBLIOTECARIO");

        assertTrue(emprestimo.isDevolvido());
    }

    @ParameterizedTest
    @CsvSource({
            "-5, false",
            "0, false",
            "1, true",
            "15, true",
            "30, true",
            "31, false",
            "45, false"
    })
    void prazoEmprestimo_deveRespeitarLimitesDe1A30Dias(int dias, boolean esperadoValido) {
        Livro livro = new Livro("LP-" + dias, "Livro Parametrizado");

        if (esperadoValido) {
            Emprestimo emp = service.realizarEmprestimo("user1", livro, dias, dataFixa);
            assertEquals(dias, emp.getDiasPrazo());
        } else {
            assertThrows(IllegalArgumentException.class,
                    () -> service.realizarEmprestimo("user1", livro, dias, dataFixa));
        }
    }
}