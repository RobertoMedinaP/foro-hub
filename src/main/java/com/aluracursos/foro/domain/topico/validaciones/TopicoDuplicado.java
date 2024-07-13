package com.aluracursos.foro.domain.topico.validaciones;

import com.aluracursos.foro.domain.topico.TopicoRepository;
import com.aluracursos.foro.domain.topico.DatosInputTopico;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class TopicoDuplicado implements ValidadorDeTopico{

    private final TopicoRepository topicoRepository;

    public TopicoDuplicado(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }
    @Override
    public void validar(DatosInputTopico datos) {
        if (topicoRepository.findByTituloIgnoreCase(datos.titulo()) != null &&
                topicoRepository.findByMensajeIgnoreCase(datos.mensaje()) != null) {
            throw new ValidationException("Ya existe un tópico con el mismo título y mensaje");
        }
    }
}

