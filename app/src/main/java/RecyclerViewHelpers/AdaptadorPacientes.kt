package RecyclerViewHelpers

import Modelo.ClaseConcexion
import Modelo.dataClassPaciente
import adrielmoreno.jaimeperla.hospitalbloom.R
import adrielmoreno.jaimeperla.hospitalbloom.pacienteInfo
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.SQLException
import java.util.Calendar

class AdaptadorPacientes(var Datos: List<dataClassPaciente>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vistaw = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)
        return ViewHolder(vistaw)
    }

    override fun getItemCount() = Datos.size

    fun actualizarListado(nuevaListaPacientes: List<dataClassPaciente>) {
        Datos = nuevaListaPacientes
        notifyDataSetChanged()
    }

    fun actualizarItem(
        uuid_paciente: String,
        nuevoNombre: String,
        nuevoApellido: String,
        nuevaHoraMedicamento: String,
        nuevaEdad: Int,
        nuevaEnfermedad: String,
        nuevoHabitacion: Int,
        nuevaCama: Int
    ) {
        val index = Datos.indexOfFirst { it.uuid_paciente == uuid_paciente }
        if (index != -1) {
            Datos[index].nombres = nuevoNombre
            Datos[index].apellidos = nuevoApellido
            Datos[index].horaAplicacion = nuevaHoraMedicamento
            Datos[index].edad = nuevaEdad
            Datos[index].enfermedad = nuevaEnfermedad
            Datos[index].habitacion = nuevoHabitacion
            Datos[index].cama = nuevaCama
            notifyItemChanged(index)
        }
    }

    fun eliminarPaciente(uuidPaciente: String, position: Int) {
        val listaDePacientes = Datos.toMutableList()
        listaDePacientes.removeAt(position)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConcexion().CadenaConexion()
                val deletePaciente =
                    objConexion?.prepareStatement("DELETE FROM paciente WHERE UUID_Paciente = ?")!!
                deletePaciente.setString(1, uuidPaciente)
                deletePaciente.executeUpdate()
                val commit = objConexion.prepareStatement("COMMIT")
                commit.executeUpdate()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        Datos = listaDePacientes.toList()
        notifyItemRemoved(position)
    }

    fun actualizarPaciente(
        uuid_paciente: String,
        nuevoNombre: String,
        nuevoApellido: String,
        nuevaHoraMedicamento: String,
        nuevaEdad: Int,
        nuevaEnfermedad: String,
        nuevoHabitacion: Int,
        nuevaCama: Int
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConcexion().CadenaConexion()
                val updatePaciente =
                    objConexion?.prepareStatement("UPDATE paciente SET Nombres = ?, Apellidos = ?, Edad = ?, Efermedad = ?, numero_habitacion = ?, numero_cama = ?, hora_aplicacion = ? WHERE UUID_Paciente = ?")!!
                updatePaciente.setString(1, nuevoNombre)
                updatePaciente.setString(2, nuevoApellido)
                updatePaciente.setInt(3, nuevaEdad)
                updatePaciente.setString(4, nuevaEnfermedad)
                updatePaciente.setInt(5, nuevoHabitacion)
                updatePaciente.setInt(6, nuevaCama)
                updatePaciente.setString(7, nuevaHoraMedicamento)
                updatePaciente.setString(8, uuid_paciente)
                updatePaciente.executeUpdate()

                // Commit the changes
                val commit = objConexion.prepareStatement("COMMIT")
                commit.executeUpdate()

                launch(Dispatchers.Main) {
                    actualizarItem(
                        uuid_paciente,
                        nuevoNombre,
                        nuevoApellido,
                        nuevaHoraMedicamento,
                        nuevaEdad,
                        nuevaEnfermedad,
                        nuevoHabitacion,
                        nuevaCama
                    )
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.txtNombreCard.text = item.nombres
        holder.txtApellidosCard.text = item.apellidos
        holder.txtMedicamentoCardHora.text = item.horaAplicacion
        holder.txtEnfermedadCard.text = item.enfermedad
        holder.txtMedicamentoCard.text = item.uuid_medicamento

        holder.icBorrar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el paciente?")
            builder.setPositiveButton("Sí") { dialog, which ->
                eliminarPaciente(item.uuid_paciente, position)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.icEditar.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Actualizar datos del paciente")
            alertDialogBuilder.setMessage("Ingrese los nuevos datos del paciente:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val nuevoNombre = EditText(holder.itemView.context)
            nuevoNombre.hint = "Nombre"
            layout.addView(nuevoNombre)

            val nuevoApellido = EditText(holder.itemView.context)
            nuevoApellido.hint = "Apellido"
            layout.addView(nuevoApellido)

            val nuevaEdad = EditText(holder.itemView.context)
            nuevaEdad.hint = "Edad"
            layout.addView(nuevaEdad)

            val nuevaEnfermedad = EditText(holder.itemView.context)
            nuevaEnfermedad.hint = "Enfermedad"
            layout.addView(nuevaEnfermedad)

            val nuevaHoraMedicamento = EditText(holder.itemView.context)
            nuevaHoraMedicamento.hint = "Hora de Medicamento"
            layout.addView(nuevaHoraMedicamento)

            val nuevoHabitacion = EditText(holder.itemView.context)
            nuevoHabitacion.hint = "Habitación"
            layout.addView(nuevoHabitacion)

            val nuevaCama = EditText(holder.itemView.context)
            nuevaCama.hint = "Cama"
            layout.addView(nuevaCama)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Actualizar") { dialog, which ->
                val nombreNuevo = nuevoNombre.text.toString()
                val apellidoNuevo = nuevoApellido.text.toString()
                val edadNueva = nuevaEdad.text.toString().toIntOrNull() ?: 0
                val enfermedadNueva = nuevaEnfermedad.text.toString()
                val horaMedicamentoNueva = nuevaHoraMedicamento.text.toString()
                val habitacionNueva = nuevoHabitacion.text.toString().toIntOrNull() ?: 0
                val camaNueva = nuevaCama.text.toString().toIntOrNull() ?: 0

                if (nombreNuevo.isBlank() || apellidoNuevo.isBlank() || enfermedadNueva.isBlank() || horaMedicamentoNueva.isBlank()) {
                    Toast.makeText(holder.itemView.context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                } else if (edadNueva > 16) {
                    Toast.makeText(holder.itemView.context, "La edad debe ser igual o menor a 16 años", Toast.LENGTH_SHORT).show()
                } else {
                    actualizarPaciente(
                        item.uuid_paciente,
                        nombreNuevo,
                        apellidoNuevo,
                        horaMedicamentoNueva,
                        edadNueva,
                        enfermedadNueva,
                        habitacionNueva,
                        camaNueva
                    )
                }
            }

            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context,pacienteInfo::class.java)
            intent.putExtra(
                "Nombres",
                item.nombres
            )
            intent.putExtra(
                "Apellidos",
                item.apellidos
            )
            intent.putExtra(
                "Edad",
                item.edad
            )
            intent.putExtra(
                "Efermedad",
                item.enfermedad
            )
            intent.putExtra(
                "Fecha_Nacimiento",
                item.añoNacimiento
            )
            intent.putExtra(
                "numero_habitacion",
                item.habitacion
            )
            intent.putExtra(
                "numero_cama",
                item.cama
            )
            intent.putExtra(
                "UUID_Medicamento",
                item.uuid_medicamento
            )
            intent.putExtra(
                "hora_aplicacion",
                item.horaAplicacion
            )
            intent.putExtra(
                "medicamento_adiccional",
                item.medExtra
            )
            context.startActivity(intent)
        }
    }
}