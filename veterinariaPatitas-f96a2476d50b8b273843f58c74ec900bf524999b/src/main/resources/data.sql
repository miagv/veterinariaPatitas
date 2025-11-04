-- Insertar servicios (la entidad ServiceVet mapea a la tabla `servicios`)
-- Agregamos price y duration porque la entidad los define como NOT NULL
INSERT INTO servicios (name, description, price, duration) VALUES
('Consulta General', 'Revisión general de la mascota', 50.00, '30min'),
('Vacunación', 'Servicio de vacunación y refuerzos', 30.00, '15min'),
('Cirugía', 'Procedimientos quirúrgicos', 250.00, '120min'),
('Grooming', 'Servicio de peluquería y baño', 40.00, '45min'),
('Emergencia', 'Atención de emergencias 24/7', 0.00, 'varios');

-- Nota: la tabla `medico` no tiene columna 'apellido'. combinamos nombre y apellido en
-- el campo `nombre` para que el INSERT coincida con la entidad.
-- INSERT INTO medico (nombre, especialidad, servicio_id) VALUES
-- ('Juan Pérez', 'Medicina General', 1),
-- ('María García', 'Cirujana', 3),
-- ('Carlos López', 'Medicina General', 1),
-- ('Ana Martínez', 'Emergencias', 5),
-- ('Pedro Sánchez', 'Grooming', 4);

-- Insertar horarios disponibles por médico
-- Comentado porque los INSERT referencian IDs de `medico` que no existen cuando
-- Spring ejecuta los scripts (los beans de DataInitializer crean médicos en tiempo de ejecución).
-- Si quieres usar exclusivamente SQL para semilla, mueve también los INSERT de `medico`
-- antes de estos inserts o deja que `DataInitializer` cree los médicos.
-- INSERT INTO horario_medico (medico_id, dia_semana, hora_inicio, hora_fin) VALUES
-- (1, 'MONDAY', '09:00:00', '17:00:00'),
-- (1, 'TUESDAY', '09:00:00', '17:00:00'),
-- (2, 'MONDAY', '10:00:00', '18:00:00'),
-- (2, 'WEDNESDAY', '10:00:00', '18:00:00'),
-- (3, 'THURSDAY', '08:00:00', '16:00:00'),
-- (3, 'FRIDAY', '08:00:00', '16:00:00'),
-- (4, 'MONDAY', '00:00:00', '23:59:59'),
-- (4, 'TUESDAY', '00:00:00', '23:59:59'),
-- (5, 'WEDNESDAY', '09:00:00', '17:00:00'),
-- (5, 'THURSDAY', '09:00:00', '17:00:00');

-- ***** Datos de ejemplo para productos (se usan en el simulador de ventas)
-- Reemplaza/semilla 6 productos con precios < S/.200 y stocks variados
TRUNCATE TABLE productos;

INSERT INTO productos (name, price, stock, unit) VALUES
('Shampoo Antipulgas para Perro', 49.90, 20, 'Frasco 250ml'),
('Cepillo para Peinar', 24.50, 15, 'Unidad'),
('Manta para Dormir', 89.00, 14, 'Unidad'),
('Camita para Perro - Cozy', 159.00, 17, 'Unidad'),
('Arenero para Gatos - Compacto', 79.90, 10, 'Unidad'),
('Comida Húmeda para Gato - Lata 400g', 12.50, 5, 'Lata 400g');
