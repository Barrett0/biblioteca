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

import com.biblioteca.biblioteca.dto.UsuarioRequest;
import com.biblioteca.biblioteca.dto.UsuarioResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private UsuarioService usuarioService;

	@Test
	void deveCadastrarUsuarioComSucesso() {
		UsuarioRequest request = new UsuarioRequest();
		request.setNome("João Silva");
		request.setEmail("joao@email.com");

		when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(false);

		Usuario usuarioSalvo = new Usuario(1L, "João Silva", "joao@email.com");
		when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

		UsuarioResponse response = usuarioService.cadastrar(request);

		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals("João Silva", response.nome());
		assertEquals("joao@email.com", response.email());
		verify(usuarioRepository).save(any(Usuario.class));
	}

	@Test
	void naoDeveCadastrarUsuarioComEmailDuplicado() {
		UsuarioRequest request = new UsuarioRequest();
		request.setNome("João Silva");
		request.setEmail("joao@email.com");

		when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(true);

		assertThrows(RecursoJaExisteException.class, () -> usuarioService.cadastrar(request));
		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	@Test
	void deveListarTodosUsuarios() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");
		when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

		List<UsuarioResponse> lista = usuarioService.listarTodos();

		assertEquals(1, lista.size());
		assertEquals("João Silva", lista.get(0).nome());
		verify(usuarioRepository).findAll();
	}

	@Test
	void deveBuscarUsuarioPorId() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

		UsuarioResponse response = usuarioService.buscarPorId(1L);

		assertEquals(1L, response.id());
		assertEquals("João Silva", response.nome());
		assertEquals("joao@email.com", response.email());
		verify(usuarioRepository).findById(1L);
	}

	@Test
	void naoDeveBuscarUsuarioInexistente() {
		when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.buscarPorId(999L));
		verify(usuarioRepository).findById(999L);
	}

	@Test
	void deveAtualizarUsuarioComSucesso() {
		Usuario usuarioExistente = new Usuario(1L, "João Silva", "joao@email.com");
		UsuarioRequest request = new UsuarioRequest();
		request.setNome("João Atualizado");
		request.setEmail("joao.novo@email.com");

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
		when(usuarioRepository.existsByEmail("joao.novo@email.com")).thenReturn(false);

		Usuario usuarioAtualizado = new Usuario(1L, "João Atualizado", "joao.novo@email.com");
		when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

		UsuarioResponse response = usuarioService.atualizar(1L, request);

		assertEquals("João Atualizado", response.nome());
		assertEquals("joao.novo@email.com", response.email());
		verify(usuarioRepository).save(usuarioExistente);
	}

	@Test
	void naoDeveAtualizarUsuarioComEmailDuplicado() {
		Usuario usuarioExistente = new Usuario(1L, "João Silva", "joao@email.com");
		UsuarioRequest request = new UsuarioRequest();
		request.setNome("João Atualizado");
		request.setEmail("outro@email.com");

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
		when(usuarioRepository.existsByEmail("outro@email.com")).thenReturn(true);

		assertThrows(RecursoJaExisteException.class, () -> usuarioService.atualizar(1L, request));
		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	@Test
	void naoDeveAtualizarUsuarioInexistente() {
		UsuarioRequest request = new UsuarioRequest();
		request.setNome("João Atualizado");
		request.setEmail("joao.novo@email.com");

		when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.atualizar(999L, request));
		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	@Test
	void deveExcluirUsuarioComSucesso() {
		Usuario usuarioExistente = new Usuario(1L, "João Silva", "joao@email.com");
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));

		usuarioService.excluir(1L);

		verify(usuarioRepository).delete(usuarioExistente);
	}

	@Test
	void naoDeveExcluirUsuarioInexistente() {
		when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.excluir(999L));
		verify(usuarioRepository, never()).delete(any(Usuario.class));
	}
}
