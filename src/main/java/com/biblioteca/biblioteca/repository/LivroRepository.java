package com.biblioteca.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.biblioteca.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long>{
	
    boolean existsByIsbn(String isbn);


}
