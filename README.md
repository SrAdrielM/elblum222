create table 
paciente( UUID_Paciente varchar2(50) primary key, 
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
CONSTRAINT fk_medicamento FOREIGN KEY (UUID_Medicamento) REFERENCES Medicamento(UUID_Medicamento) );

create table Medicamento( UUID_Medicamento varchar2(51) primary key, Nombre_medicamento varchar2(100) );

create table detalle_PacienteMedicamentoos( UUID_Paciente varchar2(50), UUID_Medicamento varchar2(50), CONSTRAINT fk_medicamentos FOREIGN KEY (UUID_Medicamento) REFERENCES Medicamento(UUID_Medicamento), CONSTRAINT fk_Pacienteee FOREIGN KEY (UUID_Paciente) REFERENCES paciente(UUID_Paciente) );
