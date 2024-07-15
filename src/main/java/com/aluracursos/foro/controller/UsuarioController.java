package com.aluracursos.foro.controller;

import com.aluracursos.foro.domain.usuarios.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Operaciones de gestión de usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario, UriComponentsBuilder uriComponentsBuilder){
        Usuario usuario = usuarioRepository.save(new Usuario(datosRegistroUsuario));
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(usuario.getNombre(),
                usuario.getCorreoElectronico()
        );
        URI uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(datosRespuestaUsuario);
    }


    @GetMapping
    public ResponseEntity paginadoUsuarios() {
        Stream<Object> usuarios = usuarioRepository.findAll().stream().map(u -> new DatosListarUsuario(u.getId(), u.getCorreoElectronico(), u.getNombre()));
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retornarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = Optional.ofNullable(usuarioRepository.getReferenceById(id));
        if (!usuario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        DatosListarUsuario datosListarUsuario = new DatosListarUsuario(usuario.get().getId(), usuario.get().getCorreoElectronico(),
                usuario.get().getNombre());
        return ResponseEntity.ok(datosListarUsuario);
    }

    @PutMapping()
    @Transactional
    public ResponseEntity actualizarUsuario(@RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario){
        Usuario usuario = usuarioRepository.getReferenceById(datosActualizarUsuario.id());

        usuario.actualizarDatos(datosActualizarUsuario);

        return ResponseEntity.ok(new DatosRespuestaUsuario(usuario.getNombre(), usuario.getCorreoElectronico()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        var usuario = usuarioRepository.findById(id);
        if(usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

}

