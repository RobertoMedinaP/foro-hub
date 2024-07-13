package com.aluracursos.foro.domain.topico;

import com.aluracursos.foro.domain.curso.Curso;
import com.aluracursos.foro.domain.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public record DatosRegistroTopico(
        String titulo,
        String mensaje,
        @JsonManagedReference
        Usuario idUsuario,
        Curso nombreCurso
) {
}

