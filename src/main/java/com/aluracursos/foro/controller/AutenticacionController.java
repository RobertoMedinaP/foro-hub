package com.aluracursos.foro.controller;

import com.aluracursos.foro.domain.usuarios.DatosAutenticacionUsuario;
import com.aluracursos.foro.domain.usuarios.Usuario;
import com.aluracursos.foro.security.DatosJWTToken;
import com.aluracursos.foro.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
//@Tag(name = "Autenticación", description = "Operaciones de autenticación") Revisar swagger
public class AutenticacionController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    //@Operation(summary = "Iniciar sesión", description = "Permite iniciar sesión a un usuario") tb swagger
    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario){
        Authentication authToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.correoElectronico(),
                datosAutenticacionUsuario.contrasena());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var JWTToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        String tipo = "Bearer";
        return ResponseEntity.ok(new DatosJWTToken(JWTToken,tipo));
    }
}
