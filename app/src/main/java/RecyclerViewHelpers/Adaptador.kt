package RecyclerViewHelpers

import Modelo.ClaseConcexion
import Modelo.dataClassPaciente
import adrielmoreno.jaimeperla.hospitalbloom.R
import android.app.AlertDialog
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
import kotlinx.coroutines.withContext
import java.sql.SQLException

class Adaptador(var Datos: List<dataClassPaciente>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the card layout
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    fun actualizarPaciente(uuid_paciente: String, nuevoNombre: String, nuevoApellido: String, nuevaHoraMedicamento: String, nuevaEdad: Int, nuevaEnfermedad: String, nuevoHabitacion: Int, nuevaCama: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConcexion().CadenaConexion()
                val updatePaciente = objConexion?.prepareStatement("UPDATE paciente SET Nombres = ?, Apellidos = ?, Edad = ?, Efermedad = ?, numero_habitacion = ?, numero_cama = ?, hora_aplicacion = ? WHERE UUID_Paciente = ?")!!
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

                // Update the UI
                withContext(Dispatchers.Main) {
                    val index = Datos.indexOfFirst { it.uuid_pacinete == uuid_paciente }
                    if (index != -1) {
                        val updatedPacienteitem = Datos[index].copy(
                            nombres = nuevoNombre,
                            apellidos = nuevoApellido,
                            edad = nuevaEdad,
                            enfermedad = nuevaEnfermedad,
                            habitacion = nuevoHabitacion,
                            cama = nuevaCama,
                            horaAplicacion = nuevaHoraMedicamento
                        )
                        Datos = Datos.toMutableList().apply {
                            set(index, updatedPacienteitem)
                        }
                        notifyItemChanged(index)
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun eliminarPaciente(uuid_paciente: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConcexion().CadenaConexion()
                val deletePaciente = objConexion?.prepareStatement("DELETE FROM paciente WHERE UUID_Paciente = ?")!!
                deletePaciente.setString(1, uuid_paciente)
                deletePaciente.executeUpdate()

                // Commit the changes
                val commit = objConexion.prepareStatement("COMMIT")
                commit.executeUpdate()

                // Update the UI
                withContext(Dispatchers.Main) {
                    val index = Datos.indexOfFirst { it.uuid_pacinete == uuid_paciente }
                    if (index != -1) {
                        Datos = Datos.toMutableList().apply {
                            removeAt(index)
                        }
                        notifyItemRemoved(index)
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to the card views
        val item = Datos[position]
        holder.txtNombreCard.text = item.nombres
        holder.txtApellidosCard.text = item.apellidos
        holder.txtMedicamentoCardHora.text = item.horaAplicacion
        holder.txtEnfermedadCard.text = item.enfermedad
        holder.txtMedicamentoCard.text = item.medicamento

        holder.icBorrar.setOnClickListener {
            eliminarPaciente(item.uuid_pacinete)
        }

        holder.icEditar.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Actualizar datos del paciente")
            alertDialogBuilder.setMessage("Ingrese los nuevos datos del paciente:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val nuevoNombre = EditText(holder.itemView.context)
            nuevoNombre.hint = "Nombres"
            layout.addView(nuevoNombre)

            val nuevoApellido = EditText(holder.itemView.context)
            nuevoApellido.hint = "Apellidos"
            layout.addView(nuevoApellido)

            val nuevaHoraMedicamento = EditText(holder.itemView.context)
            nuevaHoraMedicamento.hint = "Hora de aplicación"
            layout.addView(nuevaHoraMedicamento)

            val nuevaEdad = EditText(holder.itemView.context)
            nuevaEdad.hint = "Edad"
            nuevaEdad.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(nuevaEdad)

            val nuevaEnfermedad = EditText(holder.itemView.context)
            nuevaEnfermedad.hint = "Enfermedad"
            layout.addView(nuevaEnfermedad)

            val nuevoHabitacion = EditText(holder.itemView.context)
            nuevoHabitacion.hint = "Número de habitación"
            nuevoHabitacion.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(nuevoHabitacion)

            val nuevaCama = EditText(holder.itemView.context)
            nuevaCama.hint = "Número de cama"
            nuevaCama.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(nuevaCama)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Actualizar") { dialog, _ ->
                val nombreNuevo = nuevoNombre.text.toString()
                val apellidoNuevo = nuevoApellido.text.toString()
                val horaNuevaMedicamento = nuevaHoraMedicamento.text.toString()
                val edadNueva = nuevaEdad.text.toString().toIntOrNull() ?: 0
                val enfermedadNueva = nuevaEnfermedad.text.toString()
                val habitacionNueva = nuevoHabitacion.text.toString().toIntOrNull() ?: 0
                val camaNueva = nuevaCama.text.toString().toIntOrNull() ?: 0

                if (nombreNuevo.isBlank() || apellidoNuevo.isBlank() || horaNuevaMedicamento.isBlank() || enfermedadNueva.isBlank() || habitacionNueva == 0 || camaNueva == 0) {
                    Toast.makeText(holder.itemView.context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                } else {
                    actualizarPaciente(item.uuid_pacinete, nombreNuevo, apellidoNuevo, horaNuevaMedicamento, edadNueva, enfermedadNueva, habitacionNueva, camaNueva)
                }
            }

            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}
