package Modelo

data class dataClassPaciente(
    var uuid_paciente: String,
    var nombres: String,
    var apellidos: String,
    var edad: Int,
    var añoNacimiento: String,
    var enfermedad: String,
    var habitacion: Int,
    var cama: Int,
    var uuid_medicamento: String,
    var horaAplicacion: String,
    var medExtra: String
)