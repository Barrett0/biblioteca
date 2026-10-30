package com.biblioteca.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioRequest {

	@NotBlank(message = "O nome do usuário é obrigatório")
	private String nome;

	@NotBlank(message = "O e-mail do usuário é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;

	public UsuarioRequest() {
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
