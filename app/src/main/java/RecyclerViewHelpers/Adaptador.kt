package RecyclerViewHelpers

import Modelo.dataClassPaciente
import adrielmoreno.jaimeperla.hospitalbloom.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adaptador(var Datos: List<dataClassPaciente>): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //unimos recycler y card
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Controlamos la card we
        val item = Datos[position]
        holder.txtNombreCard.text = item.nombres
        holder.txtApellidosCard.text = item.apellidos
        holder.txtMedicamentoCardHora.text= item.horaAplicacion
        holder.txtEnfermedadCard.text = item.enfermedad
        holder.txtMedicamentoCard.text = item.medicamento

        holder.icBorrar.setOnClickListener {

        }
        holder.icEditar.setOnClickListener {

        }
    }

}