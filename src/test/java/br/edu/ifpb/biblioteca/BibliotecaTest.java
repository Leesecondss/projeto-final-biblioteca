package br.edu.ifpb.biblioteca;

import br.edu.ifpb.biblioteca.model.Livro;
import br.edu.ifpb.biblioteca.repository.Acervo;
import br.edu.ifpb.biblioteca.service.Biblioteca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class BibliotecaTest {

    private Acervo acervo;
    private Biblioteca biblioteca;

    @BeforeEach
    void setup() {
        acervo = new Acervo();
        acervo.salvar(new Livro("L1", "Clean Code"));
        acervo.salvar(new Livro("L2", "Engenharia de Software"));
        acervo.salvar(new Livro("L3", "Algoritmos"));
        acervo.salvar(new Livro("L4", "Estruturas de Dados"));
        biblioteca = new Biblioteca(acervo);
    }

    @Test
    void deveRealizarEmprestimo_quandoLivroDisponivelEDentroDoLimite() {
        biblioteca.emprestarLivro("L1", "aluno1");

        Livro livro = acervo.buscarPorId("L1");
        assertFalse(livro.isDisponivel());
        assertEquals("aluno1", livro.getUsuarioEmprestimo());
    }

    @Test
    void deveRejeitarEmprestimo_quandoLivroJaEmprestado() {
        biblioteca.emprestarLivro("L1", "aluno1");

        assertThrows(IllegalStateException.class, () -> {
            biblioteca.emprestarLivro("L1", "aluno2");
        });
    }

    @Test
    void deveRejeitarEmprestimo_quandoUsuarioInvalidoOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            biblioteca.emprestarLivro("L1", "");
        });
    }

    @Test
    void deveRejeitarEmprestimo_quandoLivroNaoExiste() {
        assertThrows(IllegalArgumentException.class, () -> {
            biblioteca.emprestarLivro("INEXISTENTE", "aluno1");
        });
    }

    // Valor-limite e teste de regressão: protege o máximo exato de 3 livros por usuário
    @Test
    void devePermitirAte3LivrosERejeitarO4_limiteMaximo() {
        biblioteca.emprestarLivro("L1", "aluno1");
        biblioteca.emprestarLivro("L2", "aluno1");
        biblioteca.emprestarLivro("L3", "aluno1");

        assertEquals(3, biblioteca.contarEmprestimosDoUsuario("aluno1"));

        assertThrows(IllegalStateException.class, () -> {
            biblioteca.emprestarLivro("L4", "aluno1");
        });
    }

    // Teste parametrizado (Particionamento de equivalência e valores-limite)
    @ParameterizedTest
    @CsvSource({
        "-1, false",
        "0, true",
        "2, true",
        "3, false",
        "4, false"
    })
    void deveValidarLimiteDeEmprestimosAtivos(int emprestimosAtuais, boolean esperado) {
        assertEquals(esperado, biblioteca.podeEmprestar(emprestimosAtuais));
    }

    // Segurança (Item 12): usuário B não pode devolver livro emprestado pelo usuário A
    @Test
    void deveBloquearDevolucao_quandoSolicitadaPorOutroUsuario() {
        biblioteca.emprestarLivro("L1", "aluno1");

        assertThrows(SecurityException.class, () -> {
            biblioteca.devolverLivro("L1", "aluno2");
        });
    }

    @Test
    void deveDevolverLivroComSucesso_quandoSolicitadoPeloMesmoUsuario() {
        biblioteca.emprestarLivro("L1", "aluno1");
        biblioteca.devolverLivro("L1", "aluno1");

        Livro livro = acervo.buscarPorId("L1");
        assertTrue(livro.isDisponivel());
        assertNull(livro.getUsuarioEmprestimo());
    }
}