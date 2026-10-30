package com.biblioteca.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.biblioteca.dto.AutorRequest;
import com.biblioteca.biblioteca.dto.AutorResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Autor;
import com.biblioteca.biblioteca.repository.AutorRepository;

@Service
public class AutorService {

	private final AutorRepository autorRepository;

	public AutorService(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

	@Transactional
	public AutorResponse cadastrar(AutorRequest request) {
		if (autorRepository.existsByEmail(request.getEmail())) {
			throw new RecursoJaExisteException("Já existe um autor cadastrado com esse e-mail");
		}
		Autor autor = new Autor(null, request.getNome(), request.getEmail());
		Autor autorSalvo = autorRepository.save(autor);

		return AutorResponse.fromEntity(autorSalvo);
	}

	@Transactional(readOnly = true)
	public List<AutorResponse> listarTodos() {
		return autorRepository.findAll().stream().map(AutorResponse::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public AutorResponse buscarPorId(Long id) {
		Autor autor = autorRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado"));

		return AutorResponse.fromEntity(autor);
	}

	@Transactional
	public AutorResponse atualizar(Long id, AutorRequest request) {
		Autor autor = autorRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado"));

		if (!autor.getEmail().equals(request.getEmail()) && autorRepository.existsByEmail(request.getEmail())) {
			throw new RecursoJaExisteException("Já existe um autor cadastrado com esse e-mail");
		}

		autor.setNome(request.getNome());
		autor.setEmail(request.getEmail());

		Autor autorAtualizado = autorRepository.save(autor);

		return AutorResponse.fromEntity(autorAtualizado);
	}

	@Transactional
	public void excluir(Long id) {
		Autor autor = autorRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado"));

		autorRepository.delete(autor);
	}
}
