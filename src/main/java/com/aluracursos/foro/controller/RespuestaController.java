package com.aluracursos.foro.controller;

import com.aluracursos.foro.domain.respuesta.*;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/respuestas")
//@SecurityRequirement(name = "bearer-key") swagger
//@Tag(name = "Respúesta", description = "Operaciones de gestión de respuesta") swagger
public class RespuestaController {
    @Autowired
    RespuestaService respuestaService;

    @Autowired
    RespuestaRepository respuestaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosListarRespuesta> registarTopico(@RequestBody @Valid DatosInputRespuesta datosEntradaRespuestas,
                                                               UriComponentsBuilder uriComponentsBuilder) {
        DatosRegistroRespuesta registroNuevo = respuestaService.guardarRespuesta(datosEntradaRespuestas);
        Respuesta respuesta = new Respuesta(registroNuevo);
        respuestaRepository.save(respuesta);
        DatosListarRespuesta datosListarRespuesta = new DatosListarRespuesta(respuesta.getId(),respuesta.getMensaje(),
                respuesta.getTopico().getTitulo(),respuesta.getAutor().getNombre().toString(),respuesta.getSolucion(),respuesta.getFechaCreacion());
        URI uri = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(datosListarRespuesta);
    }

    @GetMapping
    public ResponseEntity<?> consultar(
            @PageableDefault(size = 5,sort = { "id" }, direction = Sort.Direction.ASC) Pageable paginacion) {
        return ResponseEntity.ok(respuestaRepository.findAll(paginacion).stream()
                .map(t -> new DatosListarRespuesta(t.getId(),t.getMensaje(),t.getTopico().getTitulo(),t.getAutor().getNombre().toString(),t.getSolucion(),t.getFechaCreacion())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosListarRespuesta> retornarUsuario(@PathVariable Long id) {
        Optional<Respuesta> respuesta = Optional.ofNullable(respuestaRepository.getReferenceById(id));
        if (!respuesta.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        DatosListarRespuesta datosListarUsuario = new DatosListarRespuesta(respuesta.get().getId(), respuesta.get().getMensaje(),
                respuesta.get().getTopico().getTitulo(),respuesta.get().getAutor().getNombre(), respuesta.get().getSolucion(),respuesta.get().getFechaCreacion());
        return ResponseEntity.ok(datosListarUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosListarRespuesta> actualizarTopico(@PathVariable Long id, @RequestBody DatosActualizarRespuesta datos) {
        Respuesta respuestaActualizado = respuestaService.actualizarRespuesta(id, datos);
        DatosListarRespuesta datosActualizados = new DatosListarRespuesta(respuestaActualizado.getId(),respuestaActualizado.getMensaje(),
                respuestaActualizado.getTopico().getTitulo(),respuestaActualizado.getAutor().getNombre(),respuestaActualizado.getSolucion(),
                respuestaActualizado.getFechaCreacion());
        return ResponseEntity.ok(datosActualizados);

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Respuesta> respuesta = respuestaRepository.findById(id);
        System.out.println(respuesta);
        if(!respuesta.isPresent()) {
            throw new ValidationException("este id para la respuesta no fue encontrado");
        }
        respuestaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

