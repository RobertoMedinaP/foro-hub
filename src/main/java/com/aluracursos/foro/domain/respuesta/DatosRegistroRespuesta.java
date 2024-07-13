package com.aluracursos.foro.domain.respuesta;

import com.aluracursos.foro.domain.topico.Topico;
import com.aluracursos.foro.domain.usuarios.Usuario;

public record DatosRegistroRespuesta(
        String mensaje,
        Topico topico,
        Usuario autor,
        String solucion
) {

}
