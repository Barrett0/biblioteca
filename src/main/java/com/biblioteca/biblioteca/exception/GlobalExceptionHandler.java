package com.biblioteca.biblioteca.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<Map<String, Object>> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException exception) {
		return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(RecursoJaExisteException.class)
	public ResponseEntity<Map<String, Object>> tratarConflito(RecursoJaExisteException exception) {
		return criarResposta(HttpStatus.CONFLICT, exception.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> tratarViolacaoDeIntegridade(DataIntegrityViolationException exception) {
		return criarResposta(HttpStatus.CONFLICT,
				"Operação não permitida pois viola a integridade dos dados (registro vinculado a outros dados)");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> tratarMensagemIlegivel(HttpMessageNotReadableException exception) {
		return criarResposta(HttpStatus.BAD_REQUEST, "Corpo da requisição ausente ou mal formatado");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException exception) {
		Map<String, String> erros = new HashMap<>();

		exception.getBindingResult().getFieldErrors().forEach(error ->
				erros.put(error.getField(), error.getDefaultMessage())
		);

		Map<String, Object> resposta = new HashMap<>();
		resposta.put("status", HttpStatus.BAD_REQUEST.value());
		resposta.put("erro", "Erro de validação");
		resposta.put("mensagens", erros);
		resposta.put("data", LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> tratarErroInesperado(Exception exception) {
		return criarResposta(HttpStatus.INTERNAL_SERVER_ERROR,
				"Ocorreu um erro interno no servidor: " + exception.getMessage());
	}

	private ResponseEntity<Map<String, Object>> criarResposta(HttpStatus status, String mensagem) {
		Map<String, Object> resposta = new HashMap<>();
		resposta.put("status", status.value());
		resposta.put("erro", status.getReasonPhrase());
		resposta.put("mensagem", mensagem);
		resposta.put("data", LocalDateTime.now());

		return ResponseEntity.status(status).body(resposta);
	}
}
