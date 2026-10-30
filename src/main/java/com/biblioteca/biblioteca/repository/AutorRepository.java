package com.biblioteca.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.biblioteca.model.Autor;

public interface AutorRepository extends JpaRepository<Autor,Long>{
	
	boolean existsByEmail(String email);

}
