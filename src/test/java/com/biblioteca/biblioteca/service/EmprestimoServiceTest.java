package com.biblioteca.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.biblioteca.dto.EmprestimoRequest;
import com.biblioteca.biblioteca.dto.EmprestimoResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Autor;
import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.biblioteca.repository.LivroRepository;
import com.biblioteca.biblioteca.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

	@Mock
	private EmprestimoRepository emprestimoRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private LivroRepository livroRepository;

	@InjectMocks
	private EmprestimoService emprestimoService;

	@Test
	void deveRealizarEmprestimoComSucesso() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livro = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		EmprestimoRequest request = new EmprestimoRequest();
		request.setUsuarioId(1L);
		request.setLivroId(1L);

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
		when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
		when(emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro)).thenReturn(false);
		when(emprestimoRepository.countByUsuarioIdAndDataDevolucaoIsNull(1L)).thenReturn(0L);

		Emprestimo emprestimoSalvo = new Emprestimo(
				1L,
				usuario,
				livro,
				LocalDate.now(),
				LocalDate.now().plusDays(7)
		);

		when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoSalvo);

		EmprestimoResponse response = emprestimoService.realizarEmprestimo(request);

		assertNotNull(response);
		assertEquals("João Silva", response.usuarioNome());
		assertEquals("Clean Code", response.livroTitulo());
		assertEquals(LocalDate.now(), response.dataEmprestimo());
		assertEquals(LocalDate.now().plusDays(7), response.dataPrevistaDevolucao());
		assertNull(response.dataDevolucao());
		verify(emprestimoRepository).save(any(Emprestimo.class));
	}

	@Test
	void naoDeveEmprestarLivroJaEmprestado() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");
		Livro livro = new Livro(1L, "Clean Code", null, "9780132350884", 2008);

		EmprestimoRequest request = new EmprestimoRequest();
		request.setUsuarioId(1L);
		request.setLivroId(1L);

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
		when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
		when(emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro)).thenReturn(true);

		assertThrows(RecursoJaExisteException.class, () -> emprestimoService.realizarEmprestimo(request));
		verify(emprestimoRepository, never()).save(any(Emprestimo.class));
	}

	@Test
	void naoDeveEmprestarQuandoLimiteDeLivrosAtingido() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");
		Livro livro = new Livro(1L, "Clean Code", null, "9780132350884", 2008);

		EmprestimoRequest request = new EmprestimoRequest();
		request.setUsuarioId(1L);
		request.setLivroId(1L);

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
		when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
		when(emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro)).thenReturn(false);
		when(emprestimoRepository.countByUsuarioIdAndDataDevolucaoIsNull(1L)).thenReturn(3L);

		assertThrows(RecursoJaExisteException.class, () -> emprestimoService.realizarEmprestimo(request));
		verify(emprestimoRepository, never()).save(any(Emprestimo.class));
	}

	@Test
	void naoDeveEmprestarQuandoUsuarioNaoExiste() {
		EmprestimoRequest request = new EmprestimoRequest();
		request.setUsuarioId(999L);
		request.setLivroId(1L);

		when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> emprestimoService.realizarEmprestimo(request));
		verify(livroRepository, never()).findById(anyLong());
		verify(emprestimoRepository, never()).save(any(Emprestimo.class));
	}

	@Test
	void naoDeveEmprestarQuandoLivroNaoExiste() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");

		EmprestimoRequest request = new EmprestimoRequest();
		request.setUsuarioId(1L);
		request.setLivroId(999L);

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
		when(livroRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> emprestimoService.realizarEmprestimo(request));
		verify(emprestimoRepository, never()).save(any(Emprestimo.class));
	}

	@Test
	void deveDevolverLivroComSucesso() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livro = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		Emprestimo emprestimo = new Emprestimo(
				1L,
				usuario,
				livro,
				LocalDate.now().minusDays(7),
				LocalDate.now()
		);

		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(invocation -> invocation.getArgument(0));

		EmprestimoResponse response = emprestimoService.devolver(1L);

		assertEquals(LocalDate.now(), response.dataDevolucao());
		verify(emprestimoRepository).save(emprestimo);
	}

	@Test
	void naoDeveDevolverLivroDuasVezes() {
		Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com");
		Autor autor = new Autor(1L, "Robert C. Martin", "robert@email.com");
		Livro livro = new Livro(1L, "Clean Code", autor, "9780132350884", 2008);

		Emprestimo emprestimo = new Emprestimo(
				1L,
				usuario,
				livro,
				LocalDate.now().minusDays(7),
				LocalDate.now().minusDays(1)
		);
		emprestimo.setDataDevolucao(LocalDate.now().minusDays(1));

		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

		assertThrows(RecursoJaExisteException.class, () -> emprestimoService.devolver(1L));
		verify(emprestimoRepository, never()).save(any(Emprestimo.class));
	}

	@Test
	void naoDeveDevolverEmprestimoInexistente() {
		when(emprestimoRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> emprestimoService.devolver(999L));
		verify(emprestimoRepository, never()).save(any(Emprestimo.class));
	}

	@Test
	void deveListarEmprestimosAtrasados() {
		Usuario usuario = new Usuario(1L, "João", "joao@email.com");
		Autor autor = new Autor(1L, "Robert Martin", "robert@email.com");
		Livro livro = new Livro(1L, "Clean Code", autor, "ISBN-123", 2008);

		Emprestimo emprestimo = new Emprestimo(
				1L,
				usuario,
				livro,
				LocalDate.now().minusDays(10),
				LocalDate.now().minusDays(3)
		);

		when(emprestimoRepository.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now()))
				.thenReturn(List.of(emprestimo));

		List<EmprestimoResponse> resultado = emprestimoService.listarAtrasados();

		assertEquals(1, resultado.size());
		assertEquals("Clean Code", resultado.get(0).livroTitulo());
		verify(emprestimoRepository).findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now());
	}

	@Test
	void deveListarHistoricoPorUsuario() {
		Usuario usuario = new Usuario(1L, "João", "joao@email.com");
		Livro livro = new Livro(1L, "Clean Code", null, "ISBN-123", 2008);
		Emprestimo emprestimo = new Emprestimo(1L, usuario, livro, LocalDate.now(), LocalDate.now().plusDays(7));

		when(usuarioRepository.existsById(1L)).thenReturn(true);
		when(emprestimoRepository.findByUsuarioId(1L)).thenReturn(List.of(emprestimo));

		List<EmprestimoResponse> resultado = emprestimoService.listarHistoricoPorUsuario(1L);

		assertEquals(1, resultado.size());
		verify(emprestimoRepository).findByUsuarioId(1L);
	}

	@Test
	void naoDeveListarHistoricoQuandoUsuarioNaoExiste() {
		when(usuarioRepository.existsById(999L)).thenReturn(false);

		assertThrows(RecursoNaoEncontradoException.class,
				() -> emprestimoService.listarHistoricoPorUsuario(999L));
	}
}
