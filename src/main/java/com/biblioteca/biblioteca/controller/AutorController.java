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

import com.biblioteca.biblioteca.dto.AutorRequest;
import com.biblioteca.biblioteca.dto.AutorResponse;
import com.biblioteca.biblioteca.service.AutorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/autores")
@Tag(name = "Autores", description = "Operações de gerenciamento de autores")
public class AutorController {

	private final AutorService autorService;

	public AutorController(AutorService autorService) {
		this.autorService = autorService;
	}

	@Operation(summary = "Cadastrar autor", description = "Cadastrar um novo autor na biblioteca")
	@PostMapping
	public ResponseEntity<AutorResponse> cadastrar(@Valid @RequestBody AutorRequest request) {
		AutorResponse response = autorService.cadastrar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Listar autores", description = "Retornar todos os autores cadastrados")
	@GetMapping
	public ResponseEntity<List<AutorResponse>> listarTodos() {
		return ResponseEntity.ok(autorService.listarTodos());
	}

	@Operation(summary = "Buscar autor por ID", description = "Retornar os dados de um autor pelo seu identificador")
	@GetMapping("/{id}")
	public ResponseEntity<AutorResponse> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(autorService.buscarPorId(id));
	}

	@Operation(summary = "Atualizar autor", description = "Atualiza o nome e o e-mail de um autor cadastrado")
	@PutMapping("/{id}")
	public ResponseEntity<AutorResponse> atualizar(
			@PathVariable Long id,
			@Valid @RequestBody AutorRequest request) {
		return ResponseEntity.ok(autorService.atualizar(id, request));
	}

	@Operation(summary = "Excluir autor", description = "Excluir um autor pelo seu identificador")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		autorService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
