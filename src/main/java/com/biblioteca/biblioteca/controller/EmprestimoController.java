package com.biblioteca.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.biblioteca.dto.EmprestimoRequest;
import com.biblioteca.biblioteca.dto.EmprestimoResponse;
import com.biblioteca.biblioteca.service.EmprestimoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimos", description = "Operações de empréstimo e devolução de livros")
public class EmprestimoController {

	private final EmprestimoService emprestimoService;

	public EmprestimoController(EmprestimoService emprestimoService) {
		this.emprestimoService = emprestimoService;
	}

	@Operation(summary = "Realizar empréstimo", description = "Registra um novo empréstimo de livro para um usuário")
	@PostMapping
	public ResponseEntity<EmprestimoResponse> realizarEmprestimo(@Valid @RequestBody EmprestimoRequest request) {
		EmprestimoResponse response = emprestimoService.realizarEmprestimo(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Listar todos os empréstimos", description = "Retorna todos os empréstimos cadastrados")
	@GetMapping
	public ResponseEntity<List<EmprestimoResponse>> listarTodos() {
		return ResponseEntity.ok(emprestimoService.listarTodos());
	}

	@Operation(summary = "Buscar empréstimo por ID", description = "Retorna os dados de um empréstimo pelo seu identificador")
	@GetMapping("/{id}")
	public ResponseEntity<EmprestimoResponse> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(emprestimoService.buscarPorId(id));
	}

	@Operation(summary = "Devolver livro", description = "Registra a devolução de um empréstimo em aberto")
	@PutMapping("/{id}/devolver")
	public ResponseEntity<EmprestimoResponse> devolver(@PathVariable Long id) {
		return ResponseEntity.ok(emprestimoService.devolver(id));
	}

	@Operation(summary = "Listar empréstimos ativos", description = "Retorna todos os empréstimos ainda não devolvidos")
	@GetMapping("/ativos")
	public ResponseEntity<List<EmprestimoResponse>> listarAtivos() {
		return ResponseEntity.ok(emprestimoService.listarAtivos());
	}

	@Operation(summary = "Histórico de empréstimos do usuário", description = "Retorna todos os empréstimos já realizados por um usuário")
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<EmprestimoResponse>> listarHistoricoPorUsuario(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(emprestimoService.listarHistoricoPorUsuario(usuarioId));
	}

	@Operation(summary = "Empréstimos ativos do usuário", description = "Retorna os empréstimos ativos de um usuário específico")
	@GetMapping("/usuario/{usuarioId}/ativos")
	public ResponseEntity<List<EmprestimoResponse>> listarAtivosPorUsuario(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(emprestimoService.listarAtivosPorUsuario(usuarioId));
	}

	@Operation(summary = "Listar empréstimos atrasados", description = "Retorna empréstimos não devolvidos cuja data prevista já passou")
	@GetMapping("/atrasados")
	public ResponseEntity<List<EmprestimoResponse>> listarAtrasados() {
		return ResponseEntity.ok(emprestimoService.listarAtrasados());
	}
}
