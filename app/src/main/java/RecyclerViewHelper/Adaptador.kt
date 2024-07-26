package RecyclerViewHelper

import Modelo.dataClassMedicina
import Modelo.dataClassPaciente
import adrielmoreno.jaimeperla.hospitalbloom.R
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PacienteAdapter(private val pacientes: List<dataClassPaciente>, private val context: Context, private val medicinas: List<dataClassMedicina>) :
    RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)
        return PacienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = pacientes[position]
        val medicina = medicinas[position] // Aquí se asume que hay una correspondencia uno a uno entre pacientes y medicinas
        holder.bind(paciente)
        holder.itemView.setOnClickListener {
            showPatientInfoDialog(paciente, medicina)
        }
    }

    override fun getItemCount(): Int = pacientes.size

    private fun showPatientInfoDialog(paciente: dataClassPaciente, medicina: dataClassMedicina) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_paciente_info, null)

        val textViewName = dialogView.findViewById<TextView>(R.id.txtNombresShow)
        val textViewSurname = dialogView.findViewById<TextView>(R.id.txtApellidosShow)
        val textViewAge = dialogView.findViewById<TextView>(R.id.txtEdadShow)
        val textViewBirthDate = dialogView.findViewById<TextView>(R.id.txtNacimientoShow)
        val textViewDisease = dialogView.findViewById<TextView>(R.id.txtEnfermedadShow)
        val textViewRoomNumber = dialogView.findViewById<TextView>(R.id.txtHabitacionShow)
        val textViewBedNumber = dialogView.findViewById<TextView>(R.id.txtCamaShow)
        val textViewMedicament = dialogView.findViewById<TextView>(R.id.txtMedicamento1Show)
        val textViewMedHour = dialogView.findViewById<TextView>(R.id.txtHoraMed1Show)
        val textViewMedExtra = dialogView.findViewById<TextView>(R.id.txtInfoExtraShow)

        textViewName.text = "Nombre: ${paciente.nombres}"
        textViewSurname.text = "Apellidos: ${paciente.apellidos}"
        textViewAge.text = "Edad: ${paciente.edad}"
        textViewBirthDate.text = "Fecha de Nacimiento: ${paciente.añoNacimiento}"
        textViewDisease.text = "Enfermedad: ${paciente.enfermedad}"
        textViewRoomNumber.text = "Número de Habitación: ${paciente.habitacion}"
        textViewBedNumber.text = "Número de Cama: ${paciente.cama}"
        textViewMedicament.text = "Medicamento: ${medicina.Nombre_medicamento}"
        textViewMedHour.text = "Hora de Aplicación: ${paciente.horaAplicacion}"
        textViewMedExtra.text = "Medicamento extra: ${paciente.medExtra}"

        AlertDialog.Builder(context)
            .setTitle("Información del Paciente")
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }

    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.txtNombreCard)

        fun bind(paciente: dataClassPaciente) {
            textViewName.text = paciente.nombres
        }
    }
}