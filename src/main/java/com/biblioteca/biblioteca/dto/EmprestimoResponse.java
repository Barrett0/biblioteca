package com.biblioteca.biblioteca.dto;

import java.time.LocalDate;

import com.biblioteca.biblioteca.model.Emprestimo;

public record EmprestimoResponse(
        Long id,
        Long usuarioId,
        String usuarioNome,
        Long livroId,
        String livroTitulo,
        LocalDate dataEmprestimo,
        LocalDate dataPrevistaDevolucao,
        LocalDate dataDevolucao
) {

    public static EmprestimoResponse fromEntity(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getUsuario() != null ? emprestimo.getUsuario().getId() : null,
                emprestimo.getUsuario() != null ? emprestimo.getUsuario().getNome() : null,
                emprestimo.getLivro() != null ? emprestimo.getLivro().getId() : null,
                emprestimo.getLivro() != null ? emprestimo.getLivro().getTitulo() : null,
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevistaDevolucao(),
                emprestimo.getDataDevolucao()
        );
    }
}
