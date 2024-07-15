create table respuesta(
    id bigint auto_increment,
    mensaje text not null,
    topico_id bigint not null,
    fecha_creacion timestamp default current_timestamp,
    autor_id bigint not null,
    solucion boolean default false,
    primary key (id),
    constraint fk_respuesta_topico_id foreign key (topico_id) references topico(id),
    constraint fk_respuesta_autor_id foreign key (autor_id) references usuarios(id)
)