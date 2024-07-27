package RecyclerViewHelpers

import adrielmoreno.jaimeperla.hospitalbloom.R
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val txtNombreCard = view.findViewById<TextView>(R.id.txtNombreCard)
    val txtApellidosCard = view.findViewById<TextView>(R.id.txtApellidoCard)
    val txtMedicamentoCardHora = view.findViewById<TextView>(R.id.txtHoraAplicaionCard)
    val txtEnfermedadCard = view.findViewById<TextView>(R.id.txtEnfermedadCard)
    val txtMedicamentoCard = view.findViewById<TextView>(R.id.txtMedicamentoCard)
    val icEditar =view.findViewById<TextView>(R.id.icEditar)
    val icBorrar = view.findViewById<TextView>(R.id.icBorrar)
}