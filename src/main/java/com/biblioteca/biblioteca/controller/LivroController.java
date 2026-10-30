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

import com.biblioteca.biblioteca.dto.LivroRequest;
import com.biblioteca.biblioteca.dto.LivroResponse;
import com.biblioteca.biblioteca.service.LivroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Operações de gerenciamento de livros")
public class LivroController {

	private final LivroService livroService;

	public LivroController(LivroService livroService) {
		this.livroService = livroService;
	}

	@Operation(summary = "Cadastrar livro", description = "Cadastrar um livro associado a um autor existente")
	@PostMapping
	public ResponseEntity<LivroResponse> cadastrar(@Valid @RequestBody LivroRequest request) {
		LivroResponse response = livroService.cadastrar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Listar livros", description = "Retornar todos os livros cadastrados")
	@GetMapping
	public ResponseEntity<List<LivroResponse>> listarTodos() {
		return ResponseEntity.ok(livroService.listarTodos());
	}

	@Operation(summary = "Buscar livro por ID", description = "Retornar os dados de um livro pelo seu identificador")
	@GetMapping("/{id}")
	public ResponseEntity<LivroResponse> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(livroService.buscarPorId(id));
	}

	@Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro cadastrado")
	@PutMapping("/{id}")
	public ResponseEntity<LivroResponse> atualizar(
			@PathVariable Long id,
			@Valid @RequestBody LivroRequest request) {
		return ResponseEntity.ok(livroService.atualizar(id, request));
	}

	@Operation(summary = "Excluir livro", description = "Excluir um livro pelo seu identificador")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		livroService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
