package com.biblioteca.biblioteca.dto;

import com.biblioteca.biblioteca.model.Autor;

  public record AutorResponse(
   
		  Long id,
        String nome,
        String email
  ) 
 {

    public static AutorResponse fromEntity(Autor autor) {
        
    	return new AutorResponse(
                autor.getId(),
                autor.getNome(),
                autor.getEmail()
        );
    }
}
