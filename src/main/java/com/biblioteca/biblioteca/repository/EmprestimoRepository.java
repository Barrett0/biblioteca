package com.biblioteca.biblioteca.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

	boolean existsByLivroAndDataDevolucaoIsNull(Livro livro);

	List<Emprestimo> findByDataDevolucaoIsNull();

	List<Emprestimo> findByUsuarioId(Long usuarioId);

	List<Emprestimo> findByUsuarioIdAndDataDevolucaoIsNull(Long usuarioId);

	List<Emprestimo> findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate data);

	long countByUsuarioIdAndDataDevolucaoIsNull(Long usuarioId);
}
