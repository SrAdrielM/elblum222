package Modelo

data class dataClassPaciente(
    var uuid_pacinete : String,
    var nombres: String,
    var apellidos: String,
    var edad: Int,
    var añoNacimiento: String,
    var enfermedad: String,
    var habitacion: Int,
    var cama: Int,
    var medicamento :String,
    var horaAplicacion : String,
    var medExtra : String
)
