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

import com.biblioteca.biblioteca.dto.AutorRequest;
import com.biblioteca.biblioteca.dto.AutorResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Autor;
import com.biblioteca.biblioteca.repository.AutorRepository;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

	@Mock
	private AutorRepository autorRepository;

	@InjectMocks
	private AutorService autorService;

	@Test
	void deveCadastrarAutorComSucesso() {
		AutorRequest request = new AutorRequest();
		request.setNome("Robert C. Martin");
		request.setEmail("unclebob@email.com");

		when(autorRepository.existsByEmail("unclebob@email.com")).thenReturn(false);

		Autor autorSalvo = new Autor(1L, "Robert C. Martin", "unclebob@email.com");
		when(autorRepository.save(any(Autor.class))).thenReturn(autorSalvo);

		AutorResponse response = autorService.cadastrar(request);

		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals("Robert C. Martin", response.nome());
		assertEquals("unclebob@email.com", response.email());
		verify(autorRepository).save(any(Autor.class));
	}

	@Test
	void naoDeveCadastrarAutorComEmailDuplicado() {
		AutorRequest request = new AutorRequest();
		request.setNome("Robert C. Martin");
		request.setEmail("unclebob@email.com");

		when(autorRepository.existsByEmail("unclebob@email.com")).thenReturn(true);

		assertThrows(RecursoJaExisteException.class, () -> autorService.cadastrar(request));
		verify(autorRepository, never()).save(any(Autor.class));
	}

	@Test
	void deveListarTodosOsAutores() {
		Autor autor = new Autor(1L, "Robert C. Martin", "unclebob@email.com");
		when(autorRepository.findAll()).thenReturn(List.of(autor));

		List<AutorResponse> lista = autorService.listarTodos();

		assertEquals(1, lista.size());
		assertEquals("Robert C. Martin", lista.get(0).nome());
		verify(autorRepository).findAll();
	}

	@Test
	void deveBuscarAutorPorIdComSucesso() {
		Autor autor = new Autor(1L, "Robert C. Martin", "unclebob@email.com");
		when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

		AutorResponse response = autorService.buscarPorId(1L);

		assertEquals(1L, response.id());
		assertEquals("Robert C. Martin", response.nome());
		verify(autorRepository).findById(1L);
	}

	@Test
	void naoDeveBuscarAutorInexistente() {
		when(autorRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> autorService.buscarPorId(999L));
		verify(autorRepository).findById(999L);
	}

	@Test
	void deveAtualizarAutorComSucesso() {
		Autor autorExistente = new Autor(1L, "Robert C. Martin", "unclebob@email.com");
		AutorRequest request = new AutorRequest();
		request.setNome("Uncle Bob");
		request.setEmail("bob@email.com");

		when(autorRepository.findById(1L)).thenReturn(Optional.of(autorExistente));
		when(autorRepository.existsByEmail("bob@email.com")).thenReturn(false);

		Autor autorAtualizado = new Autor(1L, "Uncle Bob", "bob@email.com");
		when(autorRepository.save(any(Autor.class))).thenReturn(autorAtualizado);

		AutorResponse response = autorService.atualizar(1L, request);

		assertEquals("Uncle Bob", response.nome());
		assertEquals("bob@email.com", response.email());
		verify(autorRepository).save(autorExistente);
	}

	@Test
	void naoDeveAtualizarAutorComEmailDuplicado() {
		Autor autorExistente = new Autor(1L, "Robert C. Martin", "unclebob@email.com");
		AutorRequest request = new AutorRequest();
		request.setNome("Uncle Bob");
		request.setEmail("outro@email.com");

		when(autorRepository.findById(1L)).thenReturn(Optional.of(autorExistente));
		when(autorRepository.existsByEmail("outro@email.com")).thenReturn(true);

		assertThrows(RecursoJaExisteException.class, () -> autorService.atualizar(1L, request));
		verify(autorRepository, never()).save(any(Autor.class));
	}

	@Test
	void deveExcluirAutorComSucesso() {
		Autor autorExistente = new Autor(1L, "Robert C. Martin", "unclebob@email.com");
		when(autorRepository.findById(1L)).thenReturn(Optional.of(autorExistente));

		autorService.excluir(1L);

		verify(autorRepository).delete(autorExistente);
	}

	@Test
	void naoDeveExcluirAutorInexistente() {
		when(autorRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> autorService.excluir(999L));
		verify(autorRepository, never()).delete(any(Autor.class));
	}
}
