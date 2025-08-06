CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL,
    date_time TIMESTAMP NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Client(id)
);

INSERT INTO clients (name) VALUES ('Laura Martínez');
INSERT INTO clients (name) VALUES ('Carlos Gómez');
INSERT INTO clients (name) VALUES ('Ana Rodríguez');
INSERT INTO clients (name) VALUES ('Pedro Sánchez');
INSERT INTO clients (name) VALUES ('María Fernández');

-- Cliente 1
INSERT INTO appointments (client_id, date_time) VALUES (1, '2025-08-11 09:00:00');  -- lunes
INSERT INTO appointments (client_id, date_time) VALUES (1, '2025-09-08 09:00:00');  -- lunes

-- Cliente 2
INSERT INTO appointments (client_id, date_time) VALUES (2, '2025-08-12 10:00:00');  -- martes
INSERT INTO appointments (client_id, date_time) VALUES (2, '2025-09-09 10:00:00');  -- martes

-- Cliente 3
INSERT INTO appointments (client_id, date_time) VALUES (3, '2025-08-13 11:45:00');  -- miércoles
INSERT INTO appointments (client_id, date_time) VALUES (3, '2025-09-10 11:45:00');  -- miércoles

-- Cliente 4
INSERT INTO appointments (client_id, date_time) VALUES (4, '2025-08-14 17:45:00');  -- jueves
INSERT INTO appointments (client_id, date_time) VALUES (4, '2025-09-11 17:45:00');  -- jueves

-- Cliente 5
INSERT INTO appointments (client_id, date_time) VALUES (5, '2025-08-15 10:15:00');  -- viernes
INSERT INTO appointments (client_id, date_time) VALUES (5, '2025-09-12 10:15:00');  -- viernes