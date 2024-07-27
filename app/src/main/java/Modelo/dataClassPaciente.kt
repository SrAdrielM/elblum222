package Modelo

data class dataClassPaciente(
    var uuid_pacinete : String,
    var nombres: String,
    var apellidos: String,
    var edad: String,
    var añoNacimiento: String,
    var enfermedad: String,
    var habitacion: Int,
    var cama: Int,
    var horaAplicacion : String,
    var medExtra : String
)
