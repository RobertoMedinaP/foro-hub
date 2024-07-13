package com.aluracursos.foro.domain.respuesta;

import com.aluracursos.foro.errores.ValidacionDeIntegridad;
import com.aluracursos.foro.domain.topico.Topico;
import com.aluracursos.foro.domain.topico.TopicoRepository;
import com.aluracursos.foro.domain.usuarios.Usuario;
import com.aluracursos.foro.domain.usuarios.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RespuestaService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    public DatosRegistroRespuesta guardarRespuesta(DatosInputRespuesta datos) {
        Long number = Long.parseLong(datos.idUsuario());
        Optional<Usuario> usuario = usuarioRepository.findById(number);
        if (!usuario.isPresent()) {
            throw new ValidacionDeIntegridad("No existe el usuario");
        }
        number = Long.parseLong(datos.idTopico());
        Optional<Topico> topico = topicoRepository.findById(number);
        if (!topico.isPresent()) {
            throw new ValidacionDeIntegridad("Este topico no fue encontrado");
        }
        DatosRegistroRespuesta registro = new DatosRegistroRespuesta(datos.mensaje(), topico.get(), usuario.get(), datos.solucion());

        return registro;
    }

    public Respuesta actualizarRespuesta(Long id, DatosActualizarRespuesta datos) {
        Optional<Respuesta> optionalRespuesta = respuestaRepository.findById(id);

        if (!optionalRespuesta.isPresent()) {
            throw new EntityNotFoundException("Tópico no encontrado");
        }

        Respuesta respuesta = optionalRespuesta.get();

        respuesta.actualizarDatos(datos);

        return respuestaRepository.save(respuesta);
    }
}

