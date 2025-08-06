CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    pacient_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);


INSERT INTO doctors (name, specialization) VALUES ('Dra. Ana Gómez', 'Pediatría');
INSERT INTO doctors (name, specialization) VALUES ('Dr. Carlos Pérez', 'Cardiología');
INSERT INTO doctors (name, specialization) VALUES ('Dr. Luis Rodríguez', 'Dermatología');
INSERT INTO doctors (name, specialization) VALUES ('Dra. María Fernández', 'Neurología');
INSERT INTO doctors (name, specialization) VALUES ('Dr. Jorge Morales', 'Ortopedia');


INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (1, '2025-08-10 09:00:00', 'Lucas Hernández');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (2, '2025-08-10 10:30:00', 'Sofía Ramírez');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (3, '2025-08-11 14:00:00', 'Mateo Torres');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (4, '2025-08-12 11:15:00', 'Valentina Ruiz');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (5, '2025-08-12 16:45:00', 'Emilia Castro');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (1, '2025-08-13 08:30:00', 'Camila López');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (2, '2025-08-13 09:15:00', 'Diego Martínez');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (3, '2025-08-14 13:00:00', 'Isabella Gómez');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (4, '2025-08-14 15:45:00', 'Sebastián Ríos');

INSERT INTO appointments (doctor_id, date_time, pacient_name)
VALUES (5, '2025-08-15 10:00:00', 'Martina Salazar');