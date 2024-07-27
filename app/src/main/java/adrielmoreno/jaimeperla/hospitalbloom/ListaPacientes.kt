package adrielmoreno.jaimeperla.hospitalbloom

import Modelo.ClaseConcexion
import Modelo.dataClassPaciente
import RecyclerViewHelpers.Adaptador
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaPacientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregarPaciente = findViewById<Button>(R.id.btnAgregar)
        val rcvListaPacientes = findViewById<RecyclerView>(R.id.rcvPacientes)

        rcvListaPacientes.layoutManager = LinearLayoutManager(this)

        fun obtenerPacientes(): List<dataClassPaciente> {
            val objconexion = ClaseConcexion().CadenaConexion()

            val statement = objconexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from paciente")

            val listadoPacientes = mutableListOf<dataClassPaciente>()

            if (resultSet != null) {
                while (resultSet.next()){
                    val uuid = resultSet.getString("UUID_Paciente")
                    val nombres = resultSet.getString("Nombres")
                    val apellidos = resultSet.getString("Apellidos")
                    val edad = resultSet.getInt("Edad")
                    val enfermedad = resultSet.getString("Efermedad")
                    val nacimiento = resultSet.getString("Fecha_Nacimiento")
                    val habitacion = resultSet.getInt("numero_habitacion")
                    val cama = resultSet.getInt("numero_cama")
                    val medicamento = resultSet.getString("UUID_Medicamento")
                    val horaaplicacion = resultSet.getString("hora_aplicacion")
                    val medicamentoextra = resultSet.getString("medicamento_adiccional")

                    val valoresJuntos = dataClassPaciente(uuid, nombres, apellidos, edad, enfermedad, nacimiento, habitacion, cama, medicamento, horaaplicacion, medicamentoextra)

                    listadoPacientes.add(valoresJuntos)
                }

            }
            return listadoPacientes
        }

        CoroutineScope(Dispatchers.IO).launch {
            val pacientesRCV = obtenerPacientes()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(pacientesRCV)
                rcvListaPacientes.adapter = adapter
            }
        }

        btnAgregarPaciente.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}