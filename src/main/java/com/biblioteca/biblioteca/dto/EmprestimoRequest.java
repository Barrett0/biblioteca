package com.biblioteca.biblioteca.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EmprestimoRequest {

	@NotNull(message = "O ID do usuário é obrigatório")
	@Positive(message = "O ID do usuário deve ser maior que zero")
	private Long usuarioId;

	@NotNull(message = "O ID do livro é obrigatório")
	@Positive(message = "O ID do livro deve ser maior que zero")
	private Long livroId;

	public EmprestimoRequest() {
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Long getLivroId() {
		return livroId;
	}

	public void setLivroId(Long livroId) {
		this.livroId = livroId;
	}
}
