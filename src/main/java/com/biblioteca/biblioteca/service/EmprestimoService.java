package com.biblioteca.biblioteca.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.biblioteca.dto.EmprestimoRequest;
import com.biblioteca.biblioteca.dto.EmprestimoResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.biblioteca.repository.LivroRepository;
import com.biblioteca.biblioteca.repository.UsuarioRepository;

@Service
public class EmprestimoService {

	private final EmprestimoRepository emprestimoRepository;
	private final UsuarioRepository usuarioRepository;
	private final LivroRepository livroRepository;

	public EmprestimoService(
			EmprestimoRepository emprestimoRepository,
			UsuarioRepository usuarioRepository,
			LivroRepository livroRepository) {
		this.emprestimoRepository = emprestimoRepository;
		this.usuarioRepository = usuarioRepository;
		this.livroRepository = livroRepository;
	}

	@Transactional
	public EmprestimoResponse realizarEmprestimo(EmprestimoRequest request) {
		Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		Livro livro = livroRepository.findById(request.getLivroId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado"));

		boolean livroEmprestado = emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro);

		if (livroEmprestado) {
			throw new RecursoJaExisteException("Este livro já está emprestado");
		}

		long emprestimosAtivos = emprestimoRepository.countByUsuarioIdAndDataDevolucaoIsNull(usuario.getId());

		if (emprestimosAtivos >= 3) {
			throw new RecursoJaExisteException("O usuário já atingiu o limite de 3 livros emprestados");
		}

		LocalDate dataEmprestimo = LocalDate.now();
		LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(7);

		Emprestimo emprestimo = new Emprestimo(
				null,
				usuario,
				livro,
				dataEmprestimo,
				dataPrevistaDevolucao
		);

		Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);
		return EmprestimoResponse.fromEntity(emprestimoSalvo);
	}

	@Transactional(readOnly = true)
	public List<EmprestimoResponse> listarTodos() {
		return emprestimoRepository.findAll()
				.stream()
				.map(EmprestimoResponse::fromEntity)
				.toList();
	}

	@Transactional(readOnly = true)
	public EmprestimoResponse buscarPorId(Long id) {
		Emprestimo emprestimo = emprestimoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado"));

		return EmprestimoResponse.fromEntity(emprestimo);
	}

	@Transactional
	public EmprestimoResponse devolver(Long id) {
		Emprestimo emprestimo = emprestimoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado"));

		if (emprestimo.getDataDevolucao() != null) {
			throw new RecursoJaExisteException("Esse livro já foi devolvido");
		}

		emprestimo.setDataDevolucao(LocalDate.now());
		Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);

		return EmprestimoResponse.fromEntity(emprestimoSalvo);
	}

	@Transactional(readOnly = true)
	public List<EmprestimoResponse> listarAtivos() {
		return emprestimoRepository.findByDataDevolucaoIsNull()
				.stream()
				.map(EmprestimoResponse::fromEntity)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<EmprestimoResponse> listarHistoricoPorUsuario(Long usuarioId) {
		if (!usuarioRepository.existsById(usuarioId)) {
			throw new RecursoNaoEncontradoException("Usuário não encontrado");
		}

		return emprestimoRepository.findByUsuarioId(usuarioId)
				.stream()
				.map(EmprestimoResponse::fromEntity)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<EmprestimoResponse> listarAtivosPorUsuario(Long usuarioId) {
		if (!usuarioRepository.existsById(usuarioId)) {
			throw new RecursoNaoEncontradoException("Usuário não encontrado");
		}

		return emprestimoRepository.findByUsuarioIdAndDataDevolucaoIsNull(usuarioId)
				.stream()
				.map(EmprestimoResponse::fromEntity)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<EmprestimoResponse> listarAtrasados() {
		return emprestimoRepository.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now())
				.stream()
				.map(EmprestimoResponse::fromEntity)
				.toList();
	}
}
