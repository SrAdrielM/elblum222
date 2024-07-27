create table paciente( 
    UUID_Paciente varchar2(50) primary key, 
    Nombres varchar2 (50) not null,
    Apellidos varchar2(50) not null, 
    Edad int not null, 
    Efermedad varchar(20)not null, 
    Fecha_Nacimiento varchar(20),
    numero_habitacion int, 
    numero_cama int, 
    UUID_Medicamento varchar2(50),
    hora_aplicacion varchar2(50),
    medicamento_adiccional varchar2 (1000),
    
    CONSTRAINT fk_medicamento 
    FOREIGN KEY (UUID_Medicamento) 
    REFERENCES Medicamento(UUID_Medicamento) 
);

create table Medicamento( 
    UUID_Medicamento varchar2(51) primary key, 
    Nombre_medicamento varchar2(100) 
);

create table detalle_PacienteMedicamentoos( 
    UUID_Paciente varchar2(50), 
    UUID_Medicamento varchar2(50), 
    
    CONSTRAINT fk_medicamentos 
    FOREIGN KEY (UUID_Medicamento) 
    REFERENCES Medicamento(UUID_Medicamento), 
    
    CONSTRAINT fk_Pacienteee 
    FOREIGN KEY (UUID_Paciente) 
    REFERENCES paciente(UUID_Paciente) 
);

-- Insertar datos en la tabla Medicamento con UUID generado aleatoriamente
INSERT INTO Medicamento (UUID_Medicamento, Nombre_medicamento) VALUES (SYS_GUID(), 'Paracetamol');
INSERT INTO Medicamento (UUID_Medicamento, Nombre_medicamento) VALUES (SYS_GUID(), 'Ibuprofeno');
INSERT INTO Medicamento (UUID_Medicamento, Nombre_medicamento) VALUES (SYS_GUID(), 'Amoxicilina');

-- Insertar datos en la tabla paciente con UUID generado aleatoriamente
INSERT INTO paciente (
    UUID_Paciente, Nombres, Apellidos, Edad, Efermedad, Fecha_Nacimiento, 
    numero_habitacion, numero_cama, UUID_Medicamento, hora_aplicacion, medicamento_adiccional
) VALUES (
    SYS_GUID(), 'Juan', 'Pérez', 30, 'Gripe', '1994-03-15', 101, 1, 
    (SELECT UUID_Medicamento FROM Medicamento WHERE Nombre_medicamento = 'Paracetamol'), '8 horas', 'Ninguno'
);

INSERT INTO paciente (
    UUID_Paciente, Nombres, Apellidos, Edad, Efermedad, Fecha_Nacimiento, 
    numero_habitacion, numero_cama, UUID_Medicamento, hora_aplicacion, medicamento_adiccional
) VALUES (
    SYS_GUID(), 'María', 'García', 45, 'Migraña', '1978-07-20', 102, 2, 
    (SELECT UUID_Medicamento FROM Medicamento WHERE Nombre_medicamento = 'Ibuprofeno'), '9 horas', 'Paracetamol adicional'
);

INSERT INTO paciente (
    UUID_Paciente, Nombres, Apellidos, Edad, Efermedad, Fecha_Nacimiento, 
    numero_habitacion, numero_cama, UUID_Medicamento, hora_aplicacion, medicamento_adiccional
) VALUES (
    SYS_GUID(), 'Carlos', 'López', 60, 'Infección', '1963-11-30', 103, 3, 
    (SELECT UUID_Medicamento FROM Medicamento WHERE Nombre_medicamento = 'Amoxicilina'), '5 horas', 'Amoxicilina adicional'
);

-- Insertar datos en la tabla detalle_PacienteMedicamentoos con UUIDs generados aleatoriamente
INSERT INTO detalle_PacienteMedicamentoos (UUID_Paciente, UUID_Medicamento) 
VALUES (
    (SELECT UUID_Paciente FROM paciente WHERE Nombres = 'Juan' AND Apellidos = 'Pérez'), 
    (SELECT UUID_Medicamento FROM Medicamento WHERE Nombre_medicamento = 'Paracetamol')
);

INSERT INTO detalle_PacienteMedicamentoos (UUID_Paciente, UUID_Medicamento) 
VALUES (
    (SELECT UUID_Paciente FROM paciente WHERE Nombres = 'María' AND Apellidos = 'García'), 
    (SELECT UUID_Medicamento FROM Medicamento WHERE Nombre_medicamento = 'Ibuprofeno')
);

INSERT INTO detalle_PacienteMedicamentoos (UUID_Paciente, UUID_Medicamento) 
VALUES (
    (SELECT UUID_Paciente FROM paciente WHERE Nombres = 'Carlos' AND Apellidos = 'López'), 
    (SELECT UUID_Medicamento FROM Medicamento WHERE Nombre_medicamento = 'Amoxicilina')
);

select * from medicamento;

select * from paciente;
