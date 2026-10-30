package com.biblioteca.biblioteca.dto;

import com.biblioteca.biblioteca.model.Usuario;

public record UsuarioResponse(
			
			Long id,
			String nome,
			String email
		){
		public static UsuarioResponse fromEntity(Usuario usuario){
		    return new UsuarioResponse(
		    		usuario.getId(),
		    		usuario.getNome(),
		    		usuario.getEmail()
		    		
);	

		}
	}

