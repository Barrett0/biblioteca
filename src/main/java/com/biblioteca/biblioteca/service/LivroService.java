package com.biblioteca.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.biblioteca.dto.LivroRequest;
import com.biblioteca.biblioteca.dto.LivroResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Autor;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.repository.AutorRepository;
import com.biblioteca.biblioteca.repository.LivroRepository;

@Service
public class LivroService {

	private final LivroRepository livroRepository;
	private final AutorRepository autorRepository;

	public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
		this.livroRepository = livroRepository;
		this.autorRepository = autorRepository;
	}

	@Transactional
	public LivroResponse cadastrar(LivroRequest request) {
		if (livroRepository.existsByIsbn(request.getIsbn())) {
			throw new RecursoJaExisteException("Já existe um livro cadastrado com esse ISBN");
		}

		Autor autor = autorRepository.findById(request.getAutorId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado"));

		Livro livro = new Livro();
		livro.setTitulo(request.getTitulo());
		livro.setIsbn(request.getIsbn());
		livro.setAnoPublicacao(request.getAnoPublicacao());
		livro.setAutor(autor);

		Livro livroSalvo = livroRepository.save(livro);
		return LivroResponse.fromEntity(livroSalvo);
	}

	@Transactional(readOnly = true)
	public List<LivroResponse> listarTodos() {
		return livroRepository.findAll().stream().map(LivroResponse::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public LivroResponse buscarPorId(Long id) {
		Livro livro = livroRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado"));

		return LivroResponse.fromEntity(livro);
	}

	@Transactional
	public LivroResponse atualizar(Long id, LivroRequest request) {
		Livro livro = livroRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado"));

		Autor autor = autorRepository.findById(request.getAutorId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado"));

		if (!livro.getIsbn().equals(request.getIsbn()) && livroRepository.existsByIsbn(request.getIsbn())) {
			throw new RecursoJaExisteException("Já existe outro livro cadastrado com esse ISBN");
		}

		livro.setTitulo(request.getTitulo());
		livro.setIsbn(request.getIsbn());
		livro.setAnoPublicacao(request.getAnoPublicacao());
		livro.setAutor(autor);

		Livro livroAtualizado = livroRepository.save(livro);
		return LivroResponse.fromEntity(livroAtualizado);
	}

	@Transactional
	public void excluir(Long id) {
		Livro livro = livroRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado"));

		livroRepository.delete(livro);
	}
}
