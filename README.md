create table paciente
( UUID_Paciente varchar2(50) primary key, 
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
UUID_Medicamento varchar2(50)primary key, 
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


-------------------------------------------------------------------------------------------------------------------------------------------------------------------
--apartado de inserciones


INSERT INTO Medicamento (UUID_Medicamento, Nombre_medicamento) VALUES 

(SYS_GUID(), 'Amoxicilina');

select *from medicamento;

select *from paciente;

INSERT INTO paciente (UUID_Paciente, Nombres, Apellidos, Edad, Efermedad, Fecha_Nacimiento, numero_habitacion, numero_cama, UUID_Medicamento, hora_aplicacion, medicamento_adiccional)
VALUES 
(SYS_GUID(), 'Juan', 'Pérez', 12, 'Gripe', '1993-01-15', 2, 1, 'AA2B9DDA1FBD4D4AADCC1BC33B8847F1', '08:00', 'Gotas para ojos');

INSERT INTO detalle_PacienteMedicamentoos (UUID_Paciente, UUID_Medicamento)
VALUES 
('F119A9AD73414BEBB0C152D8FFE63F18','AA2B9DDA1FBD4D4AADCC1BC33B8847F1');

select * from medicamento;

select * from paciente;
select * from detalle_PacienteMedicamentoos;
