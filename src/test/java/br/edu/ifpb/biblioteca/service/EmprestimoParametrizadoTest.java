package br.edu.ifpb.biblioteca.service;

import br.edu.ifpb.biblioteca.model.Emprestimo;
import br.edu.ifpb.biblioteca.model.Livro;
import br.edu.ifpb.biblioteca.repository.EmprestimoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoParametrizadoTest {

    private EmprestimoService service;
    private LocalDate dataFixa;

    @BeforeEach
    void setUp() {
        var repository = new EmprestimoRepositoryMemoria();
        var validator = new EmprestimoValidator();
        service = new EmprestimoService(repository, validator);
        dataFixa = LocalDate.of(2026, 11, 25);
    }

    @ParameterizedTest(name = "prazo={0} dias -> esperadoValido={1}")
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
        Livro livro = new Livro("LP-" + dias, "Livro Teste Prazo");

        if (esperadoValido) {
            Emprestimo emprestimo = service.realizarEmprestimo("user1", livro, dias, dataFixa);
            assertEquals(dias, emprestimo.getDiasPrazo());
        } else {
            assertThrows(IllegalArgumentException.class,
                    () -> service.realizarEmprestimo("user1", livro, dias, dataFixa));
        }
    }

    @ParameterizedTest(name = "emprestimosAtivosIniciais={0} -> podeNovoEmprestimo={1}")
    @CsvSource({
            "0, true",
            "1, true",
            "2, true",
            "3, false"
    })
    void limiteEmprestimosSimultaneos_deveRespeitarMaximoDe3(int ativosIniciais, boolean podeNovoEmprestimo) {
        for (int i = 0; i < ativosIniciais; i++) {
            service.realizarEmprestimo("user1", new Livro("L-INIT-" + i, "Livro Inicial " + i), 7, dataFixa);
        }

        Livro novoLivro = new Livro("L-NOVO", "Novo Livro");

        if (podeNovoEmprestimo) {
            Emprestimo emprestimo = service.realizarEmprestimo("user1", novoLivro, 7, dataFixa);
            assertNotNull(emprestimo.getId());
        } else {
            assertThrows(IllegalStateException.class,
                    () -> service.realizarEmprestimo("user1", novoLivro, 7, dataFixa));
        }
    }
}