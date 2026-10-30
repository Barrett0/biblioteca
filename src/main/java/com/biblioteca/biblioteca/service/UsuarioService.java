package com.biblioteca.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.biblioteca.dto.UsuarioRequest;
import com.biblioteca.biblioteca.dto.UsuarioResponse;
import com.biblioteca.biblioteca.exception.RecursoJaExisteException;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	public UsuarioResponse cadastrar(UsuarioRequest request) {
		if (usuarioRepository.existsByEmail(request.getEmail())) {
			throw new RecursoJaExisteException("Já existe um usuário cadastrado com esse e-mail");
		}
		Usuario usuario = new Usuario(null, request.getNome(), request.getEmail());
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return UsuarioResponse.fromEntity(usuarioSalvo);
	}

	@Transactional(readOnly = true)
	public List<UsuarioResponse> listarTodos() {
		return usuarioRepository.findAll().stream().map(UsuarioResponse::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public UsuarioResponse buscarPorId(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		return UsuarioResponse.fromEntity(usuario);
	}

	@Transactional
	public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		if (!usuario.getEmail().equals(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
			throw new RecursoJaExisteException("Já existe um usuário cadastrado com esse e-mail");
		}

		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());

		Usuario usuarioAtualizado = usuarioRepository.save(usuario);

		return UsuarioResponse.fromEntity(usuarioAtualizado);
	}

	@Transactional
	public void excluir(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

		usuarioRepository.delete(usuario);
	}
}
