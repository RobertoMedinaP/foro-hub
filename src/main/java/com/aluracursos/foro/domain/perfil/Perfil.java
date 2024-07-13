package com.aluracursos.foro.domain.perfil;

import com.aluracursos.foro.domain.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


    @Entity(name = "Perfil")
    @Table(name = "perfil")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = "id")
    public class Perfil {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nombre;
        @ManyToMany(mappedBy = "perfiles")
        private List<Usuario> usuarios;

        public Perfil(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return "Perfil [nombre=" + nombre + ", usuarios=" + usuarios + "]";
        }

    }
