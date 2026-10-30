package com.biblioteca.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.biblioteca.dto.LivroRequest;
import com.biblioteca.biblioteca.dto.LivroResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Autor;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.repository.AutorRepository;
import com.biblioteca.biblioteca.repository.LivroRepository;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

	@Mock
	private LivroRepository livroRepository;

	@Mock
	private AutorRepository autorRepository;

	@InjectMocks
	private LivroService livroService;

	@Test
	void deveCadastrarLivroComSucesso() {
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");

		LivroRequest request = new LivroRequest();
		request.setTitulo("Clean Code");
		request.setIsbn("9780132350884");
		request.setAnoPublicacao(2008);
		request.setAutorId(1L);

		when(livroRepository.existsByIsbn("9780132350884")).thenReturn(false);
		when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

		Livro livroSalvo = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);
		when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

		LivroResponse response = livroService.cadastrar(request);

		assertNotNull(response);
		assertEquals("Clean Code", response.getTitulo());
		assertEquals("9780132350884", response.getIsbn());
		assertEquals(2008, response.getAnoPublicacao());
		assertEquals("Robert C. Martin", response.getAutorNome());
		verify(livroRepository).save(any(Livro.class));
	}

	@Test
	void naoDeveCadastrarLivroComIsbnDuplicado() {
		LivroRequest request = new LivroRequest();
		request.setTitulo("Clean Code");
		request.setIsbn("9780132350884");
		request.setAnoPublicacao(2008);
		request.setAutorId(1L);

		when(livroRepository.existsByIsbn("9780132350884")).thenReturn(true);

		assertThrows(RecursoJaExisteException.class, () -> livroService.cadastrar(request));
		verify(livroRepository, never()).save(any(Livro.class));
	}

	@Test
	void naoDeveCadastrarLivroQuandoAutorNaoExistir() {
		LivroRequest request = new LivroRequest();
		request.setTitulo("Clean Code");
		request.setIsbn("9780132350884");
		request.setAnoPublicacao(2008);
		request.setAutorId(999L);

		when(livroRepository.existsByIsbn("9780132350884")).thenReturn(false);
		when(autorRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> livroService.cadastrar(request));
		verify(livroRepository, never()).save(any(Livro.class));
	}

	@Test
	void deveListarTodosLivros() {
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livro = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		when(livroRepository.findAll()).thenReturn(List.of(livro));

		List<LivroResponse> lista = livroService.listarTodos();

		assertEquals(1, lista.size());
		assertEquals("Clean Code", lista.get(0).getTitulo());
		verify(livroRepository).findAll();
	}

	@Test
	void deveBuscarLivroPorIdComSucesso() {
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livro = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

		LivroResponse response = livroService.buscarPorId(1L);

		assertEquals(1L, response.getId());
		assertEquals("Clean Code", response.getTitulo());
		verify(livroRepository).findById(1L);
	}

	@Test
	void naoDeveBuscarLivroInexistente() {
		when(livroRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> livroService.buscarPorId(999L));
		verify(livroRepository).findById(999L);
	}

	@Test
	void deveAtualizarLivroComSucesso() {
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livroExistente = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		LivroRequest request = new LivroRequest();
		request.setTitulo("Clean Architecture");
		request.setIsbn("9780134494166");
		request.setAnoPublicacao(2017);
		request.setAutorId(1L);

		when(livroRepository.findById(1L)).thenReturn(Optional.of(livroExistente));
		when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
		when(livroRepository.existsByIsbn("9780134494166")).thenReturn(false);

		Livro livroAtualizado = new Livro(1L, "Clean Architecture", autor, "9780134494166", 2017);
		when(livroRepository.save(any(Livro.class))).thenReturn(livroAtualizado);

		LivroResponse response = livroService.atualizar(1L, request);

		assertEquals("Clean Architecture", response.getTitulo());
		assertEquals("9780134494166", response.getIsbn());
		verify(livroRepository).save(livroExistente);
	}

	@Test
	void naoDeveAtualizarLivroInexistente() {
		LivroRequest request = new LivroRequest();
		request.setTitulo("Clean Architecture");
		request.setIsbn("9780134494166");
		request.setAnoPublicacao(2017);
		request.setAutorId(1L);

		when(livroRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> livroService.atualizar(999L, request));
		verify(livroRepository, never()).save(any(Livro.class));
	}

	@Test
	void naoDeveAtualizarLivroComAutorInexistente() {
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livroExistente = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		LivroRequest request = new LivroRequest();
		request.setTitulo("Clean Code");
		request.setIsbn("9780132350884");
		request.setAnoPublicacao(2008);
		request.setAutorId(999L);

		when(livroRepository.findById(1L)).thenReturn(Optional.of(livroExistente));
		when(autorRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> livroService.atualizar(1L, request));
		verify(livroRepository, never()).save(any(Livro.class));
	}

	@Test
	void naoDeveAtualizarLivroComIsbnDuplicado() {
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livroExistente = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		LivroRequest request = new LivroRequest();
		request.setTitulo("Clean Architecture");
		request.setIsbn("9780134494166");
		request.setAnoPublicacao(2017);
		request.setAutorId(1L);

		when(livroRepository.findById(1L)).thenReturn(Optional.of(livroExistente));
		when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
		when(livroRepository.existsByIsbn("9780134494166")).thenReturn(true);

		assertThrows(RecursoJaExisteException.class, () -> livroService.atualizar(1L, request));
		verify(livroRepository, never()).save(any(Livro.class));
	}

	@Test
	void deveExcluirLivroComSucesso() {
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livroExistente = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		when(livroRepository.findById(1L)).thenReturn(Optional.of(livroExistente));

		livroService.excluir(1L);

		verify(livroRepository).delete(livroExistente);
	}

	@Test
	void naoDeveExcluirLivroInexistente() {
		when(livroRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> livroService.excluir(999L));
		verify(livroRepository, never()).delete(any(Livro.class));
	}
}
