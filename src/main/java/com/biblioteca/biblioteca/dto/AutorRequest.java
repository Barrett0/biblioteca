package com.biblioteca.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AutorRequest {

	@NotBlank(message = "O nome do autor é obrigatório")
	private String nome;

	@NotBlank(message = "O e-mail do autor é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;

	public AutorRequest() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
