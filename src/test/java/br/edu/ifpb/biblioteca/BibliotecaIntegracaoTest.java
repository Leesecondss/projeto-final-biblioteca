package br.edu.ifpb.biblioteca;

import br.edu.ifpb.biblioteca.model.Livro;
import br.edu.ifpb.biblioteca.repository.Acervo;
import br.edu.ifpb.biblioteca.service.Biblioteca;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BibliotecaIntegracaoTest {

    @Test
    void fluxoCompletoEmprestimoEDevolucao_deveAtualizarEstadoNoAcervo() {
        Acervo acervo = new Acervo();
        acervo.salvar(new Livro("101", "Java"));
        acervo.salvar(new Livro("102", "Testes"));

        Biblioteca biblioteca = new Biblioteca(acervo);

        biblioteca.emprestarLivro("101", "userA");
        biblioteca.emprestarLivro("102", "userA");
        assertEquals(2, biblioteca.contarEmprestimosDoUsuario("userA"));

        biblioteca.devolverLivro("101", "userA");
        assertTrue(acervo.buscarPorId("101").isDisponivel());
        assertFalse(acervo.buscarPorId("102").isDisponivel());
        assertEquals(1, biblioteca.contarEmprestimosDoUsuario("userA"));
    }
}