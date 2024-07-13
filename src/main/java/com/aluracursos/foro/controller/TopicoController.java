package com.aluracursos.foro.controller;

import com.aluracursos.foro.domain.topico.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/topicos")
//@SecurityRequirement(name = "bearer-key")
//@Tag(name = "Tópico", description = "Operaciones de gestión de tópicos") revisar doc
public class TopicoController {
    @Autowired
    private TopicoService topicoService;

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosListarTopicos> registrarTopico(@RequestBody @Valid DatosInputTopico datos, UriComponentsBuilder uriComponentsBuilder) {
        Topico topico = topicoService.registrarTopico(datos);
        DatosListarTopicos datosListarTopico = new DatosListarTopicos(topico.getId(), topico.getTitulo(),
                topico.getMensaje(),topico.getAutor().getNombre(),topico.getCurso().getNombre(), topico.getFechaCreacion());
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(datosListarTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListarTopicos>> listarTopicosActivos(
            @PageableDefault(size = 10, sort = { "fechaCreacion" }, direction = Sort.Direction.ASC) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findByStatusTrue(paginacion).map(DatosListarTopicos::new));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<DatosListarTopicos>> buscarPorCursoYAnio(
            @RequestParam String nombreCurso,
            @RequestParam int year,
            @PageableDefault(size = 10, sort = { "fechaCreacion" }, direction = Sort.Direction.ASC) Pageable paginacion) {
        Page<Topico> topicos = topicoService.buscarTopicosPorCursoYAnio(nombreCurso, year, paginacion);
        return ResponseEntity.ok(topicos.map(DatosListarTopicos::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> obtenerTopicoPorId(@PathVariable Long id) {
        Optional<Topico> topico = topicoService.buscarTopicoPorId(id);
        if (!topico.isPresent()){
            return ResponseEntity.notFound().build();
        }
        DatosDetalleTopico datosDetalleTopico = new DatosDetalleTopico(
                topico.get().getId(),
                topico.get().getTitulo(),
                topico.get().getMensaje(),
                topico.get().getFechaCreacion(),
                topico.get().getStatus(),
                topico.get().getAutor().getNombre(),
                topico.get().getCurso().getNombre()
        );
        return ResponseEntity.ok(datosDetalleTopico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosListarTopicos> actualizarTopico(@PathVariable Long id, @RequestBody DatosInputTopico datos) {
        Topico topicoActualizado = topicoService.actualizarTopico(id, datos);
        DatosListarTopicos datosActualizados = new DatosListarTopicos(topicoActualizado.getId(),topicoActualizado.getTitulo(),topicoActualizado.getMensaje(),topicoActualizado.getAutor().getNombre(),topicoActualizado.getCurso().getNombre(),topicoActualizado.getFechaCreacion());
        return ResponseEntity.ok(datosActualizados);

    }

    @DeleteMapping("/desactivar/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        topico.eliminar();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopicoBD(@PathVariable Long id) {
        topicoService.eliminarTopicoPorId(id);
        return ResponseEntity.noContent().build();
    }
}

