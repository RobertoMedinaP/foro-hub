package com.aluracursos.foro.domain.usuarios;

import com.aluracursos.foro.domain.perfil.Perfil;
import jakarta.validation.constraints.Email;

import java.util.List;

public record DatosRegistroUsuario(String nombre,
                                   @Email
                                   String correoElectronico,
                                   String contrasena,
                                   List<Perfil> perfiles) {
}
