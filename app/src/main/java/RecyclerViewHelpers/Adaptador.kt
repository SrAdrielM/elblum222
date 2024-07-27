package RecyclerViewHelpers

import Modelo.ClaseConcexion
import Modelo.dataClassPaciente
import adrielmoreno.jaimeperla.hospitalbloom.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    fun eliminarPaciente(nombre: String, position: Int) {
        val listaProv = Datos.toMutableList()
        listaProv.removeAt(position)

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConcexion().CadenaConexion()
            val eliminarProv = objConexion?.prepareStatement("delete from paciente where  UUID_Paciente = ?")!!
            eliminarProv.setString(1, nombre)

            eliminarProv.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }

        Datos = listaProv.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

}