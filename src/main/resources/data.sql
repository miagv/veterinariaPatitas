-- Insertar servicios
INSERT INTO service_vet (name, description) VALUES
('Consulta General', 'Revisión general de la mascota'),
('Vacunación', 'Servicio de vacunación y refuerzos'),
('Cirugía', 'Procedimientos quirúrgicos'),
('Grooming', 'Servicio de peluquería y baño'),
('Emergencia', 'Atención de emergencias 24/7');

-- Insertar médicos
INSERT INTO medico (nombre, apellido, especialidad, servicio_id) VALUES
('Juan', 'Pérez', 'Medicina General', 1),
('María', 'García', 'Cirujana', 3),
('Carlos', 'López', 'Medicina General', 1),
('Ana', 'Martínez', 'Emergencias', 5),
('Pedro', 'Sánchez', 'Grooming', 4);

-- Insertar horarios disponibles por médico
INSERT INTO horario_medico (medico_id, dia_semana, hora_inicio, hora_fin) VALUES
(1, 'LUNES', '09:00:00', '17:00:00'),
(1, 'MARTES', '09:00:00', '17:00:00'),
(2, 'LUNES', '10:00:00', '18:00:00'),
(2, 'MIÉRCOLES', '10:00:00', '18:00:00'),
(3, 'JUEVES', '08:00:00', '16:00:00'),
(3, 'VIERNES', '08:00:00', '16:00:00'),
(4, 'LUNES', '00:00:00', '23:59:59'),
(4, 'MARTES', '00:00:00', '23:59:59'),
(5, 'MIÉRCOLES', '09:00:00', '17:00:00'),
(5, 'JUEVES', '09:00:00', '17:00:00');