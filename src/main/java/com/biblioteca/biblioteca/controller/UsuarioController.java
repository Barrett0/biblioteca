package com.biblioteca.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.biblioteca.dto.UsuarioRequest;
import com.biblioteca.biblioteca.dto.UsuarioResponse;
import com.biblioteca.biblioteca.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações de gerenciamento de usuários da biblioteca")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Operation(summary = "Cadastrar usuário", description = "Cadastra um novo usuário na biblioteca")
	@PostMapping
	public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioRequest request) {
		UsuarioResponse response = usuarioService.cadastrar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Listar usuários", description = "Retorna todos os usuários cadastrados")
	@GetMapping
	public ResponseEntity<List<UsuarioResponse>> listarTodos() {
		return ResponseEntity.ok(usuarioService.listarTodos());
	}

	@Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico")
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(usuarioService.buscarPorId(id));
	}

	@Operation(summary = "Atualizar usuário", description = "Atualiza o nome e o e-mail de um usuário cadastrado")
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponse> atualizar(
			@PathVariable Long id,
			@Valid @RequestBody UsuarioRequest request) {
		UsuarioResponse resposta = usuarioService.atualizar(id, request);
		return ResponseEntity.ok(resposta);
	}

	@Operation(summary = "Excluir usuário", description = "Exclui um usuário pelo seu identificador")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		usuarioService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
