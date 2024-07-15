create table topico(
    id bigint not null auto_increment,
    titulo varchar(200) not null unique,
    mensaje text not null,
    fecha_creacion timestamp default current_timestamp,
    status varchar(50),
    autor_id bigint not null,
    curso_id bigint not null,
    primary key (id),
    foreign key (autor_id) references usuarios(id),
    foreign key (curso_id) references cursos(id)
);