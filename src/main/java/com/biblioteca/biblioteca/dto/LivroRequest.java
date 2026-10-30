package com.biblioteca.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LivroRequest {

	@NotBlank(message = "O título é obrigatório")
	private String titulo;

	@NotBlank(message = "O ISBN é obrigatório")
	private String isbn;

	@NotNull(message = "O ano de publicação é obrigatório")
	@Positive(message = "O ano deve ser maior que zero")
	private Integer anoPublicacao;

	@NotNull(message = "O ID do autor é obrigatório")
	@Positive(message = "O ID do autor deve ser maior que zero")
	private Long autorId;

	public LivroRequest() {
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getAnoPublicacao() {
		return anoPublicacao;
	}

	public void setAnoPublicacao(Integer anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public Long getAutorId() {
		return autorId;
	}

	public void setAutorId(Long autorId) {
		this.autorId = autorId;
	}
}
