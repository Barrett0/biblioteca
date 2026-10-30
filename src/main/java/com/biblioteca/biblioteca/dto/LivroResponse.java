package com.biblioteca.biblioteca.dto;

import com.biblioteca.biblioteca.model.Livro;

public class LivroResponse {

	private Long id;
	private String titulo;
	private String isbn;
	private Integer anoPublicacao;
	private Long autorId;
	private String autorNome;

	public LivroResponse() {
	}

	public LivroResponse(Long id, String titulo, String isbn, Integer anoPublicacao, Long autorId, String autorNome) {
		this.id = id;
		this.titulo = titulo;
		this.isbn = isbn;
		this.anoPublicacao = anoPublicacao;
		this.autorId = autorId;
		this.autorNome = autorNome;
	}

	public static LivroResponse fromEntity(Livro livro) {
		return new LivroResponse(
				livro.getId(),
				livro.getTitulo(),
				livro.getIsbn(),
				livro.getAnoPublicacao(),
				livro.getAutor() != null ? livro.getAutor().getId() : null,
				livro.getAutor() != null ? livro.getAutor().getNome() : null
		);
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getIsbn() {
		return isbn;
	}

	public Integer getAnoPublicacao() {
		return anoPublicacao;
	}

	public Long getAutorId() {
		return autorId;
	}

	public String getAutorNome() {
		return autorNome;
	}
}
