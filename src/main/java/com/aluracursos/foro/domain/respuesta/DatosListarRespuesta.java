package com.aluracursos.foro.domain.respuesta;

import java.time.LocalDateTime;

public record DatosListarRespuesta(
        Long id,
        String mensaje,
        String topico,
        String autor,
        String solucion,
        LocalDateTime fechaCreacion
) {
}
