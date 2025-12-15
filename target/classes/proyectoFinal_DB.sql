-- 1. CREACIÓN DE LA BASE DE DATOS
-- ----------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS TerapiaSaludMental 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE TerapiaSaludMental;

--------------------------------------------------------------------------------
-- TABLA USUARIOS
-- Entidad principal para Pacientes, Profesionales y Administradores.
--------------------------------------------------------------------------------
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    
    -- Utilizado para el login y debe ser único (H.U. 1)
    email VARCHAR(100) NOT NULL UNIQUE, 
    
    -- Almacena el hash de la contraseña (BCrypt, implementado en UsuarioServicio)
    password_hash VARCHAR(255) NOT NULL, 
    
    -- Define si es PACIENTE, PROFESIONAL o ADMINISTRADOR
    tipo_usuario ENUM('PACIENTE', 'PROFESIONAL', 'ADMINISTRADOR') NOT NULL DEFAULT 'PACIENTE',
    
    -- Campo usado para el filtro de especialidad (H.U. 3)
    especialidad VARCHAR(100), 
    
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- TABLA CUESTIONARIO_RESPUESTAS
-- Guarda la evaluación inicial del paciente.
--------------------------------------------------------------------------------
CREATE TABLE cuestionario_respuestas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Referencia al usuario que completó el cuestionario
    usuario_id BIGINT NOT NULL, 
    
    fecha_evaluacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    puntuacion_total INT NOT NULL, 
    resumen_resultado TEXT, 
    
    -- Clave Foránea: Relaciona la respuesta con el usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

--------------------------------------------------------------------------------
-- TABLA CITAS
-- Guarda las sesiones programadas entre pacientes y profesionales.
--------------------------------------------------------------------------------
CREATE TABLE citas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Referencia al paciente que agenda la cita
    paciente_id BIGINT NOT NULL, 
    
    -- Referencia al terapeuta (profesional) asignado
    profesional_id BIGINT NOT NULL, 
    
    fecha_hora DATETIME NOT NULL,
    
    -- Estado de la sesión
    estado VARCHAR(50) NOT NULL DEFAULT 'programada', 
    
    -- Clave Foránea 1: Paciente
    FOREIGN KEY (paciente_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    
    -- Clave Foránea 2: Profesional
    FOREIGN KEY (profesional_id) REFERENCES usuarios(id) ON DELETE RESTRICT
);

-- Índice de optimización para búsquedas rápidas de citas por paciente
CREATE INDEX idx_citas_paciente_fecha ON citas (paciente_id, fecha_hora);

INSERT INTO usuarios (nombre, email, password_hash, tipo_usuario, especialidad) 
VALUES ('Terapeuta Prueba', 'prueba@test.com', '$2a$10$Zhd5/kuPMh7yA5eV8Ey7W.WobYw0vSUcOpyHtPQCuYwSqvXuE2qt', 'PROFESIONAL', 'Psicología Clínica');

INSERT INTO usuarios (nombre, email, password_hash, tipo_usuario, especialidad) 
VALUES ('Terapeuta Prueba', 'profe@test.com', '$2a$10$fXJ5JNLwyvz8zTtt2k7DBupAa50ukPK5tATayOZ84cchm4Ooq0SWi', 'PROFESIONAL', 'Psicología Clínica');

INSERT INTO usuarios (nombre, email, password_hash, tipo_usuario, especialidad)
VALUES (
    'Admin General',
    'adminG@test.com',
    '$2a$10$fXJ5JNLwyvz8zTtt2k7DBupAa50ukPK5tATayOZ84cchm4Ooq0SWi',
    'ADMINISTRADOR',
    'Administrador'
);-- La contraseña deberia de ser Admin123

-- O para mas seguridad se crea el usuario como paciente desde la app y luego se utiliza este update con el usuario creado para hacerlo administrador 

UPDATE usuarios SET tipo_usuario = 'ADMINISTRADOR',
    especialidad = 'Administrador'
WHERE email = 'admin@test.com';



Select * from usuarios


